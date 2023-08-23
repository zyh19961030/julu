package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
@ApiModel(value="问卷添加入参", description="问卷添加入参")
public class QuestionParam {
    /**问卷名称*/
    @Excel(name = "问卷名称", width = 15)
    @ApiModelProperty(value = "问卷名称")
    private java.lang.String quName;
    /**问卷描述*/
    @Excel(name = "问卷描述", width = 15)
    @ApiModelProperty(value = "问卷描述")
    private java.lang.String quDesc;
    @ApiModelProperty(value = "答案对应数据库名")
    private java.lang.String tableName;
}
