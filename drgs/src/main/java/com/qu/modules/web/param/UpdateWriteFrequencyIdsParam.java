package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "设置填报频次Param", description = "设置填报频次Param")
public class UpdateWriteFrequencyIdsParam {
    @ApiModelProperty(value = "问卷id")
    @NotNull(message = "问卷id数组不能为空")
    private Integer[] quId;
    @ApiModelProperty(value = "填报频次 0患者登记表 1月度汇总表 2季度汇总表 3年度汇总表")
    @NotNull(message = "填报频次不能为空")
    private Integer writeFrequency;
}
