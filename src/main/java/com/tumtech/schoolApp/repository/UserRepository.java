package com.tumtech.schoolApp.repository;

import com.tumtech.schoolApp.enums.Role;
import com.tumtech.schoolApp.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository  extends JpaRepository<Users, UUID> {
    Optional<Users> findByEmail (String email);
    Optional<Users> findByStudentNo(String studentNo);
    Boolean existsByEmail (String email);
    List<Users> findAllByRole (Role role);
}
