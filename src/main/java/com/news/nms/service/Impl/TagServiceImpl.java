package com.news.nms.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.news.nms.entity.Tag;
import com.news.nms.mapper.TagMapper;
import com.news.nms.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}
