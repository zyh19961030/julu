package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value="科室数量统计列表入参", description="科室数量统计列表入参")
public class DepartmentQuantityStatisticsParam {
    @NotBlank(message = "开始时间不能为空")
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])",message="开始时间格式不对")
    @ApiModelProperty(value = "开始时间  格式：日:yyyy-MM-dd")
    private String dateStart;
    @NotBlank(message = "结束时间不能为空")
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])",message="结束时间格式不对")
    @ApiModelProperty(value = "结束时间  格式：日:yyyy-MM-dd")
    private String dateEnd;
}
