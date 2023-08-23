package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="特殊逻辑入参", description="特殊逻辑入参")
public class SpecialLogicParam {
    @ApiModelProperty(value = "主键")
    private Integer id;
    /**特殊跳题逻辑*/
    @ApiModelProperty(value = "特殊跳题逻辑")
    private String specialJumpLogic;
}
