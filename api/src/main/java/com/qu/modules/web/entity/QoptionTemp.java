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
 * @Description: 选项临时表
 * @Author: jeecg-boot
 * @Date:   2022-10-22
 * @Version: V1.0
 */
@Data
@TableName("qoption_temp")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qoption_temp对象", description="选项临时表")
public class QoptionTemp {
    
	/**主键*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键")
	private Integer id;
	/**题目id*/
	@Excel(name = "题目id", width = 15)
    @ApiModelProperty(value = "题目id")
	private Integer subId;
	/**选项名称*/
	@Excel(name = "选项名称", width = 15)
    @ApiModelProperty(value = "选项名称")
	private String opName;
	/**选项顺序*/
	@Excel(name = "选项顺序", width = 15)
    @ApiModelProperty(value = "选项顺序")
	private Integer opOrder;
	/**跳题逻辑*/
	@Excel(name = "跳题逻辑", width = 15)
    @ApiModelProperty(value = "跳题逻辑")
	private String jumpLogic;
	/**是否其他  0:否 1:是*/
	@Excel(name = "是否其他  0:否 1:是", width = 15)
    @ApiModelProperty(value = "是否其他  0:否 1:是")
	private Integer others;
	/**绑定值*/
	@Excel(name = "绑定值", width = 15)
    @ApiModelProperty(value = "绑定值")
	private String bindValue;
	/**0:正常1:已删除*/
	@Excel(name = "0:正常1:已删除", width = 15)
    @ApiModelProperty(value = "0:正常1:已删除")
	private Integer del;
	/**创建者*/
	@Excel(name = "创建者", width = 15)
    @ApiModelProperty(value = "创建者")
	private String creater;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新者*/
	@Excel(name = "更新者", width = 15)
    @ApiModelProperty(value = "更新者")
	private String updater;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
	/**选项值*/
	@Excel(name = "选项值", width = 15)
    @ApiModelProperty(value = "选项值")
	private String opValue;
	/**特殊跳题逻辑,前端使用*/
	@Excel(name = "特殊跳题逻辑,前端使用", width = 15)
    @ApiModelProperty(value = "特殊跳题逻辑,前端使用")
	private String specialJumpLogic;
	/**选项分值*/
	@Excel(name = "选项分值", width = 15)
    @ApiModelProperty(value = "选项分值")
	private java.math.BigDecimal optionScore;
	/**答案*/
	@Excel(name = "答案", width = 15)
    @ApiModelProperty(value = "答案")
	private String answerName;
	/**答案值*/
	@Excel(name = "答案值", width = 15)
    @ApiModelProperty(value = "答案值")
	private String answerValue;
	/**大于的值*/
	@Excel(name = "大于的值", width = 15)
    @ApiModelProperty(value = "大于的值")
	private String greaterThanValue;
	/**小于的值*/
	@Excel(name = "小于的值", width = 15)
    @ApiModelProperty(value = "小于的值")
	private String lessThanValue;
}
