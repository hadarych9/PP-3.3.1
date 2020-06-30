package com.hadarych.pp311.service;

import com.hadarych.pp311.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

public interface UserService extends UserDetailsService {

    boolean doesUserNotExist(String name);
    boolean addUser(User user);
    User getById(Long id);
    User getByName(String name);
    void deleteUser(Long id);
    boolean updateUser(User user);
    Collection getAllUsers();
    void dropTable();

}
