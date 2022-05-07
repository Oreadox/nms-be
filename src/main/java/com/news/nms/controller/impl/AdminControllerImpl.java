package com.news.nms.controller.impl;

import com.news.nms.config.PermissionConfig;
import com.news.nms.controller.AdminController;
import com.news.nms.entity.Admin;
import com.news.nms.model.request.AdminPostRequest;
import com.news.nms.model.request.AdminPutRequest;
import com.news.nms.model.response.AdminListResponse;
import com.news.nms.model.response.AdminResponse;
import com.news.nms.model.response.AdminTotpResponse;
import com.news.nms.model.response.BaseResponse;
import com.news.nms.model.response.data.AdminData;
import com.news.nms.model.response.data.TotpData;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminControllerImpl implements AdminController {
    @Autowired
    private AdminService adminService;

    @Override
    @GetMapping
    @RequiresAuthentication
    public ResponseEntity<?> getAdminStatus() {
        Subject subject = SecurityUtils.getSubject();
        Admin admin = adminService.getById(((Admin) subject.getPrincipal()).getId());
        AdminData data = new AdminData();
        data.setAdmin(admin);
        return new ResponseEntity<>(
                AdminResponse.builder().status(1).message("成功").data(data).build()
                , HttpStatus.OK);
    }

    @Override
    @GetMapping(value = "/{id}")
    @RequiresAuthentication
    public ResponseEntity<?> getAdminStatusById(@PathVariable Integer id) {
        Subject subject = SecurityUtils.getSubject();
        Admin admin = (Admin) subject.getPrincipal();
        if (!subject.isPermitted(PermissionConfig.USER_ALL) && !(id.equals(admin.getId()))) {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("无权查看信息").build()
                    , HttpStatus.UNAUTHORIZED);
        }
        admin = adminService.getById(id);
        if (admin != null) {
            AdminData data = new AdminData();
            data.setAdmin(admin);
            return new ResponseEntity<>(
                    AdminResponse.builder().status(1).message("成功").data(data).build()
                    , HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("未找到该用户").build()
                    , HttpStatus.OK);
        }
    }

    @Override
    @GetMapping(value = "/all")
    @RequiresPermissions(PermissionConfig.USER_ALL)
    public ResponseEntity<?> getAllAdmin() {
        List<AdminData> dataList = new ArrayList<>();
        List<Admin> adminList = adminService.getAll();
        for (Admin admin : adminList) {
            AdminData data = new AdminData();
            data.setAdmin(admin);
            dataList.add(data);
        }
        return new ResponseEntity<>(
                AdminListResponse.builder().status(1).message("成功").data(dataList).build()
                , HttpStatus.OK);
    }

    @Override
    @GetMapping(value = "/search/{keyword}")
    @RequiresPermissions(PermissionConfig.USER_ALL)
    public ResponseEntity<?> getAdminStatusByKeyword(@PathVariable String keyword) {
        List<AdminData> dataList = new ArrayList<>();
        List<Admin> adminList = adminService.getByKeyword(keyword);
        for (Admin admin : adminList) {
            AdminData data = new AdminData();
            data.setAdmin(admin);
            dataList.add(data);
        }
        return new ResponseEntity<>(
                AdminListResponse.builder().status(1).message("成功").data(dataList).build()
                , HttpStatus.OK);
    }

    @Override
    @PostMapping
    @RequiresPermissions(PermissionConfig.USER_ALL)
    public ResponseEntity<?> addAdmin(@RequestBody @Validated AdminPostRequest request) {
        if (adminService.getByUsername(request.getUsername()) != null) {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("用户名重复").build()
                    , HttpStatus.OK);
        }
        Admin admin = Admin.builder().username(request.getUsername()).name(request.getName())
                .passwordHash(new Md5Hash(request.getPassword(), "", 8).toHex())
                .build();

        if (request.getPermission() != null && PermissionConfig.verifyPermission(request.getPermission())) {
            admin.setPermission(PermissionConfig.toString(request.getPermission()));
        }
        try {
            adminService.save(admin);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("保存失败").build()
                    , HttpStatus.OK);
        }
        return new ResponseEntity<>(
                BaseResponse.builder().status(1).message("成功").build()
                , HttpStatus.OK);
    }

    @Override
    @PutMapping
    @RequiresAuthentication
    public ResponseEntity<?> modifyAdmin(@RequestBody @Validated AdminPutRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Admin admin = (Admin) subject.getPrincipal();
        Integer id = request.getId();
        if (id != null) {
            if (!subject.isPermitted(PermissionConfig.USER_ALL) && !id.equals(admin.getId())) {
                return new ResponseEntity<>(
                        BaseResponse.builder().status(0).message("无权修改信息").build()
                        , HttpStatus.UNAUTHORIZED);
            } else {
                admin = adminService.getById(id);
            }
        }
        if (admin != null) {
            Admin adminNew = Admin.builder().id(admin.getId()).name(request.getName())
                    .email(request.getEmail()).phone(request.getPhone()).department(request.getDepartment())
                    .enableTotp(request.getEnableTotp()).build();
            Boolean enabledTotp = admin.getEnableTotp();
            String password = request.getPassword();
            if (password != null && !password.equals(""))
                adminNew.setPasswordHash(new Md5Hash(password, "", 8).toHex());
            String permission = PermissionConfig.toString(request.getPermission());
            if (permission != null && subject.isPermitted(PermissionConfig.USER_ALL)
                    && PermissionConfig.verifyPermission(permission)) {
                adminNew.setPermission(permission);
            }
            TotpData data = null;
            if (request.getEnableTotp() != null)
                if (!enabledTotp && request.getEnableTotp()) {
                    String totp = new GoogleAuthenticator().createCredentials().getKey();
                    adminNew.setTotp(totp);
                    data = TotpData.builder().totp(totp).build();
                }
//            System.out.println(adminNew);
            try {
                adminService.updateById(adminNew);
            } catch (Exception e) {
                return new ResponseEntity<>(
                        BaseResponse.builder().status(0).message("保存失败").build()
                        , HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    AdminTotpResponse.builder().status(1).message("保存成功").data(data).build()
                    , HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("该用户不存在").build()
                    , HttpStatus.OK);
        }
    }

    @Override
    @DeleteMapping(value = "/{id}")
    @RequiresPermissions(PermissionConfig.USER_ALL)
    public ResponseEntity<?> deleteAdmin(@PathVariable Integer id) {
        Admin admin = adminService.getById(id);
        if (admin != null) {
            try {
                // 该用户创建的新闻也会被数据库关联删除
                adminService.removeById(admin);
            } catch (Exception e) {
                return new ResponseEntity<>(
                        BaseResponse.builder().status(0).message("删除失败").build()
                        , HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    BaseResponse.builder().status(1).message("删除成功").build()
                    , HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(1).message("该用户不存在").build()
                    , HttpStatus.OK);
        }
    }
}
