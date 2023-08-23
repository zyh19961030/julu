package com.qu.modules.web.vo;

import java.util.LinkedHashMap;
import java.util.List;

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
@ApiModel(value = "全院单病种上报数量统计-点击某个病种-统计分析表格Vo", description = "全院单病种上报数量统计-点击某个病种-统计分析表格Vo")
public class QSingleDiseaseTakeStatisticAnalysisTableVo {


    @ApiModelProperty(value = "表头")
    private List<LinkedHashMap<String,String>> fieldItems;

    @ApiModelProperty(value = "表数据")
    private List<LinkedHashMap<String,String>> singleDataList;


}
