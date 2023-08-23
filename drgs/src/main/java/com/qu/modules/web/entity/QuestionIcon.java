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
 * @Description: 问卷图标表
 * @Author: jeecg-boot
 * @Date:   2022-09-05
 * @Version: V1.0
 */
@Data
@TableName("question_icon")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="question_icon对象", description="问卷图标表")
public class QuestionIcon {
    
	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
	private Integer id;
	/**图标名称*/
	@Excel(name = "图标名称", width = 15)
    @ApiModelProperty(value = "图标名称")
	private String fileName;
	/**类型 0检查表图标*/
	@Excel(name = "类型 0检查表图标", width = 15)
    @ApiModelProperty(value = "类型 0检查表图标")
	private Integer type;
	/**访问路径*/
	@Excel(name = "访问路径", width = 15)
    @ApiModelProperty(value = "访问路径")
	private String url;
	/**0:正常1:已删除*/
	@Excel(name = "0:正常1:已删除", width = 15)
    @ApiModelProperty(value = "0:正常1:已删除")
	private Integer del;
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
}
