package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "题库分页Param", description = "题库分页Param")
public class QsubjectlibParam {
    @ApiModelProperty(value = "题目名称")
    private java.lang.String subName;

    @ApiModelProperty(value = "备注")
    private java.lang.String remark;

}
