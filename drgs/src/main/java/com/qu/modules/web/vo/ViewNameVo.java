package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author
 */
@Data
@ApiModel(value = "视图Vo", description = "视图Vo")
public class ViewNameVo {
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "问卷名称")
    private String name;
}
