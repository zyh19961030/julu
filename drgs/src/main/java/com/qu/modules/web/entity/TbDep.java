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
 * @Description: 科室表
 * @Author: jeecg-boot
 * @Date:   2022-04-20
 * @Version: V1.0
 */
@Data
@TableName("tb_dep")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tb_dep对象", description="科室表")
public class TbDep {
    
	/**主键ID*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
	private String id;
	/**部门名称*/
	@Excel(name = "部门名称", width = 15)
    @ApiModelProperty(value = "部门名称")
	private String depname;
	/**部门编号*/
	@Excel(name = "部门编号", width = 15)
    @ApiModelProperty(value = "部门编号")
	private String depcode;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
	private String status;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
	private String isdelete;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**部门类型*/
	@Excel(name = "部门类型", width = 15)
    @ApiModelProperty(value = "部门类型")
	private String type;
	/**部门等级*/
	@Excel(name = "部门等级", width = 15)
    @ApiModelProperty(value = "部门等级")
	private String level;
	/**父级部门id*/
	@Excel(name = "父级部门id", width = 15)
    @ApiModelProperty(value = "父级部门id")
	private String parientid;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createtime;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private Date updatetime;
	/**类型名称*/
	@Excel(name = "类型名称", width = 15)
    @ApiModelProperty(value = "类型名称")
	private String typename;
	/**所属医院*/
	@Excel(name = "所属医院", width = 15)
    @ApiModelProperty(value = "所属医院")
	private String system;
}
