package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AnswerCheckIdVo", description = "AnswerCheckIdVo")
public class AnswerCheckIdVo {

    @ApiModelProperty(value = "答案id")
    private Integer answerCheckId;

}
