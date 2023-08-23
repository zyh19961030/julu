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
@ApiModel(value = "全院单病种上报数量统计-点击某个病种-科室对比Vo", description = "全院单病种上报数量统计-点击某个病种-科室对比Vo")
public class QSingleDiseaseTakeStatisticDepartmentComparisonVo {

    @ApiModelProperty(value = "科室名称")
    private String deptName;

    @ApiModelProperty(value = "需上报")
    private Integer needWriteCount;

    @ApiModelProperty(value = "已填报")
    private Integer completeWriteCount;

    @ApiModelProperty(value = "未上报")
    private Integer notWriteCount;

    @ApiModelProperty(value = "上报率")
    private String completeReportCountryRate;

    @ApiModelProperty(value = "平均住院日")
    private BigDecimal averageInHospitalDay;

    @ApiModelProperty(value = "平均住院费用")
    private BigDecimal averageInHospitalFee;

    @ApiModelProperty(value = "手术并发症发生率")
    private String complicationRate;

    @ApiModelProperty(value = "死亡率")
    private String mortality;

    @ApiModelProperty(value = "平均药品费用")
    private BigDecimal averageDrugFee;

    @ApiModelProperty(value = "平均手术治疗费")
    private BigDecimal averageOperationFee;

    @ApiModelProperty(value = "平均一次性耗材费")
    private BigDecimal averageDisposableConsumableFee;

}
