package com.news.nms.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.news.nms.entity.Tag;
import com.news.nms.mapper.TagMapper;
import com.news.nms.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    TagMapper tagMapper;

    @Override
    public List<Tag> getAll() {
        return tagMapper.selectList(null);
    }

    @Override
    public Tag getByTagName(String name) {
        List<Tag> tagList = tagMapper.selectList(new QueryWrapper<Tag>().eq("tag_name", name));
        if (tagList != null)
            return tagList.get(0);
        return null;
    }
}
