/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.controller;

import com.github.prabhuprabhakaran.minify.entity.URLEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Prabhu Prabhakaran
 */
@Controller
@RequestMapping("/app/home")
public class HomeController {

    @GetMapping("/url/list")
    public ResponseEntity list() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/url/{id}")
    public ResponseEntity getURL(@PathVariable Integer id) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/url/{id}")
    public ResponseEntity deleteURL(@PathVariable Integer id) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/url/{id}")
    public ResponseEntity saveURL(@PathVariable Integer id) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/url/{id}")
    public ResponseEntity updateURL(@PathVariable Integer id, @RequestBody URLEntity uRLEntity) {
        return ResponseEntity.ok().build();
    }
}
