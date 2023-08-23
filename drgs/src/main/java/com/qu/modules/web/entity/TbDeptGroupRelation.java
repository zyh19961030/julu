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
 * @Description: 分组管理与科室关联表
 * @Author: jeecg-boot
 * @Date:   2022-09-19
 * @Version: V1.0
 */
@Data
@TableName("tb_dept_group_relation")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tb_dept_group_relation对象", description="分组管理与科室关联表")
public class TbDeptGroupRelation {
    
	/**分组表id*/
	@Excel(name = "分组表id", width = 15)
    @ApiModelProperty(value = "分组表id")
	private String groupId;
	/**科室id*/
	@Excel(name = "科室id", width = 15)
    @ApiModelProperty(value = "科室id")
	private String deptId;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
}
