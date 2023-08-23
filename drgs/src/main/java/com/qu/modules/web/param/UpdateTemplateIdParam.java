package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "UpdateTemplateIdParam", description = "UpdateTemplateIdParam")
public class UpdateTemplateIdParam {
    @NotNull(message = "问卷id不能为空")
    @ApiModelProperty(value = "问卷id")
    private Integer quId;
    @NotNull(message = "模板id不能为空")
    @ApiModelProperty(value = "模板id")
    private String templateId;
}
