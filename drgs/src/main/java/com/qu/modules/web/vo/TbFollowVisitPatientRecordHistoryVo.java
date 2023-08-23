package com.qu.modules.web.vo;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 随访患者记录表
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
@Data
@TableName("tb_follow_visit_patient_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbFollowVisitPatientRecordHistoryVo", description="TbFollowVisitPatientRecordHistoryVo")
public class TbFollowVisitPatientRecordHistoryVo {

    @ApiModelProperty(value = "id")
	private Integer id;

    @ApiModelProperty(value = "随访次数_第N次随访")
    private Integer followVisitNumber;

    @ApiModelProperty(value = "随访关联内容的问卷id")
    private Integer questionId;

    @ApiModelProperty(value = "答案id")
    private Integer answerId;

//    @ApiModelProperty(value = "状态 1待填报 2已填报 3随访被终止")
//    private Integer status;



}
