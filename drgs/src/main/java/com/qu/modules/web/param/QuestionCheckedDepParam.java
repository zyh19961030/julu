package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value="QuestionCheckedDepParam", description="QuestionCheckedDepParam")
public class QuestionCheckedDepParam {
    /**视图名称*/
    @ApiModelProperty(value = "视图名称")
    @NotBlank(message = "视图名称不能为空")
    private String viewName;
    /**问卷id*/
    @ApiModelProperty(value = "问卷id")
    @NotBlank(message = "问卷id不能为空")
    private Integer quId;
}
