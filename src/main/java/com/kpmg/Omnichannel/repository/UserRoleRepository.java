package com.kpmg.Omnichannel.repository;

import com.kpmg.Omnichannel.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    void deleteByUser_UserIdAndRole_RoleId(UUID userId, UUID roleId);

    boolean existsByUser_UserIdAndRole_RoleId(UUID userId, UUID roleId);
}

