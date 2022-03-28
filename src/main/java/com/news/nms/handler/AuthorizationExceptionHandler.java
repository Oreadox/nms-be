package com.news.nms.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AuthorizationExceptionHandler {
    @ResponseBody
    @ExceptionHandler(org.apache.shiro.authz.AuthorizationException.class)
    public ResponseEntity<?> handleShiroException(Exception e) {
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
}
