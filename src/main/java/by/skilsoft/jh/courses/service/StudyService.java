package by.skilsoft.jh.courses.service;

import by.skilsoft.jh.courses.service.dto.StudyDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link by.skilsoft.jh.courses.domain.Study}.
 */
public interface StudyService {
    /**
     * Save a study.
     *
     * @param studyDTO the entity to save.
     * @return the persisted entity.
     */
    StudyDTO save(StudyDTO studyDTO);

    /**
     * Partially updates a study.
     *
     * @param studyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StudyDTO> partialUpdate(StudyDTO studyDTO);

    /**
     * Get all the studies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StudyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" study.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudyDTO> findOne(Long id);

    /**
     * Delete the "id" study.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
