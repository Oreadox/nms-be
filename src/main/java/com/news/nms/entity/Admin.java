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
public class Admin {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String name;
    private String totp;
    private Boolean enableTotp;
    private String passwordHash;
    private String permission;
    private Date createTime;
}
