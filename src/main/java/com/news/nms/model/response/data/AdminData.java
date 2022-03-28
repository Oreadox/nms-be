package com.news.nms.model.response.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class AdminData {
    private Integer id;
    private String username;
    private String name;
    @JsonProperty(value = "enable_totp")
    private Boolean enableTotp;
    @JsonProperty(value = "create_time")
    private Date createTime;
}
