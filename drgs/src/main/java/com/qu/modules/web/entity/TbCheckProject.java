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
 * @Description: 检查项目表
 * @Author: jeecg-boot
 * @Date:   2023-04-10
 * @Version: V1.0
 */
@Data
@TableName("tb_check_project")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tb_check_project对象", description="检查项目表")
public class TbCheckProject {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**检查项目名字*/
	@Excel(name = "检查项目名字", width = 15)
    @ApiModelProperty(value = "检查项目名字")
	private String name;
	/**章节类目,科室指控模板中的章节标题*/
	@Excel(name = "章节类目,科室指控模板中的章节标题", width = 15)
    @ApiModelProperty(value = "章节类目,科室指控模板中的章节标题")
	private String chapter;
	/**是否应用于数据发布   0是  1否*/
	@Excel(name = "是否应用于数据发布   0是  1否", width = 15)
    @ApiModelProperty(value = "是否应用于数据发布   0是  1否")
	private Integer dataRelease;
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
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
	private String isDelete;
	/**章节类型*/
	@Excel(name = "章节类型", width = 15)
    @ApiModelProperty(value = "章节类型")
	private String type;
}
