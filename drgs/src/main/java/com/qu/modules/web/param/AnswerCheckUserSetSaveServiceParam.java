package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel(value = "AnswerCheckUserSetSaveServiceParam", description = "AnswerCheckUserSetSaveServiceParam")
public class AnswerCheckUserSetSaveServiceParam {

    @NotBlank(message = "用户idid不能为空")
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "问卷id")
    private List<Integer> quId;

}
