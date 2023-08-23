package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 随访模板疾病表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbFollowVisitTemplateDiseaseInfoParam", description="TbFollowVisitTemplateDiseaseInfoParam")
public class TbFollowVisitTemplateDiseaseInfoVo {

	/**疾病code*/
	@Excel(name = "疾病code", width = 15)
    @ApiModelProperty(value = "疾病code")
	private String code;
	/**疾病名称*/
	@Excel(name = "疾病名称", width = 15)
    @ApiModelProperty(value = "疾病名称")
	private String name;

}
