package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description: 检查明细列设置表
 * @Author: jeecg-boot
 * @Date:   2022-08-09
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CheckDetailSetParam", description="CheckDetailSetParam")
public class CheckDetailSetParam {
    
	/**主键*/
    @ApiModelProperty(value = "主键")
	private Integer id;
	/**问卷id*/
    @ApiModelProperty(value = "问卷id")
	private Integer questionId;
	/**题目id*/
    @ApiModelProperty(value = "题目id")
	private Integer subjectId;
	/**题目名称*/
	@ApiModelProperty(value = "题目名称")
	private String subjectName;
	/**排序序号*/
    @ApiModelProperty(value = "排序序号")
	private Integer sortNumber;
	/**是否显示 0显示 1不显示*/
    @ApiModelProperty(value = "是否显示 0显示 1不显示")
	private Integer showType;
	/**父节点id*/
	@ApiModelProperty(value = "父节点id")
	private Integer questionParentId;

	@ApiModelProperty(value = "分组下的题目")
	private List<CheckDetailSetParam> childList;

}
