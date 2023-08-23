package com.qu.modules.web.param;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="问卷再次发布入参", description="问卷再次发布入参")
public class QuestionAgainReleaseParam {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "题目id集合")
    private List<Integer> subjectIds;
    @ApiModelProperty(value = "历史题目id开启痕迹集合")
    private List<Integer> markSubjectIds;
}
