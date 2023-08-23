package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value="科室单病种上报例数列表入参", description="科室单病种上报例数列表入参")
public class QSingleDiseaseTakeNumberListParam {
    @ApiModelProperty(value = "单病种分类id集合")
    private String[] categoryIdList;
    @ApiModelProperty(value = "科室id数组")
    private String[] deptList;
    @NotBlank(message = "开始时间不能为空")
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="开始时间格式不对")
    @ApiModelProperty(value = "开始时间  格式：月:yyyy-MM")
    private String dateStart;
    @NotBlank(message = "结束时间不能为空")
    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="结束时间格式不对")
    @ApiModelProperty(value = "结束时间  格式：月:yyyy-MM")
    private String dateEnd;
}
