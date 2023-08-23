package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "科室单病种上报例数列表2Vo", description = "科室单病种上报例数列表2Vo")
public class QSingleDiseaseTakeNumberVo {

    @ApiModelProperty(value = "科室名称")
    private String deptName;

    @ApiModelProperty(value = "数据")
    private List<QSingleDiseaseTakeNumberListInDeptVo> numberList;




}
