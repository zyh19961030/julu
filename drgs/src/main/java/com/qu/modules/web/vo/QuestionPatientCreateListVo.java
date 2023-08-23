package com.qu.modules.web.vo;

import org.jeecgframework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "患者登记表-新建查询返回Vo", description = "患者登记表-新建查询返回Vo")
public class QuestionPatientCreateListVo {
    @Excel(name = "主键", width = 15)
    @ApiModelProperty(value = "主键")
    private Integer id;
    /**
     * 问卷名称
     */
    @Excel(name = "问卷名称", width = 15)
    @ApiModelProperty(value = "问卷名称")
    private String quName;

    /**图标*/
    @Excel(name = "图标", width = 15)
    @ApiModelProperty(value = "图标")
    private java.lang.String icon;

    /**答案对应数据库名*/
    @Excel(name = "答案对应数据库名", width = 15)
    @ApiModelProperty(value = "答案对应数据库名")
    private java.lang.String tableName;

}
