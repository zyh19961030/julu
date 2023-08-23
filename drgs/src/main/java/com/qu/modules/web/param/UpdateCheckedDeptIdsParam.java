package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "UpdateCheckedDeptIdsParam", description = "UpdateCheckedDeptIdsParam")
public class UpdateCheckedDeptIdsParam {
    @NotNull(message = "问卷id不能为空")
    @ApiModelProperty(value = "问卷id")
    private Integer quId;
    @NotNull(message = "部门id数组不能为空")
    @ApiModelProperty(value = "部门id数组")
    private String[] deptIds;
}
