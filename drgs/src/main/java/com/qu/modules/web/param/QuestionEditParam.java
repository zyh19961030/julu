package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="问卷编辑入参", description="问卷编辑入参")
public class QuestionEditParam {
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
    @ApiModelProperty(value = "问卷名称")
    private java.lang.String quName;
    @ApiModelProperty(value = "问卷描述")
    private java.lang.String quDesc;
    @ApiModelProperty(value = "科室查看权限，科室id逗号分割")
    private java.lang.String deptIds;
    @ApiModelProperty(value = "答案对应数据库名")
    private java.lang.String tableName;
    @ApiModelProperty(value = "0:草稿箱 1:已发布")
    private java.lang.Integer quStatus;
    @ApiModelProperty(value = "0:正常1:已停用")
    private java.lang.Integer quStop;

}
