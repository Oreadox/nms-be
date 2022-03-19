package com.news.nms.controller;

import com.news.nms.config.PermissionConfig;
import com.news.nms.entity.Admin;
import com.news.nms.service.AdminService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
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
        String keyword = (String) params.get("search_keyword");
        admin = (Admin) subject.getPrincipal();
        if (id != null || keyword != null) {
            if (!subject.isPermitted(PermissionConfig.USER_ALL) &&
                    !((id != null) && (id.equals(admin.getId())))) {
                resp.put("message", "无权查看信息");
                resp.put("status", 0);
                return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
            }
            if (id != null) {
                // 根据id查找
                admin = adminService.getById(id);
            } else {
                // 根据关键字查找
                admin = adminService.getByKeyword(keyword);
            }
        }
//        System.out.println(admin);
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
    @RequiresPermissions(PermissionConfig.USER_ALL)
    public ResponseEntity<?> createAdmin(@RequestBody Map<String, Object> params) {
        Map<String, Object> resp = new HashMap<>();
        Admin admin = new Admin();
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        if (username == null || password == null) {
            resp.put("message", "用户名或密码不能为空");
            resp.put("status", 0);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        admin.setUsername(username);
        admin.setPasswordHash(new Md5Hash(password, "", 8).toHex());
        admin.setName((String) params.get("name"));
        admin.setPermission((String) params.get("permission"));
        try {
            adminService.save(admin);
        } catch (Exception e) {
            resp.put("message", "创建用户失败");
            resp.put("status", 0);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        resp.put("message", "创建成功");
        resp.put("status", 1);
        return new ResponseEntity<>(resp, HttpStatus.OK);

    }

    @PutMapping
    @RequiresAuthentication
    public ResponseEntity<?> modifyAdmin(@RequestBody Map<String, Object> params) {

        return null;
    }

    @DeleteMapping
    @RequiresPermissions(PermissionConfig.USER_ALL)
    public ResponseEntity<?> deleteAdmin(@RequestBody Map<String, Object> params) {

        return null;
    }
}
