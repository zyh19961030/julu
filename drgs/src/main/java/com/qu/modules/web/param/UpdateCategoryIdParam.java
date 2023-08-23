package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "设置分类Param", description = "设置分类Param")
public class UpdateCategoryIdParam {
    @ApiModelProperty(value = "问卷id")
    private Integer[] quId;
    @ApiModelProperty(value = "分类id")
    private String[] categoryId;
    @ApiModelProperty(value = "分类类型：0其他 1单病种")
    private Integer categoryType;
}
