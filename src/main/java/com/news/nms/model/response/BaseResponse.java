package com.news.nms.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private Integer status;
    private String message;
}
