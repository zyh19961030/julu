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
@ApiModel(value = "单病种填报增加分组导航Vo", description = "单病种填报增加分组导航Vo")
public class SingleDiseaseAnswerNavigationVo {
    @ApiModelProperty(value = "分组名称")
    private String groupName;
    @ApiModelProperty(value = "分组id")
    private Integer groupId;
//    @ApiModelProperty(value = "已答题数")
//    private Integer alreadyAnswerCount;
//    @ApiModelProperty(value = "未答题数")
//    private Integer notAnswerCount;
//    @ApiModelProperty(value = "总题数")
//    private Integer questionCount;
}
