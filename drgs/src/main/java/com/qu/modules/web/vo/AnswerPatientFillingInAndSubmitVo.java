package com.qu.modules.web.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "患者登记表填报中和已提交Vo", description = "患者登记表填报中和已提交Vo")
public class AnswerPatientFillingInAndSubmitVo {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "问卷id")
    private Integer quId;
    @ApiModelProperty(value = "问卷名称")
    private String quName;
    /**答案对应数据库名*/
    @ApiModelProperty(value = "答案对应数据库名")
    private java.lang.String tableName;
    /**患者姓名*/
    @ApiModelProperty(value = "患者姓名")
    private java.lang.String patientName;
    /**住院号*/
    @ApiModelProperty(value = "住院号")
    private java.lang.String hospitalInNo;
    /**年龄*/
    @ApiModelProperty(value = "年龄")
    private java.lang.Integer age;
    /**住院时间*/
    @ApiModelProperty(value = "住院时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date inTime;
    /**修改时间*/
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
    /**提交时间*/
    @ApiModelProperty(value = "提交时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date submitTime;
    @ApiModelProperty(value = "答题人姓名")
    private String createrName;
}
