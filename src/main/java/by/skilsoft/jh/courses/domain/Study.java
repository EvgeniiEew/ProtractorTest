package by.skilsoft.jh.courses.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Study.
 */
@Entity
@Table(name = "study")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Study implements Serializable, BaseCheckedEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_of_start", nullable = false)
    private LocalDate dateOfStart;

    @NotNull
    @Column(name = "date_of_exam", nullable = false)
    private LocalDate dateOfExam;

    @NotNull
    @Column(name = "grade", nullable = false)
    private Integer grade;

    @ManyToOne
    @JsonIgnoreProperties(value = { "name" }, allowSetters = true)
    private Courses coursename;

    @ManyToOne
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Study id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOfStart() {
        return this.dateOfStart;
    }

    public Study dateOfStart(LocalDate dateOfStart) {
        this.setDateOfStart(dateOfStart);
        return this;
    }

    public void setDateOfStart(LocalDate dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public LocalDate getDateOfExam() {
        return this.dateOfExam;
    }

    public Study dateOfExam(LocalDate dateOfExam) {
        this.setDateOfExam(dateOfExam);
        return this;
    }

    public void setDateOfExam(LocalDate dateOfExam) {
        this.dateOfExam = dateOfExam;
    }

    public Integer getGrade() {
        return this.grade;
    }

    public Study grade(Integer grade) {
        this.setGrade(grade);
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Courses getCoursename() {
        return this.coursename;
    }

    public void setCoursename(Courses courses) {
        this.coursename = courses;
    }

    public Study coursename(Courses courses) {
        this.setCoursename(courses);
        return this;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Study student(Student student) {
        this.setStudent(student);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Study)) {
            return false;
        }
        return id != null && id.equals(((Study) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Study{" +
            "id=" + getId() +
            ", dateOfStart='" + getDateOfStart() + "'" +
            ", dateOfExam='" + getDateOfExam() + "'" +
            ", grade=" + getGrade() +
            "}";
    }
}
