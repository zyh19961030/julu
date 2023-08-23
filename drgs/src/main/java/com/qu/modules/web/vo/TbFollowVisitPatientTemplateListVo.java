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
@ApiModel(value="TbFollowVisitPatientTemplateListVo", description="TbFollowVisitPatientTemplateListVo")
public class TbFollowVisitPatientTemplateListVo {

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

    @ApiModelProperty(value = "随访计划名称")
    private String followVisitTemplate;

    @ApiModelProperty(value = "计划总随访次数")
    private Integer followVisitCountNumber;

    @ApiModelProperty(value = "已随访次数")
    private Integer alreadyFollowVisitCountNumber;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "建议下次随访日期")
    private Date followVisitTime;

    @ApiModelProperty(value = "状态 1执行中 2计划已完成 3已提前终止 4二次住院提前终止")
    private Integer status;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出院时间")
    private Date outTime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
