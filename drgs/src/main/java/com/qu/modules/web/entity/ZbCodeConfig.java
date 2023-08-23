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

/**
 * @Description: 指标code配置表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Data
@TableName("zb_code_config")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="zb_code_config对象", description="指标code配置表")
public class ZbCodeConfig {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**编码（诊断编码或者手术编码）*/
	@Excel(name = "编码（诊断编码或者手术编码）", width = 15)
    @ApiModelProperty(value = "编码（诊断编码或者手术编码）")
	private String code;
	/**名称（诊断名称或者手术名称）*/
	@Excel(name = "名称（诊断名称或者手术名称）", width = 15)
    @ApiModelProperty(value = "名称（诊断名称或者手术名称）")
	private String name;
	/**1-诊断编码 2-手术及操作编码*/
	@Excel(name = "1-诊断编码 2-手术及操作编码", width = 15)
    @ApiModelProperty(value = "1-诊断编码 2-手术及操作编码")
	private String lx;
	/**指标编号*/
	@Excel(name = "指标编号", width = 15)
    @ApiModelProperty(value = "指标编号")
	private String zbcode;
	/**分组*/
	@Excel(name = "分组", width = 15)
    @ApiModelProperty(value = "分组")
	private String zbgroup;
	/**分组名称*/
	@Excel(name = "分组名称", width = 15)
    @ApiModelProperty(value = "分组名称")
	private String zbgroupname;
	/**1-编码对应的是指标分母 2-编码对应的是指标分子 3-指标分组*/
	@Excel(name = "1-编码对应的是指标分母 2-编码对应的是指标分子 3-指标分组", width = 15)
    @ApiModelProperty(value = "1-编码对应的是指标分母 2-编码对应的是指标分子 3-指标分组")
	private String zblx;
}
