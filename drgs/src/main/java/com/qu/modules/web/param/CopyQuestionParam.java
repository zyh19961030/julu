package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "复制检查表Param", description = "复制检查表Param")
public class CopyQuestionParam {
    @NotNull(message = "问卷id不能为空")
    @ApiModelProperty(value = "问卷id")
    private Integer quId;

    @NotBlank(message = "查检表名字不能为空")
    @ApiModelProperty(value = "查检表名字")
    private String newQuestionName;

    @NotBlank(message = "数据库子表表名不能为空")
    @ApiModelProperty(value = "数据库子表表名")
    private String newTableName;

}
