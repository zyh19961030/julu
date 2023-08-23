package com.qu.modules.web.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 数据字典表
 * @Author: jeecg-boot
 * @Date:   2022-09-30
 * @Version: V1.0
 */
@Data
@TableName("tb_data")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tb_data对象", description="数据字典表")
public class TbData {
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键ID")
	private String id;
	/**数据编码*/
	@Excel(name = "数据编码", width = 15)
    @ApiModelProperty(value = "数据编码")
	private String code;
	/**创建者*/
	@Excel(name = "创建者", width = 15)
    @ApiModelProperty(value = "创建者")
	private String creator;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createtime;
	/**数据类型*/
	@Excel(name = "数据类型", width = 15)
    @ApiModelProperty(value = "数据类型")
	private String datatype;
	/**key值*/
	@Excel(name = "key值", width = 15)
    @ApiModelProperty(value = "key值")
	private String keyvalue;
	/**父级ID*/
	@Excel(name = "父级ID", width = 15)
    @ApiModelProperty(value = "父级ID")
	private String parientid;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
	private String status;
	/**修改者*/
	@Excel(name = "修改者", width = 15)
    @ApiModelProperty(value = "修改者")
	private String updator;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private Date updatetime;
	/**值*/
	@Excel(name = "值", width = 15)
    @ApiModelProperty(value = "值")
	private String value;
}
