package com.qu.modules.web.vo;

import com.qu.modules.web.entity.Question;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "问卷分页Vo", description = "问卷分页Vo")
public class QuestionPageVo {
    @ApiModelProperty(value = "总条数")
    private int total;
    @ApiModelProperty(value = "数据")
    private List<Question> questionList;
}
