package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "UpdateResponsibilityUserIdsParam", description = "UpdateResponsibilityUserIdsParam")
public class UpdateResponsibilityUserIdsParam {
    @NotNull(message = "问卷id不能为空")
    @ApiModelProperty(value = "问卷id")
    private Integer quId;
    @NotNull(message = "责任人id数组不能为空")
    @ApiModelProperty(value = "责任人id数组")
    private String[] userIds;
}
