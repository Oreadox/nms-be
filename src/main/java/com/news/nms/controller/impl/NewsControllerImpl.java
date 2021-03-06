package com.news.nms.controller.impl;

import com.news.nms.config.PermissionConfig;
import com.news.nms.controller.NewsController;
import com.news.nms.entity.Admin;
import com.news.nms.entity.News;
import com.news.nms.entity.NewsTags;
import com.news.nms.entity.Tag;
import com.news.nms.model.request.NewsPostRequest;
import com.news.nms.model.request.NewsPutRequest;
import com.news.nms.model.response.BaseResponse;
import com.news.nms.model.response.NewsListResponse;
import com.news.nms.model.response.NewsResponse;
import com.news.nms.model.response.data.NewsData;
import com.news.nms.service.AdminService;
import com.news.nms.service.NewsService;
import com.news.nms.service.NewsTagsService;
import com.news.nms.service.TagService;
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
public class NewsControllerImpl implements NewsController {
    @Autowired
    NewsService newsService;
    @Autowired
    AdminService adminService;
    @Autowired
    TagService tagService;
    @Autowired
    NewsTagsService newsTagsService;

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getNewsById(@PathVariable Integer id) {
        Subject subject = SecurityUtils.getSubject();
        News news = newsService.getById(id);
        if (news == null) {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("该新闻不存在").build()
                    , HttpStatus.OK);
        } else {
            if (news.getChecked() <= 0 && !(subject.isPermitted(PermissionConfig.NEWS_CHECK) ||
                    subject.isPermitted(PermissionConfig.NEWS_NEW_AND_EDIT))) {
                return new ResponseEntity<>(
                        BaseResponse.builder().status(0).message("该新闻尚未通过审核").build()
                        , HttpStatus.OK);
            } else {
                NewsData data = NewsData.builder()
                        .authorUsername(adminService.getById(news.getAuthorId()).getUsername()).build();
                data.setNews(news);
                List<Integer> tags = new ArrayList<>();
                List<NewsTags> newsTagsList = newsTagsService.getByNewsId(id);
                for (NewsTags newsTags : newsTagsList) {
                    tags.add(newsTags.getTagId());
                }
                data.setTags(tags);
                news.setCount(news.getCount() + 1);
                try {
                    newsService.updateById(news);
                } catch (Exception ignored) {
                }
                return new ResponseEntity<>(
                        NewsResponse.builder().status(1).message("成功").data(data).build()
                        , HttpStatus.OK);
            }
        }
    }

    @Override
    @GetMapping(value = "/page/{page}")
    public ResponseEntity<?> getNewsByPage(@PathVariable Integer page) {
        List<NewsData> dataList = new ArrayList<>();
        List<News> newsList = newsService.getByPage(page, 15);
        for (News news : newsList) {
            if (news.getChecked() <= 0)
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

    @Override
    @GetMapping(value = "/page")
    public ResponseEntity<?> getNewsByPage() {
        return getNewsByPage(1);
    }

    @Override
    @GetMapping(value = "/tag/{tag}/page/{page}")
    public ResponseEntity<?> getNewsByTagAndPage(@PathVariable String tag, @PathVariable Integer page) {
        List<NewsData> dataList = new ArrayList<>();
        List<News> newsList = newsService.getByTagNameAndPage(tag, page, 15);
        for (News news : newsList) {
            if (news.getChecked() <= 0)
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

    @Override
    @GetMapping(value = "/tag/{tag}")
    public ResponseEntity<?> getNewsByTag(@PathVariable String tag) {
        List<NewsData> dataList = new ArrayList<>();
        List<News> newsList = newsService.getByTagName(tag);
        for (News news : newsList) {
            if (news.getChecked() <= 0)
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


    @Override
    @GetMapping(value = "/popular/{num}")
    public ResponseEntity<?> getNewsByPopular(@PathVariable Integer num) {
        List<NewsData> dataList = new ArrayList<>();
        List<News> newsList = newsService.getByCount(num);
        for (News news : newsList) {
            if (news.getChecked() <= 0)
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

    @Override
    @GetMapping(value = "/popular")
    public ResponseEntity<?> getNewsByPopular() {
        return getNewsByPopular(10);
    }

    @Override
    @GetMapping(value = "/search/{keyword}")
    public ResponseEntity<?> getNewsByKeyword(@PathVariable String keyword) {
        List<NewsData> dataList = new ArrayList<>();
        List<News> newsList = newsService.getByKeyword(keyword);
        for (News news : newsList) {
            if (news.getChecked() <= 0)
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    @PostMapping
    @RequiresPermissions(PermissionConfig.NEWS_NEW_AND_EDIT)
    public ResponseEntity<?> addNews(@RequestBody @Validated NewsPostRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Admin admin = (Admin) subject.getPrincipal();
        News news = News.builder().title(request.getTitle()).content(request.getContent())
                .useMarkdown(request.getUseMarkdown()).authorId(admin.getId()).build();
        try {
            newsService.save(news);
            List<Integer> tagIdList = request.getTags();
            if (tagIdList != null) {
                for (Integer tagId : tagIdList) {
                    Tag tag = tagService.getById(tagId);
                    NewsTags newsTags = NewsTags.builder()
                            .newsId(news.getId()).tagId(tag.getId()).tagName(tag.getTagName()).build();
                    newsTagsService.save(newsTags);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>(
                    BaseResponse.builder().status(0).message("保存失败").build()
                    , HttpStatus.OK);
        }
        return new ResponseEntity<>(
                BaseResponse.builder().status(1).message("提交成功，待审核").build()
                , HttpStatus.OK);
    }

    @Override
    @PutMapping
    @RequiresPermissions(value = {PermissionConfig.NEWS_NEW_AND_EDIT, PermissionConfig.NEWS_CHECK},
            logical = Logical.OR)
    public ResponseEntity<?> modifyNews(@RequestBody @Validated NewsPutRequest request) {
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
                if (canEdit) {
                    List<Integer> tagIdList = request.getTags();
                    if (tagIdList != null) {
                        newsTagsService.removeByNewsId(news.getId());
                        for (Integer tagId : tagIdList) {
                            Tag tag = tagService.getById(tagId);
                            NewsTags newsTags = NewsTags.builder()
                                    .newsId(news.getId()).tagId(tag.getId()).tagName(tag.getTagName()).build();
                            newsTagsService.save(newsTags);
                        }
                    }
                }
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

    @Override
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
                newsTagsService.removeByNewsId(id);
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
