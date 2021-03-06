package com.news.nms.model.response.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.news.nms.entity.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

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
    private Integer count;
    @JsonProperty(value = "release_time")
    private Timestamp releaseTime;
    private Integer checked;
    private List<Integer> tags;

    public void setNews(News news){
        this.id = news.getId();
        this.title = news.getTitle();
        this.content = news.getContent();
        this.count = news.getCount();
        this.releaseTime = news.getReleaseTime();
        this.checked=news.getChecked();
    }
}
