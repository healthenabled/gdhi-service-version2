package it.gdhi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloWorldController {
    @GetMapping("/helloWorld")
    public ResponseEntity<String> helloWorld(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body("HELLO WORLD");
    }
}
