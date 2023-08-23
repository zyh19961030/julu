package com.qu.modules.web.param;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 随访模板表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbFollowVisitTemplateAddOrUpdateParam", description="TbFollowVisitTemplateAddOrUpdateParam")
public class TbFollowVisitTemplateAddOrUpdateParam {
    
	/**id*/
    @ApiModelProperty(value = "id,添加为空，修改必传")
	private Integer id;
	/**随访计划模版名称*/
    @ApiModelProperty(value = "随访计划模版名称")
	private String name;
	/**随访计划时间起点 0出院日期*/
    @ApiModelProperty(value = "随访计划时间起点 0出院日期，目前固定传0")
	private Integer dateStartType;

	@ApiModelProperty(value = "适用疾病集合")
	private List<String> diseaseList;
//	private List<TbFollowVisitTemplateDiseaseAddParam> diseaseList;

	@ApiModelProperty(value = "随访计划周期集合")
	private List<TbFollowVisitTemplateCycleAddParam> cycleList;

}
