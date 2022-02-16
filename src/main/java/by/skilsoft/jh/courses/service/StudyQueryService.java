package by.skilsoft.jh.courses.service;

import by.skilsoft.jh.courses.domain.*; // for static metamodels
import by.skilsoft.jh.courses.domain.Study;
import by.skilsoft.jh.courses.repository.StudyRepository;
import by.skilsoft.jh.courses.service.criteria.StudyCriteria;
import by.skilsoft.jh.courses.service.dto.StudyDTO;
import by.skilsoft.jh.courses.service.mapper.StudyMapper;
import by.skilsoft.jh.courses.util.CheckUserStatusUtil;
import by.skilsoft.jh.courses.web.rest.errors.BadRequestAlertException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Study} entities in the database.
 * The main input is a {@link StudyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudyDTO} or a {@link Page} of {@link StudyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudyQueryService extends QueryService<Study> {

    private final Logger log = LoggerFactory.getLogger(StudyQueryService.class);

    private final StudyRepository studyRepository;

    private final StudyMapper studyMapper;

    public StudyQueryService(StudyRepository studyRepository, StudyMapper studyMapper) {
        this.studyRepository = studyRepository;
        this.studyMapper = studyMapper;
    }

    /**
     * Return a {@link List} of {@link StudyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudyDTO> findByCriteria(StudyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Study> specification = createSpecification(criteria);
        return studyMapper.toDto(studyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StudyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudyDTO> findByCriteria(StudyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Study> specification = createSpecification(criteria);
        return studyRepository.findAll(specification, page).map(studyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Study> specification = createSpecification(criteria);
        return studyRepository.count(specification);
    }

    /**
     * Function to convert {@link StudyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Study> createSpecification(StudyCriteria criteria) {
        Specification<Study> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Study_.id));
            }

            if (criteria.getDateOfStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfStart(), Study_.dateOfStart));
            }
            if (criteria.getDateOfExam() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfExam(), Study_.dateOfExam));
            }
            if (criteria.getGrade() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGrade(), Study_.grade));
            }
            if (criteria.getCoursenameId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCoursenameId(), root -> root.join(Study_.coursename, JoinType.LEFT).get(Courses_.id))
                    );
            }

            if (criteria.getCoursesCourseName() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCoursesCourseName(),
                            root -> root.join(Study_.coursename, JoinType.LEFT).get(Courses_.courseName)
                        )
                    );
            }

            if (criteria.getStudentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStudentId(), root -> root.join(Study_.student, JoinType.LEFT).get(Student_.id))
                    );
            }

            if (criteria.getStudentFirstName() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStudentFirstName(),
                            root -> root.join(Study_.student, JoinType.LEFT).get(Student_.firstName)
                        )
                    );
            }
        }
        return specification;
    }
}
