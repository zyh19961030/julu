package com.qu.modules.web.param;

import org.jeecgframework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="选项新增入参", description="选项新增入参")
public class QoptionParam {
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
    /**选项名称*/
    @Excel(name = "选项名称", width = 15)
    @ApiModelProperty(value = "选项名称")
    private java.lang.String opName;
    /**跳题逻辑*/
    @Excel(name = "跳题逻辑", width = 15)
    @ApiModelProperty(value = "跳题逻辑")
    private java.lang.String jumpLogic;
    @ApiModelProperty(value = "是否其他  0:否 1:是")
    private Integer others;
    @ApiModelProperty(value = "绑定值")
    private java.lang.String bindValue;
    /**选项值*/
    @Excel(name = "选项值", width = 15)
    @ApiModelProperty(value = "选项值")
    private java.lang.String opValue;
    /**特殊跳题逻辑,前端使用*/
    @Excel(name = "特殊跳题逻辑,前端使用", width = 15)
    @ApiModelProperty(value = "特殊跳题逻辑,前端使用")
    private java.lang.String specialJumpLogic;
    /**选项分值*/
    @Excel(name = "选项分值", width = 15)
    @ApiModelProperty(value = "选项分值")
    private java.math.BigDecimal optionScore;
    /**答案*/
    @Excel(name = "答案", width = 15)
    @ApiModelProperty(value = "答案")
    private java.lang.String answerName;
    /**答案值*/
    @Excel(name = "答案值", width = 15)
    @ApiModelProperty(value = "答案值")
    private java.lang.String answerValue;
    /**大于的值*/
    @Excel(name = "大于的值", width = 15)
    @ApiModelProperty(value = "大于的值")
    private java.lang.String greaterThanValue;
    /**小于的值*/
    @Excel(name = "小于的值", width = 15)
    @ApiModelProperty(value = "小于的值")
    private java.lang.String lessThanValue;
}
