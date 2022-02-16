package by.skilsoft.jh.courses.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link by.skilsoft.jh.courses.domain.Study} entity. This class is used
 * in {@link by.skilsoft.jh.courses.web.rest.StudyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /studies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateOfStart;

    private LocalDateFilter dateOfExam;

    private IntegerFilter grade;

    private LongFilter coursenameId;

    private StringFilter coursesCourseName;

    private LongFilter studentId;

    private StringFilter studentFirstName;

    private Boolean distinct;

    public StudyCriteria() {}

    public StudyCriteria(StudyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateOfStart = other.dateOfStart == null ? null : other.dateOfStart.copy();
        this.dateOfExam = other.dateOfExam == null ? null : other.dateOfExam.copy();
        this.grade = other.grade == null ? null : other.grade.copy();
        this.coursenameId = other.coursenameId == null ? null : other.coursenameId.copy();
        this.coursesCourseName = other.coursesCourseName == null ? null : other.coursesCourseName.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
        this.studentFirstName = other.studentFirstName == null ? null : other.studentFirstName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StudyCriteria copy() {
        return new StudyCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDateOfStart() {
        return dateOfStart;
    }

    public LocalDateFilter dateOfStart() {
        if (dateOfStart == null) {
            dateOfStart = new LocalDateFilter();
        }
        return dateOfStart;
    }

    public void setDateOfStart(LocalDateFilter dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public LocalDateFilter getDateOfExam() {
        return dateOfExam;
    }

    public LocalDateFilter dateOfExam() {
        if (dateOfExam == null) {
            dateOfExam = new LocalDateFilter();
        }
        return dateOfExam;
    }

    public void setDateOfExam(LocalDateFilter dateOfExam) {
        this.dateOfExam = dateOfExam;
    }

    public IntegerFilter getGrade() {
        return grade;
    }

    public IntegerFilter grade() {
        if (grade == null) {
            grade = new IntegerFilter();
        }
        return grade;
    }

    public void setGrade(IntegerFilter grade) {
        this.grade = grade;
    }

    public LongFilter getCoursenameId() {
        return coursenameId;
    }

    public LongFilter coursenameId() {
        if (coursenameId == null) {
            coursenameId = new LongFilter();
        }
        return coursenameId;
    }

    public void setCoursenameId(LongFilter coursenameId) {
        this.coursenameId = coursenameId;
    }

    public StringFilter getCoursesCourseName() {
        return coursesCourseName;
    }

    public StringFilter coursesCourseName() {
        if (coursesCourseName == null) {
            coursesCourseName = new StringFilter();
        }
        return coursesCourseName;
    }

    public void setCoursesCourseName(StringFilter coursesCourseName) {
        this.coursesCourseName = coursesCourseName;
    }

    public LongFilter getStudentId() {
        return studentId;
    }

    public LongFilter studentId() {
        if (studentId == null) {
            studentId = new LongFilter();
        }
        return studentId;
    }

    public void setStudentId(LongFilter studentId) {
        this.studentId = studentId;
    }

    public StringFilter getStudentFirstName() {
        return studentFirstName;
    }

    public StringFilter studentFirstName() {
        if (studentFirstName == null) {
            studentFirstName = new StringFilter();
        }
        return studentFirstName;
    }

    public void setStudentFirstName(StringFilter studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StudyCriteria that = (StudyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateOfStart, that.dateOfStart) &&
            Objects.equals(dateOfExam, that.dateOfExam) &&
            Objects.equals(grade, that.grade) &&
            Objects.equals(coursenameId, that.coursenameId) &&
            Objects.equals(coursesCourseName, that.coursesCourseName) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(studentFirstName, that.studentFirstName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateOfStart, dateOfExam, grade, coursenameId, coursesCourseName, studentId, studentFirstName, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dateOfStart != null ? "dateOfStart=" + dateOfStart + ", " : "") +
            (dateOfExam != null ? "dateOfExam=" + dateOfExam + ", " : "") +
            (grade != null ? "grade=" + grade + ", " : "") +
            (coursenameId != null ? "coursenameId=" + coursenameId + ", " : "") +
            (coursesCourseName != null ? "coursesCourseName=" + coursesCourseName + ", " : "") +
            (studentId != null ? "studentId=" + studentId + ", " : "") +
            (studentFirstName != null ? "studentFirstName=" + studentFirstName + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
