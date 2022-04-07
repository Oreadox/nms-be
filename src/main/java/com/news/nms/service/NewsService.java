package com.news.nms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.news.nms.entity.News;

import java.util.List;

public interface NewsService extends IService<News> {
    List<News> getByKeyword(String keyword);
    List<News> getUnchecked();
    List<News> getByPage(Integer page, Integer numEachPage);
    List<News> getByCount(Integer num);
    List<News> getByAuthorId(Integer id);
    List<News> getAll();
}
