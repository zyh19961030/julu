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
@ApiModel(value = "全院单病种上报数量统计-查看图表-科室上报单病种数量排列图-排列图Vo", description = "全院单病种上报数量统计-查看图表-科室上报单病种数量排列图-排列图Vo")
public class QSingleDiseaseTakeReportStatisticDeptPermutationVo {
    @ApiModelProperty(value = "x轴科室")
    private String dept;
    @ApiModelProperty(value = "y轴数量")
    private String number;
}
