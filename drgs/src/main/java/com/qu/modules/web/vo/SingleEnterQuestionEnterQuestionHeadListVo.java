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
@ApiModel(value = "全院单病种上报数量统计-点击某个病种-统计分析表格Vo", description = "全院单病种上报数量统计-点击某个病种-统计分析表格Vo")
public class SingleEnterQuestionEnterQuestionHeadListVo {

    @ApiModelProperty(value = "展示列")
    private List<SingleEnterQuestionEnterQuestionHeadListDetailVo> columnList;

    @ApiModelProperty(value = "填报题目列")
    private List<SingleEnterQuestionEnterQuestionHeadListDetailVo> subjectList;


}
