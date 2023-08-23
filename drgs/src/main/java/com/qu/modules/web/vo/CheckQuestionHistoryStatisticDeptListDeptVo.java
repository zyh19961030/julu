package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "检查管理_历史统计列表_被检查科室筛选条件Vo", description = "检查管理_历史统计列表_被检查科室筛选条件Vo")
public class CheckQuestionHistoryStatisticDeptListDeptVo {
    @ApiModelProperty(value = "科室id")
    private String departmentId;
    @ApiModelProperty(value = "科室名称")
    private String departmentName;
}
