package ru.kata.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.springboot.model.Role;
import ru.kata.springboot.model.User;
import ru.kata.springboot.service.RoleService;
import ru.kata.springboot.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
        model.addAttribute("users", userService.findAll());

        addAttributesToMainPage(model, principal);

        return "/admin/user-list";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute("newUser") @Valid User newUser,
                             BindingResult bindingResult, Model model, Principal principal) {
        Optional<User> userByEmail = userService.findByEmail(newUser.getEmail());
        if (userByEmail.isPresent()) {
            bindingResult.rejectValue("email", "error.email",
                    "This email is already in use");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            addAttributesToMainPage(model, principal);
            return "/admin/user-list";
        }

        this.userService.save(newUser);
        return "redirect:/admin/users/";
    }

    @GetMapping("/users/edit")
    public String editUserForm(@RequestParam("id") Long id, Model model) {
        Optional<User> userById = userService.findById(id);

        if (userById.isPresent()) {
            model.addAttribute("user", userById.get());
            model.addAttribute("listRoles", roleService.findAll());
            return "/admin/edit-user";
        } else {
            return "redirect:/admin/users";
        }
    }

    @PatchMapping("/users/edit")
    public String editUser(@ModelAttribute("updatingUser") @Valid User updatingUser,
                           BindingResult bindingResult, Model model, Principal principal) {
        Optional<User> userByEmail = userService.findByEmail(updatingUser.getEmail());
        if (userByEmail.isPresent() && (!userByEmail.get().getId().equals(updatingUser.getId()))) {
            bindingResult.rejectValue("email", "error.email",
                    "This email is already in use");
        }

        if (bindingResult.hasErrors()) {
            addAttributesToMainPage(model, principal);
            model.addAttribute("editUserError", true);
            return "/admin/user-list";
        }

        userService.updateUser(updatingUser);
        return "redirect:/admin/users";
    }

    @DeleteMapping("/users/delete")
    public String deleteUser(@ModelAttribute("deletingUser") User user) {
        Long id = user.getId();
        if (userService.findById(id).isPresent()) {
            userService.deleteById(id);
        }
        return "redirect:/admin/users";
    }

    private void addAttributesToMainPage(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()).orElse(new User());

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .map(role -> role.split("_")[1])
                .toList();

        model.addAttribute("authUser", user);
        model.addAttribute("userRoles", roles);

        if (!model.containsAttribute("updatingUser")) {
            model.addAttribute("updatingUser", new User());
        }

        if (!model.containsAttribute("newUser")) {
            model.addAttribute("newUser", new User());
        }

        if (!model.containsAttribute("deletingUser")) {
            model.addAttribute("deletingUser", new User());
        }

        model.addAttribute("listRoles", roleService.findAll());
        model.addAttribute("users", userService.findAll());
    }
}

