package by.skilsoft.jh.courses.service.dto;

import java.io.Serializable;

public class PermissionDto implements Serializable {

    private Long id;
    private String className;
    private Integer permission;
    private String sid;

    public PermissionDto() {}

    public PermissionDto(Long id, String className, Integer permission, String sid) {
        this.id = id;
        this.className = className;
        this.permission = permission;
        this.sid = sid;
    }

    public Long getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public Integer getPermission() {
        return permission;
    }

    public String getSid() {
        return sid;
    }
}
