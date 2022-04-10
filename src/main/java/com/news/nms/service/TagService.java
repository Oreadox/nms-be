package com.news.nms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.news.nms.entity.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {
    List<Tag> getAll();
    Tag getByTagName(String name);
}
