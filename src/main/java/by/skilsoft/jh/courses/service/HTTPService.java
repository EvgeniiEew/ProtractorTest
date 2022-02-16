package by.skilsoft.jh.courses.service;

import by.skilsoft.jh.courses.domain.PermissionForObject;
import by.skilsoft.jh.courses.service.dto.CheckPermissionDto;
import by.skilsoft.jh.courses.service.dto.PermissionDto;
import java.util.List;

public interface HTTPService {
    List<PermissionForObject> getEntityIds(String entityName);

    void addPermissionForUser(List<PermissionDto> permissionDto);

    Boolean checkPermissionEntry(CheckPermissionDto checkPermissionDto);
}
