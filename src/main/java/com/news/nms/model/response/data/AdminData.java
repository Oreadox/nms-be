package com.news.nms.model.response.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.news.nms.config.PermissionConfig;
import com.news.nms.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminData {
    private Integer id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String department;
    @JsonProperty(value = "enable_totp")
    private Boolean enableTotp;
    @JsonProperty(value = "create_time")
    private Timestamp createTime;
    private Set<String> permission;

    public void setAdmin(Admin admin) {
        this.id = admin.getId();
        this.username = admin.getUsername();
        this.name = admin.getName();
        this.email = admin.getEmail();
        this.phone = admin.getPhone();
        this.department = admin.getDepartment();
        this.enableTotp = admin.getEnableTotp();
        this.createTime = admin.getCreateTime();
        this.permission = PermissionConfig.toSet(admin.getPermission());
    }
}
