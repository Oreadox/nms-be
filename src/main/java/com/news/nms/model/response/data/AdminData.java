package com.news.nms.model.response.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.news.nms.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminData {
    private Integer id;
    private String username;
    private String name;
    private String email;
    private String department;
    @JsonProperty(value = "enable_totp")
    private Boolean enableTotp;
    @JsonProperty(value = "create_time")
    private Timestamp createTime;

    public void setAdmin(Admin admin) {
        this.id = admin.getId();
        this.username = admin.getUsername();
        this.name = admin.getName();
        this.email = admin.getEmail();
        this.department = admin.getDepartment();
        this.enableTotp = admin.getEnableTotp();
        this.createTime = admin.getCreateTime();
    }
}
