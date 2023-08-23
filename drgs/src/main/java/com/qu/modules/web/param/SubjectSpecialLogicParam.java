package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="题目编辑特殊逻辑入参", description="题目编辑特殊逻辑入参")
public class SubjectSpecialLogicParam {

    @ApiModelProperty(value = "题目特殊逻辑数组")
    private List<SpecialLogicParam> subjectSpecialLogicList;

    @ApiModelProperty(value = "选项特殊逻辑数组")
    private List<SpecialLogicParam>  optionSpecialLogicList;
}
