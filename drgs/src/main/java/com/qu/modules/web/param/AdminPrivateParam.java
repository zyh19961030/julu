package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="修改入参", description="修改入参")
public class AdminPrivateParam {
    private String name;
    private String table;
    private Integer quId;
}
