package com.news.nms.controller;

import com.news.nms.config.PermissionConfig;
import com.news.nms.entity.Admin;
import com.news.nms.service.AdminService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping
    @RequiresAuthentication
    public ResponseEntity<?> getAdminStatus() {
        Map<String, Object> resp = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        Admin admin = adminService.getById(((Admin) subject.getPrincipal()).getId());
        data.put("id", admin.getId());
        data.put("username", admin.getUsername());
        data.put("name", admin.getName());
        data.put("enableTotp", admin.getEnableTotp());
        data.put("permission", admin.getPermission());
        resp.put("data", data);
        resp.put("message", "成功");
        resp.put("status", 1);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping(value = "/{idOrKeyword}")
    @RequiresAuthentication
    public ResponseEntity<?> getAdminStatusById(@PathVariable String idOrKeyword) {
        Integer id = null;
        String keyword = null;
        try {
            id = Integer.parseInt(idOrKeyword);
        } catch (NumberFormatException e) {
            keyword = idOrKeyword;
        }
        Subject subject = SecurityUtils.getSubject();
        Map<String, Object> resp = new HashMap<>();
        if (id != null) {
            Admin admin = (Admin) subject.getPrincipal();
            if (!subject.isPermitted(PermissionConfig.USER_ALL) && !(id.equals(admin.getId()))) {
                resp.put("message", "无权查看信息");
                resp.put("status", 0);
                return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
            }
            admin = adminService.getById(id);
            if (admin != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", admin.getId());
                data.put("username", admin.getUsername());
                data.put("name", admin.getName());
                data.put("enableTotp", admin.getEnableTotp());
                data.put("permission", admin.getPermission());
                data.put("create_time", admin.getCreateTime());
                resp.put("data", data);
                resp.put("message", "成功");
                resp.put("status", 1);
            } else {
                resp.put("message", "未找到该用户");
                resp.put("status", 0);
            }
        } else {
            List<Map<String, Object>> data = new ArrayList<>();
            List<Admin> admins = adminService.getByKeyword(keyword);
//        System.out.println(admin);
            for (Admin admin : admins) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", admin.getId());
                map.put("username", admin.getUsername());
                map.put("name", admin.getName());
                map.put("enableTotp", admin.getEnableTotp());
                map.put("permission", admin.getPermission());
                map.put("create_time", admin.getCreateTime());
                data.add(map);
            }
            if (admins.size() == 0) {
                resp.put("message", "未找到符合条件的用户");
                resp.put("status", 0);
            }
            resp.put("data", data);
            resp.put("message", "成功");
            resp.put("status", 1);
        }

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
            Boolean enabledTotp = admin.getEnableTotp();
            adminNew.setId(admin.getId());
            adminNew.setName((String) params.get("name"));
            //adminNew.setTotp((String) params.get("totp"));
            Boolean enableTotp = (Boolean) params.get("enable_totp");
            adminNew.setEnableTotp(enableTotp);
            if (!enabledTotp && enableTotp) {
                Map<String, Object> data = new HashMap<>();
                String totp = new GoogleAuthenticator().createCredentials().getKey();
                data.put("totp", totp);
                resp.put("data", data);
                adminNew.setTotp(totp);
            }
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

    @DeleteMapping(value = "/{id}")
    @RequiresPermissions(PermissionConfig.USER_ALL)
    public ResponseEntity<?> deleteAdmin(@PathVariable Integer id) {
        Map<String, Object> resp = new HashMap<>();
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
