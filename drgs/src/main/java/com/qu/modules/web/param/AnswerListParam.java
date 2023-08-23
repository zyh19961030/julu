package com.qu.modules.web.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel(value="AnswerCheckParam入参", description="AnswerCheckParam入参")
public class AnswerListParam {

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "日期_起始时间 格式：yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "日期_结束时间 格式：yyyy-MM-dd")
    private Date endDate;

    @ApiModelProperty(value = "问卷名称(也就是检查项目)")
    private String quName;

    @ApiModelProperty(value = "科室id")
    private String deptId;

}
