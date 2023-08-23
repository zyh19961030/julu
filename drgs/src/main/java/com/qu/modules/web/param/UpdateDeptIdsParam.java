package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "批量更新问卷权限Param", description = "批量更新问卷权限Param")
public class UpdateDeptIdsParam {
    @ApiModelProperty(value = "问卷id数组")
    private java.lang.String[] quIds;
    @ApiModelProperty(value = "部门id数组")
    private java.lang.String[] deptIds;
}
