package com.news.nms.model.response.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.news.nms.entity.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsData {
    private Integer id;
    private String title;
    private String content;
    @JsonProperty(value = "author_username")
    private String authorUsername;
    @JsonProperty(value = "release_time")
    private Date releaseTime;

    public void setNews(News news){
        this.id = news.getId();
        this.title = news.getTitle();
        this.content = news.getTitle();
        this.releaseTime = news.getReleaseTime();
    }
}
