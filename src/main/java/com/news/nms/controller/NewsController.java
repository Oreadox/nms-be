package com.news.nms.controller;

import com.news.nms.config.PermissionConfig;
import com.news.nms.entity.Admin;
import com.news.nms.entity.News;
import com.news.nms.service.AdminService;
import com.news.nms.service.NewsService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("/news")
public class NewsController {
    @Autowired
    NewsService newsService;
    @Autowired
    AdminService adminService;

    @GetMapping(value="/{idOrKeyword}")
    public ResponseEntity<?> getNews(@PathVariable String idOrKeyword) {
        // TODO: tag功能待添加
        Integer id = null;
        String keyword = null;
        try {
            id = Integer.parseInt(idOrKeyword);
        } catch (NumberFormatException e) {
            keyword = idOrKeyword;
        }
        Map<String, Object> resp = new HashMap<>();
        List<Object> data = new ArrayList<>();
        Subject subject = SecurityUtils.getSubject();
        if (id != null) {
            News news = newsService.getById(id);
            if(news == null){
                resp.put("status", 0);
                resp.put("message", "该新闻不存在");
            } else {
                if(!news.getChecked() && !subject.isPermitted(PermissionConfig.NEWS_CHECK)){
                    resp.put("status", 0);
                    resp.put("message", "该新闻尚未通过审核");
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", news.getTitle());
                    map.put("content", news.getContent());
                    map.put("checked", news.getChecked());
                    map.put("author_username", adminService.getById(news.getId()).getUsername());
                    map.put("release_time", news.getReleaseTime());
                    data.add(map);
                    resp.put("data", data);
                    resp.put("status", 1);
                    resp.put("message", "成功");
                }
            }
        } else {
            List<News> newsList = newsService.getByKeyword(keyword);
            for(News news:newsList){
                if(!news.getChecked())
                    continue;
                Map<String, Object> map = new HashMap<>();
                map.put("title", news.getTitle());
                map.put("content", news.getContent());
                map.put("checked", news.getChecked());
                map.put("author_username", adminService.getById(news.getId()).getUsername());
                map.put("release_time", news.getReleaseTime());
                data.add(map);
            }
            resp.put("data", data);
            resp.put("status", 1);
            resp.put("message", "成功");
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping(value="/unchecked")
    @RequiresPermissions(PermissionConfig.NEWS_CHECK)
    public ResponseEntity<?> getUncheckedNews() {
        return null;
    }

    @PostMapping
    @RequiresPermissions(PermissionConfig.NEWS_NEW_AND_EDIT)
    public ResponseEntity<?> addNews(@RequestBody Map<String, Object> params) {
        // TODO: tag功能待添加
        Map<String, Object> resp = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        Admin admin = (Admin) subject.getPrincipal();
        String title = (String) params.get("title");
        String content = (String) params.get("content");
        if(title==null || content==null){
            resp.put("status", 0);
            resp.put("message", "标题或内容不能为空");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        News news = new News();
        news.setTitle(title);
        news.setContent(title);
        news.setAuthorId(admin.getId());
        try {
            newsService.save(news);
        } catch (Exception e){
            resp.put("status", 0);
            resp.put("message", "保存失败");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        resp.put("status", 1);
        resp.put("message", "提交成功，待审核");
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PutMapping
    @RequiresPermissions(value = {PermissionConfig.NEWS_NEW_AND_EDIT, PermissionConfig.NEWS_CHECK},
            logical = Logical.OR)
    public ResponseEntity<?> modifyNews(@RequestBody Map<String, Object> params) {
        return null;
    }

    @DeleteMapping(value="/{id}")
    @RequiresPermissions(PermissionConfig.NEWS_NEW_AND_EDIT)
    public ResponseEntity<?> deleteNews(@PathVariable Integer id) {
        return null;
    }
}
