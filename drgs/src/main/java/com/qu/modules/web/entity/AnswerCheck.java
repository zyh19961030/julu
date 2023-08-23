package com.qu.modules.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @Description: 检查表问卷总表
 * @Author: jeecg-boot
 * @Date:   2022-07-30
 * @Version: V1.0
 */
@Data
@TableName("answer_check")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="answer_check对象", description="检查表问卷总表")
public class AnswerCheck {
    
	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
	private Integer id;
	/**问卷id*/
	@Excel(name = "问卷id", width = 15)
    @ApiModelProperty(value = "问卷id")
	private Integer quId;
	/**答案json*/
	@Excel(name = "答案json", width = 15)
    @ApiModelProperty(value = "答案json")
	private String answerJson;
	/**0:草稿1:已提交*/
	@Excel(name = "0:草稿1:已提交", width = 15)
    @ApiModelProperty(value = "0:草稿1:已提交")
	private Integer answerStatus;
	/**答题人id-提交人*/
	@Excel(name = "答题人id-提交人", width = 15)
    @ApiModelProperty(value = "答题人id-提交人")
	private String creater;
	/**答题人姓名*/
	@Excel(name = "答题人姓名", width = 15)
    @ApiModelProperty(value = "答题人姓名")
	private String createrName;
	/**答题人部门id*/
	@Excel(name = "答题人部门id", width = 15)
    @ApiModelProperty(value = "答题人部门id")
	private String createrDeptId;
	/**答题人部门名称*/
	@Excel(name = "答题人部门名称", width = 15)
    @ApiModelProperty(value = "答题人部门名称")
	private String createrDeptName;
	/**对应子表的id，可以当主键，(子表中同字段)*/
	@Excel(name = "对应子表的id，可以当主键，(子表中同字段)", width = 15)
    @ApiModelProperty(value = "对应子表的id，可以当主键，(子表中同字段)")
	private String summaryMappingTableId;
	/**检查月份_题目中的*/
	@Excel(name = "检查月份_题目中的", width = 15)
    @ApiModelProperty(value = "检查月份_题目中的")
	private String checkMonth;
	/**被检查科室id*/
	@Excel(name = "被检查科室id", width = 15)
    @ApiModelProperty(value = "被检查科室id")
	private String checkedDept;
//	/**被检查科室名称*/
//	@Excel(name = "被检查科室名称", width = 15)
//    @ApiModelProperty(value = "被检查科室名称")
//	private String checkedDeptName;
	/**被检查科室医生id*/
	@Excel(name = "被检查科室医生id", width = 15)
    @ApiModelProperty(value = "被检查科室医生id")
	private String checkedDoct;
//	/**被检查科室医生姓名*/
//	@Excel(name = "被检查科室医生姓名", width = 15)
//    @ApiModelProperty(value = "被检查科室医生姓名")
//	private String checkedDoctName;
	/**被检查患者住院号 -废弃*/
	@Excel(name = "被检查患者住院号 -废弃", width = 15)
    @ApiModelProperty(value = "被检查患者住院号 -废弃")
	private String checkedPatientId;
	/**被检查患者姓名*/
	@Excel(name = "被检查患者姓名", width = 15)
    @ApiModelProperty(value = "被检查患者姓名")
	private String checkedPatientName;
	/**总得分*/
	@Excel(name = "总得分", width = 15)
    @ApiModelProperty(value = "总得分")
	private String totalScore;
	/**是否合格  0合格  1不合格*/
	@Excel(name = "是否合格  0合格  1不合格", width = 15)
    @ApiModelProperty(value = "是否合格  0合格  1不合格")
	private String passStatus;
	/**提交时间*/
    @ApiModelProperty(value = "提交时间")
	private Date submitTime;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**修改时间*/
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;
	/**答题时间-填报时间*/
    @ApiModelProperty(value = "答题时间-填报时间")
	private Date answerTime;
	/**0:正常1:已删除*/
	@Excel(name = "0:正常1:已删除", width = 15)
    @ApiModelProperty(value = "0:正常1:已删除")
	private Integer del;
	/**问卷版本*/
	@Excel(name = "问卷版本", width = 15)
	@ApiModelProperty(value = "问卷版本")
	private java.lang.String questionVersion;
    /**被检查患者住院号*/
    @Excel(name = "被检查患者住院号", width = 15)
    @ApiModelProperty(value = "被检查患者住院号")
    private String caseId;
	/**来源 0:PC 1:小程序*/
	@Excel(name = "来源 0:PC 1:小程序", width = 15)
	@ApiModelProperty(value = "来源 0:PC 1:小程序")
	private Integer source;
}
