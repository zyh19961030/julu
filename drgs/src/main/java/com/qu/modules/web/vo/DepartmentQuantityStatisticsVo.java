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
@ApiModel(value = "科室数量统计列表Vo", description = "科室数量统计列表Vo")
public class DepartmentQuantityStatisticsVo {


    @ApiModelProperty(value = "科室id")
    private String tqmsDept;

    @ApiModelProperty(value = "科室名称")
    @Excel(name = "科室名称")
    private String deptName;

    @ApiModelProperty(value = "需上报")
    @Excel(name = "需上报")
    private Integer needReportCount;

    @ApiModelProperty(value = "未上报")
    @Excel(name = "未上报")
    private Integer notReportCount;

    @ApiModelProperty(value = "科室上报待审核")
    @Excel(name = "科室上报待审核")
    private Integer uploadWaitCheckCount;

    @ApiModelProperty(value = "科室提交率")
    @Excel(name = "科室提交率")
    private String uploadWaitCheckRate;

    @ApiModelProperty(value = "已上报")
    @Excel(name = "已上报")
    private Integer completeReportCount;

    @ApiModelProperty(value = "上报率")
    @Excel(name = "上报率")
    private String completeReportRate;

}
