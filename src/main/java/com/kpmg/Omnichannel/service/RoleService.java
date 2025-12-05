package com.kpmg.omnichannel.service;

import com.kpmg.omnichannel.dto.RoleRequest;
import com.kpmg.omnichannel.dto.RoleResponse;
import com.kpmg.omnichannel.exception.ResourceAlreadyExistsException;
import com.kpmg.omnichannel.exception.ResourceNotFoundException;
import com.kpmg.omnichannel.model.Role;
import com.kpmg.omnichannel.model.User;
import com.kpmg.omnichannel.model.UserRole;
import com.kpmg.omnichannel.repository.RoleRepository;
import com.kpmg.omnichannel.repository.UserRepository;
import com.kpmg.omnichannel.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Role with name " + request.getName() + " already exists");
        }

        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        Role savedRole = roleRepository.save(role);
        return convertToResponse(savedRole);
    }

    public RoleResponse getRoleById(UUID roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        return convertToResponse(role);
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RoleResponse updateRole(UUID roleId, RoleRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        if (!role.getName().equals(request.getName()) && roleRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Role with name " + request.getName() + " already exists");
        }

        role.setName(request.getName());
        role.setDescription(request.getDescription());

        Role updatedRole = roleRepository.save(role);
        return convertToResponse(updatedRole);
    }

    @Transactional
    public void deleteRole(UUID roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        roleRepository.delete(role);
    }

    @Transactional
    public void assignRoleToUser(UUID userId, UUID roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        if (userRoleRepository.existsByUser_UserIdAndRole_RoleId(userId, roleId)) {
            throw new ResourceAlreadyExistsException("User already has this role");
        }

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);
    }

    @Transactional
    public void removeRoleFromUser(UUID userId, UUID roleId) {
        if (!userRoleRepository.existsByUser_UserIdAndRole_RoleId(userId, roleId)) {
            throw new ResourceNotFoundException("User does not have this role");
        }

        userRoleRepository.deleteByUser_UserIdAndRole_RoleId(userId, roleId);
    }

    private RoleResponse convertToResponse(Role role) {
        return RoleResponse.builder()
                .roleId(role.getRoleId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }
}

