package com.qu.modules.web.vo;

import com.qu.modules.web.entity.Qsubjectlib;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "题库分页Vo", description = "题库分页Vo")
public class QsubjectlibPageVo {
    @ApiModelProperty(value = "总条数")
    private int total;
    @ApiModelProperty(value = "数据")
    private List<Qsubjectlib> qsubjectlibList;
}
