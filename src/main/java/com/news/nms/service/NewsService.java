package com.news.nms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.news.nms.entity.News;

import java.util.List;

public interface NewsService extends IService<News> {
    List<News> getByKeyword(String keyword);
    List<News> getUnchecked();
}
