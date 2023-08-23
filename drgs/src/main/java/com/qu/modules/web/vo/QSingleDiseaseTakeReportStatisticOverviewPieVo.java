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
@ApiModel(value = "全院单病种上报数量统计-查看图表-单病种上报数据概览-饼图Vo", description = "全院单病种上报数量统计-查看图表-单病种上报数据概览-饼图Vo")
public class QSingleDiseaseTakeReportStatisticOverviewPieVo {
    @ApiModelProperty(value = "病种名称")
    private String disease;
    @ApiModelProperty(value = "人数")
    private String number;
    @ApiModelProperty(value = "百分比")
    private String percentage;
}
