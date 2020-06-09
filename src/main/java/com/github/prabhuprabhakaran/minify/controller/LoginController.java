package com.github.prabhuprabhakaran.minify.controller;

import com.github.prabhuprabhakaran.minify.controller.service.LoginService;
import com.github.prabhuprabhakaran.minify.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Prabhu Prabhakaran
 */
@Controller
@RequestMapping("/app")
public class LoginController {

    @Autowired
    LoginService loginService;

    @GetMapping()
    public String defaultPage(Model model) {
        return "redirect:/app/home";
    }

    @GetMapping({"/login", "/register"})
    public String loginPage(@RequestParam(required = false, defaultValue = "") String error, Model model) {
        model.addAttribute("user", new Users());
        model.addAttribute("newUser", new Users());
        if (error.equals("loginError")) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login";
    }

    @RequestMapping("/Oauthlogin")
    public ResponseEntity login() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Users user, Model model) {
        boolean created = false;
        try {
            created = loginService.registerNewUser(user.getUsername(), user.getPassword());
            if (created) {
                model.addAttribute("success", "Registered Sucessfully");
            } else {
                model.addAttribute("error", "Registration Failed: Username already exists");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Registration Failed: Username should be a valid Email");
        }
        return "login";
    }
}
