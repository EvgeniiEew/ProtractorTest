package by.skilsoft.jh.courses.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProviderMapperTest {

    private ProviderMapper providerMapper;

    @BeforeEach
    public void setUp() {
        providerMapper = new ProviderMapperImpl();
    }
}
