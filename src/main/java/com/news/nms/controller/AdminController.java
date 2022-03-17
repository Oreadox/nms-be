package com.news.nms.controller;

import com.news.nms.entity.Admin;
import com.news.nms.service.AdminService;
import org.apache.shiro.SecurityUtils;
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
    public ResponseEntity<?> getAdminStatus(@RequestBody Map<String, Object> params) {
        Subject subject = SecurityUtils.getSubject();
        Map<String, Object> resp = new HashMap<>();
        Admin admin;
        Integer id = (Integer) params.get("id");
        System.out.println(id);
        if(id==null)
            admin = (Admin) subject.getPrincipal();
        else
            // todo: 权限控制
            admin = adminService.getById(id);
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
            // todo: 添加相关说明
            resp.put("message", "失败");
            resp.put("status", 0);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody Map<String, Object> params) {

        return null;
    }

    @PutMapping
    public ResponseEntity<?> modifyAdmin(@RequestBody Map<String, Object> params) {

        return null;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAdmin(@RequestBody Map<String, Object> params) {

        return null;
    }
}
