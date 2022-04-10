package com.news.nms.controller.impl;

import com.news.nms.controller.TagController;
import com.news.nms.entity.Tag;
import com.news.nms.model.response.TagListResponse;
import com.news.nms.model.response.data.TagData;
import com.news.nms.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagControllerImpl implements TagController {
    @Autowired
    TagService tagService;

    @Override
    @GetMapping("/all")
    public ResponseEntity<?> getAllTag() {
        List<Tag> tagList = tagService.getAll();
        List<TagData> dataList = new ArrayList<>();
        for (Tag tag : tagList) {
            TagData data = new TagData();
            data.setTag(tag);
            dataList.add(data);
        }
        return new ResponseEntity<>(
                TagListResponse.builder().status(1).message("成功").data(dataList).build()
                , HttpStatus.OK);
    }
}
