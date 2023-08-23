package com.qu.modules.web.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "问卷分页Vo", description = "问卷分页Vo")
public class QuestionAndCategoryPageVo {
    @ApiModelProperty(value = "总条数")
    private long total;
    @ApiModelProperty(value = "数据")
    private List<QuestionAndCategoryVo> questionList;
}
