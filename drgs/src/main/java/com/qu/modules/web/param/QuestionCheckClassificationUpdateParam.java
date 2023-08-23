package com.qu.modules.web.param;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
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
@ApiModel(value="QuestionCheckClassificationUpdateParam", description="QuestionCheckClassificationUpdateParam")
public class QuestionCheckClassificationUpdateParam {

    @NotBlank(message = "id不能为空")
    @ApiModelProperty(value = "id")
	private Integer id;
    @NotBlank(message = "分类名称不能为空")
    @ApiModelProperty(value = "分类名称")
	private String name;
    @ApiModelProperty(value = "查检表id集合")
	private List<Integer> questionIdList;
}
