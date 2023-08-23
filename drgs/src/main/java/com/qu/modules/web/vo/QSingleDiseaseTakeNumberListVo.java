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
@ApiModel(value = "科室单病种上报例数列表Vo", description = "科室单病种上报例数列表Vo")
public class QSingleDiseaseTakeNumberListVo {


    @ApiModelProperty(value = "单病种名称")
    private String disease;

    @ApiModelProperty(value = "科室名称")
    private String deptName;

    @ApiModelProperty(value = "数据")
    private String number;


}
