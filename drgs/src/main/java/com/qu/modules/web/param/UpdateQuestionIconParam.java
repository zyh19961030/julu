package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "设置问卷图标Param", description = "设置问卷图标Param")
public class UpdateQuestionIconParam {
    @ApiModelProperty(value = "问卷id")
    @NotNull(message = "问卷id数组不能为空")
    private Integer[] quId;
    @ApiModelProperty(value = "图标url")
    @NotBlank(message = "图标url不能为空")
    private String url;
}
