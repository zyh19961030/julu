package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="QueryCheckQuestionClassificationByUpdateParam", description="QueryCheckQuestionClassificationByUpdateParam")
public class QueryCheckQuestionClassificationByUpdateParam {

    @ApiModelProperty(value = "问卷名称")
    private String quName;

    @ApiModelProperty(value = "查检表分类Id")
    private Integer questionCheckClassificationId;


}
