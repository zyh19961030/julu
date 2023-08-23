package com.qu.modules.web.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="AnswerCheckParam入参", description="AnswerCheckParam入参")
public class AnswerCheckListMiniAppParam {

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

    @ApiModelProperty(value = "当前用户登录科室id_小程序专用")
    private String userDeptId;

    @ApiModelProperty(value = "检查人：1本人 2本科室全部")
    private Integer checkUserType;

    @ApiModelProperty(value = "检查月份")
    private String checkMonth;

}
