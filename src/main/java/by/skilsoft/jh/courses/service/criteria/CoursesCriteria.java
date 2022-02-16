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
 * Criteria class for the {@link by.skilsoft.jh.courses.domain.Courses} entity. This class is used
 * in {@link by.skilsoft.jh.courses.web.rest.CoursesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /courses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CoursesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter courseName;

    private LocalDateFilter dateOfStart;

    private LocalDateFilter dateOfEnd;

    private LongFilter nameId;

    private StringFilter providerName;

    private Boolean distinct;

    public CoursesCriteria() {}

    public CoursesCriteria(CoursesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.courseName = other.courseName == null ? null : other.courseName.copy();
        this.dateOfStart = other.dateOfStart == null ? null : other.dateOfStart.copy();
        this.dateOfEnd = other.dateOfEnd == null ? null : other.dateOfEnd.copy();
        this.nameId = other.nameId == null ? null : other.nameId.copy();
        this.providerName = other.providerName == null ? null : other.providerName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CoursesCriteria copy() {
        return new CoursesCriteria(this);
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

    public StringFilter getCourseName() {
        return courseName;
    }

    public StringFilter courseName() {
        if (courseName == null) {
            courseName = new StringFilter();
        }
        return courseName;
    }

    public void setCourseName(StringFilter courseName) {
        this.courseName = courseName;
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

    public LocalDateFilter getDateOfEnd() {
        return dateOfEnd;
    }

    public LocalDateFilter dateOfEnd() {
        if (dateOfEnd == null) {
            dateOfEnd = new LocalDateFilter();
        }
        return dateOfEnd;
    }

    public void setDateOfEnd(LocalDateFilter dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
    }

    public LongFilter getNameId() {
        return nameId;
    }

    public LongFilter nameId() {
        if (nameId == null) {
            nameId = new LongFilter();
        }
        return nameId;
    }

    public void setNameId(LongFilter nameId) {
        this.nameId = nameId;
    }

    public StringFilter getProviderName() {
        return providerName;
    }

    public StringFilter providerName() {
        if (providerName == null) {
            providerName = new StringFilter();
        }
        return providerName;
    }

    public void setProviderName(StringFilter providerName) {
        this.providerName = providerName;
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
        final CoursesCriteria that = (CoursesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(courseName, that.courseName) &&
            Objects.equals(dateOfStart, that.dateOfStart) &&
            Objects.equals(dateOfEnd, that.dateOfEnd) &&
            Objects.equals(nameId, that.nameId) &&
            Objects.equals(providerName, that.providerName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseName, dateOfStart, dateOfEnd, nameId, providerName, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoursesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (courseName != null ? "courseName=" + courseName + ", " : "") +
            (dateOfStart != null ? "dateOfStart=" + dateOfStart + ", " : "") +
            (dateOfEnd != null ? "dateOfEnd=" + dateOfEnd + ", " : "") +
            (nameId != null ? "nameId=" + nameId + ", " : "") +
            (providerName != null ? "providerName=" + providerName + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
