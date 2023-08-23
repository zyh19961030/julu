package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="单病种驳回参数", description="单病种驳回参数")
public class SingleDiseaseRejectParam {
    @ApiModelProperty(value = "id数组")
    private String[] ids;
    @ApiModelProperty(value = "驳回原因")
    private String examineReason;
}
