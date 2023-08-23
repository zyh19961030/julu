package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description: 分组管理表
 * @Author: jeecg-boot
 * @Date:   2022-09-19
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbDeptGroupListVo", description="TbDeptGroupListVo")
public class TbDeptGroupListVo {
    
	/**id*/
    @ApiModelProperty(value = "id")
	private String id;
	/**分组名称*/
    @ApiModelProperty(value = "分组名称")
	private String groupName;
	/**科室id数组*/
	@ApiModelProperty(value = "科室id数组")
	private List<String> deptIdList;

}
