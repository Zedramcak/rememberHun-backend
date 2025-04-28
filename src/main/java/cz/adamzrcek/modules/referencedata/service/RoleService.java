package cz.adamzrcek.modules.referencedata.service;

import cz.adamzrcek.modules.referencedata.dtos.RoleDto;

import java.util.List;

public interface RoleService {
    List<RoleDto> getAllRoles();
}
