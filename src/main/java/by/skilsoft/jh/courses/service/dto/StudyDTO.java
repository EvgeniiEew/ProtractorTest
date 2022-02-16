package by.skilsoft.jh.courses.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link by.skilsoft.jh.courses.domain.Study} entity.
 */
public class StudyDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateOfStart;

    @NotNull
    private LocalDate dateOfExam;

    @NotNull
    private Integer grade;

    private CoursesDTO coursename;

    private StudentDTO student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOfStart() {
        return dateOfStart;
    }

    public void setDateOfStart(LocalDate dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public LocalDate getDateOfExam() {
        return dateOfExam;
    }

    public void setDateOfExam(LocalDate dateOfExam) {
        this.dateOfExam = dateOfExam;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public CoursesDTO getCoursename() {
        return coursename;
    }

    public void setCoursename(CoursesDTO coursename) {
        this.coursename = coursename;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudyDTO)) {
            return false;
        }

        StudyDTO studyDTO = (StudyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudyDTO{" +
            "id=" + getId() +
            ", dateOfStart='" + getDateOfStart() + "'" +
            ", dateOfExam='" + getDateOfExam() + "'" +
            ", grade=" + getGrade() +
            ", coursename=" + getCoursename() +
            ", student=" + getStudent() +
            "}";
    }
}
