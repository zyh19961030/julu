package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "查看某科室的检查人员设置接口Vo", description = "查看某科室的检查人员设置接口Vo")
public class AnswerCheckSetAllDataVo {

    @ApiModelProperty(value = "表头")
    private List<LinkedHashMap<String,String>> fieldItems;

    @ApiModelProperty(value = "表数据")
    private List<LinkedHashMap<String,String>> detailDataList;


}
