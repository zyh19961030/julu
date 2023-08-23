package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "SelectResponsibilityUserIdsParam", description = "SelectResponsibilityUserIdsParam")
public class SelectResponsibilityUserIdsParam {
    @NotNull(message = "问卷id不能为空")
    @ApiModelProperty(value = "问卷id")
    private Integer quId;
}
