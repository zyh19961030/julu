package com.qu.modules.web.vo;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.qu.modules.web.entity.Question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "问卷分页Vo", description = "问卷分页Vo")
public class QuestionAndCategoryVo extends Question{
    /**
     * 分类名称
     */
    @Excel(name = "分类名称", width = 15)
    @ApiModelProperty(value = "分类名称")
    private String categoryName;
    /**关联检查项目名称*/
    @Excel(name = "关联检查项目名称", width = 15)
    @ApiModelProperty(value = "关联检查项目名称")
    private java.lang.String checkProjectName;
}
