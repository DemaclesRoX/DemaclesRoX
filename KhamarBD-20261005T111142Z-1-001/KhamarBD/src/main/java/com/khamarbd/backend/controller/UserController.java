package com.khamarbd.backend.controller;

import com.khamarbd.backend.dto.UserRequestDto;
import com.khamarbd.backend.entity.User;
import com.khamarbd.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // @RequestBody — register a new user (Farmer/Supplier/Specialist/Buyer)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserRequestDto userRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        User savedUser = userService.saveUser(userRequestDto);
        return ResponseEntity.ok(savedUser);
    }

    // @RequestParam — search users by district (required) and role (optional)
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String primaryRole
    ) {
        List<User> users = (primaryRole != null && !primaryRole.isBlank())
                ? userService.getUsersByRole(primaryRole)
                : userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // @PathVariable — fetch one user's profile by id
    @GetMapping("/filter/{userId}/profile")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }
}
