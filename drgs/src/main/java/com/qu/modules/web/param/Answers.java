package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "作答答案", description = "作答答案")
public class Answers {
    /*@ApiModelProperty(value = "题目id")
    private Integer subId;*/
    @ApiModelProperty(value = "题目对应字段名")
    private String subColumnName;
    @ApiModelProperty(value = "答案")
    private String subValue;
}
