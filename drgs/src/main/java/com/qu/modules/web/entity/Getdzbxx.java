package com.qu.modules.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@TableName("getdzbxx")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="getdzbxx对象", description="单病种数据视图")
public class Getdzbxx {

	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "id")
	private String id;

	@Excel(name = "病案号", width = 15)
	@ApiModelProperty(value = "病案号")
	private String caseId;

	@Excel(name = "患者id", width = 15)
	@ApiModelProperty(value = "患者id")
	private String patientNo;

	@Excel(name = "单病种名称", width = 15)
	@ApiModelProperty(value = "单病种名称")
	private String DRGID;

	@Excel(name = "题目编号", width = 15)
	@ApiModelProperty(value = "题目编号")
	private String COLID;

	@Excel(name = "答案", width = 15)
	@ApiModelProperty(value = "答案")
	private String COLCODE;

	@Excel(name = "上报时间", width = 15)
	@ApiModelProperty(value = "上报时间")
	private String SBSJ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getPatientNo() {
		return patientNo;
	}

	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}

	public String getDRGID() {
		return DRGID;
	}

	public void setDRGID(String DRGID) {
		this.DRGID = DRGID;
	}

	public String getCOLID() {
		return COLID;
	}

	public void setCOLID(String COLID) {
		this.COLID = COLID;
	}

	public String getCOLCODE() {
		return COLCODE;
	}

	public void setCOLCODE(String COLCODE) {
		this.COLCODE = COLCODE;
	}

	public String getSBSJ() {
		return SBSJ;
	}

	public void setSBSJ(String SBSJ) {
		this.SBSJ = SBSJ;
	}
}
