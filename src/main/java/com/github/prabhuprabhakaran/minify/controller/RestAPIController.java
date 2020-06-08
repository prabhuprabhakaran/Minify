/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Prabhu Prabhakaran
 */
@RestController
@RequestMapping("/api")
public class RestAPIController {

    @GetMapping("/encode")
    public ResponseEntity encode() {
        return ResponseEntity.ok("Encode API");
    }

    @GetMapping("/denied")
    public ResponseEntity denied() {
        return ResponseEntity.ok("Denied API");
    }
}
