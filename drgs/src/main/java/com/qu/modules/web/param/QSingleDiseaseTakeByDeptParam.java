package com.qu.modules.web.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel(value="本科室单病种上报记录查询-全院单病种上报记录查询共用", description="本科室单病种上报记录查询-全院单病种上报记录查询共用")
public class QSingleDiseaseTakeByDeptParam {
    @ApiModelProperty(value = "分类id")
    private String categoryId;
    @ApiModelProperty(value = "科室id")
    private String deptId;
    @ApiModelProperty(value = "患者姓名")
    private String patientName;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出院日期起始时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date outHospitalStartDate;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出院日期结束时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date outHospitalEndDate;
    @ApiModelProperty(value = "主治医生")
    private String doctorName;
    @ApiModelProperty(value = "主要诊断")
    private String mainDiagnosis;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "填报时间起始时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date writeTimeStartDate;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "填报时间结束时间 格式：yyyy-MM-dd HH:mm:ss")
    private Date writeTimeEndDate;
    @ApiModelProperty(value = "状态：0待填报 1填报中 2已填报待审核 3审核失败驳回 4通过上报上中 5已上报待国家审核 6已完成 7已被国家驳回 8无需填报")
    private Integer[] status;
    /**住院编号-病案号*/
    @ApiModelProperty(value = "住院编号-病案号")
    private java.lang.String hospitalInNo;

}
