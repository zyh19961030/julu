package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 录入表单表
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SingleEnterQuestionListParam", description="SingleEnterQuestionListParam")
public class SingleEnterQuestionListParam {

    @ApiModelProperty(value = "问卷名称")
	private String questionName;

    @ApiModelProperty(value = "问卷分类")
	private String questionNameCategoryId;

	@ApiModelProperty(value = "填报科室id")
	private java.lang.String writeDeptId;

	@ApiModelProperty(value = "查看科室id")
	private java.lang.String seeDeptId;

}
