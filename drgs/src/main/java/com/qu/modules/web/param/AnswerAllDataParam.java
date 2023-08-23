package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value="查看某一个登记表的筛选时间(月度、季度、年)的数据接口查询参数", description="查看某一个登记表的筛选时间(月度、季度、年)的数据接口查询参数")
public class AnswerAllDataParam {

    /**问卷id*/
    @ApiModelProperty(value = "问卷id")
    @NotNull(message = "记录id不能为空")
    private Integer quId;

    /**起始时间*/
    @ApiModelProperty(value = "起始时间 月格式:yyyy-MM 季度:yyyyM 年:yyyy")
    @NotBlank(message = "起始时间不能为空")
    private String startDate;

    /**结束时间*/
    @ApiModelProperty(value = "结束时间 月格式:yyyy-MM 季度:yyyyM 年:yyyy")
    @NotBlank(message = "结束时间不能为空")
    private String endDate;

    /**时间类型 月：1  季度：2   年：3*/
    @ApiModelProperty(value = "时间类型 月：1  季度：2   年：3")
    @NotNull(message = "时间类型不能为空")
    private Integer type;

}
