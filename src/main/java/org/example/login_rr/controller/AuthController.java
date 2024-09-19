package org.example.login_rr.controller;


import org.springframework.ui.Model;
import org.example.login_rr.entity.User;
import org.example.login_rr.repository.UserRepository;
import org.example.login_rr.service.DetailService;
import org.example.login_rr.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final DetailService detailService;
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, DetailService detailService, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.detailService = detailService;
        this.userService = userService;
    }

    /*
    @GetMapping("/join")
    public String joinForm() {
        return "joinform";
    }

     */

    @PostMapping("/join")
    public String join(User user) {
        userRepository.save(user);
        //return "redirect:/login";
        return "join";
    }

    @PostMapping("/login")
    public String loginForm() {
        //return "redirect:/profile";
        return "login";
    }

    @PostMapping("/logout")
    public String logout() {
        //return "redirect:/login";
        return "logout";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        //return "redirect:/join";
        return "delete";
    }



    @PatchMapping("/edit/{id}")
    public String Edit(@PathVariable long id, @RequestBody Map<String, Object> update) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + id));

        update.forEach((key, vlaue) -> {
            switch (key) {
                case "username": existingUser.setUsername((String) vlaue); break;
                case "email": existingUser.setEmail((String) vlaue); break;
                case "password": existingUser.setPassword(passwordEncoder.encode((String) vlaue)); break;
            }
        });

        return "redirect:/profile";

    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        User user = detailService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }


}
