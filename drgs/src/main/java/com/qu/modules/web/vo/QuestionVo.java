package com.qu.modules.web.vo;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "问卷Vo", description = "问卷Vo")
public class QuestionVo {
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
    /**
     * 问卷名称
     */
    @ApiModelProperty(value = "问卷名称")
    private java.lang.String quName;
    /**
     * 问卷描述
     */
    @ApiModelProperty(value = "问卷描述")
    private java.lang.String quDesc;
    /**
     * 0:草稿箱 1:已发布
     */
    @ApiModelProperty(value = "0:草稿箱 1:已发布")
    private java.lang.Integer quStatus;
    /**
     * 0:正常1:已停用
     */
    @ApiModelProperty(value = "0:正常1:已停用")
    private java.lang.Integer quStop;
    /**
     * 科室查看权限，科室id逗号分割
     */
    @ApiModelProperty(value = "科室查看权限，科室id逗号分割")
    private java.lang.String deptIds;
    /**
     * 答案对应数据库名
     */
    @ApiModelProperty(value = "答案对应数据库名")
    private java.lang.String tableName;
    /**
     * 0:正常1:已删除
     */
    @ApiModelProperty(value = "0:正常1:已删除")
    private java.lang.Integer del;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private java.lang.Integer creater;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    private java.lang.Integer updater;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;

    /**分类id*/
    @ApiModelProperty(value = "分类id")
    private java.lang.String categoryId;

    /**0其他 1单病种 2检查表 3登记表*/
    @ApiModelProperty(value = "0其他 1单病种 2检查表 3登记表")
    private java.lang.Integer categoryType;

    /**填报频次 0患者登记表 1月度汇总表 2季度汇总表 3年度汇总表*/
    @ApiModelProperty(value = "填报频次 0患者登记表 1月度汇总表 2季度汇总表 3年度汇总表")
    private java.lang.Integer writeFrequency;

    @ApiModelProperty(value = "题目")
    private List<SubjectVo> subjectVoList;

    @ApiModelProperty(value = "当前问卷版本号")
    private java.lang.String questionVersion;

    @ApiModelProperty(value = "数据当前问卷版本号")
    private java.lang.String questionVersionData;

}
