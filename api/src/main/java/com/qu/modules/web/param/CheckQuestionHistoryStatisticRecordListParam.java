package com.qu.modules.web.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="CheckQuestionHistoryStatisticRecordListParam入参", description="CheckQuestionHistoryStatisticRecordListParam入参")
public class CheckQuestionHistoryStatisticRecordListParam {

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "填报时间_起始时间 格式：yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "填报时间_结束时间 格式：yyyy-MM-dd")
    private Date endDate;

    @Pattern(regexp="\\d{4}-(0[1-9]|1[0-2])",message="选择检查月份_时间格式不对")
    @ApiModelProperty(value = "选择检查月份_时间  格式：月:yyyy-MM")
    private String checkMonth;

    @NotNull(message = "问卷id不能为空")
    @ApiModelProperty(value = "问卷id")
    private Integer quId;

    @ApiModelProperty(value = "被检查科室id")
    private String checkedDeptId;

    @NotBlank(message = "检查科室id不能为空")
    @ApiModelProperty(value = "检查科室id")
    private String deptId;
}
