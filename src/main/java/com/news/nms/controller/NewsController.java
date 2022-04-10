package com.news.nms.controller;

import com.news.nms.config.PermissionConfig;
import com.news.nms.model.request.NewsPostRequest;
import com.news.nms.model.request.NewsPutRequest;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news")
public interface NewsController {
    @GetMapping(value = "/{id}")
    ResponseEntity<?> getNewsById(@PathVariable Integer id);

    @GetMapping(value = "/page/{page}")
    ResponseEntity<?> getNewsByPage(@PathVariable Integer page);

    @GetMapping(value = "/page")
    ResponseEntity<?> getNewsByPage();

    @GetMapping(value = "/tag/{tag}/page/{page}")
    ResponseEntity<?> getNewsByTagAndPage(@PathVariable String tag, @PathVariable Integer page);

    @GetMapping(value = "/tag/{tag}")
    ResponseEntity<?> getNewsByTag(@PathVariable String tag);

    @GetMapping(value = "/popular/{num}")
    ResponseEntity<?> getNewsByPopular(@PathVariable Integer num);

    @GetMapping(value = "/popular")
    ResponseEntity<?> getNewsByPopular();

    @GetMapping(value = "/search/{keyword}")
    ResponseEntity<?> getNewsByKeyword(@PathVariable String keyword);

    @GetMapping(value = "/my")
    @RequiresAuthentication
    ResponseEntity<?> getMyNews();

    @GetMapping(value = "/all")
    @RequiresAuthentication
    ResponseEntity<?> getAllNews();

    @GetMapping(value = "/unchecked")
    @RequiresPermissions(PermissionConfig.NEWS_CHECK)
    ResponseEntity<?> getUncheckedNews();

    @PostMapping
    @RequiresPermissions(PermissionConfig.NEWS_NEW_AND_EDIT)
    ResponseEntity<?> addNews(@RequestBody @Validated NewsPostRequest request);

    @PutMapping
    @RequiresPermissions(value = {PermissionConfig.NEWS_NEW_AND_EDIT, PermissionConfig.NEWS_CHECK},
            logical = Logical.OR)
    ResponseEntity<?> modifyNews(@RequestBody @Validated NewsPutRequest request);

    @DeleteMapping(value = "/{id}")
    @RequiresPermissions(PermissionConfig.NEWS_NEW_AND_EDIT)
    ResponseEntity<?> deleteNews(@PathVariable Integer id);
}

