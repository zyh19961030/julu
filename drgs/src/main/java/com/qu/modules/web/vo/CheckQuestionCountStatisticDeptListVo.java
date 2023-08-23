package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "CheckQuestionCountStatisticDeptListVo", description = "CheckQuestionCountStatisticDeptListVo")
public class CheckQuestionCountStatisticDeptListVo {

    @ApiModelProperty(value = "科室id")
    private String id;

    @ApiModelProperty(value = "科室名称")
    private String depName;


}
