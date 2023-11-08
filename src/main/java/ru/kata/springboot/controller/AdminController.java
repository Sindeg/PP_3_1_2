package ru.kata.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.springboot.model.Role;
import ru.kata.springboot.model.User;
import ru.kata.springboot.service.RoleService;
import ru.kata.springboot.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService,
                           RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public String getUsersList(Principal principal, Model model) {
        addAttributesToMainPage(model, principal);
        return "admin/control-panel";
    }

    private void addAttributesToMainPage(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()).orElse(new User());

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .map(role -> role.split("_")[1])
                .toList();

        model.addAttribute("authUser", user);
        model.addAttribute("userRoles", roles);
        model.addAttribute("listRoles", roleService.findAll());
    }
}

