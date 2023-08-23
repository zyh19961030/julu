package com.qu.modules.web.param;

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
@ApiModel(value="TbFollowVisitPatientRecordListParam", description="TbFollowVisitPatientRecordListParam")
public class TbFollowVisitPatientRecordListParam {


    @ApiModelProperty(value = "随访计划模版名称")
	private String name;

	@ApiModelProperty(value = "对应任务开始月份_起始 格式：yyyy-MM")
	private String startTime;

	@ApiModelProperty(value = "对应任务开始月份_结束 格式：yyyy-MM")
	private String endTime;

    @ApiModelProperty(value = "随访患者姓名")
    private String patientName;

    @ApiModelProperty(value = "状态 -1全部 1待填报 2已填报 ")
    private Integer status;



}
