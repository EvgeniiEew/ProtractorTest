package by.skilsoft.jh.courses.web.rest;

import by.skilsoft.jh.courses.repository.StudentRepository;
import by.skilsoft.jh.courses.service.StudentQueryService;
import by.skilsoft.jh.courses.service.StudentService;
import by.skilsoft.jh.courses.service.criteria.StudentCriteria;
import by.skilsoft.jh.courses.service.dto.StudentDTO;
import by.skilsoft.jh.courses.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link by.skilsoft.jh.courses.domain.Student}.
 */
@RestController
@RequestMapping("/api")
public class StudentResource {

    private final Logger log = LoggerFactory.getLogger(StudentResource.class);

    private static final String ENTITY_NAME = "student";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentService studentService;

    private final StudentRepository studentRepository;

    private final StudentQueryService studentQueryService;

    public StudentResource(StudentService studentService, StudentRepository studentRepository, StudentQueryService studentQueryService) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
        this.studentQueryService = studentQueryService;
    }

    /**
     * {@code POST  /students} : Create a new student.
     *
     * @param studentDTO the studentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentDTO, or with status {@code 400 (Bad Request)} if the student has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/students")
    public ResponseEntity<StudentDTO> createStudent(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @Valid @RequestBody StudentDTO studentDTO
    ) throws URISyntaxException {
        log.debug("REST request to save Student : {}", studentDTO);
        if (studentDTO.getId() != null) {
            throw new BadRequestAlertException("A new student cannot already have an ID", ENTITY_NAME, "idexists");
        }
        studentDTO.setId(0L);
        StudentDTO result = studentService.save(studentDTO);
        return ResponseEntity
            .created(new URI("/api/students/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /students/:id} : Updates an existing student.
     *
     * @param id the id of the studentDTO to save.
     * @param studentDTO the studentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentDTO,
     * or with status {@code 400 (Bad Request)} if the studentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/students/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudentDTO studentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Student : {}, {}", id, studentDTO);
        if (studentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentDTO> result = studentService.partialUpdate(studentDTO);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentDTO.getId().toString()))
            .body(result.get());
    }

    /**
     * {@code PATCH  /students/:id} : Partial updates given fields of an existing student, field will ignore if it is null
     *
     * @param id the id of the studentDTO to save.
     * @param studentDTO the studentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentDTO,
     * or with status {@code 400 (Bad Request)} if the studentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/students/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentDTO> partialUpdateStudent(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudentDTO studentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Student partially : {}, {}", id, studentDTO);
        if (studentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentDTO> result = studentService.partialUpdate(studentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /students} : get all the students.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of students in body.
     */
    @GetMapping("/students")
    public ResponseEntity<List<StudentDTO>> getAllStudents(
        @RequestHeader(value = "Authorization", required = false) String token,
        StudentCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get Students by criteria: {}", criteria);
        Page<StudentDTO> page = studentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /students/count} : count all the students.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/students/count")
    public ResponseEntity<Long> countStudents(
        @RequestHeader(value = "Authorization", required = false) String token,
        StudentCriteria criteria
    ) {
        log.debug("REST request to count Students by criteria: {}", criteria);
        return ResponseEntity.ok().body(studentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /students/:id} : get the "id" student.
     *
     * @param id the id of the studentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDTO> getStudent(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @PathVariable Long id
    ) {
        log.debug("REST request to get Student : {}", id);
        Optional<StudentDTO> studentDTO = studentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentDTO);
    }

    /**
     * {@code DELETE  /students/:id} : delete the "id" student.
     *
     * @param id the id of the studentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete Student : {}", id);
        studentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}