package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @Description: 录入表单表
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SingleEnterQuestionEnterQuestionListParam", description="SingleEnterQuestionEnterQuestionListParam")
public class SingleEnterQuestionEnterQuestionListParam {

	@NotNull(message = "录入表单id不能为空")
	@ApiModelProperty(value = "录入表单id")
	private Integer singleEnterQuestionId;

	@NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "状态 1:待处理  2:已完成")
	private Integer status;


}
