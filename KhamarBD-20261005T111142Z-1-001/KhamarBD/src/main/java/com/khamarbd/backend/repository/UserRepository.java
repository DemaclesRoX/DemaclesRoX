package com.khamarbd.backend.repository;

import com.khamarbd.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.primaryRole = :role")
    List<User> findByRole(@Param("role") String role);

    @Query("SELECT u FROM User u WHERE u.primaryRole = :role AND u.isVerified = true")
    List<User> findByRoleAndIsVerifiedTrue(@Param("role") String role);

    @Query("SELECT u FROM User u WHERE u.primaryRole = :role AND u.specializationSector = :specialistSector")
    List<User> findByRoleAndSpecialistSector(@Param("role") String role, @Param("specialistSector") String specialistSector);

    List<User> findByPrimaryRole(String primaryRole);
}
