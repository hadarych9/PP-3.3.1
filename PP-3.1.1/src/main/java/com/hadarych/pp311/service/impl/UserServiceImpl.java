package com.hadarych.pp311.service.impl;

import com.hadarych.pp311.entity.Role;
import com.hadarych.pp311.entity.User;
import com.hadarych.pp311.repository.UserRepository;
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
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public boolean doesUserNotExist(String name) {
        return getByName(name) == null;
    }

    @Override
    public boolean addUser(User user) {
        if (doesUserNotExist(user.getUsername())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            repository.saveAndFlush(user);
            return true;
        } else return false;
    }

    @Override
    public User getById(Long id) {
        return repository.getOne(id);
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
    public void updateUser(User user) {
        User existingUser = repository.getOne(user.getId());
        System.out.println(existingUser.getUsername());
        if(!user.getUsername().equals("")) existingUser.setName(user.getUsername());
        if(!user.getPassword().equals("")) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        if(user.getAge() != null) existingUser.setAge(user.getAge());
        if(user.getRoles() != null) {
            existingUser.setRoles(user.getRoles());
        }
        repository.saveAndFlush(existingUser);
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
