package com.qu.modules.web.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel(value="患者登记表已提交查询参数", description="患者登记表已提交查询参数")
public class AnswerPatientSubmitParam {
    /**患者姓名*/
    @ApiModelProperty(value = "患者姓名")
    private java.lang.String patientName;
    /**问卷名称*/
    @ApiModelProperty(value = "问卷名称")
    private String quName;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "入院日期起始时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date inHospitalStartDate;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "入院日期结束时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date   inHospitalEndDate;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "提交时间起始时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date submitStartDate;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "提交时间结束时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date submitEndDate;
    @ApiModelProperty(value = "答题人姓名")
    private String createrName;
    /**住院号*/
    @Excel(name = "住院号", width = 15)
    @ApiModelProperty(value = "住院号")
    private java.lang.String hospitalInNo;
}
