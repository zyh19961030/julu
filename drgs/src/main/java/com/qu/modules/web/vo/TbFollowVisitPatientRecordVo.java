package com.qu.modules.web.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

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
@ApiModel(value="TbFollowVisitPatientRecordVo", description="TbFollowVisitPatientRecordVo")
public class TbFollowVisitPatientRecordVo {

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

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出院时间")
    private Date outTime;

    @ApiModelProperty(value = "随访次数_第N次随访")
    private Integer followVisitNumber;

    @ApiModelProperty(value = "随访计划")
    private String followVisitTemplateName;

    @ApiModelProperty(value = "随访计划时间起点 0出院日期")
    private Integer dateStartType;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "建议随访日期")
    private Date followVisitTime;

    @ApiModelProperty(value = "随访患者模板总记录表id")
    private Integer followVisitPatientTemplateId;

    @ApiModelProperty(value = "随访关联内容的问卷id")
    private Integer questionId;

    @ApiModelProperty(value = "填报记录(历史随访记录)")
    private List<TbFollowVisitPatientRecordHistoryVo> historyRecord;

    @ApiModelProperty(value = "本次随访记录答案id")
    private Integer answerId;

//    @ApiModelProperty(value = "随访内容")
//    private String followVisitTemplate;

    @ApiModelProperty(value = "状态 1待填报 2已填报 3随访被终止")
    private Integer status;

}
