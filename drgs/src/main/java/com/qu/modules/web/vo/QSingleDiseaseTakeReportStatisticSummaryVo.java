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
@ApiModel(value = "全院单病种上报数量统计-查看图表-各病种上报情况汇总表Vo", description = "全院单病种上报数量统计-查看图表-各病种上报情况汇总表Vo")
public class QSingleDiseaseTakeReportStatisticSummaryVo {
    @ApiModelProperty(value = "x轴病种")
    private String disease;
    @ApiModelProperty(value = "y轴数量")
    private String number;
}
