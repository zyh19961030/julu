package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author
 */
@Data
@ApiModel(value = "QuestionNameVo", description = "QuestionNameVo")
public class QuestionNameVo {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "问卷名称")
    private String quName;
}
