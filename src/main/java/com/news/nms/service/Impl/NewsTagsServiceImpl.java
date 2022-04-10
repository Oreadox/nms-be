package com.news.nms.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.news.nms.entity.NewsTags;
import com.news.nms.mapper.NewsTagsMapper;
import com.news.nms.service.NewsTagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NewsTagsServiceImpl extends ServiceImpl<NewsTagsMapper, NewsTags> implements NewsTagsService {
    @Autowired
    NewsTagsMapper newsTagsMapper;

    @Override
    public Boolean checkByTagIdAndNewsId(Integer tagId, Integer newsId) {
        return newsTagsMapper.selectList(
                new QueryWrapper<NewsTags>().eq("tag_id", tagId).eq("news_id", newsId))!=null;
    }

    @Override
    public void removeByNewsId(Integer id) {
        newsTagsMapper.delete(new QueryWrapper<NewsTags>().eq("news_id", id));
    }
}
