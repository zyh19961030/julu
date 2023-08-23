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
 * @Description: 随访模板周期表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Data
@TableName("tb_follow_visit_template_cycle")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tb_follow_visit_template_cycle对象", description="随访模板周期表")
public class TbFollowVisitTemplateCycle {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**随访计划模板id*/
	@Excel(name = "随访计划模板id", width = 15)
    @ApiModelProperty(value = "随访计划模板id")
	private Integer followVisitTemplateId;
	/**次数*/
	@Excel(name = "次数", width = 15)
    @ApiModelProperty(value = "次数")
	private Integer frequency;
	/**距时间起点的整数*/
	@Excel(name = "距时间起点的整数", width = 15)
    @ApiModelProperty(value = "距时间起点的整数")
	private Integer dateStartNumber;
	/**距时间起点 1天 2周 3月 4年*/
	@Excel(name = "距时间起点 1天 2周 3月 4年", width = 15)
    @ApiModelProperty(value = "距时间起点 1天 2周 3月 4年")
	private Integer dateStartType;
	/**关联内容的问卷标题*/
	@Excel(name = "关联内容的问卷标题", width = 15)
    @ApiModelProperty(value = "关联内容的问卷标题")
	private Integer questionId;
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
