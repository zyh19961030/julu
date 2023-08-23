package com.qu.modules.web.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "QuestionSetColumnVo", description = "QuestionSetColumnVo")
public class QuestionSetColumnVo {

    @ApiModelProperty(value = "左边所有数据")
    private List<QuestionSetColumnAllVo> allData;


    @ApiModelProperty(value = "右边选择的id集合")
    private List<QuestionSetColumnChooseVo> chooseData;

}
