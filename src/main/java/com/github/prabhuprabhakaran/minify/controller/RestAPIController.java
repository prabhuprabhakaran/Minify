/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.controller;

import com.github.prabhuprabhakaran.minify.controller.service.URLService;
import com.github.prabhuprabhakaran.minify.entity.URLEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Prabhu Prabhakaran
 */
@RestController
@RequestMapping("/api")
public class RestAPIController {

    @Autowired
    URLService lService;

    @GetMapping("/encode")
    public ResponseEntity encode(@RequestParam String url) {
        URLEntity lEntity = new URLEntity();
        lEntity.setUrl(url);
        boolean addURLEntity = lService.addURLEntity(lEntity);
        if (addURLEntity) {
            return ResponseEntity.ok(lEntity.getReturn());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Already URL Found");
        }
    }
}
