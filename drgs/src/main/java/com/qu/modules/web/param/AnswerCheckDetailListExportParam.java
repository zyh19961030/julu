package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value="AnswerCheckDetailListParam检查表_检查明细记录入参", description="AnswerCheckDetailListParam检查表_检查明细记录入参")
public class AnswerCheckDetailListExportParam {
    @ApiModelProperty(value = "问卷id")
    @NotNull(message = "问卷id不能为空")
    private Integer quId;
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="选择检查月份_时间格式不对")
    @ApiModelProperty(value = "选择检查月份_时间  格式：月:yyyy-MM")
    private String checkMonth;
    @ApiModelProperty(value = "userId")
    @NotBlank(message = "userId不能为空")
    private String userId;
}
