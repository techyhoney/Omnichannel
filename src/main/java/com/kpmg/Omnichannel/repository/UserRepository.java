package com.kpmg.omnichannel.repository;

import com.kpmg.omnichannel.model.User;
import com.kpmg.omnichannel.model.UserStatus;
import com.kpmg.omnichannel.model.UserType;
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

