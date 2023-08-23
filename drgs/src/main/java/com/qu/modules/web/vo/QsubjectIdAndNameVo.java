package com.qu.modules.web.vo;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@TableName("qsubject")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="QsubjectIdAndNameVo", description="QsubjectIdAndNameVo")
public class QsubjectIdAndNameVo {

	/**主键*/
	@TableId(type = IdType.AUTO)
	@Excel(name = "主键", width = 15)
	@ApiModelProperty(value = "主键")
	private Integer id;
	/**题目名称*/
	@Excel(name = "题目名称", width = 15)
    @ApiModelProperty(value = "题目名称")
	private String subName;

}
