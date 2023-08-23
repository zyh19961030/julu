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
@ApiModel(value = "CheckQuestionDefectStatisticListVo", description = "CheckQuestionDefectStatisticListVo")
public class CheckQuestionDefectStatisticListVo {

    @ApiModelProperty(value = "问卷id")
    private Integer quId;

    @ApiModelProperty(value = "问卷名称")
    private String quName;

    /**
     * 检查科室id
     */
    @ApiModelProperty(value = "检查科室id")
    private String deptId;

    @ApiModelProperty(value = "检查科室名称")
    private String deptName;
    /**
     * 被检查科室id
     */
    @ApiModelProperty(value = "被检查科室id")
    private String checkedDept;

    @ApiModelProperty(value = "被检查科室名称")
    private String checkedDeptName;
    /**
     * 检查月份_题目中的
     */
    @ApiModelProperty(value = "检查月份_题目中的")
    private String checkMonth;
    /**
     * 总得分
     */
    @ApiModelProperty(value = "总得分")
    private String totalScore;
    /**
     * 缺陷项目总数
     */
    @ApiModelProperty(value = "缺陷项目总数")
    private String totalFault;
    /**
     * 是否合格
     */
    @ApiModelProperty(value = "是否合格")
    private String passStatus;


    @ApiModelProperty(value = "缺陷项目数")
    private Integer defectCount;

    @ApiModelProperty(value = "存在问题")
    private String problem;



}
