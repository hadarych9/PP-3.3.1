package com.hadarych.pp311.repository;

import com.hadarych.pp311.entity.Role;
import com.hadarych.pp311.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select r from Role r where r.role = :role")
    Role getByRole(@Param("role") String role);

    @Query("select r from Role r where r.id = :id")
    Role getById(@Param("id") Long id);

}
