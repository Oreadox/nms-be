package com.news.nms.controller;

import com.news.nms.config.PermissionConfig;
import com.news.nms.entity.Admin;
import com.news.nms.entity.News;
import com.news.nms.model.request.NewsPostRequest;
import com.news.nms.model.request.NewsPutRequest;
import com.news.nms.model.response.BaseResponse;
import com.news.nms.model.response.NewsListResponse;
import com.news.nms.model.response.NewsResponse;
import com.news.nms.model.response.data.NewsData;
import com.news.nms.service.AdminService;
import com.news.nms.service.NewsService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    NewsService newsService;
    @Autowired
    AdminService adminService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getNewsById(@PathVariable Integer id) {
        Subject subject = SecurityUtils.getSubject();
        News news = newsService.getById(id);
        if (news == null) {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("该新闻不存在").build()
                    , HttpStatus.OK);
        } else {
            if (news.getChecked()<=0 && !(subject.isPermitted(PermissionConfig.NEWS_CHECK)||
                    subject.isPermitted(PermissionConfig.NEWS_NEW_AND_EDIT))) {
                return new ResponseEntity<>(
                        BaseResponse.builder().status(0).message("该新闻尚未通过审核").build()
                        , HttpStatus.OK);
            } else {
                NewsData data = NewsData.builder()
                        .authorUsername(adminService.getById(news.getAuthorId()).getUsername()).build();
                data.setNews(news);
                news.setCount(news.getCount() + 1);
                try {
                    newsService.updateById(news);
                } catch (Exception ignored){ }
                return new ResponseEntity<>(
                        NewsResponse.builder().status(1).message("成功").data(data).build()
                        , HttpStatus.OK);
            }
        }
    }

    @GetMapping(value = "/page/{page}")
    public ResponseEntity<?> getNewsByPage(@PathVariable Integer page) {
        List<NewsData> dataList = new ArrayList<>();
        List<News> newsList = newsService.getByPage(page, 15);
        for (News news : newsList) {
            if (news.getChecked()<=0)
                continue;
            NewsData data = NewsData.builder()
                    .authorUsername(adminService.getById(news.getAuthorId()).getUsername()).build();
            data.setNews(news);
            dataList.add(data);
        }
        return new ResponseEntity<>(
                NewsListResponse.builder().status(1).message("成功").data(dataList).build()
                , HttpStatus.OK);
    }

    @GetMapping(value = "/page")
    public ResponseEntity<?> getNewsByPage() {
        return getNewsByPage(1);
    }

    @GetMapping(value = "/popular/{num}")
    public ResponseEntity<?> getNewsByPopular(@PathVariable Integer num) {
        List<NewsData> dataList = new ArrayList<>();
        List<News> newsList = newsService.getByCount(num);
        for (News news : newsList) {
            if (news.getChecked()<=0)
                continue;
            NewsData data = NewsData.builder()
                    .authorUsername(adminService.getById(news.getAuthorId()).getUsername()).build();
            data.setNews(news);
            dataList.add(data);
        }
        return new ResponseEntity<>(
                NewsListResponse.builder().status(1).message("成功").data(dataList).build()
                , HttpStatus.OK);
    }

    @GetMapping(value = "/popular")
    public ResponseEntity<?> getNewsByPopular() {
        return getNewsByPopular(10);
    }

    @GetMapping(value = "/search/{keyword}")
    public ResponseEntity<?> getNews(@PathVariable String keyword) {
        List<NewsData> dataList = new ArrayList<>();
        List<News> newsList = newsService.getByKeyword(keyword);
        for (News news : newsList) {
            if (news.getChecked()<=0)
                continue;
            NewsData data = NewsData.builder()
                    .authorUsername(adminService.getById(news.getAuthorId()).getUsername()).build();
            data.setNews(news);
            dataList.add(data);
        }
        return new ResponseEntity<>(
                NewsListResponse.builder().status(1).message("成功").data(dataList).build()
                , HttpStatus.OK);
    }

    @GetMapping(value = "/my")
    @RequiresAuthentication
    public ResponseEntity<?> getMyNews() {
        Subject subject = SecurityUtils.getSubject();
        Admin admin = adminService.getById(((Admin) subject.getPrincipal()).getId());
        List<NewsData> dataList = new ArrayList<>();
        List<News> newsList = newsService.getByAuthorId(admin.getId());
        for (News news : newsList) {
            NewsData data = NewsData.builder()
                    .authorUsername(adminService.getById(news.getAuthorId()).getUsername()).build();
            data.setNews(news);
            data.setContent("");
            dataList.add(data);
        }
        return new ResponseEntity<>(
                NewsListResponse.builder().status(1).message("成功").data(dataList).build()
                , HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    @RequiresAuthentication
    public ResponseEntity<?> getAllNews() {
        List<NewsData> dataList = new ArrayList<>();
        List<News> newsList = newsService.getAll();
        for (News news : newsList) {
            NewsData data = NewsData.builder()
                    .authorUsername(adminService.getById(news.getAuthorId()).getUsername()).build();
            data.setNews(news);
            data.setContent("");
            dataList.add(data);
        }
        return new ResponseEntity<>(
                NewsListResponse.builder().status(1).message("成功").data(dataList).build()
                , HttpStatus.OK);
    }

    @GetMapping(value = "/unchecked")
    @RequiresPermissions(PermissionConfig.NEWS_CHECK)
    public ResponseEntity<?> getUncheckedNews() {
        List<News> newsList = newsService.getUnchecked();
        List<NewsData> dataList = new ArrayList<>();
        for (News news : newsList) {
            NewsData data = NewsData.builder()
                    .authorUsername(adminService.getById(news.getAuthorId()).getUsername()).build();
            data.setNews(news);
            dataList.add(data);
        }
        return new ResponseEntity<>(
                NewsListResponse.builder().status(1).message("成功").data(dataList).build()
                , HttpStatus.OK);
    }

    @PostMapping
    @RequiresPermissions(PermissionConfig.NEWS_NEW_AND_EDIT)
    public ResponseEntity<?> addNews(@RequestBody @Validated NewsPostRequest request) {
        // TODO: tag功能待添加
        Subject subject = SecurityUtils.getSubject();
        Admin admin = (Admin) subject.getPrincipal();
        News news = News.builder().title(request.getTitle()).content(request.getContent())
                .useMarkdown(request.getUseMarkdown()).authorId(admin.getId()).build();
        try {
            newsService.save(news);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("保存失败").build()
                    , HttpStatus.OK);
        }
        return new ResponseEntity<>(
                BaseResponse.builder().status(1).message("提交成功，待审核").build()
                , HttpStatus.OK);
    }

    @PutMapping
    @RequiresPermissions(value = {PermissionConfig.NEWS_NEW_AND_EDIT, PermissionConfig.NEWS_CHECK},
            logical = Logical.OR)
    public ResponseEntity<?> modifyNews(@RequestBody @Validated NewsPutRequest request) {
        // TODO: tag功能待添加
        Subject subject = SecurityUtils.getSubject();
        Boolean canCheck = subject.isPermitted(PermissionConfig.NEWS_CHECK);
        Boolean canEdit = subject.isPermitted(PermissionConfig.NEWS_NEW_AND_EDIT);
        News news = newsService.getById(request.getId());
        if (news == null) {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("该新闻不存在").build()
                    , HttpStatus.OK);
        } else {
            News news1 = new News().setId(request.getId());
            if (canCheck) {
                news1.setChecked(request.getChecked());
            }
            if (canEdit) {
                news1.setTitle(request.getTitle()).setContent((request.getContent()));
            }
            try {
                newsService.updateById(news1);
            } catch (Exception e) {
                return new ResponseEntity<>(
                        BaseResponse.builder().status(0).message("保存失败").build()
                        , HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    BaseResponse.builder().status(1).message("提交成功").build()
                    , HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/{id}")
    @RequiresPermissions(PermissionConfig.NEWS_NEW_AND_EDIT)
    public ResponseEntity<?> deleteNews(@PathVariable Integer id) {
        News news = newsService.getById(id);
        if (news == null) {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("该新闻不存在").build()
                    , HttpStatus.OK);
        } else {
            try {
                newsService.removeById(id);
            } catch (Exception e) {
                return new ResponseEntity<>(
                        BaseResponse.builder().status(0).message("删除失败").build()
                        , HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    BaseResponse.builder().status(1).message("删除成功").build()
                    , HttpStatus.OK);
        }
    }
}
