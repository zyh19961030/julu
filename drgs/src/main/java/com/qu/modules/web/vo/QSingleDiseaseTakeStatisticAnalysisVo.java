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
@ApiModel(value = "全院单病种上报数量统计-点击某个病种-统计分析Vo", description = "全院单病种上报数量统计-点击某个病种-统计分析Vo")
public class QSingleDiseaseTakeStatisticAnalysisVo {

    @ApiModelProperty(value = "分类id")
    private String categoryId;

    @ApiModelProperty(value = "年月")
    private String yearMonth;

    @ApiModelProperty(value = "上报例数")
    private Integer completeReportCountryCount;

    @ApiModelProperty(value = "平均住院日")
    private BigDecimal averageInHospitalDay;

    @ApiModelProperty(value = "平均住院费用")
    private BigDecimal averageInHospitalFee;

    @ApiModelProperty(value = "死亡率")
    private String mortality;

    @ApiModelProperty(value = "手术并发症发生率")
    private String complicationRate;

}
