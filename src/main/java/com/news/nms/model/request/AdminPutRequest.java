package com.news.nms.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminPutRequest {
    private Integer id;
    private String username;
    private String name;
    private Boolean enableTotp;
    private String password;
    private String permission;
}
