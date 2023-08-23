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
 * @Description: 问卷被检查科室关联表
 * @Author: jeecg-boot
 * @Date:   2022-09-18
 * @Version: V1.0
 */
@Data
@TableName("question_checked_dept")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="question_checked_dept对象", description="问卷被检查科室关联表")
public class QuestionCheckedDept {
    
	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
	private Integer id;
	/**问卷id*/
	@Excel(name = "问卷id", width = 15)
    @ApiModelProperty(value = "问卷id")
	private Integer quId;
	/**科室id*/
	@Excel(name = "科室id", width = 15)
    @ApiModelProperty(value = "科室id")
	private String deptId;
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
	/**类型 1被检查科室 2责任人(dept_id为userId)*/
	@Excel(name = "类型 1被检查科室 2责任人(dept_id为userId)", width = 15)
    @ApiModelProperty(value = "类型 1被检查科室 2责任人(dept_id为userId)")
	private Integer type;
	/**用户所在科室的id(可能是辅助科室)*/
	@Excel(name = "用户所在科室的id(可能是辅助科室)", width = 15)
    @ApiModelProperty(value = "用户所在科室的id(可能是辅助科室)")
	private String userDeptId;
}
