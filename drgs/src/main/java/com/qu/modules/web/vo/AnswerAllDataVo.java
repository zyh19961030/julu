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
@ApiModel(value = "查看某一个登记表的筛选时间(月度、季度、年)的数据接口Vo", description = "查看某一个登记表的筛选时间(月度、季度、年)的数据接口Vo")
public class AnswerAllDataVo {

    @ApiModelProperty(value = "表头")
    private List<LinkedHashMap<String,String>> fieldItems;

    @ApiModelProperty(value = "表数据")
    private List<LinkedHashMap<String,String>> detailDataList;


}
