package com.qu.modules.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "QSingleDiseaseTakeReportStatisticDto", description = "QSingleDiseaseTakeReportStatisticDto")
public class QSingleDiseaseTakeReportStatisticDto {
    /*@ApiModelProperty(value = "问卷id")
        @Excel(name = "科室名称")
    private Integer questionId;*/
    @ApiModelProperty(value = "分类id")
//    @Excel(name = "分类id")
    private String categoryId;
    @ApiModelProperty(value = "病种名称")
    @Excel(name = "病种名称")
    private String disease;
    @ApiModelProperty(value = "需要上报数")
    @Excel(name = "需要上报数")
    private Integer needReportCount;
    @ApiModelProperty(value = "已上报数")
    @Excel(name = "已上报数")
    private Integer completeReportCount;
    @ApiModelProperty(value = "未上报数")
    @Excel(name = "未上报数")
    private Integer notReportCount;
//    @ApiModelProperty(value = "国家上报率")
//    @Excel(name = "国家上报率")
//    private String completeReportCountryRate;
    @ApiModelProperty(value = "总和住院日")
    @Excel(name = "总和住院日")
    private Integer sumInHospitalDay;
    @ApiModelProperty(value = "总和住院费用")
    @Excel(name = "总和住院费用")
    private BigDecimal sumInHospitalFee;
    @ApiModelProperty(value = "总和药品费用")
    @Excel(name = "总和药品费用")
    private BigDecimal sumDrugFee;
    @ApiModelProperty(value = "总和手术治疗费")
    @Excel(name = "总和手术治疗费")
    private BigDecimal sumOperationTreatmentFee;
    @ApiModelProperty(value = "总和一次性耗材费")
    @Excel(name = "总和一次性耗材费")
    private BigDecimal sumDisposableConsumable;
    @ApiModelProperty(value = "死亡个数")
    @Excel(name = "死亡个数")
    private Integer mortalityCount;
    @ApiModelProperty(value = "手术并发症发生个数")
    @Excel(name = "手术并发症发生个数")
    private Integer operationComplicationRateCount;

}
