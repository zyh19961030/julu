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
 * @Description: 药品规则关联表
 * @Author: jeecg-boot
 * @Date:   2021-09-14
 * @Version: V1.0
 */
@Data
@TableName("drug_rules_relation")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="drug_rules_relation对象", description="药品规则关联表")
public class DrugRulesRelation {
    
	/**药品规则答案id*/
	@Excel(name = "药品规则答案id", width = 15)
    @ApiModelProperty(value = "药品规则答案id")
	private Integer drugRulesOptionId;
	/**用药目的id*/
	@Excel(name = "用药目的id", width = 15)
    @ApiModelProperty(value = "用药目的id")
	private Integer medicationPurposeId;
	/**药品物理作用id*/
	@Excel(name = "药品物理作用id", width = 15)
    @ApiModelProperty(value = "药品物理作用id")
	private Integer drugPhysicalActionId;
	/**区分根据一级或二级关联：1为一级，2为二级*/
	@Excel(name = "区分根据一级或二级关联：1为一级，2为二级", width = 15)
    @ApiModelProperty(value = "区分根据一级或二级关联：1为一级，2为二级")
	private Integer type;
	/**区分题目类型：1为单选和多选类型，2为时间类型和单行输入文本类型*/
	@Excel(name = "区分题目类型：1为单选和多选类型，2为时间类型和单行输入文本类型", width = 15)
	@ApiModelProperty(value = "区分题目类型：1为单选和多选类型，2为时间类型和单行输入文本类型")
	private Integer subject_type;
}
