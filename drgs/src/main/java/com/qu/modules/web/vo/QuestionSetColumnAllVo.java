package com.qu.modules.web.vo;

import org.jeecgframework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "QuestionSetColumnAllVo", description = "QuestionSetColumnAllVo")
public class QuestionSetColumnAllVo {
    @Excel(name = "主键", width = 15)
    @ApiModelProperty(value = "主键")
    private Integer id;
    /**
     * 问卷名称
     */
    @Excel(name = "问卷名称", width = 15)
    @ApiModelProperty(value = "问卷名称")
    private String quName;

}
