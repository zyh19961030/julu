package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value="CheckQuestionHistoryStatisticDeptListParam入参", description="CheckQuestionHistoryStatisticDeptListParam入参")
public class CheckQuestionHistoryStatisticDeptListParam {
    /**问卷id*/
    @ApiModelProperty(value = "问卷id")
    @NotNull(message = "问卷id不能为空")
    private Integer quId;


}
