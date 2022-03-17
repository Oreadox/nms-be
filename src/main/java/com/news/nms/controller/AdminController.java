package com.news.nms.controller;

import com.news.nms.config.PermissionConfig;
import com.news.nms.entity.Admin;
import com.news.nms.service.AdminService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping
    @RequiresAuthentication
    public ResponseEntity<?> getAdminStatus(@RequestBody Map<String, Object> params) {
        Subject subject = SecurityUtils.getSubject();
        Map<String, Object> resp = new HashMap<>();
        Admin admin;
        Integer id = (Integer) params.get("id");
        System.out.println(id);
        if(id==null){
            admin = (Admin) subject.getPrincipal();
        } else {
            if(subject.isPermitted(PermissionConfig.USER_ALL)){
                admin = adminService.getById(id);
            } else {
                resp.put("message", "无权查看信息");
                resp.put("status", 0);
                return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
            }
        }
        System.out.println(admin);
        if (admin != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", admin.getId());
            data.put("username", admin.getUsername());
            data.put("name", admin.getName());
            data.put("enableTotp", admin.getEnableTotp());
            data.put("permission", admin.getPermission());
            resp.put("data", data);
            resp.put("message", "成功");
            resp.put("status", 1);
        } else {
            resp.put("message", "未找到该用户");
            resp.put("status", 0);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping
    @RequiresAuthentication
    public ResponseEntity<?> createAdmin(@RequestBody Map<String, Object> params) {

        return null;
    }

    @PutMapping
    @RequiresAuthentication
    public ResponseEntity<?> modifyAdmin(@RequestBody Map<String, Object> params) {

        return null;
    }

    @DeleteMapping
    @RequiresAuthentication
    public ResponseEntity<?> deleteAdmin(@RequestBody Map<String, Object> params) {

        return null;
    }
}
