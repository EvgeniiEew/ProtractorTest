package by.skilsoft.jh.courses.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoursesMapperTest {

    private CoursesMapper coursesMapper;

    @BeforeEach
    public void setUp() {
        coursesMapper = new CoursesMapperImpl();
    }
}
