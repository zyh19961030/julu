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
 * @Description: 随访模板表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Data
@TableName("tb_follow_visit_template")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tb_follow_visit_template对象", description="随访模板表")
public class TbFollowVisitTemplate {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**随访计划模版名称*/
	@Excel(name = "随访计划模版名称", width = 15)
    @ApiModelProperty(value = "随访计划模版名称")
	private String name;
	/**随访计划时间起点 0出院日期*/
	@Excel(name = "随访计划时间起点 0出院日期", width = 15)
    @ApiModelProperty(value = "随访计划时间起点 0出院日期")
	private Integer dateStartType;
	/**状态 1-正常 2-停用*/
	@Excel(name = "状态 1-正常 2-停用", width = 15)
    @ApiModelProperty(value = "状态 1-正常 2-停用")
	private Integer status;
	/**删除状态（0：未删除  1：已删除）*/
	@Excel(name = "删除状态（0：未删除  1：已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0：未删除  1：已删除）")
	private Integer delState;
	/**创建人id*/
	@Excel(name = "创建人id", width = 15)
    @ApiModelProperty(value = "创建人id")
	private String createUser;
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
