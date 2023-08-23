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
 * @Description: 药品规则问题表
 * @Author: jeecg-boot
 * @Date:   2021-09-13
 * @Version: V1.0
 */
@Data
@TableName("drug_rules_subject")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="drug_rules_subject对象", description="药品规则问题表")
public class DrugRulesSubject {
    
	/**药品规则问题id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "药品规则问题id")
	private Integer id;
	/**问题id,关联qsubject表的id*/
	@Excel(name = "问题id,关联qsubject表的id", width = 15)
    @ApiModelProperty(value = "问题id,关联qsubject表的id")
	private Integer subjectId;
	/**问题名称*/
	@Excel(name = "问题名称", width = 15)
    @ApiModelProperty(value = "问题名称")
	private String subjectName;
	/**药品规则问卷id*/
	@Excel(name = "药品规则问卷id", width = 15)
    @ApiModelProperty(value = "药品规则问卷id")
	private Integer drugRulesQuestionId;
	/**删除标识：1为删除，0为正常*/
	@Excel(name = "删除标识：1为删除，0为正常", width = 15)
    @ApiModelProperty(value = "删除标识：1为删除，0为正常")
	private Integer del;
	/**是否已添加标识：1为已添加，0为未添加*/
	@Excel(name = "是否已添加标识：1为已添加，0为未添加", width = 15)
	@ApiModelProperty(value = "是否已添加标识：1为已添加，0为未添加")
	private Integer exist;
	/**匹配规则标识：1为药品名称，0为药品物理作用id*/
	@Excel(name = "匹配规则标识：1为药品名称，0为药品物理作用id", width = 15)
    @ApiModelProperty(value = "匹配规则标识：1为药品名称，0为药品物理作用id")
	private Integer matches;
	/**时间限制标识：1为勾选，0为不勾选*/
	@Excel(name = "时间限制标识：1为勾选，0为不勾选", width = 15)
    @ApiModelProperty(value = "时间限制标识：1为勾选，0为不勾选")
	private Integer timeLimit;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
	/**时间类型：1、起始，2、终止，3、天数，4、其他*/
	@Excel(name = "时间类型：1、起始，2、终止，3、天数，4、其他", width = 15)
	@ApiModelProperty(value = "时间类型：1、起始，2、终止，3、天数，4、其他")
	private Integer timeType;
	/**医嘱执行类型：1、长期，2、临时，3、其他*/
	@Excel(name = "医嘱执行类型：1、长期，2、临时，3、其他", width = 15)
	@ApiModelProperty(value = "医嘱执行类型：1、长期，2、临时，3、其他")
	private Integer orderExecuteType;
	/**题目类型 1.单选   2.多选   3.日期   4.时间  5.下拉框    6.单行文本   7.多行文本  8.分组框  9.提示标题 10.日期选择器-月 11.日期选择器-季度 12.日期选择器-年*/
	@Excel(name = "题目类型 1.单选   2.多选   3.日期   4.时间  5.下拉框    6.单行文本   7.多行文本  8.分组框  9.提示标题 10.日期选择器-月 11.日期选择器-季度 12.日期选择器-年", width = 15)
	@ApiModelProperty(value = "题目类型 1.单选   2.多选   3.日期   4.时间  5.下拉框    6.单行文本   7.多行文本  8.分组框  9.提示标题 10.日期选择器-月 11.日期选择器-季度 12.日期选择器-年")
	private String subType;
	/**开始时间项*/
	@Excel(name = "开始时间项", width = 15)
    @ApiModelProperty(value = "开始时间项")
	private String startEvent;
	/**开始时间前后*/
	@Excel(name = "开始时间前后", width = 15)
    @ApiModelProperty(value = "开始时间前后")
	private String startAround;
	/**开始时长*/
	@Excel(name = "开始时长", width = 15)
    @ApiModelProperty(value = "开始时长")
	private String startTime;
	/**结束时间项*/
	@Excel(name = "结束时间项", width = 15)
    @ApiModelProperty(value = "结束时间项")
	private String endEvent;
	/**结束时间前后*/
	@Excel(name = "结束时间前后", width = 15)
    @ApiModelProperty(value = "结束时间前后")
	private String endAround;
	/**结束时长*/
	@Excel(name = "结束时长", width = 15)
    @ApiModelProperty(value = "结束时长")
	private String endTime;
}
