package com.qu.modules.web.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 查检表分类表
 * @Author: jeecg-boot
 * @Date:   2023-04-27
 * @Version: V1.0
 */
@Data
@TableName("question_check_classification")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ClassificationQuestionVoVo", description="ClassificationQuestionVoVo")
public class ClassificationQuestionVo {


		@ApiModelProperty(value = "查检表id")
		private Integer quId;

		@ApiModelProperty(value = "查检表名称")
		private String quName;


}
