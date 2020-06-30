package com.hadarych.pp311.controller;

import com.hadarych.pp311.entity.Role;
import com.hadarych.pp311.entity.User;
import com.hadarych.pp311.service.RoleService;
import com.hadarych.pp311.service.SecurityService;
import com.hadarych.pp311.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AdminRestController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    SecurityService secService;

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> readRoles(){
        final List<Role> roles = (List<Role>) roleService.getAllRoles();
        return roles != null && !roles.isEmpty()
                ? new ResponseEntity<>(roles, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody User user){
        userService.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> read(){
        final List<User> users = (List<User>) userService.getAllUsers();
        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> read(@PathVariable(name = "id") Long id){
        final User user = userService.getById(id);
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/users")
    public ResponseEntity<?> update(@RequestBody User user){
        final boolean updated = userService.updateUser(user);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
