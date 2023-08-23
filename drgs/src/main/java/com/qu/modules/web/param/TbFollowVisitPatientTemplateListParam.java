package com.qu.modules.web.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 随访模板表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbFollowVisitPatientRecordListParam", description="TbFollowVisitPatientRecordListParam")
public class TbFollowVisitPatientTemplateListParam {


    @ApiModelProperty(value = "随访计划模版名称")
	private String name;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建_起始时间 格式：yyyy-MM-dd")
    private Date startCreateTime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建_结束时间 格式：yyyy-MM-dd")
    private Date endCreateTime;

    @ApiModelProperty(value = "随访患者姓名")
    private String patientName;

    @ApiModelProperty(value = "主要诊断")
    private String diagnosis;

    @ApiModelProperty(value = "状态 -1全部 1执行中 2计划已完成 3已提前终止")
    private Integer status;



}
