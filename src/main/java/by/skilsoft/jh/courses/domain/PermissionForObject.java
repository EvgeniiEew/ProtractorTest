package by.skilsoft.jh.courses.domain;

import java.io.Serializable;
import java.util.Objects;

public class PermissionForObject implements Serializable {

    private Long objectId;
    private Integer mask;

    public PermissionForObject(Long objectId, Integer mask) {
        this.objectId = objectId;
        this.mask = mask;
    }

    public PermissionForObject() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionForObject that = (PermissionForObject) o;
        return mask == that.mask && objectId.equals(that.objectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, mask);
    }

    public Long getObjectId() {
        return objectId;
    }

    public Integer getMask() {
        return mask;
    }

    @Override
    public String toString() {
        return "PermissionForObject{" + "objId=" + objectId + ", mask=" + mask + '}';
    }
}
