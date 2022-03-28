package com.news.nms.model.response;

import com.news.nms.entity.News;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Accessors(chain = true)
@AllArgsConstructor
public class NewsMapResponse extends BaseResponse {
    
    public void setNews(List<News> newsList) {
        for (News news : newsList) {
            addNews(news);
        }
    }

    public void addNews(News news) {

    }
}
