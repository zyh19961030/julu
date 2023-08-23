package com.qu.modules.web.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 随访模板表
 * @Author: jeecg-boot
 * @Date: 2023-02-22
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "TbFollowVisitPatientTemplateGenerateParam", description = "TbFollowVisitPatientTemplateGenerateParam")
public class TbFollowVisitPatientTemplateGenerateParam {


    @ApiModelProperty(value = "zbgroup")
    @NotBlank(message = "zbgroup_不能为空")
    private String zbgroup;

    @ApiModelProperty(value = "患者姓名")
    @NotBlank(message = "患者姓名_不能为空")
    private String name;

    @ApiModelProperty(value = "病案号")
    @NotBlank(message = "病案号_不能为空")
    private String caseId;

    @ApiModelProperty(value = "住院次数")
    @NotBlank(message = "住院次数_不能为空")
    private String inHospitalNumber;

    @ApiModelProperty(value = "电话")
    @NotBlank(message = "电话_不能为空")
    private String phone;

    @ApiModelProperty(value = "患者主要诊断")
    @NotBlank(message = "患者主要诊断_不能为空")
    private String diagnosis;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出院时间")
    @NotNull(message = "出院时间_不能为空")
    private Date outTime;

    @ApiModelProperty(value = "科室id")
    @NotBlank(message = "科室id_不能为空")
    private String deptId;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "联系人")
    private String contactName;

    @ApiModelProperty(value = "联系人电话")
    private String contactPhone;

    @ApiModelProperty(value = "住院医师")
    private String inHospitalDoctor;

}
