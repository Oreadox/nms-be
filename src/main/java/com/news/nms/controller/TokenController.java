package com.news.nms.controller;

import com.news.nms.model.request.TokenPostRequest;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public interface TokenController {
    @GetMapping
    @RequiresAuthentication
    ResponseEntity<?> getLoginStatus();

    @PostMapping
    ResponseEntity<?> createToken(@RequestBody @Validated TokenPostRequest request);

    @DeleteMapping
    @RequiresAuthentication
    ResponseEntity<?> logout();
}
