package com.news.nms.model.response;

import com.news.nms.model.response.data.TagData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TagListResponse {
    private Integer status;
    private String message;
    private List<TagData> data;
}
