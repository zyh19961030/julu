package com.qu.modules.web.vo;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "患者登记表填报中和已提交分页Vo", description = "患者登记表填报中和已提交分页Vo")
public class AnswerPatientFillingInAndSubmitPageVo {
    @ApiModelProperty(value = "总条数")
    private long total;
    @ApiModelProperty(value = "数据")
    private List<AnswerPatientFillingInAndSubmitVo> answerPatientFillingInVos = Lists.newArrayList();
}
