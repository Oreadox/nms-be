package com.news.nms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class News {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;             // 未格式化的新闻内容
    private Boolean useMarkdown;
    private Boolean checked;
    private Integer AuthorId;
    private Date releaseTime;
}

