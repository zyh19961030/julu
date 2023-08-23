package com.qu.modules.web.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 随访模板表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbFollowVisitTemplateListParam", description="TbFollowVisitTemplateListParam")
public class TbFollowVisitTemplateListVo {

	@ApiModelProperty(value = "id")
	private Integer id;

    @ApiModelProperty(value = "随访计划模版名称")
	private String name;

	@ApiModelProperty(value = "适用疾病")
	private String disease;

    @ApiModelProperty(value = "状态 1-正常 2-停用")
    private Integer status;

	@ApiModelProperty(value = "创建人")
	private String createUser;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "最近修改时间")
	private Date updateTime;




}
