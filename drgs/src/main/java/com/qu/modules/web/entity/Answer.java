package com.qu.modules.web.entity;

import java.util.Date;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 答案表
 * @Author: jeecg-boot
 * @Date: 2021-03-28
 * @Version: V1.0
 */
@Data
@TableName("answer")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "answer对象", description = "答案表")
public class Answer {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @Excel(name = "主键", width = 15)
    @ApiModelProperty(value = "主键")
    private Integer id;
    /**
     * 问卷id
     */
    @Excel(name = "问卷id", width = 15)
    @ApiModelProperty(value = "问卷id")
    private Integer quId;
    /**
     * 答案json
     */
    @Excel(name = "答案json", width = 15)
    @ApiModelProperty(value = "答案json")
    private java.lang.String answerJson;
    /**
     * 0:草稿1:已提交
     */
    @Excel(name = "0:草稿1:已提交", width = 15)
    @ApiModelProperty(value = "0:草稿1:已提交")
    private java.lang.Integer answerStatus;
    /**
     * 答题人
     */
    @Excel(name = "答题人", width = 15)
    @ApiModelProperty(value = "答题人")
    private java.lang.String creater;
    /**
     * 答题人姓名
     */
    @Excel(name = "答题人姓名", width = 15)
    @ApiModelProperty(value = "答题人姓名")
    private java.lang.String createrName;
    /**
     * 答题时间
     */
    @Excel(name = "答题时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "答题时间")
    private Date createTime;
    /**
     * 答题人部门id
     */
    @Excel(name = "答题人部门id", width = 15)
    @ApiModelProperty(value = "答题人部门id")
    private String createrDeptid;
    /**
     * 答题人部门名称
     */
    @Excel(name = "答题人部门名称", width = 15)
    @ApiModelProperty(value = "答题人部门名称")
    private String createrDeptname;
    /**
     * 对应子表的id，可以当主键，(子表中同字段)
     */
    @Excel(name = "对应子表的id，可以当主键，(子表中同字段)", width = 15)
    @ApiModelProperty(value = "对应子表的id，可以当主键，(子表中同字段)")
	private java.lang.String summaryMappingTableId;
	/**患者姓名*/
	@Excel(name = "患者姓名", width = 15)
    @ApiModelProperty(value = "患者姓名")
	private java.lang.String patientName;
	/**住院号*/
	@Excel(name = "住院号", width = 15)
    @ApiModelProperty(value = "住院号")
	private java.lang.String hospitalInNo;
	/**年龄*/
	@Excel(name = "年龄", width = 15)
    @ApiModelProperty(value = "年龄")
	private java.lang.Integer age;
	/**住院时间*/
    @ApiModelProperty(value = "住院时间")
	private java.util.Date inTime;
    /**出院时间*/
    @ApiModelProperty(value = "出院时间")
	private java.util.Date outTime;
	/**修改时间*/
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**提交时间*/
	@Excel(name = "提交时间", width = 15)
    @ApiModelProperty(value = "提交时间")
	private java.util.Date submitTime;
    /**0:正常1:已删除*/
    @Excel(name = "0:正常1:已删除", width = 15)
    @ApiModelProperty(value = "0:正常1:已删除")
    private java.lang.Integer del;
    /**答题时间-填报时间*/
    @Excel(name = "答题时间-填报时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "答题时间-填报时间")
    private java.util.Date answerTime;
    /**填报的月/季度/年*/
    @Excel(name = "填报的月/季度/年", width = 15)
    @ApiModelProperty(value = "填报的月/季度/年")
    private java.lang.String questionAnswerTime;
    /**问卷版本*/
    @Excel(name = "问卷版本", width = 15)
    @ApiModelProperty(value = "问卷版本")
    private java.lang.String questionVersion;
}
