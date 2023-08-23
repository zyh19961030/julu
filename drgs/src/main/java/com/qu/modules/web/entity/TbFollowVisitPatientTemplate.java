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

import java.util.Date;

/**
 * @Description: 随访患者模板总记录表
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
@Data
@TableName("tb_follow_visit_patient_template")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tb_follow_visit_patient_template对象", description="随访患者模板总记录表")
public class TbFollowVisitPatientTemplate {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**随访患者信息表id*/
	@Excel(name = "随访患者信息表id", width = 15)
    @ApiModelProperty(value = "随访患者信息表id")
	private Integer followVisitPatientId;
	/**随访计划模板id*/
	@Excel(name = "随访计划模板id", width = 15)
    @ApiModelProperty(value = "随访计划模板id")
	private Integer followVisitTemplateId;
	/**计划总随访次数*/
	@Excel(name = "计划总随访次数", width = 15)
    @ApiModelProperty(value = "计划总随访次数")
	private Integer followVisitCountNumber;
	/**状态 1执行中 2计划已完成 3已提前终止 4二次住院提前终止*/
	@Excel(name = "状态 1执行中 2计划已完成 3已提前终止 4二次住院提前终止", width = 15)
    @ApiModelProperty(value = "状态 1执行中 2计划已完成 3已提前终止 4二次住院提前终止")
	private Integer status;
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
}
