package by.skilsoft.jh.courses.web.rest;

import by.skilsoft.jh.courses.repository.StudyRepository;
import by.skilsoft.jh.courses.service.StudyQueryService;
import by.skilsoft.jh.courses.service.StudyService;
import by.skilsoft.jh.courses.service.criteria.StudyCriteria;
import by.skilsoft.jh.courses.service.dto.StudyDTO;
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
 * REST controller for managing {@link by.skilsoft.jh.courses.domain.Study}.
 */
@RestController
@RequestMapping("/api")
public class StudyResource {

    private final Logger log = LoggerFactory.getLogger(StudyResource.class);

    private static final String ENTITY_NAME = "study";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudyService studyService;

    private final StudyRepository studyRepository;

    private final StudyQueryService studyQueryService;

    public StudyResource(StudyService studyService, StudyRepository studyRepository, StudyQueryService studyQueryService) {
        this.studyService = studyService;
        this.studyRepository = studyRepository;
        this.studyQueryService = studyQueryService;
    }

    /**
     * {@code POST  /studies} : Create a new study.
     *
     * @param studyDTO the studyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studyDTO, or with status {@code 400 (Bad Request)} if the study has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/studies")
    public ResponseEntity<StudyDTO> createStudy(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @Valid @RequestBody StudyDTO studyDTO
    ) throws URISyntaxException {
        log.debug("REST request to save Study : {}", studyDTO);
        if (studyDTO.getId() != null) {
            throw new BadRequestAlertException("A new study cannot already have an ID", ENTITY_NAME, "idexists");
        }
        studyDTO.setId(0L);
        StudyDTO result = studyService.save(studyDTO);
        return ResponseEntity
            .created(new URI("/api/studies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /studies/:id} : Updates an existing study.
     *
     * @param id the id of the studyDTO to save.
     * @param studyDTO the studyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studyDTO,
     * or with status {@code 400 (Bad Request)} if the studyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/studies/{id}")
    public ResponseEntity<StudyDTO> updateStudy(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudyDTO studyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Study : {}, {}", id, studyDTO);
        if (studyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudyDTO> result = studyService.partialUpdate(studyDTO);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studyDTO.getId().toString()))
            .body(result.get());
    }

    /**
     * {@code PATCH  /studies/:id} : Partial updates given fields of an existing study, field will ignore if it is null
     *
     * @param id the id of the studyDTO to save.
     * @param studyDTO the studyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studyDTO,
     * or with status {@code 400 (Bad Request)} if the studyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/studies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudyDTO> partialUpdateStudy(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudyDTO studyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Study partially : {}, {}", id, studyDTO);
        if (studyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudyDTO> result = studyService.partialUpdate(studyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /studies} : get all the studies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studies in body.
     */
    @GetMapping("/studies")
    public ResponseEntity<List<StudyDTO>> getAllStudies(
        @RequestHeader(value = "Authorization", required = false) String token,
        StudyCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get Studies by criteria: {}", criteria);
        Page<StudyDTO> page = studyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /studies/count} : count all the studies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/studies/count")
    public ResponseEntity<Long> countStudies(
        @RequestHeader(value = "Authorization", required = false) String token,
        StudyCriteria criteria
    ) {
        log.debug("REST request to count Studies by criteria: {}", criteria);
        return ResponseEntity.ok().body(studyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /studies/:id} : get the "id" study.
     *
     * @param id the id of the studyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/studies/{id}")
    public ResponseEntity<StudyDTO> getStudy(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @PathVariable Long id
    ) {
        log.debug("REST request to get Study : {}", id);
        Optional<StudyDTO> studyDTO = studyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studyDTO);
    }

    /**
     * {@code DELETE  /studies/:id} : delete the "id" study.
     *
     * @param id the id of the studyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/studies/{id}")
    public ResponseEntity<Void> deleteStudy(
        @RequestHeader(value = "Authorization", required = false) String bearerToken,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete Study : {}", id);
        studyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
