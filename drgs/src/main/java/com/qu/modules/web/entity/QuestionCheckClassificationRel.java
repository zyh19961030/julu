package com.qu.modules.web.entity;

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
 * @Description: 查检表分类与查检表关联表
 * @Author: jeecg-boot
 * @Date:   2023-04-27
 * @Version: V1.0
 */
@Data
@TableName("question_check_classification_rel")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="question_check_classification_rel对象", description="查检表分类与查检表关联表")
public class QuestionCheckClassificationRel {
    
	/**查检表分类表id*/
	@Excel(name = "查检表分类表id", width = 15)
    @ApiModelProperty(value = "查检表分类表id")
	private Integer questionCheckClassificationId;
	/**查检表id*/
	@Excel(name = "查检表id", width = 15)
    @ApiModelProperty(value = "查检表id")
	private Integer questionId;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
}
