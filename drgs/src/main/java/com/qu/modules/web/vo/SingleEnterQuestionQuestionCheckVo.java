package com.qu.modules.web.vo;

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
@ApiModel(value="SingleEnterQuestionQuestionCheckVo", description="SingleEnterQuestionQuestionCheckVo")
public class SingleEnterQuestionQuestionCheckVo {

	/**主键*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键")
	private Integer id;
	/**问卷名称*/
	@ApiModelProperty(value = "问卷名称")
	private String quName;

	@ApiModelProperty(value = "录入表单名称")
	private String name;
	/**图标*/
	@ApiModelProperty(value = "图标")
	private String icon;

	@ApiModelProperty(value = "录入表单id")
	private Integer singleEnterQuestionId;

}
