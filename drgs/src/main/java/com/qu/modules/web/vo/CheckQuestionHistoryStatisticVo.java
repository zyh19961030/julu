package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CheckQuestionHistoryStatisticVo", description="CheckQuestionHistoryStatisticVo")
public class CheckQuestionHistoryStatisticVo {

	/**主键*/
	@ApiModelProperty(value = "主键")
	private Integer id;
	/**问卷名称*/
	@ApiModelProperty(value = "问卷名称")
	private String quName;
	/**图标*/
	@ApiModelProperty(value = "图标")
	private String icon;
	/**科室统计是否显示 0显示 1不显示*/
	@ApiModelProperty(value = "科室统计是否显示 0显示 1不显示")
	private Integer deptStatisticShow;
	/**缺陷统计是否显示 0显示 1不显示*/
	@ApiModelProperty(value = "缺陷统计是否显示 0显示 1不显示")
	private Integer defectStatisticShow;
	/**分类统计是否显示 0显示 1不显示*/
	@ApiModelProperty(value = "分类统计是否显示 0显示 1不显示")
	private Integer categoryStatisticShow;


}
