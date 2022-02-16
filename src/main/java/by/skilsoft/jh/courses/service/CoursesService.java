package by.skilsoft.jh.courses.service;

import by.skilsoft.jh.courses.service.dto.CoursesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link by.skilsoft.jh.courses.domain.Courses}.
 */
public interface CoursesService {
    /**
     * Save a courses.
     *
     * @param coursesDTO the entity to save.
     * @return the persisted entity.
     */
    CoursesDTO save(CoursesDTO coursesDTO);

    /**
     * Partially updates a courses.
     *
     * @param coursesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CoursesDTO> partialUpdate(CoursesDTO coursesDTO);

    /**
     * Get all the courses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CoursesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" courses.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CoursesDTO> findOne(Long id);

    /**
     * Delete the "id" courses.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
