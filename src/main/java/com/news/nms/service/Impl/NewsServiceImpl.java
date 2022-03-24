package com.news.nms.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.news.nms.entity.News;
import com.news.nms.mapper.NewsMapper;
import com.news.nms.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
    @Autowired
    NewsMapper newsMapper;

    @Override
    public List<News> getByKeyword(String keyword) {
        // TODO: tag功能待添加
        return newsMapper.selectList(new QueryWrapper<News>().
                like("title", keyword).or().like("content", keyword));
    }
}
