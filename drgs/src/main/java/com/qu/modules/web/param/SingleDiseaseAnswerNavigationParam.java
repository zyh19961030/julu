package com.qu.modules.web.param;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "单病种填报增加分组导航入参", description = "单病种填报增加分组导航入参")
public class SingleDiseaseAnswerNavigationParam {
    @ApiModelProperty(value = "单病种总表id,从医生填报查询中获得的id")
    Integer id;
    @NotNull(message = "问卷id不能为空")
    @ApiModelProperty(value = "问卷id")
    Integer questionId;
}
