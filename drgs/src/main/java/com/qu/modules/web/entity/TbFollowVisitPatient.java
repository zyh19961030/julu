package com.qu.modules.web.entity;

import java.util.Date;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 随访患者信息表
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
@Data
@TableName("tb_follow_visit_patient")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tb_follow_visit_patient对象", description="随访患者信息表")
public class TbFollowVisitPatient {

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**患者姓名*/
	@Excel(name = "患者姓名", width = 15)
    @ApiModelProperty(value = "患者姓名")
	private String name;
	/**病案号*/
	@Excel(name = "病案号", width = 15)
    @ApiModelProperty(value = "病案号")
	private String caseId;
	/**住院次数*/
	@Excel(name = "住院次数", width = 15)
    @ApiModelProperty(value = "住院次数")
	private String inHospitalNumber;
	/**电话*/
	@Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
	private String phone;
	/**患者主要诊断*/
	@Excel(name = "患者主要诊断", width = 15)
    @ApiModelProperty(value = "患者主要诊断")
	private String diagnosis;
	/**出院时间*/
	@Excel(name = "出院时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出院时间")
	private Date outTime;
    /**科室id*/
    @Excel(name = "科室id", width = 15)
    @ApiModelProperty(value = "科室id")
    private String deptId;
    /**疾病code*/
    @Excel(name = "疾病code", width = 15)
    @ApiModelProperty(value = "疾病code")
    private String code;
	/**删除状态（0：未删除  1：已删除）*/
	@Excel(name = "删除状态（0：未删除  1：已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0：未删除  1：已删除）")
	private Integer delState;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**身份证号*/
	@Excel(name = "身份证号", width = 15)
    @ApiModelProperty(value = "身份证号")
    private String idCard;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
	private String contactName;
    /**联系人电话*/
	@Excel(name = "联系人电话", width = 15)
    @ApiModelProperty(value = "联系人电话")
    private String contactPhone;
	/**住院医师*/
	@Excel(name = "住院医师", width = 15)
    @ApiModelProperty(value = "住院医师")
    private String inHospitalDoctor;


}
