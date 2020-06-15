package com.hadarych.pp311.controller;

import com.hadarych.pp311.entity.Role;
import com.hadarych.pp311.entity.User;
import com.hadarych.pp311.service.RoleService;
import com.hadarych.pp311.service.SecurityService;
import com.hadarych.pp311.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    SecurityService secService;

    @GetMapping("")
    public String getAdminPage(ModelMap modelMap){
        User currentUser = userService.getByName(secService.findLoggedInUsername());
        modelMap.addAttribute("currentUser", currentUser);
        modelMap.addAttribute("roles", roleService.getAllRoles());
        modelMap.addAttribute("userData", userService.getAllUsers());
        return "admin/adminPage";
    }

    @GetMapping("/add")
    public ModelAndView addUser(ModelMap modelMap) {
        modelMap.addAttribute("roles", roleService.getAllRoles());
        return new ModelAndView("admin/add");
    }

    @PostMapping("/add")
    public String addUser(User user, String[] roleArray) {
        Set<Role> roleSet = new HashSet<>();
        if(roleArray != null && roleArray.length != 0){
            for(String role : roleArray) roleSet.add(roleService.getByRole(role));
        } else {
            roleSet.add(roleService.getByRole("ROLE_user"));
        }
        user.setRoles(roleSet);
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update")
    public String updateUser(ModelMap modelMap, @RequestParam("id")Long id) {
        User user = userService.getById(id);
        if(user != null) {
            modelMap.addAttribute("user", user);
            modelMap.addAttribute("roles", roleService.getAllRoles());
            return "admin/update";
        } else {
            return "redirect:/admin";
        }
    }

    @PostMapping("/update")
    public String updateUser(User user, String[] roleArray) {
        Set<Role> roleSet = new HashSet<>();
        if(roleArray != null && roleArray.length != 0){
            for(String role : roleArray) roleSet.add(roleService.getByRole(role));
            user.setRoles(roleSet);
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id")Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/drop")
    public String dropTable() {
        userService.dropTable();
        roleService.dropTable();
        createAdmin();
        return "redirect:/admin";
    }

    private void createAdmin(){
        String password = "123";
        User admin = new User("admin", password, 0L);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleService.getByRole("ROLE_admin"));
        admin.setRoles(roleSet);
        if(userService.addUser(admin)) secService.autoLogin(admin.getUsername(), password);
    }
}
