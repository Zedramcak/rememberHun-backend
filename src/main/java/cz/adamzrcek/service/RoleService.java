package cz.adamzrcek.service;

import cz.adamzrcek.dtos.RoleDto;
import cz.adamzrcek.entity.Role;
import cz.adamzrcek.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Cacheable("roles")
    public List<RoleDto> getAllRoles(){
        return roleRepository.findAll().stream().map(this::convertToDto).toList();
    }

    private RoleDto convertToDto(Role role){
        return new RoleDto(role.getId(), role.getName());
    }
}
