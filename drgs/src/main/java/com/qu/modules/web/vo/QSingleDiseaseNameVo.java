package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "病种名称筛选条件Vo", description = "按单病种填报Vo")
public class QSingleDiseaseNameVo {
    @ApiModelProperty(value = "分类id")
    private String categoryId;
    @ApiModelProperty(value = "图片")
    private String icon;
    @ApiModelProperty(value = "疾病名称")
    private String disease;
}
