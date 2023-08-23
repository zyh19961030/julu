package com.qu.modules.web.param;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AnswerCheckUserSetSaveParam", description = "AnswerCheckUserSetSaveParam")
public class AnswerCheckUserSetSaveParam {
    @ApiModelProperty(value = "问卷id")
    private List<Integer> quId;
    @ApiModelProperty(value = "用户id")
    private List<String> userId;
    @ApiModelProperty(value = "类型 1保存行(传userId入参) 2保存列(传quId入参)")
    private Integer type;

}
