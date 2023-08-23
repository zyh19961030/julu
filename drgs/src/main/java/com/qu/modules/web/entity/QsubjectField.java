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

/**
 * @Description: 题目字段表
 * @Author: jeecg-boot
 * @Date:   2021-05-10
 * @Version: V1.0
 */
@Data
@TableName("qsubject_field")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qsubject_field对象", description="题目字段表")
public class QsubjectField {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**字段名*/
	@Excel(name = "字段名", width = 15)
    @ApiModelProperty(value = "字段名")
	private String fieldName;
	/**题目名(数据采集项)*/
	@Excel(name = "题目名(数据采集项)", width = 15)
    @ApiModelProperty(value = "题目名(数据采集项)")
	private String subjectName;
	/**题目类型名称*/
	@Excel(name = "题目类型名称", width = 15)
    @ApiModelProperty(value = "题目类型名称")
	private String type;
	/**题目数据库类型*/
	@Excel(name = "题目数据库类型", width = 15)
    @ApiModelProperty(value = "题目数据库类型")
	private String typeDatabase;
}
