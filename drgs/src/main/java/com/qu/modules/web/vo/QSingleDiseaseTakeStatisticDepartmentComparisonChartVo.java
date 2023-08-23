package com.qu.modules.web.vo;

import java.util.List;

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
@ApiModel(value = "全院单病种上报数量统计-点击某个病种-科室对比-五个图表Vo", description = "全院单病种上报数量统计-点击某个病种-科室对比-五个图表Vo")
public class QSingleDiseaseTakeStatisticDepartmentComparisonChartVo {



    @ApiModelProperty(value = "年月")
    private String yearMonth;


    @ApiModelProperty(value = "单位")
    private String rateType;


    @ApiModelProperty(value = "数据")
    private List<QSingleDiseaseTakeStatisticDepartmentComparisonChartInDeptVo> deptList;


}
