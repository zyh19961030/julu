package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "调换题目顺序Param", description = "调换题目顺序Param")
public class UpdateOrderNumParam {
    @ApiModelProperty(value = "第一个ID号")
    private Integer ida;
    @ApiModelProperty(value = "第二个ID号")
    private Integer idb;
}
