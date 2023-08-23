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
@ApiModel(value = "全院单病种上报数量统计-查看图表-单病种上报数据概览-折线图Vo", description = "全院单病种上报数量统计-查看图表-单病种上报数据概览-折线图Vo")
public class QSingleDiseaseTakeReportStatisticOverviewLineVo {
    @ApiModelProperty(value = "x轴日期")
    private String date;
    @ApiModelProperty(value = "y轴数量")
    private String number;
}
