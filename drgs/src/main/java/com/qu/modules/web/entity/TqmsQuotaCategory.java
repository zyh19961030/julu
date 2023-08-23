package com.qu.modules.web.entity;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 分类表
 * @Author: jeecg-boot
 * @Date:   2021-05-10
 * @Version: V1.0
 */
@Data
@TableName("tqms_quota_category")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tqms_quota_category对象", description="分类表")
public class TqmsQuotaCategory {
    
	/**categoryId*/
	@Excel(name = "categoryId", width = 15)
    @ApiModelProperty(value = "categoryId")
	private Long categoryId;
	/**分类名称*/
	@Excel(name = "分类名称", width = 15)
    @ApiModelProperty(value = "分类名称")
	private String categoryName;
	/**1-单病种及分类 2-普通分类*/
	@Excel(name = "1-单病种及分类 2-普通分类", width = 15)
    @ApiModelProperty(value = "1-单病种及分类 2-普通分类")
	private Integer type;
	/**父级分类ID*/
	@Excel(name = "父级分类ID", width = 15)
    @ApiModelProperty(value = "父级分类ID")
	private Long parentId;
	/**单病种类型*/
	@Excel(name = "单病种类型", width = 15)
    @ApiModelProperty(value = "单病种类型")
	private String diseaseType;
	/**是否单病种*/
	@Excel(name = "是否单病种", width = 15)
    @ApiModelProperty(value = "是否单病种")
	private Integer isSingleDisease;
}
