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
 * @Description: 药品规则答案表
 * @Author: jeecg-boot
 * @Date:   2021-09-12
 * @Version: V1.0
 */
@Data
@TableName("drug_rules_option")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="drug_rules_option对象", description="药品规则答案表")
public class DrugRulesOption {
    
	/**药品规则答案id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "药品规则答案id")
	private Integer id;
	/**问题id，关联qoption的id*/
	@Excel(name = "问题id，关联qoption的id", width = 15)
    @ApiModelProperty(value = "问题id，关联qoption的id")
	private Integer optionId;
	/**药品规则问题id*/
	@Excel(name = "药品规则问题id", width = 15)
    @ApiModelProperty(value = "药品规则问题id")
	private Integer drugRulesSubjectId;
	/**删除标识*/
	@Excel(name = "删除标识", width = 15)
    @ApiModelProperty(value = "删除标识")
	private Integer del;
	/**创建日期*/
    @ApiModelProperty(value = "创建日期")
	private Date createTime;
	/**修改日期*/
    @ApiModelProperty(value = "修改日期")
	private Date updateTime;
}
