package by.skilsoft.jh.courses.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import by.skilsoft.jh.courses.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudyDTO.class);
        StudyDTO studyDTO1 = new StudyDTO();
        studyDTO1.setId(1L);
        StudyDTO studyDTO2 = new StudyDTO();
        assertThat(studyDTO1).isNotEqualTo(studyDTO2);
        studyDTO2.setId(studyDTO1.getId());
        assertThat(studyDTO1).isEqualTo(studyDTO2);
        studyDTO2.setId(2L);
        assertThat(studyDTO1).isNotEqualTo(studyDTO2);
        studyDTO1.setId(null);
        assertThat(studyDTO1).isNotEqualTo(studyDTO2);
    }
}
