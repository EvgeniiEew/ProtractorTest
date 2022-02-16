package by.skilsoft.jh.courses.service;

import by.skilsoft.jh.courses.domain.*; // for static metamodels
import by.skilsoft.jh.courses.domain.Provider;
import by.skilsoft.jh.courses.repository.ProviderRepository;
import by.skilsoft.jh.courses.service.criteria.ProviderCriteria;
import by.skilsoft.jh.courses.service.dto.ProviderDTO;
import by.skilsoft.jh.courses.service.mapper.ProviderMapper;
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
 * Service for executing complex queries for {@link Provider} entities in the database.
 * The main input is a {@link ProviderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProviderDTO} or a {@link Page} of {@link ProviderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProviderQueryService extends QueryService<Provider> {

    private final Logger log = LoggerFactory.getLogger(ProviderQueryService.class);

    private final ProviderRepository providerRepository;

    private final ProviderMapper providerMapper;

    public ProviderQueryService(ProviderRepository providerRepository, ProviderMapper providerMapper) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
    }

    /**
     * Return a {@link List} of {@link ProviderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProviderDTO> findByCriteria(ProviderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Provider> specification = createSpecification(criteria);
        return providerMapper.toDto(providerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProviderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProviderDTO> findByCriteria(ProviderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Provider> specification = createSpecification(criteria);
        return providerRepository.findAll(specification, page).map(providerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProviderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Provider> specification = createSpecification(criteria);
        return providerRepository.count(specification);
    }

    /**
     * Function to convert {@link ProviderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Provider> createSpecification(ProviderCriteria criteria) {
        Specification<Provider> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Provider_.id));
            }

            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Provider_.name));
            }
        }
        return specification;
    }
}
