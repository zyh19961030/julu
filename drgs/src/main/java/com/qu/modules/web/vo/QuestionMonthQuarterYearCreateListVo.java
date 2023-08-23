package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
@ApiModel(value = "患者登记表-新建查询返回Vo", description = "患者登记表-新建查询返回Vo")
public class QuestionMonthQuarterYearCreateListVo {
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
    private String icon;

    /**填报状态*/
    /*@Excel(name = "填报状态", width = 15)
    @ApiModelProperty(value = "填报状态")
    private Integer status;*/

}
