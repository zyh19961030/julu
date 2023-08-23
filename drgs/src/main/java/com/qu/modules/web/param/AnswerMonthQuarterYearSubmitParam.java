package com.qu.modules.web.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(value="月度汇总-定期汇总已提交查询参数", description="月度汇总-定期汇总已提交查询参数")
public class AnswerMonthQuarterYearSubmitParam {
    /**问卷名称*/
    @ApiModelProperty(value = "问卷名称")
    private String quName;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "提交时间起始时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date submitStartDate;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "提交时间结束时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date submitEndDate;

    /**所属年份，季度，月份-填报的月/季度/年*/
    @ApiModelProperty(value = "所属年份，季度，月份-填报的月/季度/年")
    private java.lang.String questionAnswerTime;

    @ApiModelProperty(value = "答题人姓名")
    private String createrName;

    /**填报频次 -1全部 1月度汇总表 2季度汇总表 3年度汇总表*/
    @NotNull(message = "填报频次不能为空")
    @ApiModelProperty(value = "填报频次 -1全部 1月度汇总表 2季度汇总表 3年度汇总表")
    private java.lang.Integer writeFrequency;
}
