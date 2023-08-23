package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "QuestionSetColumnAllVo", description = "QuestionSetColumnAllVo")
public class QuestionSetColumnChooseVo {

    @ApiModelProperty(value = "问卷id")
    private Integer id;
    /**
     * 问卷名称
     */
    @ApiModelProperty(value = "问卷名称")
    private String quName;

}
