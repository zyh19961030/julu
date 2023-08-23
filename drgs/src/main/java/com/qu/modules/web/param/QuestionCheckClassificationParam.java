package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="QuestionCheckClassificationParam", description="QuestionCheckClassificationParam")
public class QuestionCheckClassificationParam {
    /**问卷名称*/
    @ApiModelProperty(value = "问卷名称")
    private String quName;
    @ApiModelProperty(value = "分类id")
    private Integer questionCheckClassificationId;
//    /**问卷描述*/
//    @ApiModelProperty(value = "问卷描述")
//    private String quDesc;
//    @ApiModelProperty(value = "答案对应数据库名")
//    private String tableName;
}
