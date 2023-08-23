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
@ApiModel(value = "工作台提醒Vo", description = "工作台提醒Vo")
public class WorkbenchReminderVo {
    @ApiModelProperty(value = "已出院患者中未填报数据的条数")
    private Integer notWriteCount;
    @ApiModelProperty(value = "填报数据不符合要求已驳回的条数")
    private Integer rejectCount;
    @ApiModelProperty(value = "是否为临床科室，0是临床科室，1不是临床科室")
    private Integer clinical;
}
