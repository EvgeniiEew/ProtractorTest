package by.skilsoft.jh.courses.repository;

import by.skilsoft.jh.courses.domain.Courses;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Courses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoursesRepository extends JpaRepository<Courses, Long>, JpaSpecificationExecutor<Courses> {
    @PostAuthorize(
        "hasPermission(returnObject, 'READ') or hasPermission(returnObject, 'WRITE') or hasPermission(returnObject, 'DELETE') or hasPermission(returnObject, 'ADMINISTRATION') or hasAuthority('ROLE_ADMIN')"
    )
    @Query("select courses from Courses courses where courses.id =:id")
    Optional<Courses> findOneWithPermission(@Param("id") Long id);

    @Query("select courses from Courses courses where courses.id =:id")
    Optional<Courses> findOne(@Param("id") Long id);
}
