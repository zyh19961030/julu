package com.qu.modules.web.entity;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 查检表的统计明细表
 * @Author: jeecg-boot
 * @Date:   2023-03-21
 * @Version: V1.0
 */
@Data
@TableName("answer_check_statistic_detail")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="answer_check_statistic_detail对象", description="查检表的统计明细表")
public class AnswerCheckStatisticDetail {
    
	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
	private Integer id;
	/**检查表总表id*/
	@Excel(name = "检查表总表id", width = 15)
	@ApiModelProperty(value = "检查表总表id")
	private Integer answerCheckId;
	/**检查月份*/
	@Excel(name = "检查月份", width = 15)
    @ApiModelProperty(value = "检查月份")
	private String checkMonth;
	/**问卷id*/
	@Excel(name = "问卷id", width = 15)
    @ApiModelProperty(value = "问卷id")
	private Integer quId;
	/**填报人id*/
	@Excel(name = "填报人id", width = 15)
    @ApiModelProperty(value = "填报人id")
	private String answerUser;
	/**填报人名称*/
	@Excel(name = "填报人名称", width = 15)
    @ApiModelProperty(value = "填报人名称")
	private String answerUserName;
	/**检查科室ID*/
	@Excel(name = "检查科室ID", width = 15)
    @ApiModelProperty(value = "检查科室ID")
	private String answerDeptId;
	/**检查科室名称*/
	@Excel(name = "检查科室名称", width = 15)
    @ApiModelProperty(value = "检查科室名称")
	private String answerDeptName;
	/**被检查科室ID*/
	@Excel(name = "被检查科室ID", width = 15)
    @ApiModelProperty(value = "被检查科室ID")
	private String checkedDeptId;
	/**被检查科室名称*/
	@Excel(name = "被检查科室名称", width = 15)
    @ApiModelProperty(value = "被检查科室名称")
	private String checkedDeptName;
	/**患者姓名*/
	@Excel(name = "患者姓名", width = 15)
    @ApiModelProperty(value = "患者姓名")
	private String patientName;
	/**病案号*/
	@Excel(name = "病案号", width = 15)
    @ApiModelProperty(value = "病案号")
	private String caseId;
	/**住院医师*/
	@Excel(name = "住院医师", width = 15)
    @ApiModelProperty(value = "住院医师")
	private String hospitalDoctor;
	/**缺陷题目上一级分组框字段ID*/
	@Excel(name = "缺陷题目上一级分组框字段ID", width = 15)
    @ApiModelProperty(value = "缺陷题目上一级分组框字段ID")
	private Integer groupId;
	/**缺陷题目上一级分组框字段*/
	@Excel(name = "缺陷题目上一级分组框字段", width = 15)
    @ApiModelProperty(value = "缺陷题目上一级分组框字段")
	private String groupColumnName;
	/**分组框文字*/
	@Excel(name = "分组框文字", width = 15)
    @ApiModelProperty(value = "分组框文字")
	private String groupText;
	/**分组框得分*/
	@Excel(name = "分组框得分", width = 15)
    @ApiModelProperty(value = "分组框得分")
	private String groupScore;
	/**缺陷项目题目文字*/
	@Excel(name = "缺陷项目题目文字", width = 15)
    @ApiModelProperty(value = "缺陷项目题目文字")
	private String subjectText;
	/**缺陷项目题目字段*/
	@Excel(name = "缺陷项目题目字段", width = 15)
    @ApiModelProperty(value = "缺陷项目题目字段")
	private String subjectColumnName;
	/**题目ID*/
	@Excel(name = "题目ID", width = 15)
    @ApiModelProperty(value = "题目ID")
	private Integer subjectId;
	/**题目分值类型（加分/扣分）*/
	@Excel(name = "题目分值类型（加分/扣分）", width = 15)
    @ApiModelProperty(value = "题目分值类型（加分/扣分）")
	private Integer subjectScoreType;
	/**答案文字*/
	@Excel(name = "答案文字", width = 15)
    @ApiModelProperty(value = "答案文字")
	private String answerText;
	/**答案选项值*/
	@Excel(name = "答案选项值", width = 15)
    @ApiModelProperty(value = "答案选项值")
	private String answerValue;
	/**答案对应分值*/
	@Excel(name = "答案对应分值", width = 15)
    @ApiModelProperty(value = "答案对应分值")
	private String answerScore;
	/**痕迹文字*/
	@Excel(name = "痕迹文字", width = 15)
    @ApiModelProperty(value = "痕迹文字")
	private String mark;
	/**痕迹图片*/
	@Excel(name = "痕迹图片", width = 15)
    @ApiModelProperty(value = "痕迹图片")
	private String markImg;
	/**0:正常1:已删除*/
	@Excel(name = "0:正常1:已删除", width = 15)
    @ApiModelProperty(value = "0:正常1:已删除")
	private Integer del;
	/**来源 0:PC 1:小程序*/
	@Excel(name = "来源 0:PC 1:小程序", width = 15)
    @ApiModelProperty(value = "来源 0:PC 1:小程序")
	private Integer source;
}
