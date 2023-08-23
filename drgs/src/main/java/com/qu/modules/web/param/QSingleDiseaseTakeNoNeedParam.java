package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "设为无需填报入参", description = "设为无需填报入参")
public class QSingleDiseaseTakeNoNeedParam {
    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "id 必填")
    private Integer id;
    @ApiModelProperty(value = "原因id 暂时不传")
    private String reasonId;
    @NotBlank(message = "原因不能为空")
    @ApiModelProperty(value = "原因 必填")
    private String reason;
    @ApiModelProperty(value = "原因中的备注")
    private String reasonNote;
}
