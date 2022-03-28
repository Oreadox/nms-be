package com.news.nms.model.response;

import com.news.nms.model.response.data.AdminData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdminListResponse {
    private Integer status;
    private String message;
    private List<AdminData> data;
}
