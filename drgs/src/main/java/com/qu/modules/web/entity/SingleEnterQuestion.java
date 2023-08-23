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
 * @Description: 录入表单表
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Data
@TableName("single_enter_question")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="single_enter_question对象", description="录入表单表")
public class SingleEnterQuestion {
    
	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
	private Integer id;
	/**查检表id*/
	@Excel(name = "查检表id", width = 15)
    @ApiModelProperty(value = "查检表id")
	private Integer questionId;
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
	/**录入表单名称*/
	@Excel(name = "录入表单名称", width = 15)
	@ApiModelProperty(value = "录入表单名称")
	private String name;
	/**题目id*/
	@Excel(name = "题目id", width = 15)
	@ApiModelProperty(value = "题目id")
	private Integer subjectId;
	/**题目数据库列名*/
	@Excel(name = "题目数据库列名", width = 15)
	@ApiModelProperty(value = "题目数据库列名")
	private String subjectColumnName;
}
