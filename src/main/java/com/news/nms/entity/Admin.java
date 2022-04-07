package com.news.nms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String name;
    private String email;
    private String department;
    private String totp;
    private Boolean enableTotp;
    private String passwordHash;
    private String permission;
    private Timestamp createTime;
}
