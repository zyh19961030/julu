package com.qu.modules.web.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel(value="患者登记表填报中查询参数", description="患者登记表填报中查询参数")
public class AnswerPatientFillingInParam {
    /**患者姓名*/
    @ApiModelProperty(value = "患者姓名")
    private String patientName;
    /**住院号*/
    @ApiModelProperty(value = "住院号")
    private String hospitalInNo;
//    /**问卷名称*/
//    @ApiModelProperty(value = "问卷名称")
//    private String quName;
    @ApiModelProperty(value = "问卷id")
    private Integer quId;
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
    @ApiModelProperty(value = "出院时间起始时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date outHospitalStartDate;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出院时间结束时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date outHospitalEndDate;
}
