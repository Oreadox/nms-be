package com.news.nms.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.news.nms.model.response.data.TotpData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdminTotpResponse {
    private Integer status;
    private String message;
    @JsonInclude(NON_NULL)
    private TotpData data;
}
