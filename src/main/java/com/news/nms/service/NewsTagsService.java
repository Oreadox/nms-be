package com.news.nms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.news.nms.entity.NewsTags;

import java.util.List;

public interface NewsTagsService extends IService<NewsTags> {
    Boolean checkByTagIdAndNewsId(Integer tagId, Integer newsId);
    void removeByNewsId(Integer id);
    List<NewsTags> getByNewsId(Integer id);
}
