package by.skilsoft.jh.courses.service.mapper;

import by.skilsoft.jh.courses.domain.Provider;
import by.skilsoft.jh.courses.service.dto.ProviderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Provider} and its DTO {@link ProviderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProviderMapper extends EntityMapper<ProviderDTO, Provider> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProviderDTO toDtoName(Provider provider);
}
