package cz.adamzrcek.modules.referencedata.service;

import cz.adamzrcek.modules.referencedata.dtos.RoleDto;
import cz.adamzrcek.modules.referencedata.entity.Role;
import cz.adamzrcek.modules.referencedata.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Cacheable("roles")
    @Override
    public List<RoleDto> getAllRoles(){
        return roleRepository.findAll().stream().map(this::convertToDto).toList();
    }

    private RoleDto convertToDto(Role role){
        return new RoleDto(role.getId(), role.getName());
    }
}
