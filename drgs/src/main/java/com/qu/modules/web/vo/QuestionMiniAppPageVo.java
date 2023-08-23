package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "问卷小程序分页数据Vo", description = "问卷小程序分页数据Vo")
public class QuestionMiniAppPageVo {
    /**主键*/
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
    /**问卷名称*/
    @ApiModelProperty(value = "问卷名称")
    private java.lang.String quName;
    /**图标*/
    @ApiModelProperty(value = "图标")
    private String icon;
}
