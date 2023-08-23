package com.qu.modules.web.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="QuestionListParam", description="QuestionListParam")
public class QuestionListParam {
    /**问卷名称*/
    @ApiModelProperty(value = "问卷名称")
    private String quName;

    /**一级分类 0其他 1单病种 2检查表 3登记表*/
    @ApiModelProperty(value = "一级分类 0其他 1单病种 2检查表 3登记表")
    private java.lang.Integer categoryType;

    /**二级分类id*/
    @ApiModelProperty(value = "二级分类id")
    private java.lang.String categoryId;

    /**填报频次 0患者登记表 1月度汇总表 2季度汇总表 3年度汇总表*/
    @ApiModelProperty(value = "填报频次 0患者登记表 1月度汇总表 2季度汇总表 3年度汇总表")
    private java.lang.Integer writeFrequency;

    /**填报科室id*/
    @ApiModelProperty(value = "填报科室id")
    private java.lang.String writeDeptId;

    /**查看科室id*/
    @ApiModelProperty(value = "查看科室id")
    private java.lang.String seeDeptId;

    /**被检查科室id*/
    @ApiModelProperty(value = "被检查科室id")
    private java.lang.String checkedDeptId;

    /**是否发布 0:草稿箱 1:已发布*/
    @ApiModelProperty(value = "是否发布 0:草稿箱 1:已发布")
    private java.lang.Integer quStatus;


}
