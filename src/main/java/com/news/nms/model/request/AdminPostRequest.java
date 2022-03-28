package com.news.nms.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminPostRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;
    private String name;
    @NotBlank(message = "密码")
    private String password;
    private String permission;
}
