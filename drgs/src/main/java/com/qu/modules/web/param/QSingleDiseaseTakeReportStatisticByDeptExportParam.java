package com.qu.modules.web.param;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="全院单病种上报统计(全院单病种数量统计_科室筛选)_导出", description="全院单病种上报统计(全院单病种数量统计_科室筛选)_导出")
public class QSingleDiseaseTakeReportStatisticByDeptExportParam {
    @NotNull(message = "开始时间不能为空")
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="开始时间格式不对")
    @ApiModelProperty(value = "开始时间  格式：年:yyyy 月:yyyy-MM 日:yyyy-MM-dd")
    private String dateStart;
    @NotNull(message = "结束时间不能为空")
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="结束时间格式不对")
    @ApiModelProperty(value = "结束时间  格式：年:yyyy 月:yyyy-MM 日:yyyy-MM-dd")
    private String dateEnd;
    @ApiModelProperty(value = "科室id")
    private String deptId;
    @NotNull(message = "userId不能为空")
    @ApiModelProperty(value = "userId")
    private String userId;

    /*@NotNull(message = "时间类型不能为空")
    @Pattern(regexp = "yearly|monthly|daily",message = "时间类型不正确")
    @ApiModelProperty(value = "时间类型：年yearly 月monthly 日daily")
    private String dateType;
    @NotNull(message = "开始时间不能为空")
    @Pattern(regexp="\\d{4}|\\d{4}-(0[1-9]|1[0-2])|\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])",message="开始时间格式不对")
    @ApiModelProperty(value = "开始时间  格式：年:yyyy 月:yyyy-MM 日:yyyy-MM-dd")
    private String dateStart;
    @NotNull(message = "结束时间不能为空")
    @Pattern(regexp="\\d{4}|\\d{4}-(0[1-9]|1[0-2])|\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])",message="结束时间格式不对")
    @ApiModelProperty(value = "结束时间  格式：年:yyyy 月:yyyy-MM 日:yyyy-MM-dd")
    private String dateEnd;
    @ApiModelProperty(value = "科室id数组")
    private String[] dept;
    @ApiModelProperty(value = "是否显示科室  0不显示科室  1显示科室")
    private Integer deptShow;*/
}
