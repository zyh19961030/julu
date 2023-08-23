package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value="SubjectQuantityStatisticsParam入参", description="SubjectQuantityStatisticsParam入参")
public class SubjectQuantityStatisticsParam {

    @NotNull(message = "问卷id不能为空")
    @ApiModelProperty(value = "问卷id")
    private Integer id;

}
