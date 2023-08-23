package com.qu.modules.web.param;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="题目编辑逻辑入参", description="题目编辑逻辑入参")
public class SubjectLogicParam {

    @ApiModelProperty(value = "题目逻辑数组")
    private List<LogicParam> subjectLogicList;

    @ApiModelProperty(value = "选项逻辑数组")
    private List<LogicParam>  optionLogicList;
}
