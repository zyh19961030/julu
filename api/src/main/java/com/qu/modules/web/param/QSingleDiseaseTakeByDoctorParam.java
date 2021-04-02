package com.qu.modules.web.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel(value="按医生填报入参", description="按医生填报入参")
public class QSingleDiseaseTakeByDoctorParam {
    @ApiModelProperty(value = "患者姓名")
    private String patientName;
    @ApiModelProperty(value = "主治医生")
    private String doctorName;
    @ApiModelProperty(value = "病种名称")
    private String diseaseName;
    @ApiModelProperty(value = "状态：0待填报 1填报中 2已填报待审核 3审核失败驳回 4通过上报上中 5已上报待国家审核 6已完成 7已被国家驳回")
    private Integer status;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "住院日期起始时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date inHospitalStartDate;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "住院日期结束时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date inHospitalEndDate;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出院日期起始时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date outHospitalStartDate;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出院日期结束时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date outHospitalEndDate;
}