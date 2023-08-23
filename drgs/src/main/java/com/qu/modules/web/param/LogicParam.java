package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="逻辑入参", description="逻辑入参")
public class LogicParam {
    @ApiModelProperty(value = "主键")
    private Integer id;
    /**跳题逻辑*/
    @ApiModelProperty(value = "跳题逻辑")
    private String jumpLogic;
}
