package by.skilsoft.jh.courses.service.mapper;

import by.skilsoft.jh.courses.domain.Student;
import by.skilsoft.jh.courses.service.dto.StudentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Student} and its DTO {@link StudentDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StudentMapper extends EntityMapper<StudentDTO, Student> {
    @Named("firstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    StudentDTO toDtoFirstName(Student student);
}
