package com.qu.modules.web.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 用户表vo
 * </p>
 *
 */
@Data
@ApiModel(value = "QuestionSetLineAllVo", description = "QuestionSetLineAllVo")
public class QuestionSetLineAllVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "账户名称")
    private String userName;
//
//    @ApiModelProperty(value = "手机号")
//    private String phone;
//
//    @ApiModelProperty(value = "所属医院ID（如果为平台的则为空）")
//    private String system;
//
//    @ApiModelProperty(value = "所属科室")
//    private String depId;

}
