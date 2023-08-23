package com.qu.modules.web.param;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SelectCheckedDeptIdsParam", description = "SelectCheckedDeptIdsParam")
public class SelectCheckedDeptIdsParam {
    @NotNull(message = "问卷id不能为空")
    @ApiModelProperty(value = "问卷id")
    private Integer quId;
}
