/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String loginPage(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity login() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity register() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public ResponseEntity logout() {
        return ResponseEntity.ok().build();
    }

}
