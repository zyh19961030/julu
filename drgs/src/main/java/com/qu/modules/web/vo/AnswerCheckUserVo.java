package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 用户表
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AnswerCheckUserVo", description="AnswerCheckUserVo")
public class AnswerCheckUserVo {
    
	/**主键ID*/
    @ApiModelProperty(value = "主键ID")
	private String id;
	/**账户名称*/
    @ApiModelProperty(value = "账户名称")
	private String username;

}
