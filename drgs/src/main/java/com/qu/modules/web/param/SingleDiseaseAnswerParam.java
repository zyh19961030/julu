package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="单病种答案入参", description="单病种答案入参")
public class SingleDiseaseAnswerParam {
    @ApiModelProperty(value = "单病种总表id,从医生填报查询中获得的id,添加时此值为空")
    private Integer id;
    @ApiModelProperty(value = "问卷id")
    private Integer quId;
    @ApiModelProperty(value = "答案数组")
    private SingleDiseaseAnswer[] answers;
}
