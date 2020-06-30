package com.hadarych.pp311.repository;

import com.hadarych.pp311.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.name = :name")
    User getByName(@Param("name") String name);

    @Query("select u from User u where u.id = :id")
    User getById(@Param("id") Long id);

}
