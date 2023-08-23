package com.qu.modules.web.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "月度/季度/年汇总填报中和已提交Vo", description = "月度/季度/年汇总填报中和已提交Vo")
public class AnswerMonthQuarterYearFillingInAndSubmitVo {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "问卷id")
    private Integer quId;
    @ApiModelProperty(value = "问卷名称")
    private String quName;
    @ApiModelProperty(value = "图标")
    private String icon;
    /**修改时间*/
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
    /**填报的月/季度/年*/
    @ApiModelProperty(value = "填报的月/季度/年")
    private java.lang.String questionAnswerTime;
    @ApiModelProperty(value = "答题人姓名")
    private String createrName;
    /**提交时间*/
    @ApiModelProperty(value = "提交时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date submitTime;
    @ApiModelProperty(value = "状态 0:可编辑1:已提交无法编辑")
    private Integer answerStatus;
    /**填报频次 0患者登记表 1月度汇总表 2季度汇总表 3年度汇总表*/
    @ApiModelProperty(value = "填报频次 0患者登记表 1月度汇总表 2季度汇总表 3年度汇总表")
    private java.lang.Integer writeFrequency;

}
