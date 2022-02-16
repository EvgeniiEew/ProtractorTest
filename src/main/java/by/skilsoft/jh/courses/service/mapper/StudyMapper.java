package by.skilsoft.jh.courses.service.mapper;

import by.skilsoft.jh.courses.domain.Study;
import by.skilsoft.jh.courses.service.dto.StudyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Study} and its DTO {@link StudyDTO}.
 */
@Mapper(componentModel = "spring", uses = { CoursesMapper.class, StudentMapper.class })
public interface StudyMapper extends EntityMapper<StudyDTO, Study> {
    @Mapping(target = "coursename", source = "coursename", qualifiedByName = "courseName")
    @Mapping(target = "student", source = "student", qualifiedByName = "firstName")
    StudyDTO toDto(Study s);
}
