/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Prabhu Prabhakaran
 */
@Controller
@RequestMapping("/")
public class DefaultController {

    @GetMapping
    public String landingPage(Model model) {
        return "redirect:/app/home";
    }
}
