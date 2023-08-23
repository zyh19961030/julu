package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "按单病种填报Vo", description = "按单病种填报Vo")
public class QSingleDiseaseTakeVo {
    /*@ApiModelProperty(value = "人数")
    private int count;*/
    @ApiModelProperty(value = "问卷id")
    private Integer quId;
    @ApiModelProperty(value = "分类id")
    private String categoryId;
    @ApiModelProperty(value = "图片")
    private String icon;
    @ApiModelProperty(value = "疾病名称")
    private String disease;
}
