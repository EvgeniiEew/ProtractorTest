package by.skilsoft.jh.courses.web.rest;

import by.skilsoft.jh.courses.repository.CoursesRepository;
import by.skilsoft.jh.courses.service.CoursesQueryService;
import by.skilsoft.jh.courses.service.CoursesService;
import by.skilsoft.jh.courses.service.criteria.CoursesCriteria;
import by.skilsoft.jh.courses.service.dto.CoursesDTO;
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
 * REST controller for managing {@link by.skilsoft.jh.courses.domain.Courses}.
 */
@RestController
@RequestMapping("/api")
public class CoursesResource {

    private final Logger log = LoggerFactory.getLogger(CoursesResource.class);

    private static final String ENTITY_NAME = "courses";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoursesService coursesService;

    private final CoursesRepository coursesRepository;

    private final CoursesQueryService coursesQueryService;

    public CoursesResource(CoursesService coursesService, CoursesRepository coursesRepository, CoursesQueryService coursesQueryService) {
        this.coursesService = coursesService;
        this.coursesRepository = coursesRepository;
        this.coursesQueryService = coursesQueryService;
    }

    /**
     * {@code POST  /courses} : Create a new courses.
     *
     * @param coursesDTO the coursesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coursesDTO, or with status {@code 400 (Bad Request)} if the courses has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courses")
    public ResponseEntity<CoursesDTO> createCourses(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @Valid @RequestBody CoursesDTO coursesDTO
    ) throws URISyntaxException {
        log.debug("REST request to save Courses : {}", coursesDTO);
        if (coursesDTO.getId() != null) {
            throw new BadRequestAlertException("A new courses cannot already have an ID", ENTITY_NAME, "idexists");
        }
        coursesDTO.setId(0L);
        CoursesDTO result = coursesService.save(coursesDTO);
        return ResponseEntity
            .created(new URI("/api/courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /courses/:id} : Updates an existing courses.
     *
     * @param id the id of the coursesDTO to save.
     * @param coursesDTO the coursesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coursesDTO,
     * or with status {@code 400 (Bad Request)} if the coursesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coursesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/courses/{id}")
    public ResponseEntity<CoursesDTO> updateCourses(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CoursesDTO coursesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Courses : {}, {}", id, coursesDTO);
        if (coursesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coursesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coursesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoursesDTO> result = coursesService.partialUpdate(coursesDTO);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coursesDTO.getId().toString()))
            .body(result.get());
    }

    /**
     * {@code PATCH  /courses/:id} : Partial updates given fields of an existing courses, field will ignore if it is null
     *
     * @param id the id of the coursesDTO to save.
     * @param coursesDTO the coursesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coursesDTO,
     * or with status {@code 400 (Bad Request)} if the coursesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the coursesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the coursesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/courses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CoursesDTO> partialUpdateCourses(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CoursesDTO coursesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Courses partially : {}, {}", id, coursesDTO);
        if (coursesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coursesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coursesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoursesDTO> result = coursesService.partialUpdate(coursesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coursesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /courses} : get all the courses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courses in body.
     */
    @GetMapping("/courses")
    public ResponseEntity<List<CoursesDTO>> getAllCourses(
        @RequestHeader(value = "Authorization", required = false) String token,
        CoursesCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get Courses by criteria: {}", criteria);
        Page<CoursesDTO> page = coursesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /courses/count} : count all the courses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/courses/count")
    public ResponseEntity<Long> countCourses(
        @RequestHeader(value = "Authorization", required = false) String token,
        CoursesCriteria criteria
    ) {
        log.debug("REST request to count Courses by criteria: {}", criteria);
        return ResponseEntity.ok().body(coursesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /courses/:id} : get the "id" courses.
     *
     * @param id the id of the coursesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coursesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{id}")
    public ResponseEntity<CoursesDTO> getCourses(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @PathVariable Long id
    ) {
        log.debug("REST request to get Courses : {}", id);
        Optional<CoursesDTO> coursesDTO = coursesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coursesDTO);
    }

    /**
     * {@code DELETE  /courses/:id} : delete the "id" courses.
     *
     * @param id the id of the coursesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourses(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete Courses : {}", id);
        coursesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
