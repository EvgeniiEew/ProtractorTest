package by.skilsoft.jh.courses.service.impl;

import by.skilsoft.jh.courses.domain.Provider;
import by.skilsoft.jh.courses.repository.ProviderRepository;
import by.skilsoft.jh.courses.service.ProviderService;
import by.skilsoft.jh.courses.service.dto.ProviderDTO;
import by.skilsoft.jh.courses.service.mapper.ProviderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Provider}.
 */
@Service
@Transactional
public class ProviderServiceImpl implements ProviderService {

    private final Logger log = LoggerFactory.getLogger(ProviderServiceImpl.class);

    private final ProviderRepository providerRepository;

    private final ProviderMapper providerMapper;

    public ProviderServiceImpl(ProviderRepository providerRepository, ProviderMapper providerMapper) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
    }

    @Override
    public ProviderDTO save(ProviderDTO providerDTO) {
        log.debug("Request to save Provider : {}", providerDTO);
        Provider provider = providerMapper.toEntity(providerDTO);
        provider = providerRepository.save(provider);
        return providerMapper.toDto(provider);
    }

    @Override
    public Optional<ProviderDTO> partialUpdate(ProviderDTO providerDTO) {
        log.debug("Request to partially update Provider : {}", providerDTO);

        return providerRepository
            .findById(providerDTO.getId())
            .map(existingProvider -> {
                providerMapper.partialUpdate(existingProvider, providerDTO);

                return existingProvider;
            })
            .map(providerRepository::save)
            .map(providerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProviderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Providers");
        return providerRepository.findAll(pageable).map(providerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProviderDTO> findOne(Long id) {
        log.debug("Request to get Provider : {}", id);
        return providerRepository.findById(id).map(providerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Provider : {}", id);
        providerRepository.deleteById(id);
    }
}
