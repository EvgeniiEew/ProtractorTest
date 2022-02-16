package by.skilsoft.jh.courses.service;

import by.skilsoft.jh.courses.domain.*; // for static metamodels
import by.skilsoft.jh.courses.domain.Courses;
import by.skilsoft.jh.courses.repository.CoursesRepository;
import by.skilsoft.jh.courses.service.criteria.CoursesCriteria;
import by.skilsoft.jh.courses.service.dto.CoursesDTO;
import by.skilsoft.jh.courses.service.mapper.CoursesMapper;
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
 * Service for executing complex queries for {@link Courses} entities in the database.
 * The main input is a {@link CoursesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CoursesDTO} or a {@link Page} of {@link CoursesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CoursesQueryService extends QueryService<Courses> {

    private final Logger log = LoggerFactory.getLogger(CoursesQueryService.class);

    private final CoursesRepository coursesRepository;

    private final CoursesMapper coursesMapper;

    public CoursesQueryService(CoursesRepository coursesRepository, CoursesMapper coursesMapper) {
        this.coursesRepository = coursesRepository;
        this.coursesMapper = coursesMapper;
    }

    /**
     * Return a {@link List} of {@link CoursesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CoursesDTO> findByCriteria(CoursesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Courses> specification = createSpecification(criteria);
        return coursesMapper.toDto(coursesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CoursesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CoursesDTO> findByCriteria(CoursesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Courses> specification = createSpecification(criteria);
        return coursesRepository.findAll(specification, page).map(coursesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CoursesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Courses> specification = createSpecification(criteria);
        return coursesRepository.count(specification);
    }

    /**
     * Function to convert {@link CoursesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Courses> createSpecification(CoursesCriteria criteria) {
        Specification<Courses> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Courses_.id));
            }

            if (criteria.getCourseName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCourseName(), Courses_.courseName));
            }
            if (criteria.getDateOfStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfStart(), Courses_.dateOfStart));
            }
            if (criteria.getDateOfEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfEnd(), Courses_.dateOfEnd));
            }
            if (criteria.getNameId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNameId(), root -> root.join(Courses_.name, JoinType.LEFT).get(Provider_.id))
                    );
            }

            if (criteria.getProviderName() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProviderName(), root -> root.join(Courses_.name, JoinType.LEFT).get(Provider_.name))
                    );
            }
        }
        return specification;
    }
}
