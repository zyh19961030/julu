package com.qu.modules.web.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="全院单病种上报数量统计-点击某个病种-科室对比-五个图表入参", description="全院单病种上报数量统计-点击某个病种-科室对比-五个图表入参")
public class QSingleDiseaseTakeStatisticDepartmentComparisonChartParam {
    @NotBlank(message = "分类id(单病种)不能为空")
    @ApiModelProperty(value = "分类id")
    private String categoryId;
    @NotBlank(message = "图表类型不能为空")
    @ApiModelProperty(value = "图表类型  0上报例数多科室趋势图 1平均住院日多科室趋势图 2平均住院费用多科室趋势图 3死亡率多科室趋势图 4手术并发症发生率多科室趋势图")
    private String chartType;
    @NotBlank(message = "开始时间不能为空")
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="开始时间格式不对")
    @ApiModelProperty(value = "开始时间  格式：月:yyyy-MM")
    private String dateStart;
    @NotBlank(message = "结束时间不能为空")
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="结束时间格式不对")
    @ApiModelProperty(value = "结束时间  格式：月:yyyy-MM")
    private String dateEnd;
}
