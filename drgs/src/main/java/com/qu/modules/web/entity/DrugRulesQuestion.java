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
 * @Description: 药品规则问卷表
 * @Author: jeecg-boot
 * @Date:   2021-09-12
 * @Version: V1.0
 */
@Data
@TableName("drug_rules_question")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="drug_rules_question对象", description="药品规则问卷表")
public class DrugRulesQuestion {
    
	/**药品规则问卷id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "药品规则问卷id")
	private Integer id;
	/**问卷id,关联question表的id*/
	@Excel(name = "问卷id,关联question表的id", width = 15)
    @ApiModelProperty(value = "问卷id,关联question表的id")
	private Integer questionId;
	/**问卷名称*/
	@Excel(name = "问卷名称", width = 15)
    @ApiModelProperty(value = "问卷名称")
	private String questionName;
	/**删除标识*/
	@Excel(name = "删除标识", width = 15)
    @ApiModelProperty(value = "删除标识")
	private Integer del;
	/**创建日期*/
    @ApiModelProperty(value = "创建日期")
	private Date createTime;
	/**更新日期*/
    @ApiModelProperty(value = "更新日期")
	private Date updateTime;
}
