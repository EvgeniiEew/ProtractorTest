package by.skilsoft.jh.courses.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import by.skilsoft.jh.courses.IntegrationTest;
import by.skilsoft.jh.courses.domain.Courses;
import by.skilsoft.jh.courses.domain.Student;
import by.skilsoft.jh.courses.domain.Study;
import by.skilsoft.jh.courses.repository.StudyRepository;
import by.skilsoft.jh.courses.service.criteria.StudyCriteria;
import by.skilsoft.jh.courses.service.dto.StudyDTO;
import by.skilsoft.jh.courses.service.mapper.StudyMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StudyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudyResourceIT {

    private static final LocalDate DEFAULT_DATE_OF_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_START = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_START = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_OF_EXAM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_EXAM = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_EXAM = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_GRADE = 1;
    private static final Integer UPDATED_GRADE = 2;
    private static final Integer SMALLER_GRADE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/studies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyMapper studyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudyMockMvc;

    private Study study;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Study createEntity(EntityManager em) {
        Study study = new Study().dateOfStart(DEFAULT_DATE_OF_START).dateOfExam(DEFAULT_DATE_OF_EXAM).grade(DEFAULT_GRADE);
        return study;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Study createUpdatedEntity(EntityManager em) {
        Study study = new Study().dateOfStart(UPDATED_DATE_OF_START).dateOfExam(UPDATED_DATE_OF_EXAM).grade(UPDATED_GRADE);
        return study;
    }

    @BeforeEach
    public void initTest() {
        study = createEntity(em);
    }

    @Test
    @Transactional
    void createStudy() throws Exception {
        int databaseSizeBeforeCreate = studyRepository.findAll().size();
        // Create the Study
        StudyDTO studyDTO = studyMapper.toDto(study);
        restStudyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyDTO)))
            .andExpect(status().isCreated());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeCreate + 1);
        Study testStudy = studyList.get(studyList.size() - 1);
        assertThat(testStudy.getDateOfStart()).isEqualTo(DEFAULT_DATE_OF_START);
        assertThat(testStudy.getDateOfExam()).isEqualTo(DEFAULT_DATE_OF_EXAM);
        assertThat(testStudy.getGrade()).isEqualTo(DEFAULT_GRADE);
    }

    @Test
    @Transactional
    void createStudyWithExistingId() throws Exception {
        // Create the Study with an existing ID
        study.setId(1L);
        StudyDTO studyDTO = studyMapper.toDto(study);

        int databaseSizeBeforeCreate = studyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateOfStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyRepository.findAll().size();
        // set the field null
        study.setDateOfStart(null);

        // Create the Study, which fails.
        StudyDTO studyDTO = studyMapper.toDto(study);

        restStudyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyDTO)))
            .andExpect(status().isBadRequest());

        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateOfExamIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyRepository.findAll().size();
        // set the field null
        study.setDateOfExam(null);

        // Create the Study, which fails.
        StudyDTO studyDTO = studyMapper.toDto(study);

        restStudyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyDTO)))
            .andExpect(status().isBadRequest());

        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGradeIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyRepository.findAll().size();
        // set the field null
        study.setGrade(null);

        // Create the Study, which fails.
        StudyDTO studyDTO = studyMapper.toDto(study);

        restStudyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyDTO)))
            .andExpect(status().isBadRequest());

        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudies() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList
        restStudyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(study.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfStart").value(hasItem(DEFAULT_DATE_OF_START.toString())))
            .andExpect(jsonPath("$.[*].dateOfExam").value(hasItem(DEFAULT_DATE_OF_EXAM.toString())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)));
    }

    @Test
    @Transactional
    void getStudy() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get the study
        restStudyMockMvc
            .perform(get(ENTITY_API_URL_ID, study.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(study.getId().intValue()))
            .andExpect(jsonPath("$.dateOfStart").value(DEFAULT_DATE_OF_START.toString()))
            .andExpect(jsonPath("$.dateOfExam").value(DEFAULT_DATE_OF_EXAM.toString()))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE));
    }

    @Test
    @Transactional
    void getStudiesByIdFiltering() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        Long id = study.getId();

        defaultStudyShouldBeFound("id.equals=" + id);
        defaultStudyShouldNotBeFound("id.notEquals=" + id);

        defaultStudyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudyShouldNotBeFound("id.greaterThan=" + id);

        defaultStudyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfStartIsEqualToSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfStart equals to DEFAULT_DATE_OF_START
        defaultStudyShouldBeFound("dateOfStart.equals=" + DEFAULT_DATE_OF_START);

        // Get all the studyList where dateOfStart equals to UPDATED_DATE_OF_START
        defaultStudyShouldNotBeFound("dateOfStart.equals=" + UPDATED_DATE_OF_START);
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfStartIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfStart not equals to DEFAULT_DATE_OF_START
        defaultStudyShouldNotBeFound("dateOfStart.notEquals=" + DEFAULT_DATE_OF_START);

        // Get all the studyList where dateOfStart not equals to UPDATED_DATE_OF_START
        defaultStudyShouldBeFound("dateOfStart.notEquals=" + UPDATED_DATE_OF_START);
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfStartIsInShouldWork() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfStart in DEFAULT_DATE_OF_START or UPDATED_DATE_OF_START
        defaultStudyShouldBeFound("dateOfStart.in=" + DEFAULT_DATE_OF_START + "," + UPDATED_DATE_OF_START);

        // Get all the studyList where dateOfStart equals to UPDATED_DATE_OF_START
        defaultStudyShouldNotBeFound("dateOfStart.in=" + UPDATED_DATE_OF_START);
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfStart is not null
        defaultStudyShouldBeFound("dateOfStart.specified=true");

        // Get all the studyList where dateOfStart is null
        defaultStudyShouldNotBeFound("dateOfStart.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfStartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfStart is greater than or equal to DEFAULT_DATE_OF_START
        defaultStudyShouldBeFound("dateOfStart.greaterThanOrEqual=" + DEFAULT_DATE_OF_START);

        // Get all the studyList where dateOfStart is greater than or equal to UPDATED_DATE_OF_START
        defaultStudyShouldNotBeFound("dateOfStart.greaterThanOrEqual=" + UPDATED_DATE_OF_START);
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfStartIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfStart is less than or equal to DEFAULT_DATE_OF_START
        defaultStudyShouldBeFound("dateOfStart.lessThanOrEqual=" + DEFAULT_DATE_OF_START);

        // Get all the studyList where dateOfStart is less than or equal to SMALLER_DATE_OF_START
        defaultStudyShouldNotBeFound("dateOfStart.lessThanOrEqual=" + SMALLER_DATE_OF_START);
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfStartIsLessThanSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfStart is less than DEFAULT_DATE_OF_START
        defaultStudyShouldNotBeFound("dateOfStart.lessThan=" + DEFAULT_DATE_OF_START);

        // Get all the studyList where dateOfStart is less than UPDATED_DATE_OF_START
        defaultStudyShouldBeFound("dateOfStart.lessThan=" + UPDATED_DATE_OF_START);
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfStartIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfStart is greater than DEFAULT_DATE_OF_START
        defaultStudyShouldNotBeFound("dateOfStart.greaterThan=" + DEFAULT_DATE_OF_START);

        // Get all the studyList where dateOfStart is greater than SMALLER_DATE_OF_START
        defaultStudyShouldBeFound("dateOfStart.greaterThan=" + SMALLER_DATE_OF_START);
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfExamIsEqualToSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfExam equals to DEFAULT_DATE_OF_EXAM
        defaultStudyShouldBeFound("dateOfExam.equals=" + DEFAULT_DATE_OF_EXAM);

        // Get all the studyList where dateOfExam equals to UPDATED_DATE_OF_EXAM
        defaultStudyShouldNotBeFound("dateOfExam.equals=" + UPDATED_DATE_OF_EXAM);
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfExamIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfExam not equals to DEFAULT_DATE_OF_EXAM
        defaultStudyShouldNotBeFound("dateOfExam.notEquals=" + DEFAULT_DATE_OF_EXAM);

        // Get all the studyList where dateOfExam not equals to UPDATED_DATE_OF_EXAM
        defaultStudyShouldBeFound("dateOfExam.notEquals=" + UPDATED_DATE_OF_EXAM);
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfExamIsInShouldWork() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfExam in DEFAULT_DATE_OF_EXAM or UPDATED_DATE_OF_EXAM
        defaultStudyShouldBeFound("dateOfExam.in=" + DEFAULT_DATE_OF_EXAM + "," + UPDATED_DATE_OF_EXAM);

        // Get all the studyList where dateOfExam equals to UPDATED_DATE_OF_EXAM
        defaultStudyShouldNotBeFound("dateOfExam.in=" + UPDATED_DATE_OF_EXAM);
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfExamIsNullOrNotNull() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfExam is not null
        defaultStudyShouldBeFound("dateOfExam.specified=true");

        // Get all the studyList where dateOfExam is null
        defaultStudyShouldNotBeFound("dateOfExam.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfExamIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfExam is greater than or equal to DEFAULT_DATE_OF_EXAM
        defaultStudyShouldBeFound("dateOfExam.greaterThanOrEqual=" + DEFAULT_DATE_OF_EXAM);

        // Get all the studyList where dateOfExam is greater than or equal to UPDATED_DATE_OF_EXAM
        defaultStudyShouldNotBeFound("dateOfExam.greaterThanOrEqual=" + UPDATED_DATE_OF_EXAM);
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfExamIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfExam is less than or equal to DEFAULT_DATE_OF_EXAM
        defaultStudyShouldBeFound("dateOfExam.lessThanOrEqual=" + DEFAULT_DATE_OF_EXAM);

        // Get all the studyList where dateOfExam is less than or equal to SMALLER_DATE_OF_EXAM
        defaultStudyShouldNotBeFound("dateOfExam.lessThanOrEqual=" + SMALLER_DATE_OF_EXAM);
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfExamIsLessThanSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfExam is less than DEFAULT_DATE_OF_EXAM
        defaultStudyShouldNotBeFound("dateOfExam.lessThan=" + DEFAULT_DATE_OF_EXAM);

        // Get all the studyList where dateOfExam is less than UPDATED_DATE_OF_EXAM
        defaultStudyShouldBeFound("dateOfExam.lessThan=" + UPDATED_DATE_OF_EXAM);
    }

    @Test
    @Transactional
    void getAllStudiesByDateOfExamIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where dateOfExam is greater than DEFAULT_DATE_OF_EXAM
        defaultStudyShouldNotBeFound("dateOfExam.greaterThan=" + DEFAULT_DATE_OF_EXAM);

        // Get all the studyList where dateOfExam is greater than SMALLER_DATE_OF_EXAM
        defaultStudyShouldBeFound("dateOfExam.greaterThan=" + SMALLER_DATE_OF_EXAM);
    }

    @Test
    @Transactional
    void getAllStudiesByGradeIsEqualToSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where grade equals to DEFAULT_GRADE
        defaultStudyShouldBeFound("grade.equals=" + DEFAULT_GRADE);

        // Get all the studyList where grade equals to UPDATED_GRADE
        defaultStudyShouldNotBeFound("grade.equals=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    void getAllStudiesByGradeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where grade not equals to DEFAULT_GRADE
        defaultStudyShouldNotBeFound("grade.notEquals=" + DEFAULT_GRADE);

        // Get all the studyList where grade not equals to UPDATED_GRADE
        defaultStudyShouldBeFound("grade.notEquals=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    void getAllStudiesByGradeIsInShouldWork() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where grade in DEFAULT_GRADE or UPDATED_GRADE
        defaultStudyShouldBeFound("grade.in=" + DEFAULT_GRADE + "," + UPDATED_GRADE);

        // Get all the studyList where grade equals to UPDATED_GRADE
        defaultStudyShouldNotBeFound("grade.in=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    void getAllStudiesByGradeIsNullOrNotNull() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where grade is not null
        defaultStudyShouldBeFound("grade.specified=true");

        // Get all the studyList where grade is null
        defaultStudyShouldNotBeFound("grade.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiesByGradeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where grade is greater than or equal to DEFAULT_GRADE
        defaultStudyShouldBeFound("grade.greaterThanOrEqual=" + DEFAULT_GRADE);

        // Get all the studyList where grade is greater than or equal to UPDATED_GRADE
        defaultStudyShouldNotBeFound("grade.greaterThanOrEqual=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    void getAllStudiesByGradeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where grade is less than or equal to DEFAULT_GRADE
        defaultStudyShouldBeFound("grade.lessThanOrEqual=" + DEFAULT_GRADE);

        // Get all the studyList where grade is less than or equal to SMALLER_GRADE
        defaultStudyShouldNotBeFound("grade.lessThanOrEqual=" + SMALLER_GRADE);
    }

    @Test
    @Transactional
    void getAllStudiesByGradeIsLessThanSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where grade is less than DEFAULT_GRADE
        defaultStudyShouldNotBeFound("grade.lessThan=" + DEFAULT_GRADE);

        // Get all the studyList where grade is less than UPDATED_GRADE
        defaultStudyShouldBeFound("grade.lessThan=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    void getAllStudiesByGradeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList where grade is greater than DEFAULT_GRADE
        defaultStudyShouldNotBeFound("grade.greaterThan=" + DEFAULT_GRADE);

        // Get all the studyList where grade is greater than SMALLER_GRADE
        defaultStudyShouldBeFound("grade.greaterThan=" + SMALLER_GRADE);
    }

    @Test
    @Transactional
    void getAllStudiesByCoursenameIsEqualToSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);
        Courses coursename;
        if (TestUtil.findAll(em, Courses.class).isEmpty()) {
            coursename = CoursesResourceIT.createEntity(em);
            em.persist(coursename);
            em.flush();
        } else {
            coursename = TestUtil.findAll(em, Courses.class).get(0);
        }
        em.persist(coursename);
        em.flush();
        study.setCoursename(coursename);
        studyRepository.saveAndFlush(study);
        Long coursenameId = coursename.getId();

        // Get all the studyList where coursename equals to coursenameId
        defaultStudyShouldBeFound("coursenameId.equals=" + coursenameId);

        // Get all the studyList where coursename equals to (coursenameId + 1)
        defaultStudyShouldNotBeFound("coursenameId.equals=" + (coursenameId + 1));
    }

    @Test
    @Transactional
    void getAllStudiesByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);
        Student student;
        if (TestUtil.findAll(em, Student.class).isEmpty()) {
            student = StudentResourceIT.createEntity(em);
            em.persist(student);
            em.flush();
        } else {
            student = TestUtil.findAll(em, Student.class).get(0);
        }
        em.persist(student);
        em.flush();
        study.setStudent(student);
        studyRepository.saveAndFlush(study);
        Long studentId = student.getId();

        // Get all the studyList where student equals to studentId
        defaultStudyShouldBeFound("studentId.equals=" + studentId);

        // Get all the studyList where student equals to (studentId + 1)
        defaultStudyShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudyShouldBeFound(String filter) throws Exception {
        restStudyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(study.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfStart").value(hasItem(DEFAULT_DATE_OF_START.toString())))
            .andExpect(jsonPath("$.[*].dateOfExam").value(hasItem(DEFAULT_DATE_OF_EXAM.toString())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)));

        // Check, that the count call also returns 1
        restStudyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudyShouldNotBeFound(String filter) throws Exception {
        restStudyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudy() throws Exception {
        // Get the study
        restStudyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudy() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        int databaseSizeBeforeUpdate = studyRepository.findAll().size();

        // Update the study
        Study updatedStudy = studyRepository.findById(study.getId()).get();
        // Disconnect from session so that the updates on updatedStudy are not directly saved in db
        em.detach(updatedStudy);
        updatedStudy.dateOfStart(UPDATED_DATE_OF_START).dateOfExam(UPDATED_DATE_OF_EXAM).grade(UPDATED_GRADE);
        StudyDTO studyDTO = studyMapper.toDto(updatedStudy);

        restStudyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
        Study testStudy = studyList.get(studyList.size() - 1);
        assertThat(testStudy.getDateOfStart()).isEqualTo(UPDATED_DATE_OF_START);
        assertThat(testStudy.getDateOfExam()).isEqualTo(UPDATED_DATE_OF_EXAM);
        assertThat(testStudy.getGrade()).isEqualTo(UPDATED_GRADE);
    }

    @Test
    @Transactional
    void putNonExistingStudy() throws Exception {
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();
        study.setId(count.incrementAndGet());

        // Create the Study
        StudyDTO studyDTO = studyMapper.toDto(study);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudy() throws Exception {
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();
        study.setId(count.incrementAndGet());

        // Create the Study
        StudyDTO studyDTO = studyMapper.toDto(study);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudy() throws Exception {
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();
        study.setId(count.incrementAndGet());

        // Create the Study
        StudyDTO studyDTO = studyMapper.toDto(study);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudyWithPatch() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        int databaseSizeBeforeUpdate = studyRepository.findAll().size();

        // Update the study using partial update
        Study partialUpdatedStudy = new Study();
        partialUpdatedStudy.setId(study.getId());

        partialUpdatedStudy.dateOfStart(UPDATED_DATE_OF_START).dateOfExam(UPDATED_DATE_OF_EXAM).grade(UPDATED_GRADE);

        restStudyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudy))
            )
            .andExpect(status().isOk());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
        Study testStudy = studyList.get(studyList.size() - 1);
        assertThat(testStudy.getDateOfStart()).isEqualTo(UPDATED_DATE_OF_START);
        assertThat(testStudy.getDateOfExam()).isEqualTo(UPDATED_DATE_OF_EXAM);
        assertThat(testStudy.getGrade()).isEqualTo(UPDATED_GRADE);
    }

    @Test
    @Transactional
    void fullUpdateStudyWithPatch() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        int databaseSizeBeforeUpdate = studyRepository.findAll().size();

        // Update the study using partial update
        Study partialUpdatedStudy = new Study();
        partialUpdatedStudy.setId(study.getId());

        partialUpdatedStudy.dateOfStart(UPDATED_DATE_OF_START).dateOfExam(UPDATED_DATE_OF_EXAM).grade(UPDATED_GRADE);

        restStudyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudy))
            )
            .andExpect(status().isOk());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
        Study testStudy = studyList.get(studyList.size() - 1);
        assertThat(testStudy.getDateOfStart()).isEqualTo(UPDATED_DATE_OF_START);
        assertThat(testStudy.getDateOfExam()).isEqualTo(UPDATED_DATE_OF_EXAM);
        assertThat(testStudy.getGrade()).isEqualTo(UPDATED_GRADE);
    }

    @Test
    @Transactional
    void patchNonExistingStudy() throws Exception {
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();
        study.setId(count.incrementAndGet());

        // Create the Study
        StudyDTO studyDTO = studyMapper.toDto(study);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudy() throws Exception {
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();
        study.setId(count.incrementAndGet());

        // Create the Study
        StudyDTO studyDTO = studyMapper.toDto(study);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudy() throws Exception {
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();
        study.setId(count.incrementAndGet());

        // Create the Study
        StudyDTO studyDTO = studyMapper.toDto(study);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(studyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudy() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        int databaseSizeBeforeDelete = studyRepository.findAll().size();

        // Delete the study
        restStudyMockMvc
            .perform(delete(ENTITY_API_URL_ID, study.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
