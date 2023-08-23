package com.qu.modules.web.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 用户表vo
 * </p>
 *
 */
@Data
@Builder
@ApiModel(value = "QuestionSetLineVo", description = "QuestionSetLineVo")
public class QuestionSetLineVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "左边所有数据")
    private List<QuestionSetLineAllVo> allData;

    @ApiModelProperty(value = "右边选择的id集合")
    private List<QuestionSetLineChooseVo> chooseData;

}
