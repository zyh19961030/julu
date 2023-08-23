package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value="本科室单病种上报记录查询", description="本科室单病种上报记录查询")
public class QSingleDiseaseTakeStatisticAnalysisParam {
    @ApiModelProperty(value = "分类id")
    @NotNull(message = "分类id不能为空")
    private String categoryId;
    @NotNull(message = "开始时间不能为空")
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="开始时间格式不对")
    @ApiModelProperty(value = "开始时间  格式：月:yyyy-MM")
    private String dateStart;
    @NotNull(message = "结束时间不能为空")
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="结束时间格式不对")
    @ApiModelProperty(value = "结束时间  格式：月:yyyy-MM")
    private String dateEnd;
}
