package com.news.nms.model.response.data;

import com.news.nms.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagData {
    private Integer id;
    private String name;
    public void setTag(Tag tag){
        this.id = tag.getId();
        this.name = tag.getTagName();
    }
}
