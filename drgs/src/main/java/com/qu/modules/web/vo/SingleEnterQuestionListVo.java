package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 录入表单表
 * @Author: jeecg-boot
 * @Date: 2023-05-24
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SingleEnterQuestionListVo", description = "SingleEnterQuestionListVo")
public class SingleEnterQuestionListVo {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "查检表id")
    private Integer questionId;

    @ApiModelProperty(value = "查检表名称")
    private String questionName;

    @ApiModelProperty(value = "录入表单名称")
    private String name;

    @ApiModelProperty(value = "分类名称")
    private String questionNameCategoryName;

    @ApiModelProperty(value = "需填报题目一")
    private String subjectNameOne;

    @ApiModelProperty(value = "需填报题目二")
    private String subjectNameTwo;

    @ApiModelProperty(value = "需填报题目三")
    private String subjectNameThree;

}
