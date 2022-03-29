package com.news.nms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Date;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class News {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;             // 未格式化的新闻内容
    private Boolean useMarkdown;
    private Boolean checked;
    private Integer authorId;
    private Date releaseTime;
    private Integer count;
}

