package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "单病种上报统计Vo", description = "单病种上报统计Vo")
public class QSingleDiseaseTakeReportStatisticVo {
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
    @ApiModelProperty(value = "国家上报率")
    @Excel(name = "国家上报率")
    private String completeReportCountryRate;
    @ApiModelProperty(value = "平均住院日")
    @Excel(name = "平均住院日")
    private String averageInHospitalDay;
    @ApiModelProperty(value = "平均住院费用")
    @Excel(name = "平均住院费用")
    private String averageInHospitalFee;
    @ApiModelProperty(value = "平均药品费用")
    @Excel(name = "平均药品费用")
    private String averageDrugFee;
    @ApiModelProperty(value = "平均手术治疗费")
    @Excel(name = "平均手术治疗费")
    private String averageOperationTreatmentFee;
    @ApiModelProperty(value = "平均一次性耗材费")
    @Excel(name = "平均一次性耗材费")
    private String averageDisposableConsumable;
    @ApiModelProperty(value = "死亡率")
    @Excel(name = "死亡率")
    private String mortality;
    @ApiModelProperty(value = "手术并发症发生率")
    @Excel(name = "手术并发症发生率")
    private String operationComplicationRate;

}
