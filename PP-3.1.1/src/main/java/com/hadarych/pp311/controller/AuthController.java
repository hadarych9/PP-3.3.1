package com.hadarych.pp311.controller;

import com.hadarych.pp311.entity.Role;
import com.hadarych.pp311.entity.User;
import com.hadarych.pp311.service.RoleService;
import com.hadarych.pp311.service.SecurityService;
import com.hadarych.pp311.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    SecurityService secService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(User user) {
        String password = user.getPassword();
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleService.getByRole("user"));
        user.setRoles(roleSet);
        if(userService.addUser(user)) secService.autoLogin(user.getUsername(), password);
        return "redirect:/user";
    }
}
