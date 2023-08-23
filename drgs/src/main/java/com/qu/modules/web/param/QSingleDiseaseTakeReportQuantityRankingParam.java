package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value="全院单病种上报数量统计-查看图表-单病种上报例数排名入参", description="全院单病种上报数量统计-查看图表-单病种上报例数排名入参")
public class QSingleDiseaseTakeReportQuantityRankingParam {
    @NotNull(message = "开始时间不能为空")
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="开始时间格式不对")
    @ApiModelProperty(value = "开始时间  格式：月:yyyy-MM")
    private String dateStart;
    @NotNull(message = "结束时间不能为空")
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="结束时间格式不对")
    @ApiModelProperty(value = "结束时间  格式：月:yyyy-MM")
    private String dateEnd;
}
