package com.qu.modules.web.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 随访患者记录表
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
@Data
@TableName("tb_follow_visit_patient_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbFollowVisitPatientRecordListVo", description="TbFollowVisitPatientRecordListVo")
public class TbFollowVisitPatientRecordListVo {

    @ApiModelProperty(value = "id")
	private Integer id;

    @ApiModelProperty(value = "患者姓名")
	private String patientName;

    @ApiModelProperty(value = "病案号")
	private String caseId;

    @ApiModelProperty(value = "电话")
	private String phone;

    @ApiModelProperty(value = "患者主要诊断")
    private String diagnosis;

    @ApiModelProperty(value = "随访内容")
    private String followVisitTemplate;

    @ApiModelProperty(value = "随访计划模板id")
    private Integer followVisitTemplateId;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "建议随访日期")
    private Date followVisitTime;

    @ApiModelProperty(value = "随访次数_第N次随访")
    private Integer followVisitNumber;

    @ApiModelProperty(value = "对应随访计划名称")
    private String followVisitTemplateName;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出院时间")
    private Date outTime;

    @ApiModelProperty(value = "状态 1待填报 2已填报 3随访被终止")
    private Integer status;

}
