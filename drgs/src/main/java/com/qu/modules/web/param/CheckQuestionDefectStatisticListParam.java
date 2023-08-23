package com.qu.modules.web.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="CheckQuestionDefectStatisticListParam缺陷统计_科室平均分等相关入参", description="CheckQuestionDefectStatisticListParam缺陷统计_科室平均分等相关入参")
public class CheckQuestionDefectStatisticListParam {
    @ApiModelProperty(value = "问卷id")
    @NotNull(message = "问卷id不能为空")
    private Integer quId;

    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="选择检查月份_时间格式不对")
    @ApiModelProperty(value = "选择检查月份_时间  格式：月:yyyy-MM")
    private String checkMonth;

    @NotBlank(message = "检查科室id不能为空")
    @ApiModelProperty(value = "检查科室id")
    private String deptId;

//    @NotBlank(message = "被检查科室id不能为空")
    @ApiModelProperty(value = "被检查科室id")
    private String checkedDeptId;

}
