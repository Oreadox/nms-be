package com.news.nms.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Override
    public List<News> getUnchecked() {
        return newsMapper.selectList(new QueryWrapper<News>().eq("checked", false));
    }

    @Override
    public List<News> getByPage(Integer page, Integer numEachPage) {
        Page<News> newsPage = newsMapper.selectPage(new Page<>(page, numEachPage),
                new QueryWrapper<News>().orderByDesc("id"));
        return newsPage.getRecords();
    }

    @Override
    public List<News> getByCount(Integer num) {
        return newsMapper.selectList(new QueryWrapper<News>().orderByDesc("count").last("limit "+num));
    }

    @Override
    public List<News> getByAuthorId(Integer id) {
        return newsMapper.selectList(new QueryWrapper<News>().eq("author_id", id));
    }

    @Override
    public List<News> getAll() {
        return newsMapper.selectList(null);
    }
}
