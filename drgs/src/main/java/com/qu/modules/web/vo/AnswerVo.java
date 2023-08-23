package com.qu.modules.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel(value = "作答Vo", description = "作答Vo")
public class AnswerVo {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "问卷id")
    private Integer quId;
    @ApiModelProperty(value = "问卷名称")
    private java.lang.String quName;
    @ApiModelProperty(value = "问卷描述")
    private java.lang.String quDesc;
    @ApiModelProperty(value = "0:草稿1:已提交")
    private Integer answerStatus;
    @ApiModelProperty(value = "答题人")
    private String creater;

    @ApiModelProperty(value = "答题人姓名")
    private String createrName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "答题时间")
    private Date createTime;

    @ApiModelProperty(value = "答题人部门id")
    private String createrDeptid;
    @ApiModelProperty(value = "答题人部门名称")
    private String createrDeptname;

    /**答题时间-填报时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "答题时间-填报时间")
    private java.util.Date answerTime;
    /**填报的月/季度/年*/
    @ApiModelProperty(value = "填报的月/季度/年")
    private java.lang.String questionAnswerTime;

}
