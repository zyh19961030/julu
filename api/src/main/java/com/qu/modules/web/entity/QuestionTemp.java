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
 * @Description: 问卷临时表
 * @Author: jeecg-boot
 * @Date:   2022-10-22
 * @Version: V1.0
 */
@Data
@TableName("question_temp")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="question_temp对象", description="问卷临时表")
public class QuestionTemp {
    
	/**主键*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键")
	private Integer id;
	/**问卷名称*/
	@Excel(name = "问卷名称", width = 15)
    @ApiModelProperty(value = "问卷名称")
	private String quName;
	/**问卷描述*/
	@Excel(name = "问卷描述", width = 15)
    @ApiModelProperty(value = "问卷描述")
	private String quDesc;
	/**0:草稿箱 1:已发布*/
	@Excel(name = "0:草稿箱 1:已发布", width = 15)
    @ApiModelProperty(value = "0:草稿箱 1:已发布")
	private Integer quStatus;
	/**0:正常1:已停用*/
	@Excel(name = "0:正常1:已停用", width = 15)
    @ApiModelProperty(value = "0:正常1:已停用")
	private Integer quStop;
	/**科室编辑(填报)权限，科室id逗号分割*/
	@Excel(name = "科室编辑(填报)权限，科室id逗号分割", width = 15)
    @ApiModelProperty(value = "科室编辑(填报)权限，科室id逗号分割")
	private Object deptIds;
	/**科室查看权限，科室id逗号分割*/
	@Excel(name = "科室查看权限，科室id逗号分割", width = 15)
    @ApiModelProperty(value = "科室查看权限，科室id逗号分割")
	private Object seeDeptIds;
	/**答案对应数据库名*/
	@Excel(name = "答案对应数据库名", width = 15)
    @ApiModelProperty(value = "答案对应数据库名")
	private String tableName;
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
	/**分类id*/
	@Excel(name = "分类id", width = 15)
    @ApiModelProperty(value = "分类id")
	private String categoryId;
	/**0其他 1单病种 2检查表*/
	@Excel(name = "0其他 1单病种 2检查表", width = 15)
    @ApiModelProperty(value = "0其他 1单病种 2检查表")
	private Integer categoryType;
	/**图标*/
	@Excel(name = "图标", width = 15)
    @ApiModelProperty(value = "图标")
	private String icon;
	/**填报频次 0患者登记表 1月度汇总表 2季度汇总表 3年度汇总表*/
	@Excel(name = "填报频次 0患者登记表 1月度汇总表 2季度汇总表 3年度汇总表", width = 15)
    @ApiModelProperty(value = "填报频次 0患者登记表 1月度汇总表 2季度汇总表 3年度汇总表")
	private Integer writeFrequency;
	/**问卷版本*/
	@Excel(name = "问卷版本", width = 15)
    @ApiModelProperty(value = "问卷版本")
	private Integer questionVersion;
	/**关联模板id_查检表文件*/
	@Excel(name = "关联模板id_查检表文件", width = 15)
    @ApiModelProperty(value = "关联模板id_查检表文件")
	private String templateId;
}
