package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value="全院单病种上报数量统计-查看图表-单病种上报数据概览入参", description="全院单病种上报数量统计-查看图表-单病种上报数据概览入参")
public class QSingleDiseaseTakeReportStatisticOverviewPieParam {
    @NotNull(message = "时间类型不能为空")
    @Pattern(regexp = "yearly|monthly|daily",message = "时间类型不正确")
    @ApiModelProperty(value = "时间类型：年yearly 月monthly 日daily")
    private String dateType;
    @NotNull(message = "时间不能为空")
    @Pattern(regexp="\\d{4}|\\d{4}-(0[1-9]|1[0-2])|\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])",message="时间格式不对")
    @ApiModelProperty(value = "时间  格式：年:yyyy 月:yyyy-MM 日:yyyy-MM-dd")
    private String date;
    /*@NotNull(message = "结束时间不能为空")
    @Pattern(regexp="\\d{4}|\\d{4}-(0[1-9]|1[0-2])|\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])",message="结束时间格式不对")
    @ApiModelProperty(value = "结束时间  格式：年:yyyy 月:yyyy-MM 日:yyyy-MM-dd")
    private String dateEnd;*/
    @ApiModelProperty(value = "科室id数组")
    private String[] dept;
    @ApiModelProperty(value = "分类id数组")
    private String[] categoryId;
}
