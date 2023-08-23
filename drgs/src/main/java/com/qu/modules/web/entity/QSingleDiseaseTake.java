package com.qu.modules.web.entity;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 单病种总表
 * @Author: jeecg-boot
 * @Date:   2021-05-30
 * @Version: V1.0
 */
@Data
@TableName("q_single_disease_take")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="q_single_disease_take对象", description="单病种总表")
public class QSingleDiseaseTake {

	/**id*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "id")
	private java.lang.Integer id;
	/**患者id*/
	@Excel(name = "患者id", width = 15)
	@ApiModelProperty(value = "患者id")
	private java.lang.String patientId;
	/**患者姓名*/
	@Excel(name = "患者姓名", width = 15)
	@ApiModelProperty(value = "患者姓名")
	private java.lang.String patientName;
	/**患者性别*/
	@Excel(name = "患者性别", width = 15)
	@ApiModelProperty(value = "患者性别")
	private java.lang.String patientGender;
	/**年龄*/
	@Excel(name = "年龄", width = 15)
	@ApiModelProperty(value = "年龄")
	private java.lang.String age;
	/**住院编号-病案号*/
	@Excel(name = "住院编号-病案号", width = 15)
	@ApiModelProperty(value = "住院编号-病案号")
	private java.lang.String hospitalInNo;
	/**idc10编码*/
	@Excel(name = "idc10编码", width = 15)
	@ApiModelProperty(value = "idc10编码")
	private java.lang.String icdTen;
	/**疾病名称*/
	@Excel(name = "疾病名称", width = 15)
	@ApiModelProperty(value = "疾病名称")
	private java.lang.String icdName;
	/**状态：0待填报 1填报中 2已填报待审核 3审核失败驳回 4通过上报上中 5已上报待国家审核 6已完成(上报国家成功并通过) 7已被国家驳回 8无需填报*/
	@Excel(name = "状态：0待填报 1填报中 2已填报待审核 3审核失败驳回 4通过上报上中 5已上报待国家审核 6已完成(上报国家成功并通过) 7已被国家驳回 8无需填报", width = 15)
    @ApiModelProperty(value = "状态：0待填报 1填报中 2已填报待审核 3审核失败驳回 4通过上报上中 5已上报待国家审核 6已完成(上报国家成功并通过) 7已被国家驳回 8无需填报")
	private java.lang.Integer status;
	/**住院时间*/
	@ApiModelProperty(value = "住院时间")
	private java.util.Date inTime;
	/**出院时间*/
	@ApiModelProperty(value = "出院时间")
	private java.util.Date outTime;
	/**手术名称*/
	@Excel(name = "手术名称", width = 15)
	@ApiModelProperty(value = "手术名称")
	private java.lang.String operationName;
	/**主治医生名称*/
	@Excel(name = "主治医生名称", width = 15)
	@ApiModelProperty(value = "主治医生名称")
	private java.lang.String doctorName;
	/**主治医生id*/
	@Excel(name = "主治医生id", width = 15)
	@ApiModelProperty(value = "主治医生id")
	private java.lang.String doctorId;
	/**填报表单id*/
	@Excel(name = "填报表单id", width = 15)
	@ApiModelProperty(value = "填报表单id")
	private java.lang.Integer questionId;
	/**填报表单名称*/
	@Excel(name = "填报表单名称", width = 15)
	@ApiModelProperty(value = "填报表单名称")
	private java.lang.String questionName;
	/**是否需要上报 0不需要上报  1需要上报*/
	@Excel(name = "是否需要上报 0不需要上报  1需要上报", width = 15)
	@ApiModelProperty(value = "是否需要上报 0不需要上报  1需要上报")
	private java.lang.Integer reportStatus;
	/**不需要上报原因id*/
	@Excel(name = "不需要上报原因id", width = 15)
	@ApiModelProperty(value = "不需要上报原因id")
	private java.lang.String reportNoReasonId;
	/**不需要上报原因*/
	@Excel(name = "不需要上报原因", width = 15)
	@ApiModelProperty(value = "不需要上报原因")
	private java.lang.String reportNoReason;
	/**不需要上报原因中的备注*/
	@Excel(name = "不需要上报原因中的备注", width = 15)
	@ApiModelProperty(value = "不需要上报原因中的备注")
	private java.lang.String reportNoReasonNote;
	/**医院his科室id*/
	@Excel(name = "医院his科室id", width = 15)
	@ApiModelProperty(value = "医院his科室id")
	private java.lang.String departmentId;
	/**医院his科室名*/
	@Excel(name = "医院his科室名", width = 15)
	@ApiModelProperty(value = "医院his科室名")
	private java.lang.String department;
	/**审核状态  0未审核  1已审核通过 2审核不通过*/
	@Excel(name = "审核状态  0未审核  1已审核通过 2审核不通过", width = 15)
	@ApiModelProperty(value = "审核状态  0未审核  1已审核通过 2审核不通过")
	private java.lang.String examineStatus;
	/**审核不通过原因*/
	@Excel(name = "审核不通过原因", width = 15)
	@ApiModelProperty(value = "审核不通过原因")
	private java.lang.String examineReason;
	/**国家平台驳回原因*/
	@Excel(name = "国家平台驳回原因", width = 15)
	@ApiModelProperty(value = "国家平台驳回原因")
	private java.lang.String countryExamineReason;
	/**图标*/
	@Excel(name = "图标", width = 15)
	@ApiModelProperty(value = "图标")
	private java.lang.String icon;
	/**身份证号*/
	@Excel(name = "身份证号", width = 15)
	@ApiModelProperty(value = "身份证号")
	private java.lang.String idCard;
	/**创建时间*/
	@ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**填写时间*/
	@ApiModelProperty(value = "填写时间")
	private java.util.Date writeTime;
	/**主要诊断*/
	@Excel(name = "主要诊断", width = 15)
	@ApiModelProperty(value = "主要诊断")
	private java.lang.String mainDiagnosis;
	/**提交时间*/
	@Excel(name = "提交时间", width = 15)
	@ApiModelProperty(value = "提交时间")
	private java.util.Date submitTime;
	/**次要诊断*/
	@Excel(name = "次要诊断", width = 15)
	@ApiModelProperty(value = "次要诊断")
	private java.lang.String secondaryDiagnosis;
	/**住院天数*/
	@Excel(name = "住院天数", width = 15)
	@ApiModelProperty(value = "住院天数")
	private java.lang.Integer inHospitalDay;
	/**住院费用 字符串 可以带小数*/
	@Excel(name = "住院费用 字符串 可以带小数", width = 15)
    @ApiModelProperty(value = "住院费用 字符串 可以带小数")
	private java.lang.String inHospitalFee;
	/**药品费用 字符串 可以带小数*/
	@Excel(name = "药品费用 字符串 可以带小数", width = 15)
    @ApiModelProperty(value = "药品费用 字符串 可以带小数")
	private java.lang.String drugFee;
	/**手术治疗费用 字符串 可以带小数*/
	@Excel(name = "手术治疗费用 字符串 可以带小数", width = 15)
    @ApiModelProperty(value = "手术治疗费用 字符串 可以带小数")
	private java.lang.String operationTreatmentFee;
	/**一次性耗材费用 字符串 可以带小数*/
	@Excel(name = "一次性耗材费用 字符串 可以带小数", width = 15)
    @ApiModelProperty(value = "一次性耗材费用 字符串 可以带小数")
	private java.lang.String disposableConsumable;
	/**答案json*/
	@Excel(name = "答案json", width = 15)
	@ApiModelProperty(value = "答案json")
	private java.lang.String answerJson;
	/**状态0:草稿1:已提交*/
	@Excel(name = "状态0:草稿1:已提交", width = 15)
	@ApiModelProperty(value = "状态0:草稿1:已提交")
	private java.lang.Integer answerStatus;
	/**答题人id*/
	@Excel(name = "答题人id", width = 15)
	@ApiModelProperty(value = "答题人id")
	private java.lang.String answer;
	/**答题人姓名*/
	@Excel(name = "答题人姓名", width = 15)
	@ApiModelProperty(value = "答题人姓名")
	private java.lang.String answerName;
	/**答题时间*/
	@Excel(name = "答题时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "答题时间")
	private java.util.Date answerTime;
	/**答题人部门id*/
	@Excel(name = "答题人部门id", width = 15)
	@ApiModelProperty(value = "答题人部门id")
	private java.lang.String answerDeptid;
	/**答题人部门名称*/
	@Excel(name = "答题人部门名称", width = 15)
	@ApiModelProperty(value = "答题人部门名称")
	private java.lang.String answerDeptname;
	/**动态创建表的id-废弃,被summary_mapping_table_id替代*/
	@Excel(name = "动态创建表的id-废弃,被summary_mapping_table_id替代", width = 15)
    @ApiModelProperty(value = "动态创建表的id-废弃,被summary_mapping_table_id替代")
	private java.lang.String tableId;
	/**动态创建表的表名*/
	@Excel(name = "动态创建表的表名", width = 15)
	@ApiModelProperty(value = "动态创建表的表名")
	private java.lang.String dynamicTableName;
	/**上报国家的时间*/
    @ApiModelProperty(value = "上报国家的时间")
	private java.util.Date countryReportTime;
	/**分类id*/
	@Excel(name = "分类id", width = 15)
    @ApiModelProperty(value = "分类id")
	private java.lang.Long categoryId;
	/**tqms的科室id*/
	@Excel(name = "tqms的科室id", width = 15)
    @ApiModelProperty(value = "tqms的科室id")
	private java.lang.String tqmsDept;
	/**tqms科室名字*/
	@Excel(name = "tqms科室名字", width = 15)
    @ApiModelProperty(value = "tqms科室名字")
	private java.lang.String tqmsDeptName;
	/**nlp解析结果的id*/
	@Excel(name = "nlp解析结果的id", width = 15)
    @ApiModelProperty(value = "nlp解析结果的id")
	private java.lang.String nlpAnalysisId;
	/**nlp解析结果表*/
	@Excel(name = "nlp解析结果表", width = 15)
    @ApiModelProperty(value = "nlp解析结果表")
	private java.lang.String nlpAnalysisTable;
	/**对应子表的id，可以当主键，(子表中同字段)*/
	@Excel(name = "对应子表的id，可以当主键，(子表中同字段)", width = 15)
    @ApiModelProperty(value = "对应子表的id，可以当主键，(子表中同字段)")
	private java.lang.String summaryMappingTableId;
	/**离院方式 a 医嘱离院  b 医嘱转院  c 医嘱转社区卫生服务机构/乡镇卫生院 d 非医嘱离院 e 死亡 oth 其他*/
	@Excel(name = "离院方式 a 医嘱离院  b 医嘱转院  c 医嘱转社区卫生服务机构/乡镇卫生院 d 非医嘱离院 e 死亡 oth 其他", width = 15)
    @ApiModelProperty(value = "离院方式 a 医嘱离院  b 医嘱转院  c 医嘱转社区卫生服务机构/乡镇卫生院 d 非医嘱离院 e 死亡 oth 其他")
	private java.lang.String leaveHospitalWay;
	/**是否有手术并发症 0否 1是*/
	@Excel(name = "是否有手术并发症 0否 1是", width = 15)
    @ApiModelProperty(value = "是否有手术并发症 0否 1是")
	private java.lang.String operationComplication;
	/**0:正常1:已删除*/
	@Excel(name = "0:正常1:已删除", width = 15)
	@ApiModelProperty(value = "0:正常1:已删除")
	private Integer del;
	/**问卷版本*/
	@Excel(name = "问卷版本", width = 15)
	@ApiModelProperty(value = "问卷版本")
	private java.lang.String questionVersion;
}
