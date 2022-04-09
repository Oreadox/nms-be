package com.news.nms.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminPutRequest {
    @NotNull(message = "用户id不能为空")
    private Integer id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String department;
    @JsonProperty(value = "enable_totp")
    private Boolean enableTotp;
    private String password;
    private Set<String> permission;
}
