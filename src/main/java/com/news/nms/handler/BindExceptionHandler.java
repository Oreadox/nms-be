package com.news.nms.handler;

import com.news.nms.model.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@ControllerAdvice
public class BindExceptionHandler {
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> exceptionHandler(MethodArgumentNotValidException e){
//        e.printStackTrace();
        return new ResponseEntity<>(
                BaseResponse.builder().status(0).
                        message(Objects.requireNonNull(
                                e.getBindingResult().getFieldError()).getDefaultMessage()).build()
                , HttpStatus.OK);
    }
}
