package by.skilsoft.jh.courses.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Courses.
 */
@Entity
@Table(name = "courses")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Courses implements Serializable, BaseCheckedEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "course_name", nullable = false)
    private String courseName;

    @NotNull
    @Column(name = "date_of_start", nullable = false)
    private LocalDate dateOfStart;

    @NotNull
    @Column(name = "date_of_end", nullable = false)
    private LocalDate dateOfEnd;

    @ManyToOne
    private Provider name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Courses id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public Courses courseName(String courseName) {
        this.setCourseName(courseName);
        return this;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public LocalDate getDateOfStart() {
        return this.dateOfStart;
    }

    public Courses dateOfStart(LocalDate dateOfStart) {
        this.setDateOfStart(dateOfStart);
        return this;
    }

    public void setDateOfStart(LocalDate dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public LocalDate getDateOfEnd() {
        return this.dateOfEnd;
    }

    public Courses dateOfEnd(LocalDate dateOfEnd) {
        this.setDateOfEnd(dateOfEnd);
        return this;
    }

    public void setDateOfEnd(LocalDate dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
    }

    public Provider getName() {
        return this.name;
    }

    public void setName(Provider provider) {
        this.name = provider;
    }

    public Courses name(Provider provider) {
        this.setName(provider);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Courses)) {
            return false;
        }
        return id != null && id.equals(((Courses) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Courses{" +
            "id=" + getId() +
            ", courseName='" + getCourseName() + "'" +
            ", dateOfStart='" + getDateOfStart() + "'" +
            ", dateOfEnd='" + getDateOfEnd() + "'" +
            "}";
    }
}
