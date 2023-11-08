package ru.kata.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.springboot.model.User;
import ru.kata.springboot.service.UserService;
import ru.kata.springboot.util.UserErrorResponse;
import ru.kata.springboot.util.UserNotCreatedException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final UserService userService;

    @Autowired
    public ApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/user")
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid User user,
                                                 BindingResult bindingResult) {
        Optional<User> userByEmail = userService.findByEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            bindingResult.rejectValue("email", "error.email",
                    "This email is already in use");
        }

        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new UserNotCreatedException(errorMessages);
        }

        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/user/edit")
    public ResponseEntity<HttpStatus> editUser(@RequestBody @Valid User user,
                                               BindingResult bindingResult) {
        Optional<User> userByEmail = userService.findByEmail(user.getEmail());
        if (userByEmail.isPresent() && (!userByEmail.get().getId().equals(user.getId()))) {
            bindingResult.rejectValue("email", "error.email",
                    "This email is already in use");
        }

        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new UserNotCreatedException(errorMessages);
        }

        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException userNotCreatedException) {
        UserErrorResponse response = new UserErrorResponse(userNotCreatedException.getErrors());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
