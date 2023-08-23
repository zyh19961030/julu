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
 * @Description: 科室检查统计模板
 * @Author: jeecg-boot
 * @Date:   2022-09-18
 * @Version: V1.0
 */
@Data
@TableName("tb_inspect_stats_template_dep")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tb_inspect_stats_template_dep对象", description="科室检查统计模板")
public class TbInspectStatsTemplateDep {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**统计模板Id*/
	@Excel(name = "统计模板Id", width = 15)
    @ApiModelProperty(value = "统计模板Id")
	private String templateId;
	/**科室ID*/
	@Excel(name = "科室ID", width = 15)
    @ApiModelProperty(value = "科室ID")
	private String deptId;
	/**问卷ID*/
	@Excel(name = "问卷ID", width = 15)
    @ApiModelProperty(value = "问卷ID")
	private String quId;
	/**类型 1-科室统计 2-缺陷统计 3-分类统计*/
	@Excel(name = "类型 1-科室统计 2-缺陷统计 3-分类统计", width = 15)
    @ApiModelProperty(value = "类型 1-科室统计 2-缺陷统计 3-分类统计")
	private Integer type;
}
