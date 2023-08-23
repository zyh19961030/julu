package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TbDeptGroupDepVo", description = "TbDeptGroupDepVo")
public class TbDeptGroupDepVo {
    @ApiModelProperty(value = "科室id")
    private String departmentId;
    @ApiModelProperty(value = "科室名称")
    private String departmentName;
}
