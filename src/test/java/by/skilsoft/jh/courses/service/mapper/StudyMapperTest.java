package by.skilsoft.jh.courses.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudyMapperTest {

    private StudyMapper studyMapper;

    @BeforeEach
    public void setUp() {
        studyMapper = new StudyMapperImpl();
    }
}
