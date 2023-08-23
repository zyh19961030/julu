package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "单病种上报统计分页Vo", description = "单病种上报统计Vo")
public class QSingleDiseaseTakeReportStatisticPageVo {
    @ApiModelProperty(value = "总条数")
    private long total;
    @ApiModelProperty(value = "数据")
    private List<QSingleDiseaseTakeReportStatisticVo> qSingleDiseaseTakeList;
}
