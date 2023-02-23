package com.qu.modules.web.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

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
@ApiModel(value="tb_follow_visit_patient_record对象", description="随访患者记录表")
public class TbFollowVisitPatientRecord {
    
	/**id*/
	@TableId(type = IdType.UUID)
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
	/**随访关联内容的问卷id*/
	@Excel(name = "随访关联内容的问卷id", width = 15)
    @ApiModelProperty(value = "随访关联内容的问卷id")
	private Integer questionId;
	/**建议随访日期*/
	@Excel(name = "建议随访日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "建议随访日期")
	private Date followVisitTime;
	/**随访次数_第N次随访*/
	@Excel(name = "随访次数_第N次随访", width = 15)
    @ApiModelProperty(value = "随访次数_第N次随访")
	private Integer followVisitNumber;
	/**状态 1待填报 2已填报*/
	@Excel(name = "状态 1待填报 2已填报", width = 15)
    @ApiModelProperty(value = "状态 1待填报 2已填报")
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