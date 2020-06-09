/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.controller;

import com.github.prabhuprabhakaran.minify.controller.service.URLService;
import com.github.prabhuprabhakaran.minify.entity.URLEntity;
import com.github.prabhuprabhakaran.minify.entity.Users;
import com.github.prabhuprabhakaran.minify.utils.Utils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author Prabhu Prabhakaran
 */
@Controller
@RequestMapping("/app/home")
public class HomeController {

    @Autowired
    URLService uRLService;

    @GetMapping({"", "/", "/url", "/url/{id}"})
    public String homePage(Model model) {
        loadDefaults(model);
        return "home";
    }

    @PostMapping("/url")
    public String saveURL(@ModelAttribute URLEntity lEntity, Model model, HttpServletRequest request) {
        System.out.println("Im Here");

        System.out.println(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUriString());
        try {
            boolean addURLEntity = uRLService.addURLEntity(lEntity);
            if (addURLEntity) {
                model.addAttribute("success", "URL Added : " + Utils.getShortUrl(request, lEntity.getShortenurl()));
            } else {
                model.addAttribute("error", "Already URL Found / Empty URL");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Should be a valid HTTP or HTTPS URL");
        }
        loadDefaults(model);
        return "home";
    }

    @PostMapping("/url/delete/{id}")
    public String deleteURL(@PathVariable Integer id) {
        uRLService.deleteURLEntity(id);
        return "redirect:/app/home/url/" + id;
    }

    public void loadDefaults(Model model) {
        Users lUser = Utils.getUserPrincipalObject();
        model.addAttribute("user", lUser.getUsername());
        model.addAttribute("apiKey", lUser.getToken());
        model.addAttribute("urlList", uRLService.ListURLEntity());
    }
}
