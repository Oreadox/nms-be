package com.news.nms.controller;

import com.news.nms.config.PermissionConfig;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @PostMapping("/picture")
    @RequiresPermissions(PermissionConfig.NEWS_NEW_AND_EDIT)
    public ResponseEntity<?> uploadPicture(@RequestBody Map<String, Object> params) {
        return null;
    }
}
