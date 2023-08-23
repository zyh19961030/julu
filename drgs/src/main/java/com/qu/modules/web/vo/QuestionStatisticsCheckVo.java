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

/**
 * @Description: 问卷表
 * @Author: jeecg-boot
 * @Date:   2021-05-10
 * @Version: V1.0
 */
@Data
@TableName("question")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="QuestionStatisticsCheckVo", description="QuestionStatisticsCheckVo")
public class QuestionStatisticsCheckVo {

	/**主键*/
	@TableId(type = IdType.AUTO)
	@Excel(name = "主键", width = 15)
	@ApiModelProperty(value = "主键")
	private Integer id;
	/**问卷名称*/
	@Excel(name = "问卷名称", width = 15)
	@ApiModelProperty(value = "问卷名称")
	private String quName;
    /**答案对应数据库名*/
    @Excel(name = "答案对应数据库名", width = 15)
    @ApiModelProperty(value = "答案对应数据库名")
    private java.lang.String tableName;
//	/**图标*/
//	@Excel(name = "图标", width = 15)
//	@ApiModelProperty(value = "图标")
//	private String icon;

}
