package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AnswerIdVo", description = "AnswerIdVo")
public class AnswerIdVo {

    @ApiModelProperty(value = "答案id")
    private Integer answerId;

}
