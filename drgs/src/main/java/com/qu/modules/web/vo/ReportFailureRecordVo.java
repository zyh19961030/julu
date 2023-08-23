package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @auther zhangyihao
 * @date 2021-12-23 14:09
 */
public class ReportFailureRecordVo {

    @ApiModelProperty(value = "序号")
    private Integer id;

    @ApiModelProperty(value = "住院号")
    private String hospitalInNo;

    @ApiModelProperty(value = "姓名")
    private String patientName;

    @ApiModelProperty(value = "填报病种id")
    private Integer questionId;

    @ApiModelProperty(value = "填报病种名称")
    private String questionName;

    @ApiModelProperty(value = "上报科室")
    private String departmentName;

    @ApiModelProperty(value = "失败原因")
    private String countryExamineReason;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "上报时间")
    private Date countryReportTime;

    @ApiModelProperty(value = "填报时间")
    private Date writeTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHospitalInNo() {
        return hospitalInNo;
    }

    public void setHospitalInNo(String hospitalInNo) {
        this.hospitalInNo = hospitalInNo;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCountryExamineReason() {
        return countryExamineReason;
    }

    public void setCountryExamineReason(String countryExamineReason) {
        this.countryExamineReason = countryExamineReason;
    }

    public Date getCountryReportTime() {
        return countryReportTime;
    }

    public void setCountryReportTime(Date countryReportTime) {
        this.countryReportTime = countryReportTime;
    }

    public Date getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(Date writeTime) {
        this.writeTime = writeTime;
    }
}
