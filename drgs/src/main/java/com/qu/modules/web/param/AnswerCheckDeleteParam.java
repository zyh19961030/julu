package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value="AnswerCheckDetailListParam检查表_检查明细记录入参", description="AnswerCheckDetailListParam检查表_检查明细记录入参")
public class AnswerCheckDeleteParam {
    @ApiModelProperty(value = "记录id,列表中获取")
    @NotNull(message = "记录id不能为空")
    private Integer id;
}
