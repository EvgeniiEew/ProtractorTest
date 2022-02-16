package by.skilsoft.jh.courses.service.mapper;

import by.skilsoft.jh.courses.domain.Courses;
import by.skilsoft.jh.courses.service.dto.CoursesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Courses} and its DTO {@link CoursesDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProviderMapper.class })
public interface CoursesMapper extends EntityMapper<CoursesDTO, Courses> {
    @Mapping(target = "name", source = "name", qualifiedByName = "name")
    CoursesDTO toDto(Courses s);

    @Named("courseName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "courseName", source = "courseName")
    CoursesDTO toDtoCourseName(Courses courses);
}
