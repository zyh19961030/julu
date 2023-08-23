package com.qu.modules.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @Description: 单病种统计院级表
 * @Author: jeecg-boot
 * @Date:   2022-04-21
 * @Version: V1.0
 */
@Data
@TableName("q_single_disease_statistic_hospital")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="q_single_disease_statistic_hospital对象", description="单病种统计院级表")
public class QSingleDiseaseStatisticHospital {

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**年*/
	@Excel(name = "年", width = 15)
    @ApiModelProperty(value = "年")
	private Integer year;
	/**月份*/
	@Excel(name = "月份", width = 15)
    @ApiModelProperty(value = "月份")
	private Integer month;
	/**年月格式 yyyy-MM*/
	@Excel(name = "年月格式 yyyy-MM", width = 15)
    @ApiModelProperty(value = "年月格式 yyyy-MM")
	private String yearMonthRemark;
	/**年月格式 yyyy年MM月*/
	@Excel(name = "年月格式 yyyy年MM月", width = 15)
    @ApiModelProperty(value = "年月格式 yyyy年MM月")
	private String yearMonthTitle;
	/**分类id*/
	@Excel(name = "分类id", width = 15)
    @ApiModelProperty(value = "分类id")
	private String categoryId;
	/**单病种名称*/
	@Excel(name = "单病种名称", width = 15)
    @ApiModelProperty(value = "单病种名称")
	private String singleDiseaseName;
	/**动态创建表的表名*/
	@Excel(name = "动态创建表的表名", width = 15)
    @ApiModelProperty(value = "动态创建表的表名")
	private String dynamicTableName;
	/**需要上报数*/
	@Excel(name = "需要上报数", width = 15)
    @ApiModelProperty(value = "需要上报数")
	private Integer needReportCount;
	/**已上报数*/
	@Excel(name = "已上报数", width = 15)
    @ApiModelProperty(value = "已上报数")
	private Integer completeReportCount;
	/**未上报数*/
	@Excel(name = "未上报数", width = 15)
    @ApiModelProperty(value = "未上报数")
	private Integer notReportCount;
	/**上报国家率*/
	@Excel(name = "上报国家率", width = 15)
    @ApiModelProperty(value = "上报国家率")
	private String completeReportCountryRate;
	/**平均住院天数 字符串 可以带小数*/
	@Excel(name = "平均住院天数 字符串 可以带小数", width = 15)
    @ApiModelProperty(value = "平均住院天数 字符串 可以带小数")
	private java.lang.String averageInHospitalDay;
	/**总和住院天数*/
	@Excel(name = "总和住院天数", width = 15)
    @ApiModelProperty(value = "总和住院天数")
	private java.lang.Integer sumInHospitalDay;
	/**平均住院费用 字符串 可以带小数*/
	@Excel(name = "平均住院费用 字符串 可以带小数", width = 15)
    @ApiModelProperty(value = "平均住院费用 字符串 可以带小数")
	private java.lang.String averageInHospitalFee;
	/**总和住院费用 字符串 可以带小数*/
	@Excel(name = "总和住院费用 字符串 可以带小数", width = 15)
    @ApiModelProperty(value = "总和住院费用 字符串 可以带小数")
	private java.lang.String sumInHospitalFee;
	/**平均药品费用 字符串 可以带小数*/
	@Excel(name = "平均药品费用 字符串 可以带小数", width = 15)
    @ApiModelProperty(value = "平均药品费用 字符串 可以带小数")
	private java.lang.String averageDrugFee;
	/**总和药品费用 字符串 可以带小数*/
	@Excel(name = "总和药品费用 字符串 可以带小数", width = 15)
    @ApiModelProperty(value = "总和药品费用 字符串 可以带小数")
	private java.lang.String sumDrugFee;
	/**平均手术治疗费用 字符串 可以带小数*/
	@Excel(name = "平均手术治疗费用 字符串 可以带小数", width = 15)
    @ApiModelProperty(value = "平均手术治疗费用 字符串 可以带小数")
	private java.lang.String averageOperationTreatmentFee;
	/**总和手术治疗费用 字符串 可以带小数*/
	@Excel(name = "总和手术治疗费用 字符串 可以带小数", width = 15)
    @ApiModelProperty(value = "总和手术治疗费用 字符串 可以带小数")
	private java.lang.String sumOperationTreatmentFee;
	/**平均一次性耗材费用 字符串 可以带小数*/
	@Excel(name = "平均一次性耗材费用 字符串 可以带小数", width = 15)
    @ApiModelProperty(value = "平均一次性耗材费用 字符串 可以带小数")
	private java.lang.String averageDisposableConsumable;
	/**总和一次性耗材费用 字符串 可以带小数*/
	@Excel(name = "总和一次性耗材费用 字符串 可以带小数", width = 15)
    @ApiModelProperty(value = "总和一次性耗材费用 字符串 可以带小数")
	private java.lang.String sumDisposableConsumable;
	/**死亡率*/
	@Excel(name = "死亡率", width = 15)
    @ApiModelProperty(value = "死亡率")
	private java.lang.String mortality;
	/**死亡个数*/
	@Excel(name = "死亡个数", width = 15)
    @ApiModelProperty(value = "死亡个数")
	private java.lang.Integer mortalityCount;
	/**手术并发症发生率*/
	@Excel(name = "手术并发症发生率", width = 15)
    @ApiModelProperty(value = "手术并发症发生率")
	private java.lang.String operationComplicationRate;
	/**手术并发症发生个数*/
	@Excel(name = "手术并发症发生个数", width = 15)
    @ApiModelProperty(value = "手术并发症发生个数")
	private java.lang.Integer operationComplicationRateCount;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
}
