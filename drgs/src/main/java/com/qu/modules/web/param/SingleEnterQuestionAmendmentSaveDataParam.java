package com.qu.modules.web.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="SingleEnterQuestionAmendmentSaveDataParam", description="SingleEnterQuestionAmendmentSaveDataParam")
public class SingleEnterQuestionAmendmentSaveDataParam {

    @NotBlank
    @ApiModelProperty(value = "mappingTableId不能为空")
    private String mappingTableId;

    @ApiModelProperty(value = "答案数组")
    private Answers[] answers;

    @NotNull(message = "录入表单id不能为空")
    @ApiModelProperty(value = "录入表单id")
    private Integer singleEnterQuestionId;


}
