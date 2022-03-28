package com.news.nms.model.response;

import com.news.nms.model.response.data.NewsData;
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
public class NewsListResponse {
    private Integer status;
    private String message;
    private List<NewsData> data;
}
