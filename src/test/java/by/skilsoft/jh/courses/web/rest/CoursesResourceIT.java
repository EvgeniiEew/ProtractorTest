package by.skilsoft.jh.courses.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import by.skilsoft.jh.courses.IntegrationTest;
import by.skilsoft.jh.courses.domain.Courses;
import by.skilsoft.jh.courses.domain.Provider;
import by.skilsoft.jh.courses.repository.CoursesRepository;
import by.skilsoft.jh.courses.service.criteria.CoursesCriteria;
import by.skilsoft.jh.courses.service.dto.CoursesDTO;
import by.skilsoft.jh.courses.service.mapper.CoursesMapper;
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
 * Integration tests for the {@link CoursesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoursesResourceIT {

    private static final String DEFAULT_COURSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_START = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_START = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_OF_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_END = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_END = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/courses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private CoursesMapper coursesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoursesMockMvc;

    private Courses courses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Courses createEntity(EntityManager em) {
        Courses courses = new Courses().courseName(DEFAULT_COURSE_NAME).dateOfStart(DEFAULT_DATE_OF_START).dateOfEnd(DEFAULT_DATE_OF_END);
        return courses;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Courses createUpdatedEntity(EntityManager em) {
        Courses courses = new Courses().courseName(UPDATED_COURSE_NAME).dateOfStart(UPDATED_DATE_OF_START).dateOfEnd(UPDATED_DATE_OF_END);
        return courses;
    }

    @BeforeEach
    public void initTest() {
        courses = createEntity(em);
    }

    @Test
    @Transactional
    void createCourses() throws Exception {
        int databaseSizeBeforeCreate = coursesRepository.findAll().size();
        // Create the Courses
        CoursesDTO coursesDTO = coursesMapper.toDto(courses);
        restCoursesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coursesDTO)))
            .andExpect(status().isCreated());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeCreate + 1);
        Courses testCourses = coursesList.get(coursesList.size() - 1);
        assertThat(testCourses.getCourseName()).isEqualTo(DEFAULT_COURSE_NAME);
        assertThat(testCourses.getDateOfStart()).isEqualTo(DEFAULT_DATE_OF_START);
        assertThat(testCourses.getDateOfEnd()).isEqualTo(DEFAULT_DATE_OF_END);
    }

    @Test
    @Transactional
    void createCoursesWithExistingId() throws Exception {
        // Create the Courses with an existing ID
        courses.setId(1L);
        CoursesDTO coursesDTO = coursesMapper.toDto(courses);

        int databaseSizeBeforeCreate = coursesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoursesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coursesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCourseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = coursesRepository.findAll().size();
        // set the field null
        courses.setCourseName(null);

        // Create the Courses, which fails.
        CoursesDTO coursesDTO = coursesMapper.toDto(courses);

        restCoursesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coursesDTO)))
            .andExpect(status().isBadRequest());

        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateOfStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = coursesRepository.findAll().size();
        // set the field null
        courses.setDateOfStart(null);

        // Create the Courses, which fails.
        CoursesDTO coursesDTO = coursesMapper.toDto(courses);

        restCoursesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coursesDTO)))
            .andExpect(status().isBadRequest());

        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateOfEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = coursesRepository.findAll().size();
        // set the field null
        courses.setDateOfEnd(null);

        // Create the Courses, which fails.
        CoursesDTO coursesDTO = coursesMapper.toDto(courses);

        restCoursesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coursesDTO)))
            .andExpect(status().isBadRequest());

        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourses() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList
        restCoursesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courses.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseName").value(hasItem(DEFAULT_COURSE_NAME)))
            .andExpect(jsonPath("$.[*].dateOfStart").value(hasItem(DEFAULT_DATE_OF_START.toString())))
            .andExpect(jsonPath("$.[*].dateOfEnd").value(hasItem(DEFAULT_DATE_OF_END.toString())));
    }

    @Test
    @Transactional
    void getCourses() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get the courses
        restCoursesMockMvc
            .perform(get(ENTITY_API_URL_ID, courses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courses.getId().intValue()))
            .andExpect(jsonPath("$.courseName").value(DEFAULT_COURSE_NAME))
            .andExpect(jsonPath("$.dateOfStart").value(DEFAULT_DATE_OF_START.toString()))
            .andExpect(jsonPath("$.dateOfEnd").value(DEFAULT_DATE_OF_END.toString()));
    }

    @Test
    @Transactional
    void getCoursesByIdFiltering() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        Long id = courses.getId();

        defaultCoursesShouldBeFound("id.equals=" + id);
        defaultCoursesShouldNotBeFound("id.notEquals=" + id);

        defaultCoursesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCoursesShouldNotBeFound("id.greaterThan=" + id);

        defaultCoursesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCoursesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCoursesByCourseNameIsEqualToSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where courseName equals to DEFAULT_COURSE_NAME
        defaultCoursesShouldBeFound("courseName.equals=" + DEFAULT_COURSE_NAME);

        // Get all the coursesList where courseName equals to UPDATED_COURSE_NAME
        defaultCoursesShouldNotBeFound("courseName.equals=" + UPDATED_COURSE_NAME);
    }

    @Test
    @Transactional
    void getAllCoursesByCourseNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where courseName not equals to DEFAULT_COURSE_NAME
        defaultCoursesShouldNotBeFound("courseName.notEquals=" + DEFAULT_COURSE_NAME);

        // Get all the coursesList where courseName not equals to UPDATED_COURSE_NAME
        defaultCoursesShouldBeFound("courseName.notEquals=" + UPDATED_COURSE_NAME);
    }

    @Test
    @Transactional
    void getAllCoursesByCourseNameIsInShouldWork() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where courseName in DEFAULT_COURSE_NAME or UPDATED_COURSE_NAME
        defaultCoursesShouldBeFound("courseName.in=" + DEFAULT_COURSE_NAME + "," + UPDATED_COURSE_NAME);

        // Get all the coursesList where courseName equals to UPDATED_COURSE_NAME
        defaultCoursesShouldNotBeFound("courseName.in=" + UPDATED_COURSE_NAME);
    }

    @Test
    @Transactional
    void getAllCoursesByCourseNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where courseName is not null
        defaultCoursesShouldBeFound("courseName.specified=true");

        // Get all the coursesList where courseName is null
        defaultCoursesShouldNotBeFound("courseName.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByCourseNameContainsSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where courseName contains DEFAULT_COURSE_NAME
        defaultCoursesShouldBeFound("courseName.contains=" + DEFAULT_COURSE_NAME);

        // Get all the coursesList where courseName contains UPDATED_COURSE_NAME
        defaultCoursesShouldNotBeFound("courseName.contains=" + UPDATED_COURSE_NAME);
    }

    @Test
    @Transactional
    void getAllCoursesByCourseNameNotContainsSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where courseName does not contain DEFAULT_COURSE_NAME
        defaultCoursesShouldNotBeFound("courseName.doesNotContain=" + DEFAULT_COURSE_NAME);

        // Get all the coursesList where courseName does not contain UPDATED_COURSE_NAME
        defaultCoursesShouldBeFound("courseName.doesNotContain=" + UPDATED_COURSE_NAME);
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfStartIsEqualToSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfStart equals to DEFAULT_DATE_OF_START
        defaultCoursesShouldBeFound("dateOfStart.equals=" + DEFAULT_DATE_OF_START);

        // Get all the coursesList where dateOfStart equals to UPDATED_DATE_OF_START
        defaultCoursesShouldNotBeFound("dateOfStart.equals=" + UPDATED_DATE_OF_START);
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfStartIsNotEqualToSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfStart not equals to DEFAULT_DATE_OF_START
        defaultCoursesShouldNotBeFound("dateOfStart.notEquals=" + DEFAULT_DATE_OF_START);

        // Get all the coursesList where dateOfStart not equals to UPDATED_DATE_OF_START
        defaultCoursesShouldBeFound("dateOfStart.notEquals=" + UPDATED_DATE_OF_START);
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfStartIsInShouldWork() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfStart in DEFAULT_DATE_OF_START or UPDATED_DATE_OF_START
        defaultCoursesShouldBeFound("dateOfStart.in=" + DEFAULT_DATE_OF_START + "," + UPDATED_DATE_OF_START);

        // Get all the coursesList where dateOfStart equals to UPDATED_DATE_OF_START
        defaultCoursesShouldNotBeFound("dateOfStart.in=" + UPDATED_DATE_OF_START);
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfStart is not null
        defaultCoursesShouldBeFound("dateOfStart.specified=true");

        // Get all the coursesList where dateOfStart is null
        defaultCoursesShouldNotBeFound("dateOfStart.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfStartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfStart is greater than or equal to DEFAULT_DATE_OF_START
        defaultCoursesShouldBeFound("dateOfStart.greaterThanOrEqual=" + DEFAULT_DATE_OF_START);

        // Get all the coursesList where dateOfStart is greater than or equal to UPDATED_DATE_OF_START
        defaultCoursesShouldNotBeFound("dateOfStart.greaterThanOrEqual=" + UPDATED_DATE_OF_START);
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfStartIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfStart is less than or equal to DEFAULT_DATE_OF_START
        defaultCoursesShouldBeFound("dateOfStart.lessThanOrEqual=" + DEFAULT_DATE_OF_START);

        // Get all the coursesList where dateOfStart is less than or equal to SMALLER_DATE_OF_START
        defaultCoursesShouldNotBeFound("dateOfStart.lessThanOrEqual=" + SMALLER_DATE_OF_START);
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfStartIsLessThanSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfStart is less than DEFAULT_DATE_OF_START
        defaultCoursesShouldNotBeFound("dateOfStart.lessThan=" + DEFAULT_DATE_OF_START);

        // Get all the coursesList where dateOfStart is less than UPDATED_DATE_OF_START
        defaultCoursesShouldBeFound("dateOfStart.lessThan=" + UPDATED_DATE_OF_START);
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfStartIsGreaterThanSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfStart is greater than DEFAULT_DATE_OF_START
        defaultCoursesShouldNotBeFound("dateOfStart.greaterThan=" + DEFAULT_DATE_OF_START);

        // Get all the coursesList where dateOfStart is greater than SMALLER_DATE_OF_START
        defaultCoursesShouldBeFound("dateOfStart.greaterThan=" + SMALLER_DATE_OF_START);
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfEndIsEqualToSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfEnd equals to DEFAULT_DATE_OF_END
        defaultCoursesShouldBeFound("dateOfEnd.equals=" + DEFAULT_DATE_OF_END);

        // Get all the coursesList where dateOfEnd equals to UPDATED_DATE_OF_END
        defaultCoursesShouldNotBeFound("dateOfEnd.equals=" + UPDATED_DATE_OF_END);
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfEndIsNotEqualToSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfEnd not equals to DEFAULT_DATE_OF_END
        defaultCoursesShouldNotBeFound("dateOfEnd.notEquals=" + DEFAULT_DATE_OF_END);

        // Get all the coursesList where dateOfEnd not equals to UPDATED_DATE_OF_END
        defaultCoursesShouldBeFound("dateOfEnd.notEquals=" + UPDATED_DATE_OF_END);
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfEndIsInShouldWork() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfEnd in DEFAULT_DATE_OF_END or UPDATED_DATE_OF_END
        defaultCoursesShouldBeFound("dateOfEnd.in=" + DEFAULT_DATE_OF_END + "," + UPDATED_DATE_OF_END);

        // Get all the coursesList where dateOfEnd equals to UPDATED_DATE_OF_END
        defaultCoursesShouldNotBeFound("dateOfEnd.in=" + UPDATED_DATE_OF_END);
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfEnd is not null
        defaultCoursesShouldBeFound("dateOfEnd.specified=true");

        // Get all the coursesList where dateOfEnd is null
        defaultCoursesShouldNotBeFound("dateOfEnd.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfEndIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfEnd is greater than or equal to DEFAULT_DATE_OF_END
        defaultCoursesShouldBeFound("dateOfEnd.greaterThanOrEqual=" + DEFAULT_DATE_OF_END);

        // Get all the coursesList where dateOfEnd is greater than or equal to UPDATED_DATE_OF_END
        defaultCoursesShouldNotBeFound("dateOfEnd.greaterThanOrEqual=" + UPDATED_DATE_OF_END);
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfEndIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfEnd is less than or equal to DEFAULT_DATE_OF_END
        defaultCoursesShouldBeFound("dateOfEnd.lessThanOrEqual=" + DEFAULT_DATE_OF_END);

        // Get all the coursesList where dateOfEnd is less than or equal to SMALLER_DATE_OF_END
        defaultCoursesShouldNotBeFound("dateOfEnd.lessThanOrEqual=" + SMALLER_DATE_OF_END);
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfEndIsLessThanSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfEnd is less than DEFAULT_DATE_OF_END
        defaultCoursesShouldNotBeFound("dateOfEnd.lessThan=" + DEFAULT_DATE_OF_END);

        // Get all the coursesList where dateOfEnd is less than UPDATED_DATE_OF_END
        defaultCoursesShouldBeFound("dateOfEnd.lessThan=" + UPDATED_DATE_OF_END);
    }

    @Test
    @Transactional
    void getAllCoursesByDateOfEndIsGreaterThanSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        // Get all the coursesList where dateOfEnd is greater than DEFAULT_DATE_OF_END
        defaultCoursesShouldNotBeFound("dateOfEnd.greaterThan=" + DEFAULT_DATE_OF_END);

        // Get all the coursesList where dateOfEnd is greater than SMALLER_DATE_OF_END
        defaultCoursesShouldBeFound("dateOfEnd.greaterThan=" + SMALLER_DATE_OF_END);
    }

    @Test
    @Transactional
    void getAllCoursesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);
        Provider name;
        if (TestUtil.findAll(em, Provider.class).isEmpty()) {
            name = ProviderResourceIT.createEntity(em);
            em.persist(name);
            em.flush();
        } else {
            name = TestUtil.findAll(em, Provider.class).get(0);
        }
        em.persist(name);
        em.flush();
        courses.setName(name);
        coursesRepository.saveAndFlush(courses);
        Long nameId = name.getId();

        // Get all the coursesList where name equals to nameId
        defaultCoursesShouldBeFound("nameId.equals=" + nameId);

        // Get all the coursesList where name equals to (nameId + 1)
        defaultCoursesShouldNotBeFound("nameId.equals=" + (nameId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCoursesShouldBeFound(String filter) throws Exception {
        restCoursesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courses.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseName").value(hasItem(DEFAULT_COURSE_NAME)))
            .andExpect(jsonPath("$.[*].dateOfStart").value(hasItem(DEFAULT_DATE_OF_START.toString())))
            .andExpect(jsonPath("$.[*].dateOfEnd").value(hasItem(DEFAULT_DATE_OF_END.toString())));

        // Check, that the count call also returns 1
        restCoursesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCoursesShouldNotBeFound(String filter) throws Exception {
        restCoursesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCoursesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCourses() throws Exception {
        // Get the courses
        restCoursesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourses() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();

        // Update the courses
        Courses updatedCourses = coursesRepository.findById(courses.getId()).get();
        // Disconnect from session so that the updates on updatedCourses are not directly saved in db
        em.detach(updatedCourses);
        updatedCourses.courseName(UPDATED_COURSE_NAME).dateOfStart(UPDATED_DATE_OF_START).dateOfEnd(UPDATED_DATE_OF_END);
        CoursesDTO coursesDTO = coursesMapper.toDto(updatedCourses);

        restCoursesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coursesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coursesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
        Courses testCourses = coursesList.get(coursesList.size() - 1);
        assertThat(testCourses.getCourseName()).isEqualTo(UPDATED_COURSE_NAME);
        assertThat(testCourses.getDateOfStart()).isEqualTo(UPDATED_DATE_OF_START);
        assertThat(testCourses.getDateOfEnd()).isEqualTo(UPDATED_DATE_OF_END);
    }

    @Test
    @Transactional
    void putNonExistingCourses() throws Exception {
        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();
        courses.setId(count.incrementAndGet());

        // Create the Courses
        CoursesDTO coursesDTO = coursesMapper.toDto(courses);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoursesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coursesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coursesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourses() throws Exception {
        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();
        courses.setId(count.incrementAndGet());

        // Create the Courses
        CoursesDTO coursesDTO = coursesMapper.toDto(courses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coursesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourses() throws Exception {
        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();
        courses.setId(count.incrementAndGet());

        // Create the Courses
        CoursesDTO coursesDTO = coursesMapper.toDto(courses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coursesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoursesWithPatch() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();

        // Update the courses using partial update
        Courses partialUpdatedCourses = new Courses();
        partialUpdatedCourses.setId(courses.getId());

        partialUpdatedCourses.dateOfStart(UPDATED_DATE_OF_START);

        restCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourses))
            )
            .andExpect(status().isOk());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
        Courses testCourses = coursesList.get(coursesList.size() - 1);
        assertThat(testCourses.getCourseName()).isEqualTo(DEFAULT_COURSE_NAME);
        assertThat(testCourses.getDateOfStart()).isEqualTo(UPDATED_DATE_OF_START);
        assertThat(testCourses.getDateOfEnd()).isEqualTo(DEFAULT_DATE_OF_END);
    }

    @Test
    @Transactional
    void fullUpdateCoursesWithPatch() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();

        // Update the courses using partial update
        Courses partialUpdatedCourses = new Courses();
        partialUpdatedCourses.setId(courses.getId());

        partialUpdatedCourses.courseName(UPDATED_COURSE_NAME).dateOfStart(UPDATED_DATE_OF_START).dateOfEnd(UPDATED_DATE_OF_END);

        restCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourses))
            )
            .andExpect(status().isOk());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
        Courses testCourses = coursesList.get(coursesList.size() - 1);
        assertThat(testCourses.getCourseName()).isEqualTo(UPDATED_COURSE_NAME);
        assertThat(testCourses.getDateOfStart()).isEqualTo(UPDATED_DATE_OF_START);
        assertThat(testCourses.getDateOfEnd()).isEqualTo(UPDATED_DATE_OF_END);
    }

    @Test
    @Transactional
    void patchNonExistingCourses() throws Exception {
        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();
        courses.setId(count.incrementAndGet());

        // Create the Courses
        CoursesDTO coursesDTO = coursesMapper.toDto(courses);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coursesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coursesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourses() throws Exception {
        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();
        courses.setId(count.incrementAndGet());

        // Create the Courses
        CoursesDTO coursesDTO = coursesMapper.toDto(courses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coursesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourses() throws Exception {
        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();
        courses.setId(count.incrementAndGet());

        // Create the Courses
        CoursesDTO coursesDTO = coursesMapper.toDto(courses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(coursesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourses() throws Exception {
        // Initialize the database
        coursesRepository.saveAndFlush(courses);

        int databaseSizeBeforeDelete = coursesRepository.findAll().size();

        // Delete the courses
        restCoursesMockMvc
            .perform(delete(ENTITY_API_URL_ID, courses.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
