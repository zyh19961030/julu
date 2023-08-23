package com.qu.modules.web.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="AnswerCheckListRequest", description="AnswerCheckListRequest")
public class AnswerCheckListRequest {

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

    @ApiModelProperty(value = "被检查科室id")
    private String deptId;

    @ApiModelProperty(value = "用户科室id")
    private String userDeptId;

    @ApiModelProperty(value = "创建科室id")
    private String createrDeptId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "检查月份")
    private String checkMonth;

}
