package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "题库编辑Param", description = "题库编辑Param")
public class QsubjectlibEditParam {
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
    @ApiModelProperty(value = "题目名称")
    private String subName;
    @ApiModelProperty(value = "题目类型 1.单选   2.多选   3.日期   4.时间  5.下拉框    6.单行文本   7.多行文本  8.分组框  9.提示标题")
    private String subType;
    @ApiModelProperty(value = "是否为必填 0:非必填 1:必填")
    private Integer required;
    @ApiModelProperty(value = "默认是否显示 0:不显示 1:显示")
    private Integer display;
    @ApiModelProperty(value = "最多字数限制")
    private Integer limitWords;
    @ApiModelProperty(value = "文本框校验 num:数字 email:电子邮箱 ch:中文 en:英文 idcard:身份证号码 qqno:QQ号 phone:手机号码(仅支持大陆地区) tel:电话号码")
    private String textCheck;
    @ApiModelProperty(value = "多行文本框高度")
    private Integer textHight;
    @ApiModelProperty(value = "时间是否为区间 0:非区间 1:区间")
    private Integer section;
    @ApiModelProperty(value = "默认值")
    private String defValue;
    @ApiModelProperty(value = "时间显示方式 0:时分 1:时分秒")
    private Integer defDisplay;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "数据库列名")
    private java.lang.String columnName;
    @ApiModelProperty(value = "创建数据库列名的数据类型")
    private java.lang.String columnType;

    @ApiModelProperty(value = "选项")
    private List<QoptionlibParam> qoptionlibParamList;

    @ApiModelProperty(value = "删除的选项id集合")
    private List<Integer>  qoptionlibDeleteList;
}
