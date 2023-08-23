package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @Description: 随访模板表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbFollowVisitPatientRecordAnswerAfterParam", description="TbFollowVisitPatientRecordAnswerAfterParam")
public class TbFollowVisitPatientRecordAnswerAfterParam {

    @NotNull(message = "答案id不能为空")
    @ApiModelProperty(value = "本次记录的id")
    private Integer id;

    @NotNull(message = "答案id不能为空")
    @ApiModelProperty(value = "答案id")
    private Integer answerId;

}
