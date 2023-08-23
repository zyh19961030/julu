package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description: 录入表单表
 * @Author: jeecg-boot
 * @Date: 2023-05-24
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SingleEnterQuestionInfoVo", description = "SingleEnterQuestionInfoVo")
public class SingleEnterQuestionInfoVo {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "查检表id")
    private Integer questionId;

    @ApiModelProperty(value = "查检表名称")
    private String questionName;

    @ApiModelProperty(value = "录入表单名称")
    private String name;

    @ApiModelProperty(value = "筛选题目id")
    private Integer selectSubjectId;

//    @ApiModelProperty(value = "分类名称")
//    private String questionNameCategoryName;

    @ApiModelProperty(value = "展示列")
    private List<SingleEnterQuestionInfoSubjectVo> columnList;

    @ApiModelProperty(value = "填报题目")
    private List<SingleEnterQuestionInfoSubjectVo> subjectList;

}
