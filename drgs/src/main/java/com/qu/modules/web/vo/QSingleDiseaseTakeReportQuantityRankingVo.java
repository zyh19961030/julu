package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "全院单病种上报数量统计-查看图表-单病种上报例数排名Vo", description = "全院单病种上报数量统计-查看图表-单病种上报例数排名Vo")
public class QSingleDiseaseTakeReportQuantityRankingVo {
    @ApiModelProperty(value = "x轴病种")
    private String disease;
    @ApiModelProperty(value = "y轴数量")
    private BigDecimal number;
}
