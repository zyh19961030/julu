package com.qu.modules.web.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

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
@ApiModel(value="QuestionCheckClassificationListVo", description="QuestionCheckClassificationListVo")
public class QuestionCheckClassificationListVo {
    
	/**主键*/
    @ApiModelProperty(value = "分类id")
	private Integer id;
//	/**用户Id*/
//    @ApiModelProperty(value = "用户Id")
//	private String userId;
	/**分类名称*/
    @ApiModelProperty(value = "分类名称")
	private String name;

	@ApiModelProperty(value = "分类里的查检表")
	private List<ClassificationQuestionVo> questionVoList;

}
