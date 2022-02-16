package by.skilsoft.jh.courses.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import by.skilsoft.jh.courses.IntegrationTest;
import by.skilsoft.jh.courses.domain.Student;
import by.skilsoft.jh.courses.repository.StudentRepository;
import by.skilsoft.jh.courses.service.criteria.StudentCriteria;
import by.skilsoft.jh.courses.service.dto.StudentDTO;
import by.skilsoft.jh.courses.service.mapper.StudentMapper;
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
 * Integration tests for the {@link StudentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BIRTHDAY = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/students";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentMockMvc;

    private Student student;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createEntity(EntityManager em) {
        Student student = new Student().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME).dateOfBirthday(DEFAULT_DATE_OF_BIRTHDAY);
        return student;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createUpdatedEntity(EntityManager em) {
        Student student = new Student().firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).dateOfBirthday(UPDATED_DATE_OF_BIRTHDAY);
        return student;
    }

    @BeforeEach
    public void initTest() {
        student = createEntity(em);
    }

    @Test
    @Transactional
    void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();
        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);
        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testStudent.getDateOfBirthday()).isEqualTo(DEFAULT_DATE_OF_BIRTHDAY);
    }

    @Test
    @Transactional
    void createStudentWithExistingId() throws Exception {
        // Create the Student with an existing ID
        student.setId(1L);
        StudentDTO studentDTO = studentMapper.toDto(student);

        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setFirstName(null);

        // Create the Student, which fails.
        StudentDTO studentDTO = studentMapper.toDto(student);

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setLastName(null);

        // Create the Student, which fails.
        StudentDTO studentDTO = studentMapper.toDto(student);

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateOfBirthdayIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setDateOfBirthday(null);

        // Create the Student, which fails.
        StudentDTO studentDTO = studentMapper.toDto(student);

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirthday").value(hasItem(DEFAULT_DATE_OF_BIRTHDAY.toString())));
    }

    @Test
    @Transactional
    void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc
            .perform(get(ENTITY_API_URL_ID, student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.dateOfBirthday").value(DEFAULT_DATE_OF_BIRTHDAY.toString()));
    }

    @Test
    @Transactional
    void getStudentsByIdFiltering() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        Long id = student.getId();

        defaultStudentShouldBeFound("id.equals=" + id);
        defaultStudentShouldNotBeFound("id.notEquals=" + id);

        defaultStudentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName equals to DEFAULT_FIRST_NAME
        defaultStudentShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName equals to UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName not equals to DEFAULT_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName not equals to UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the studentList where firstName equals to UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName is not null
        defaultStudentShouldBeFound("firstName.specified=true");

        // Get all the studentList where firstName is null
        defaultStudentShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName contains DEFAULT_FIRST_NAME
        defaultStudentShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName contains UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName does not contain DEFAULT_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName does not contain UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName equals to DEFAULT_LAST_NAME
        defaultStudentShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName equals to UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName not equals to DEFAULT_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName not equals to UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the studentList where lastName equals to UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName is not null
        defaultStudentShouldBeFound("lastName.specified=true");

        // Get all the studentList where lastName is null
        defaultStudentShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName contains DEFAULT_LAST_NAME
        defaultStudentShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName contains UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName does not contain DEFAULT_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName does not contain UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByDateOfBirthdayIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirthday equals to DEFAULT_DATE_OF_BIRTHDAY
        defaultStudentShouldBeFound("dateOfBirthday.equals=" + DEFAULT_DATE_OF_BIRTHDAY);

        // Get all the studentList where dateOfBirthday equals to UPDATED_DATE_OF_BIRTHDAY
        defaultStudentShouldNotBeFound("dateOfBirthday.equals=" + UPDATED_DATE_OF_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllStudentsByDateOfBirthdayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirthday not equals to DEFAULT_DATE_OF_BIRTHDAY
        defaultStudentShouldNotBeFound("dateOfBirthday.notEquals=" + DEFAULT_DATE_OF_BIRTHDAY);

        // Get all the studentList where dateOfBirthday not equals to UPDATED_DATE_OF_BIRTHDAY
        defaultStudentShouldBeFound("dateOfBirthday.notEquals=" + UPDATED_DATE_OF_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllStudentsByDateOfBirthdayIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirthday in DEFAULT_DATE_OF_BIRTHDAY or UPDATED_DATE_OF_BIRTHDAY
        defaultStudentShouldBeFound("dateOfBirthday.in=" + DEFAULT_DATE_OF_BIRTHDAY + "," + UPDATED_DATE_OF_BIRTHDAY);

        // Get all the studentList where dateOfBirthday equals to UPDATED_DATE_OF_BIRTHDAY
        defaultStudentShouldNotBeFound("dateOfBirthday.in=" + UPDATED_DATE_OF_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllStudentsByDateOfBirthdayIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirthday is not null
        defaultStudentShouldBeFound("dateOfBirthday.specified=true");

        // Get all the studentList where dateOfBirthday is null
        defaultStudentShouldNotBeFound("dateOfBirthday.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByDateOfBirthdayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirthday is greater than or equal to DEFAULT_DATE_OF_BIRTHDAY
        defaultStudentShouldBeFound("dateOfBirthday.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTHDAY);

        // Get all the studentList where dateOfBirthday is greater than or equal to UPDATED_DATE_OF_BIRTHDAY
        defaultStudentShouldNotBeFound("dateOfBirthday.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllStudentsByDateOfBirthdayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirthday is less than or equal to DEFAULT_DATE_OF_BIRTHDAY
        defaultStudentShouldBeFound("dateOfBirthday.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTHDAY);

        // Get all the studentList where dateOfBirthday is less than or equal to SMALLER_DATE_OF_BIRTHDAY
        defaultStudentShouldNotBeFound("dateOfBirthday.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllStudentsByDateOfBirthdayIsLessThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirthday is less than DEFAULT_DATE_OF_BIRTHDAY
        defaultStudentShouldNotBeFound("dateOfBirthday.lessThan=" + DEFAULT_DATE_OF_BIRTHDAY);

        // Get all the studentList where dateOfBirthday is less than UPDATED_DATE_OF_BIRTHDAY
        defaultStudentShouldBeFound("dateOfBirthday.lessThan=" + UPDATED_DATE_OF_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllStudentsByDateOfBirthdayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirthday is greater than DEFAULT_DATE_OF_BIRTHDAY
        defaultStudentShouldNotBeFound("dateOfBirthday.greaterThan=" + DEFAULT_DATE_OF_BIRTHDAY);

        // Get all the studentList where dateOfBirthday is greater than SMALLER_DATE_OF_BIRTHDAY
        defaultStudentShouldBeFound("dateOfBirthday.greaterThan=" + SMALLER_DATE_OF_BIRTHDAY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentShouldBeFound(String filter) throws Exception {
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirthday").value(hasItem(DEFAULT_DATE_OF_BIRTHDAY.toString())));

        // Check, that the count call also returns 1
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentShouldNotBeFound(String filter) throws Exception {
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = studentRepository.findById(student.getId()).get();
        // Disconnect from session so that the updates on updatedStudent are not directly saved in db
        em.detach(updatedStudent);
        updatedStudent.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).dateOfBirthday(UPDATED_DATE_OF_BIRTHDAY);
        StudentDTO studentDTO = studentMapper.toDto(updatedStudent);

        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStudent.getDateOfBirthday()).isEqualTo(UPDATED_DATE_OF_BIRTHDAY);
    }

    @Test
    @Transactional
    void putNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentWithPatch() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student using partial update
        Student partialUpdatedStudent = new Student();
        partialUpdatedStudent.setId(student.getId());

        partialUpdatedStudent.dateOfBirthday(UPDATED_DATE_OF_BIRTHDAY);

        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudent))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testStudent.getDateOfBirthday()).isEqualTo(UPDATED_DATE_OF_BIRTHDAY);
    }

    @Test
    @Transactional
    void fullUpdateStudentWithPatch() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student using partial update
        Student partialUpdatedStudent = new Student();
        partialUpdatedStudent.setId(student.getId());

        partialUpdatedStudent.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).dateOfBirthday(UPDATED_DATE_OF_BIRTHDAY);

        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudent))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStudent.getDateOfBirthday()).isEqualTo(UPDATED_DATE_OF_BIRTHDAY);
    }

    @Test
    @Transactional
    void patchNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(studentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Delete the student
        restStudentMockMvc
            .perform(delete(ENTITY_API_URL_ID, student.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
