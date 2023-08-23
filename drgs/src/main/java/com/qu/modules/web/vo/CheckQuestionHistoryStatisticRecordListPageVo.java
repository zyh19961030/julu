package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "CheckQuestionHistoryStatisticRecordListPageVo", description = "CheckQuestionHistoryStatisticRecordListPageVo")
public class CheckQuestionHistoryStatisticRecordListPageVo {
    @ApiModelProperty(value = "总条数")
    private long total;
    @ApiModelProperty(value = "数据")
    private List<CheckQuestionHistoryStatisticRecordListVo> answerVoList;
}
