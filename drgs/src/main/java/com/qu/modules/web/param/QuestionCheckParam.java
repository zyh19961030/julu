package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
@ApiModel(value="QuestionCheckParam入参", description="QuestionCheckParam入参")
public class QuestionCheckParam {
    /**问卷名称*/
    @Excel(name = "问卷名称", width = 15)
    @ApiModelProperty(value = "问卷名称")
    private String quName;
//    /**问卷描述*/
//    @Excel(name = "问卷描述", width = 15)
//    @ApiModelProperty(value = "问卷描述")
//    private String quDesc;
//    @ApiModelProperty(value = "答案对应数据库名")
//    private String tableName;
}
