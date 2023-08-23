package com.qu.modules.web.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 问卷表
 * @Author: jeecg-boot
 * @Date:   2021-05-10
 * @Version: V1.0
 */
@Data
@TableName("question")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="QuestionCheckClassificationVo", description="QuestionCheckClassificationVo")
public class QuestionCheckClassificationVo {

	@ApiModelProperty(value = "类型 1分类 2查检表")
	private Integer type;

	@ApiModelProperty(value = "分类id")
	private Integer questionCheckClassificationId;

	@ApiModelProperty(value = "分类名称")
	private String questionCheckClassificationName;

	@ApiModelProperty(value = "问卷id")
	private Integer quId;

	@ApiModelProperty(value = "问卷名称")
	private String quName;

	@ApiModelProperty(value = "问卷图标")
	private String icon;

}
