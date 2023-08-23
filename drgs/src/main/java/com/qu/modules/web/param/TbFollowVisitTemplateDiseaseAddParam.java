package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 随访模板疾病表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbFollowVisitTemplateDiseaseAddParam", description="TbFollowVisitTemplateDiseaseAddParam")
public class TbFollowVisitTemplateDiseaseAddParam {

	/**疾病code*/
    @ApiModelProperty(value = "疾病code")
	private String code;
	/**疾病名称*/
    @ApiModelProperty(value = "疾病名称")
	private String name;

}
