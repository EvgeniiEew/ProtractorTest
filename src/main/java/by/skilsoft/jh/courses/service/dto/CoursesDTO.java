package by.skilsoft.jh.courses.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link by.skilsoft.jh.courses.domain.Courses} entity.
 */
public class CoursesDTO implements Serializable {

    private Long id;

    @NotNull
    private String courseName;

    @NotNull
    private LocalDate dateOfStart;

    @NotNull
    private LocalDate dateOfEnd;

    private ProviderDTO name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public LocalDate getDateOfStart() {
        return dateOfStart;
    }

    public void setDateOfStart(LocalDate dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public LocalDate getDateOfEnd() {
        return dateOfEnd;
    }

    public void setDateOfEnd(LocalDate dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
    }

    public ProviderDTO getName() {
        return name;
    }

    public void setName(ProviderDTO name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoursesDTO)) {
            return false;
        }

        CoursesDTO coursesDTO = (CoursesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, coursesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoursesDTO{" +
            "id=" + getId() +
            ", courseName='" + getCourseName() + "'" +
            ", dateOfStart='" + getDateOfStart() + "'" +
            ", dateOfEnd='" + getDateOfEnd() + "'" +
            ", name=" + getName() +
            "}";
    }
}
