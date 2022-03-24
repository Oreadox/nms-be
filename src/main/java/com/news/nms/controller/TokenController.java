package com.news.nms.controller;

import com.news.nms.entity.Admin;
import com.news.nms.service.AdminService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/token")
public class TokenController {
    @Autowired
    AdminService adminService;


    @GetMapping
    @RequiresAuthentication
    public ResponseEntity<?> getLoginStatus() {
        Subject subject = SecurityUtils.getSubject();
        Map<String, Object> resp = new HashMap<>();
        if (subject.isAuthenticated()) {
            resp.put("status", 1);
            resp.put("message", "已登录");
        } else {
            resp.put("status", 0);
            resp.put("message", "未登录");
        }
        // todo: 发布版本删去
        resp.put("getPrincipal", subject.getPrincipal());
        resp.put("getSession", subject.getSession());
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping
//    @RequiresGuest
    public ResponseEntity<?> createToken(@RequestBody Map<String, Object> params) {
        Map<String, Object> resp = new HashMap<>();
        System.out.println(params);
        String username, password;
        try {
            username = params.get("username").toString();
            password = params.get("password").toString();

        } catch (NullPointerException e) {
            resp.put("status", 0);
            resp.put("message", "用户名或密码不能为空");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        Admin admin = adminService.getByUsername(username);
        if (admin == null) {
            resp.put("status", 0);
            resp.put("message", "该用户不存在");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        if (admin.getEnableTotp()) {
            String totpString = (String) params.get("totp");
            if (totpString == null) {
                resp.put("status", 0);
                resp.put("message", "该账号启用了两步验证，口令不能为空");
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }
            Integer code = new GoogleAuthenticator().getTotpPassword(admin.getTotp());;
            if (!Integer.getInteger(totpString).equals(code)) {
                resp.put("status", 0);
                resp.put("message", "动态口令错误");
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            resp.put("status", 0);
            resp.put("message", "密码错误");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        resp.put("status", 1);
        resp.put("message", "登录成功");
        Map<String, Object> data = new HashMap<>();
        resp.put("data", data);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @DeleteMapping
    @RequiresAuthentication
    public ResponseEntity<?> logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", 1);
        resp.put("message", "登出成功");
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
