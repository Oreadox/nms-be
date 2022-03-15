package com.news.nms.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.news.nms.entity.News;
import com.news.nms.mapper.NewsMapper;
import com.news.nms.service.NewsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
}
