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
@ApiModel(value = "检查表_检查明细记录返参vo", description = "检查表_检查明细记录返参vo")
public class AnswerCheckDetailListVo {

    @ApiModelProperty(value = "总数")
    private long total;

    @ApiModelProperty(value = "表头")
    private List<LinkedHashMap<String,Object>> fieldItems;

    @ApiModelProperty(value = "表数据")
    private List<LinkedHashMap<String,Object>> detailDataList;


}
