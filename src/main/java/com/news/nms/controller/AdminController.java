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
    public ResponseEntity<?> addAdmin(@RequestBody Map<String, Object> params) {
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
        String permission = (String) params.get("permission");
        if (permission != null && PermissionConfig.verifyPermission(permission))
            admin.setPermission(permission);
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
        Map<String, Object> resp = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        Admin admin = (Admin) subject.getPrincipal();
        Integer id = (Integer) params.get("id");
        if (id != null) {
            if (!subject.isPermitted(PermissionConfig.USER_ALL) && !id.equals(admin.getId())) {
                resp.put("message", "无权修改信息");
                resp.put("status", 0);
                return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
            } else {
                admin = adminService.getById(id);
            }
        }
        if (admin != null) {
            Admin adminNew = new Admin();
            adminNew.setId(admin.getId());
            adminNew.setName((String) params.get("name"));
            adminNew.setTotp((String) params.get("totp"));
            adminNew.setEnableTotp((Boolean) params.get("enable_totp"));
            String password = (String) params.get("password");
            if (password != null)
                adminNew.setPasswordHash(new Md5Hash(password, "", 8).toHex());
            String permission = (String) params.get("permission");
            if (permission != null && subject.isPermitted(PermissionConfig.USER_ALL)
                    && PermissionConfig.verifyPermission(permission)) {
                adminNew.setPermission(permission);
            }
            try {
                adminService.updateById(adminNew);
            } catch (Exception e) {
                resp.put("message", "更新失败");
                resp.put("status", 0);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }
            resp.put("message", "更新成功");
            resp.put("status", 1);
        } else {
            resp.put("message", "该用户不存在");
            resp.put("status", 0);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @DeleteMapping
    @RequiresPermissions(PermissionConfig.USER_ALL)
    public ResponseEntity<?> deleteAdmin(@RequestBody Map<String, Object> params) {
        Map<String, Object> resp = new HashMap<>();
        Integer id = (Integer) params.get("id");
        if (id == null) {
            resp.put("message", "缺少必要参数：id");
            resp.put("status", 0);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        Admin admin = adminService.getById(id);
        if (admin != null) {
            try {
                adminService.removeById(admin);
            } catch (Exception e) {
                resp.put("message", "删除失败");
                resp.put("status", 0);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }
            resp.put("message", "删除成功");
            resp.put("status", 1);
        } else {
            resp.put("message", "该用户不存在");
            resp.put("status", 0);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
