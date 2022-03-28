package com.news.nms.model.response;

import com.news.nms.model.response.data.AdminData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdminMapResponse extends BaseResponse {
    private AdminData data;
}
