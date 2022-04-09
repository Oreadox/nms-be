package com.news.nms.controller;

import com.news.nms.entity.Admin;
import com.news.nms.model.request.TokenPostRequest;
import com.news.nms.model.response.AdminResponse;
import com.news.nms.model.response.BaseResponse;
import com.news.nms.model.response.data.AdminData;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/token")
public class TokenController {
    @Autowired
    AdminService adminService;

    @GetMapping
    @RequiresAuthentication
    public ResponseEntity<?> getLoginStatus() {
        Subject subject = SecurityUtils.getSubject();
        Admin admin = (Admin) subject.getPrincipal();
        if (subject.isAuthenticated()) {
            AdminData data = new AdminData();
            data.setAdmin(admin);
            return new ResponseEntity<>(
                    AdminResponse.builder().status(1).message("已登录").data(data).build()
                    , HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("未登录").build()
                    , HttpStatus.OK);
        }
    }

    @PostMapping
//    @RequiresGuest
    public ResponseEntity<?> createToken(@RequestBody @Validated TokenPostRequest request) {
        String username=request.getUsername(), password=request.getPassword();
        System.out.println(request);
        Admin admin = adminService.getByUsername(username);
        if (admin == null) {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("该用户不存在").build()
                    , HttpStatus.OK);
        }
        if (admin.getEnableTotp()) {
            String totpString = request.getTotp();
            if (Objects.equals(totpString, "")) {
                return new ResponseEntity<>(
                        BaseResponse.builder().status(0).message("该账号启用了两步验证，口令不能为空").build()
                        , HttpStatus.OK);
            }
            Integer code = (new GoogleAuthenticator()).getTotpPassword(admin.getTotp());
//            System.out.println("code="+code);
            if (!Integer.valueOf(totpString).equals(code)) {
                return new ResponseEntity<>(
                        BaseResponse.builder().status(0)
                                .message("动态口令错误，若您遗失了动态口令密钥，可以联系管理员重置密码").build()
                        , HttpStatus.OK);
            }
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("密码错误").build()
                    , HttpStatus.OK);
        }
        return new ResponseEntity<>(
                BaseResponse.builder().status(1).message("登录成功").build()
                , HttpStatus.OK);
    }

    @DeleteMapping
    @RequiresAuthentication
    public ResponseEntity<?> logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new ResponseEntity<>(
                BaseResponse.builder().status(1).message("登出成功").build()
                , HttpStatus.OK);
    }
}
