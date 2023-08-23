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
@ApiModel(value = "单病种上报统计Vo", description = "单病种上报统计Vo")
public class QSingleDiseaseTakeStatisticDeptVo {
    @ApiModelProperty(value = "分类id")
    private String categoryId;
    private String dynamicTableName;
    @ApiModelProperty(value = "病种名称")
    private String disease;
    /**tqms的科室id*/
    @ApiModelProperty(value = "tqms的科室id")
    private String tqmsDept;
    /**tqms科室名字*/
    @ApiModelProperty(value = "tqms科室名字")
    private String tqmsDeptName;
    /**需要上报数*/
    @ApiModelProperty(value = "需要上报数")
    private Integer needReportCount;
    /**已上报数*/
    @ApiModelProperty(value = "已上报数")
    private Integer completeReportCount;
    /**未上报数*/
    @ApiModelProperty(value = "未上报数")
    private Integer notReportCount;
    @ApiModelProperty(value = "国家上报率")
    private String completeReportCountryRate;
    @ApiModelProperty(value = "平均住院日")
    private BigDecimal averageInHospitalDay;
    @ApiModelProperty(value = "总和住院日")
    private Integer sumInHospitalDay;
    @ApiModelProperty(value = "平均住院费用")
    private BigDecimal averageInHospitalFee;
    @ApiModelProperty(value = "总和住院费用")
    private BigDecimal sumInHospitalFee;
    @ApiModelProperty(value = "平均药品费用")
    private BigDecimal averageDrugFee;
    @ApiModelProperty(value = "总和药品费用")
    private BigDecimal sumDrugFee;
    @ApiModelProperty(value = "平均手术治疗费")
    private BigDecimal averageOperationTreatmentFee;
    @ApiModelProperty(value = "总和手术治疗费")
    private BigDecimal sumOperationTreatmentFee;
    @ApiModelProperty(value = "平均一次性耗材费")
    private BigDecimal averageDisposableConsumable;
    @ApiModelProperty(value = "总和一次性耗材费")
    private BigDecimal sumDisposableConsumable;
    @ApiModelProperty(value = "死亡率")
    private String mortality="0.00%";
    @ApiModelProperty(value = "死亡个数")
    private Integer mortalityCount;
    @ApiModelProperty(value = "手术并发症发生率")
    private String operationComplicationRate="0.00%";
    @ApiModelProperty(value = "手术并发症个数")
    private Integer operationComplicationRateCount;

}
