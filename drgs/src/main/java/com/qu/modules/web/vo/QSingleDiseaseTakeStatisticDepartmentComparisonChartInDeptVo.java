package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "全院单病种上报数量统计-点击某个病种-科室对比-五个图表科室数据Vo", description = "全院单病种上报数量统计-点击某个病种-科室对比-五个图表科室数据Vo")
public class QSingleDiseaseTakeStatisticDepartmentComparisonChartInDeptVo {

    @ApiModelProperty(value = "年月")
    private String yearMonthTitle;

    @ApiModelProperty(value = "科室名称")
    private String deptName;

    @ApiModelProperty(value = "数据")
    private String number;


}
