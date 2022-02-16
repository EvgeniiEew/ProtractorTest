package by.skilsoft.jh.courses.repository;

import by.skilsoft.jh.courses.domain.Study;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Study entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudyRepository extends JpaRepository<Study, Long>, JpaSpecificationExecutor<Study> {
    @PostAuthorize(
        "hasPermission(returnObject, 'READ') or hasPermission(returnObject, 'WRITE') or hasPermission(returnObject, 'DELETE') or hasPermission(returnObject, 'ADMINISTRATION') or hasAuthority('ROLE_ADMIN')"
    )
    @Query("select study from Study study where study.id =:id")
    Optional<Study> findOneWithPermission(@Param("id") Long id);

    @Query("select study from Study study where study.id =:id")
    Optional<Study> findOne(@Param("id") Long id);
}
