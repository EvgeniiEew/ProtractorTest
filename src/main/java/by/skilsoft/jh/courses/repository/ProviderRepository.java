package by.skilsoft.jh.courses.repository;

import by.skilsoft.jh.courses.domain.Provider;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Provider entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long>, JpaSpecificationExecutor<Provider> {
    @PostAuthorize(
        "hasPermission(returnObject, 'READ') or hasPermission(returnObject, 'WRITE') or hasPermission(returnObject, 'DELETE') or hasPermission(returnObject, 'ADMINISTRATION') or hasAuthority('ROLE_ADMIN')"
    )
    @Query("select provider from Provider provider where provider.id =:id")
    Optional<Provider> findOneWithPermission(@Param("id") Long id);

    @Query("select provider from Provider provider where provider.id =:id")
    Optional<Provider> findOne(@Param("id") Long id);
}
