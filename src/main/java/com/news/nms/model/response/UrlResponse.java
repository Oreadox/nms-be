package com.news.nms.model.response;

import com.news.nms.model.response.data.UrlData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UrlResponse {
    private Integer status;
    private String message;
    private UrlData data;
}
