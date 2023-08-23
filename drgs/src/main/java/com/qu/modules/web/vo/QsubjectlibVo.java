package com.qu.modules.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qu.modules.web.entity.Qoptionlib;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@ApiModel(value="SubjectlibVo", description="SubjectlibVo")
public class QsubjectlibVo {
    @Excel(name = "主键", width = 15)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
    @Excel(name = "题目名称", width = 15)
    @ApiModelProperty(value = "题目名称")
    private java.lang.String subName;
    /**题目类型 1.单选   2.多选   3.日期   4.时间  5.下拉框    6.单行文本   7.多行文本  8.分组框  9.提示标题*/
    @Excel(name = "题目类型 1.单选   2.多选   3.日期   4.时间  5.下拉框    6.单行文本   7.多行文本  8.分组框  9.提示标题", width = 15)
    @ApiModelProperty(value = "题目类型 1.单选   2.多选   3.日期   4.时间  5.下拉框    6.单行文本   7.多行文本  8.分组框  9.提示标题")
    private java.lang.String subType;
    /**是否为必填 0:非必填 1:必填*/
    @Excel(name = "是否为必填 0:非必填 1:必填", width = 15)
    @ApiModelProperty(value = "是否为必填 0:非必填 1:必填")
    private java.lang.Integer required;
    /**默认是否显示 0:不显示 1:显示*/
    @Excel(name = "默认是否显示 0:不显示 1:显示", width = 15)
    @ApiModelProperty(value = "默认是否显示 0:不显示 1:显示")
    private java.lang.Integer display;
    /**最多字数限制*/
    @Excel(name = "最多字数限制", width = 15)
    @ApiModelProperty(value = "最多字数限制")
    private java.lang.Integer limitWords;
    /**文本框校验 num:数字 email:电子邮箱 ch:中文 en:英文 idcard:身份证号码 qqno:QQ号 phone:手机号码(仅支持大陆地区) tel:电话号码*/
    @Excel(name = "文本框校验 num:数字 email:电子邮箱 ch:中文 en:英文 idcard:身份证号码 qqno:QQ号 phone:手机号码(仅支持大陆地区) tel:电话号码", width = 15)
    @ApiModelProperty(value = "文本框校验 num:数字 email:电子邮箱 ch:中文 en:英文 idcard:身份证号码 qqno:QQ号 phone:手机号码(仅支持大陆地区) tel:电话号码")
    private java.lang.String textCheck;
    /**多行文本框高度*/
    @Excel(name = "多行文本框高度", width = 15)
    @ApiModelProperty(value = "多行文本框高度")
    private java.lang.Integer textHight;
    /**时间是否为区间 0:非区间 1:区间*/
    @Excel(name = "时间是否为区间 0:非区间 1:区间", width = 15)
    @ApiModelProperty(value = "时间是否为区间 0:非区间 1:区间")
    private java.lang.Integer section;
    /**默认值*/
    @Excel(name = "默认值", width = 15)
    @ApiModelProperty(value = "默认值")
    private java.lang.String defValue;
    /**时间显示方式 0:时分 1:时分秒*/
    @Excel(name = "时间显示方式 0:时分 1:时分秒", width = 15)
    @ApiModelProperty(value = "时间显示方式 0:时分 1:时分秒")
    private java.lang.Integer defDisplay;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;

    @ApiModelProperty(value = "数据库列名")
    private java.lang.String columnName;
    @ApiModelProperty(value = "创建数据库列名的数据类型")
    private java.lang.String columnType;

    /**0:正常1:已删除*/
    @Excel(name = "0:正常1:已删除", width = 15)
    @ApiModelProperty(value = "0:正常1:已删除")
    private java.lang.Integer del;
    /**创建者*/
    @Excel(name = "创建者", width = 15)
    @ApiModelProperty(value = "创建者")
    private java.lang.Integer creater;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**更新者*/
    @Excel(name = "更新者", width = 15)
    @ApiModelProperty(value = "更新者")
    private java.lang.Integer updater;
    /**更新时间*/
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;

    @ApiModelProperty(value = "选项")
    private List<Qoptionlib> qoptionlibList;
}
