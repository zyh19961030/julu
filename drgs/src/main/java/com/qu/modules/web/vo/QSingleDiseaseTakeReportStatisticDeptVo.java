package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "单病种上报统计科室筛选条件Vo", description = "单病种上报统计科室筛选条件Vo")
public class QSingleDiseaseTakeReportStatisticDeptVo {
    @ApiModelProperty(value = "科室id")
    private java.lang.String departmentId;
    @ApiModelProperty(value = "科室名称")
    private java.lang.String department;
}
