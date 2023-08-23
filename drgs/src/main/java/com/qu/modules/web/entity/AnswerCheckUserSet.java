package com.qu.modules.web.entity;

import java.util.Date;

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
 * @Description: 检查表的检查人员设置表
 * @Author: jeecg-boot
 * @Date:   2022-11-30
 * @Version: V1.0
 */
@Data
@TableName("answer_check_user_set")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="answer_check_user_set对象", description="检查表的检查人员设置表")
public class AnswerCheckUserSet {
    
	/**主键*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键")
	private String id;
	/**问卷id*/
	@Excel(name = "问卷id", width = 15)
    @ApiModelProperty(value = "问卷id")
	private Integer quId;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
	private String userId;
	/**科室id*/
	@Excel(name = "科室id", width = 15)
    @ApiModelProperty(value = "科室id")
	private String deptId;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**修改时间*/
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;
	/**0:正常1:已删除*/
	@Excel(name = "0:正常1:已删除", width = 15)
    @ApiModelProperty(value = "0:正常1:已删除")
	private Integer del;
	/**类型 1设置行 2设置列*/
	@Excel(name = "类型 1设置行 2设置列", width = 15)
    @ApiModelProperty(value = "类型 1设置行 2设置列")
	private Integer type;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
	private Integer sortNumber;
}
