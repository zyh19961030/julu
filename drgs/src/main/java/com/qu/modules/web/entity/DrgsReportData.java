package com.qu.modules.web.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Data
@Document(collection = "drgs_report_data")
//@Document(collection = "metadata")
public class DrgsReportData {
    @Field("_id")
    private String id;
    @Field("diseaseId")
    private String diseaseId;
    @Field("caseId")
    private String caseId;
    @Field("diseaseTypes")
    private String diseaseTypes;
    @Field("versionNumber")
    private String versionNumber;
    @Field("formVersion")
    private String formVersion;
    @Field("reportTime")
    private String reportTime;
    @Field("firstReportTime")
    private String firstReportTime;
    @Field("createTime")
    private String createTime;
    @Field("reportContent")
    private Map<String, Object> reportContent;
    @Field("dataState")
    private String dataState;
    @Field("inner")
    private Object inner;
    @Field("innerStatus")
    private String innerStatus;
    @Field("deptNo")
    private String deptNo;
    @Field("gatherTag")
    private String gatherTag;
    @Field("leaveHosTime")
    private String leaveHosTime;
    @Field("dataTime")
    private String dataTime;
    @Field(" periodicTime")
    private String periodicTime;
    @Field("patientName")
    private String patientName;
    @Field("indicationsDoctor")
    private String indicationsDoctor;
    @Field("chargeDoctor")
    private String chargeDoctor;
    @Field("medicalGroupLeader")
    private String medicalGroupLeader;
    @Field("bigDepartment")
    private String bigDepartment;
    @Field("excludeStatus")
    private String excludeStatus;
    @Field("submitTimeliness")
    private String submitTimeliness;
    @Field("completeRate")
    private String completeRate;
    @Field("funcResult")
    private Object funcResult;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getDiseaseTypes() {
        return diseaseTypes;
    }

    public void setDiseaseTypes(String diseaseTypes) {
        this.diseaseTypes = diseaseTypes;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(String formVersion) {
        this.formVersion = formVersion;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getFirstReportTime() {
        return firstReportTime;
    }

    public void setFirstReportTime(String firstReportTime) {
        this.firstReportTime = firstReportTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Map<String, Object> getReportContent() {
        return reportContent;
    }

    public void setReportContent(Map<String, Object> reportContent) {
        this.reportContent = reportContent;
    }

    public String getDataState() {
        return dataState;
    }

    public void setDataState(String dataState) {
        this.dataState = dataState;
    }

    public Object getInner() {
        return inner;
    }

    public void setInner(Object inner) {
        this.inner = inner;
    }

    public String getInnerStatus() {
        return innerStatus;
    }

    public void setInnerStatus(String innerStatus) {
        this.innerStatus = innerStatus;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public String getGatherTag() {
        return gatherTag;
    }

    public void setGatherTag(String gatherTag) {
        this.gatherTag = gatherTag;
    }

    public String getLeaveHosTime() {
        return leaveHosTime;
    }

    public void setLeaveHosTime(String leaveHosTime) {
        this.leaveHosTime = leaveHosTime;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getPeriodicTime() {
        return periodicTime;
    }

    public void setPeriodicTime(String periodicTime) {
        this.periodicTime = periodicTime;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getIndicationsDoctor() {
        return indicationsDoctor;
    }

    public void setIndicationsDoctor(String indicationsDoctor) {
        this.indicationsDoctor = indicationsDoctor;
    }

    public String getChargeDoctor() {
        return chargeDoctor;
    }

    public void setChargeDoctor(String chargeDoctor) {
        this.chargeDoctor = chargeDoctor;
    }

    public String getMedicalGroupLeader() {
        return medicalGroupLeader;
    }

    public void setMedicalGroupLeader(String medicalGroupLeader) {
        this.medicalGroupLeader = medicalGroupLeader;
    }

    public String getBigDepartment() {
        return bigDepartment;
    }

    public void setBigDepartment(String bigDepartment) {
        this.bigDepartment = bigDepartment;
    }

    public String getExcludeStatus() {
        return excludeStatus;
    }

    public void setExcludeStatus(String excludeStatus) {
        this.excludeStatus = excludeStatus;
    }

    public String getSubmitTimeliness() {
        return submitTimeliness;
    }

    public void setSubmitTimeliness(String submitTimeliness) {
        this.submitTimeliness = submitTimeliness;
    }

    public String getCompleteRate() {
        return completeRate;
    }

    public void setCompleteRate(String completeRate) {
        this.completeRate = completeRate;
    }

    public Object getFuncResult() {
        return funcResult;
    }

    public void setFuncResult(Object funcResult) {
        this.funcResult = funcResult;
    }
}
