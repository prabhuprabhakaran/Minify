/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.controller;

import com.github.prabhuprabhakaran.minify.controller.service.URLService;
import com.github.prabhuprabhakaran.minify.entity.URLEntity;
import java.util.Optional;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Prabhu Prabhakaran
 */
@Controller
@RequestMapping("/")
public class DefaultController {

    @Autowired
    URLService uRLService;

    @GetMapping
    public String landingPage(Model model) {
        return "redirect:/app/home";
    }

    @RequestMapping("swagger")
    public String home() {
        return "redirect:/swagger-ui.html";
    }
//

    @GetMapping("r/{url}")
    public String redirect(@PathVariable String url) {
        String lReturn = "redirect:/app/home";
        if (!ObjectUtils.isEmpty(url)) {
            Optional<URLEntity> urlEntity = uRLService.getURLEntity(url);
            if (urlEntity.isPresent()) {
                lReturn = "redirect:" + urlEntity.get().getUrl();
            }
        }
        return lReturn;
    }
}
