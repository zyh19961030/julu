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
@ApiModel(value = "单病种上报数量统计Vo", description = "单病种上报数量统计Vo")
public class SingleDiseaseReportCountVo {
    @ApiModelProperty(value = "今日上报单病种数量")
    private Integer todaySingleDiseaseReportCount;
    @ApiModelProperty(value = "昨日上报单病种数量")
    private Integer yesterdaySingleDiseaseReportCount;
    @ApiModelProperty(value = "本月上报单病种数量")
    private Integer monthSingleDiseaseReportCount;
    @ApiModelProperty(value = "累计审核成功数量")
    private Integer singleDiseaseReportCount;
//    @ApiModelProperty(value = "是否为临床科室，0是临床科室，1不是临床科室")
//    private Integer clinical;
}
