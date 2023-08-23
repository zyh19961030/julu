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
 * @Description: 查检表分类表
 * @Author: jeecg-boot
 * @Date:   2023-04-27
 * @Version: V1.0
 */
@Data
@TableName("question_check_classification")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="question_check_classification对象", description="查检表分类表")
public class QuestionCheckClassification {
    
	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
	private Integer id;
	/**用户Id*/
	@Excel(name = "用户Id", width = 15)
    @ApiModelProperty(value = "用户Id")
	private String userId;
	/**分类名称*/
	@Excel(name = "分类名称", width = 15)
    @ApiModelProperty(value = "分类名称")
	private String name;
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
	/**0:正常1:已删除*/
	@Excel(name = "0:正常1:已删除", width = 15)
    @ApiModelProperty(value = "0:正常1:已删除")
	private Integer del;
}
