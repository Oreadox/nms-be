package com.news.nms.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsPostRequest {
    @NotBlank(message = "新闻标题不能为空")
    private String title;
    @NotBlank(message = "新闻内容不能为空")
    private String content;
    @JsonProperty(value = "use_markdown")
    @NotNull(message = "是否使用md标记不能为null")
    private Boolean useMarkdown;
    private List<Integer> tags;
}
