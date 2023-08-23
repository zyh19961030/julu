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
 * @Description: 接收his数据表
 * @Author: jeecg-boot
 * @Date:   2021-09-20
 * @Version: V1.0
 */
@Data
@TableName("drug_receive_his")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="drug_receive_his对象", description="接收his数据表")
public class DrugReceiveHis {

	/**主键id*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键id")
	private Integer id;
	/**用药目的或物理作用id*/
	@Excel(name = "用药目的或物理作用id", width = 15)
	@ApiModelProperty(value = "用药目的或物理作用id")
	private Integer purposeOrActionId;
	/**用药目的或物理作用名称*/
	@Excel(name = "用药目的或物理作用名称", width = 15)
	@ApiModelProperty(value = "用药目的或物理作用名称")
	private String purposeOrActionName;
	/**上级id*/
	@Excel(name = "上级id", width = 15)
	@ApiModelProperty(value = "上级id")
	private Integer pid;
}
