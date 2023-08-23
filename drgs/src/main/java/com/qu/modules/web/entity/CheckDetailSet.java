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
 * @Description: 检查明细列设置表
 * @Author: jeecg-boot
 * @Date:   2022-08-09
 * @Version: V1.0
 */
@Data
@TableName("check_detail_set")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="check_detail_set对象", description="检查明细列设置表")
public class CheckDetailSet {
    
	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
	private Integer id;
	/**问卷id*/
	@Excel(name = "问卷id", width = 15)
    @ApiModelProperty(value = "问卷id")
	private Integer questionId;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
	private String userId;
	/**题目id*/
	@Excel(name = "题目id", width = 15)
    @ApiModelProperty(value = "题目id")
	private Integer subjectId;
	/**排序序号*/
	@Excel(name = "排序序号", width = 15)
    @ApiModelProperty(value = "排序序号")
	private Integer sortNumber;
	/**题目名称*/
	@Excel(name = "题目名称", width = 15)
    @ApiModelProperty(value = "题目名称")
	private String subjectName;
	/**是否显示 0显示 1不显示*/
	@Excel(name = "是否显示 0显示 1不显示", width = 15)
    @ApiModelProperty(value = "是否显示 0显示 1不显示")
	private Integer showType;
	/**父节点id*/
	@Excel(name = "父节点id", width = 15)
    @ApiModelProperty(value = "父节点id")
	private Integer questionParentId;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**修改时间*/
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;
}
