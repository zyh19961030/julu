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
@ApiModel(value = "CheckQuestionCountStatisticVo", description = "CheckQuestionCountStatisticVo")
public class CheckQuestionCountStatisticVo {

    @ApiModelProperty(value = "检查科室id")
    private String deptId;

    @ApiModelProperty(value = "检查科室名称")
    private String deptName;

    @ApiModelProperty(value = "填报人id")
    private String userId;

    @ApiModelProperty(value = "填报人姓名")
    private String userName;

    @ApiModelProperty(value = "问卷id")
    private Integer quId;

    @ApiModelProperty(value = "问卷名称")
    private String quName;

    @ApiModelProperty(value = "数量")
    private Integer count;

}
