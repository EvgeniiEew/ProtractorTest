package by.skilsoft.jh.courses.service.impl;

import by.skilsoft.jh.courses.domain.Study;
import by.skilsoft.jh.courses.repository.StudyRepository;
import by.skilsoft.jh.courses.service.StudyService;
import by.skilsoft.jh.courses.service.dto.StudyDTO;
import by.skilsoft.jh.courses.service.mapper.StudyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Study}.
 */
@Service
@Transactional
public class StudyServiceImpl implements StudyService {

    private final Logger log = LoggerFactory.getLogger(StudyServiceImpl.class);

    private final StudyRepository studyRepository;

    private final StudyMapper studyMapper;

    public StudyServiceImpl(StudyRepository studyRepository, StudyMapper studyMapper) {
        this.studyRepository = studyRepository;
        this.studyMapper = studyMapper;
    }

    @Override
    public StudyDTO save(StudyDTO studyDTO) {
        log.debug("Request to save Study : {}", studyDTO);
        Study study = studyMapper.toEntity(studyDTO);
        study = studyRepository.save(study);
        return studyMapper.toDto(study);
    }

    @Override
    public Optional<StudyDTO> partialUpdate(StudyDTO studyDTO) {
        log.debug("Request to partially update Study : {}", studyDTO);

        return studyRepository
            .findById(studyDTO.getId())
            .map(existingStudy -> {
                studyMapper.partialUpdate(existingStudy, studyDTO);

                return existingStudy;
            })
            .map(studyRepository::save)
            .map(studyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Studies");
        return studyRepository.findAll(pageable).map(studyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudyDTO> findOne(Long id) {
        log.debug("Request to get Study : {}", id);
        return studyRepository.findById(id).map(studyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Study : {}", id);
        studyRepository.deleteById(id);
    }
}
