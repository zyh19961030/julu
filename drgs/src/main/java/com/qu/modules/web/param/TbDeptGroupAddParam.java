package com.qu.modules.web.param;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 分组管理表
 * @Author: jeecg-boot
 * @Date:   2022-09-19
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="分组管理表添加或修改入参", description="分组管理表添加或修改入参")
public class TbDeptGroupAddParam {


	/**id*/
	@ApiModelProperty(value = "id,添加为空，修改必传")
	private String id;

	/**分组名称*/
    @ApiModelProperty(value = "分组名称")
	@NotBlank(message = "分组名称不能为空")
	private String groupName;

	/**科室id数组*/
	@ApiModelProperty(value = "科室id数组")
	@NotEmpty(message = "问卷id数组不能为空")
	private List<String> deptIdList;


}
