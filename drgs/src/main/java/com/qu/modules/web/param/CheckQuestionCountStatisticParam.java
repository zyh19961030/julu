package com.qu.modules.web.param;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="CheckQuestionCountStatisticParam统计_查检数量统计", description="CheckQuestionCountStatisticParam统计_查检数量统计")
public class CheckQuestionCountStatisticParam {


    @ApiModelProperty(value = "科室类型 -1全部或者传空 1职能科室 2临床和医技科室")
    private Integer deptType;

    @ApiModelProperty(value = "科室id")
    private String deptId;

    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="选择检查月份_时间格式不对")
    @ApiModelProperty(value = "选择检查月份_时间  格式：月:yyyy-MM")
    private String checkMonth;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "填报时间_起始时间 格式：yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "填报时间_结束时间 格式：yyyy-MM-dd")
    private Date endDate;

    @ApiModelProperty(value = "前端忽略")
    private List<String> idList;

}
