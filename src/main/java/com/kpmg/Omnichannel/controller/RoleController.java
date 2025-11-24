package com.kpmg.Omnichannel.controller;

import com.kpmg.Omnichannel.dto.ApiResponse;
import com.kpmg.Omnichannel.dto.RoleRequest;
import com.kpmg.Omnichannel.dto.RoleResponse;
import com.kpmg.Omnichannel.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody RoleRequest request) {
        RoleResponse role = roleService.createRole(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Role created successfully", role));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        List<RoleResponse> roles = roleService.getAllRoles();
        return ResponseEntity.ok(ApiResponse.success("Roles retrieved successfully", roles));
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(@PathVariable UUID roleId) {
        RoleResponse role = roleService.getRoleById(roleId);
        return ResponseEntity.ok(ApiResponse.success("Role retrieved successfully", role));
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
            @PathVariable UUID roleId,
            @Valid @RequestBody RoleRequest request) {
        RoleResponse role = roleService.updateRole(roleId, request);
        return ResponseEntity.ok(ApiResponse.success("Role updated successfully", role));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse<Object>> deleteRole(@PathVariable UUID roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok(ApiResponse.success("Role deleted successfully", null));
    }

    @PostMapping("/{roleId}/users/{userId}")
    public ResponseEntity<ApiResponse<Object>> assignRoleToUser(
            @PathVariable UUID roleId,
            @PathVariable UUID userId) {
        roleService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok(ApiResponse.success("Role assigned to user successfully", null));
    }

    @DeleteMapping("/{roleId}/users/{userId}")
    public ResponseEntity<ApiResponse<Object>> removeRoleFromUser(
            @PathVariable UUID roleId,
            @PathVariable UUID userId) {
        roleService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok(ApiResponse.success("Role removed from user successfully", null));
    }
}
