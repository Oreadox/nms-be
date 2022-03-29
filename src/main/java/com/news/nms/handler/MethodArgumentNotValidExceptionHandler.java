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
public class MethodArgumentNotValidExceptionHandler {
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> ExceptionHandler(MethodArgumentNotValidException e){
//        e.printStackTrace();
        return new ResponseEntity<>(
                BaseResponse.builder().status(0).
                        message(Objects.requireNonNull(
                                e.getBindingResult().getFieldError()).getDefaultMessage()).build()
                , HttpStatus.OK);
    }
}
