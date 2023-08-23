package com.qu.modules.web.vo;

import com.qu.modules.web.entity.QSingleDiseaseTake;
import com.qu.modules.web.entity.Qsubjectlib;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "按医生填报分页Vo", description = "按医生填报分页Vo")
public class QSingleDiseaseTakeByDoctorPageVo {
    @ApiModelProperty(value = "总条数")
    private long total;
    @ApiModelProperty(value = "数据")
    private List<QSingleDiseaseTake> qSingleDiseaseTakeList;
}
