package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="选项库新增入参", description="选项库新增入参")
public class QoptionlibParam {
    @ApiModelProperty(value = "选项名称")
    private java.lang.String opName;
    @ApiModelProperty(value = "是否其他  0:否 1:是")
    private Integer others;
    @ApiModelProperty(value = "绑定值")
    private java.lang.String bindValue;
    /**选项值*/
    @ApiModelProperty(value = "选项值")
    private java.lang.String opValue;
}
