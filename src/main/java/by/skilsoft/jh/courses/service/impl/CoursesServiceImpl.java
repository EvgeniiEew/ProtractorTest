package by.skilsoft.jh.courses.service.impl;

import by.skilsoft.jh.courses.domain.Courses;
import by.skilsoft.jh.courses.repository.CoursesRepository;
import by.skilsoft.jh.courses.service.CoursesService;
import by.skilsoft.jh.courses.service.dto.CoursesDTO;
import by.skilsoft.jh.courses.service.mapper.CoursesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Courses}.
 */
@Service
@Transactional
public class CoursesServiceImpl implements CoursesService {

    private final Logger log = LoggerFactory.getLogger(CoursesServiceImpl.class);

    private final CoursesRepository coursesRepository;

    private final CoursesMapper coursesMapper;

    public CoursesServiceImpl(CoursesRepository coursesRepository, CoursesMapper coursesMapper) {
        this.coursesRepository = coursesRepository;
        this.coursesMapper = coursesMapper;
    }

    @Override
    public CoursesDTO save(CoursesDTO coursesDTO) {
        log.debug("Request to save Courses : {}", coursesDTO);
        Courses courses = coursesMapper.toEntity(coursesDTO);
        courses = coursesRepository.save(courses);
        return coursesMapper.toDto(courses);
    }

    @Override
    public Optional<CoursesDTO> partialUpdate(CoursesDTO coursesDTO) {
        log.debug("Request to partially update Courses : {}", coursesDTO);

        return coursesRepository
            .findById(coursesDTO.getId())
            .map(existingCourses -> {
                coursesMapper.partialUpdate(existingCourses, coursesDTO);

                return existingCourses;
            })
            .map(coursesRepository::save)
            .map(coursesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CoursesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Courses");
        return coursesRepository.findAll(pageable).map(coursesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CoursesDTO> findOne(Long id) {
        log.debug("Request to get Courses : {}", id);
        return coursesRepository.findById(id).map(coursesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Courses : {}", id);
        coursesRepository.deleteById(id);
    }
}
