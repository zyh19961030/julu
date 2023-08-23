package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="更新选项分值入参", description="更新选项分值入参")
public class AdminPrivateUpdateOptionValueParam {
    @ApiModelProperty(value = "检查表名")
    private String tableName;
    private String name;
}
