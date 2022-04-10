package com.news.nms.controller;

import com.news.nms.config.PermissionConfig;
import com.news.nms.model.request.AdminPostRequest;
import com.news.nms.model.request.AdminPutRequest;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public interface AdminController {
    @GetMapping
    @RequiresAuthentication
    ResponseEntity<?> getAdminStatus();

    @GetMapping(value = "/{id}")
    @RequiresAuthentication
    ResponseEntity<?> getAdminStatusById(@PathVariable Integer id);

    @GetMapping(value = "/all")
    @RequiresPermissions(PermissionConfig.USER_ALL)
    ResponseEntity<?> getAllAdmin();

    @GetMapping(value = "/search/{keyword}")
    @RequiresPermissions(PermissionConfig.USER_ALL)
    ResponseEntity<?> getAdminStatusByKeyword(@PathVariable String keyword);

    @PostMapping
    @RequiresPermissions(PermissionConfig.USER_ALL)
    ResponseEntity<?> addAdmin(@RequestBody @Validated AdminPostRequest request);

    @PutMapping
    @RequiresAuthentication
    ResponseEntity<?> modifyAdmin(@RequestBody @Validated AdminPutRequest request);

    @DeleteMapping(value = "/{id}")
    @RequiresPermissions(PermissionConfig.USER_ALL)
    ResponseEntity<?> deleteAdmin(@PathVariable Integer id);
}
