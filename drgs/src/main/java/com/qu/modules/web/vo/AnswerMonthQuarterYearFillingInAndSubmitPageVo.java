package com.qu.modules.web.vo;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "月度/季度/年汇总填报中和已提交分页Vo", description = "月度/季度/年汇总填报中和已提交分页Vo")
public class AnswerMonthQuarterYearFillingInAndSubmitPageVo {
    @ApiModelProperty(value = "总条数")
    private long total;
    @ApiModelProperty(value = "数据")
    private List<AnswerMonthQuarterYearFillingInAndSubmitVo> answerPatientFillingInVos = Lists.newArrayList();
}
