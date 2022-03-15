package com.news.nms.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.news.nms.entity.NewsTags;
import com.news.nms.mapper.NewsTagsMapper;
import com.news.nms.service.NewsTagsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NewsTagsServiceImpl extends ServiceImpl<NewsTagsMapper, NewsTags> implements NewsTagsService {
}
