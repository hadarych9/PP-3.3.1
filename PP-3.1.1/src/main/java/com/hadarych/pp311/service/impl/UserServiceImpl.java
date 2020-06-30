package com.hadarych.pp311.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hadarych.pp311.entity.Role;
import com.hadarych.pp311.entity.User;
import com.hadarych.pp311.repository.UserRepository;
import com.hadarych.pp311.service.RoleService;
import com.hadarych.pp311.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    RoleService roleService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public boolean doesUserNotExist(String name) {
        return getByName(name) == null;
    }

    @Override
    public boolean addUser(User user) {
        if (doesUserNotExist(user.getUsername())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            if (user.getRoles().isEmpty()){
                Set<Role> roleSet = new HashSet<>();
                roleSet.add(roleService.getByRole("ROLE_user"));
                user.setRoles(roleSet);
            }
            repository.saveAndFlush(user);
            return true;
        } else return false;
    }

    @Override
    public User getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public User getByName(String name) {
        return repository.getByName(name);
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean updateUser(User user) {
        User existingUser = repository.getById(user.getId());
        User check = repository.getById(user.getId());
        System.out.println(existingUser.getUsername());
        if(user.getUsername() != null && !user.getUsername().equals("")) existingUser.setName(user.getUsername());
        if(user.getPassword() != null && !user.getPassword().equals("")) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        if(user.getAge() != null && user.getAge() != null) existingUser.setAge(user.getAge());
        if(user.getRoles() != null && user.getRoles() != null) {
            existingUser.setRoles(user.getRoles());
        }
        repository.saveAndFlush(existingUser);
        return !check.equals(existingUser);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public void dropTable() {
        repository.deleteAll();
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = getByName(name);
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        for(Role role : user.getRoles()){
            grantedAuthoritySet.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthoritySet);
    }
}
