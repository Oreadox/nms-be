package com.news.nms.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsPutRequest {
    @NotNull(message = "新闻id不能为null")
    private Integer id;
    private String title;
    private String content;
    @JsonProperty(value = "use_markdown")
    private Boolean useMarkdown;
    private Integer checked;
    private List<Integer> tags;
}
