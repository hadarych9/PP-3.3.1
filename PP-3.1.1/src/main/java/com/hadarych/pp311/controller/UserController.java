package com.hadarych.pp311.controller;

import com.hadarych.pp311.entity.User;
import com.hadarych.pp311.service.RoleService;
import com.hadarych.pp311.service.SecurityService;
import com.hadarych.pp311.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    SecurityService secService;

    @GetMapping("/user")
    public ModelAndView getUserPage(ModelMap modelMap) {
        User user = userService.getByName(secService.findLoggedInUsername());
        modelMap.addAttribute("user", user);
        return new ModelAndView("userPage");
    }
}
