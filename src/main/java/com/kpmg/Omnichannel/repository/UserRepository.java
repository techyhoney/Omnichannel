package com.kpmg.Omnichannel.repository;

import com.kpmg.Omnichannel.model.User;
import com.kpmg.Omnichannel.model.UserStatus;
import com.kpmg.Omnichannel.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    List<User> findByUserType(UserType userType);

    List<User> findByStatus(UserStatus status);
}

