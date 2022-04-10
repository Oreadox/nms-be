package com.news.nms.controller;

import com.news.nms.config.PermissionConfig;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public interface FileController {
    @GetMapping("/presigned_url/{filename}")
    @RequiresPermissions(PermissionConfig.NEWS_NEW_AND_EDIT)
    ResponseEntity<?> getPresignedUrl(@PathVariable String filename);

    @GetMapping("/url/{filename}")
    @RequiresPermissions(PermissionConfig.NEWS_NEW_AND_EDIT)
    ResponseEntity<?> getFileUrl(@PathVariable String filename);
}
