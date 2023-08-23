package com.qu.modules.web.param;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="全院单病种上报统计(全院单病种数量统计_科室筛选)-点击某个病种-统计分析", description="全院单病种上报统计(全院单病种数量统计_科室筛选)-点击某个病种-统计分析")
public class QSingleDiseaseTakeStatisticAnalysisByDeptParam {
    @ApiModelProperty(value = "分类id")
    @NotNull(message = "分类id不能为空")
    private String categoryId;
    @ApiModelProperty(value = "科室id")
    private String deptId;
    @NotNull(message = "开始时间不能为空")
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="开始时间格式不对")
    @ApiModelProperty(value = "开始时间  格式：月:yyyy-MM")
    private String dateStart;
    @NotNull(message = "结束时间不能为空")
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="结束时间格式不对")
    @ApiModelProperty(value = "结束时间  格式：月:yyyy-MM")
    private String dateEnd;
}
