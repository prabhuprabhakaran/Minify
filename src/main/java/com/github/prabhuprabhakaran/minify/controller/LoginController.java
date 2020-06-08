/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.controller;

import com.github.prabhuprabhakaran.minify.entity.Users;
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

    @GetMapping()
    public String defaultPage(Model model) {
        return "redirect:/app/home";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return "home";
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

    @PostMapping("/login")
    public ResponseEntity login(@ModelAttribute Users user) {
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        return ResponseEntity.ok().build();

    }

    @PostMapping("/register")
    public String register(@ModelAttribute Users user, Model model) {
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        model.addAttribute("success", "Registered Sucessfully");
        return "login";
    }

    @GetMapping("/logout")
    public ResponseEntity logout() {
        return ResponseEntity.ok().build();
    }

}
