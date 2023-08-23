package com.qu.modules.web.param;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 随访模板周期表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Data
@TableName("tb_follow_visit_template_cycle")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tb_follow_visit_template_cycle对象", description="随访模板周期表")
public class TbFollowVisitTemplateCycleAddParam {


	/**次数*/
	@ApiModelProperty(value = "次数")
	private Integer frequency;

	/**距时间起点的整数*/
	@ApiModelProperty(value = "距时间起点的整数")
	private Integer dateStartNumber;

	/**距时间起点 1天 2周 3月 4年*/
	@ApiModelProperty(value = "距时间起点 1天 2周 3月 4年")
	private Integer dateStartType;

	/**关联内容的问卷标题*/
	@ApiModelProperty(value = "关联内容的问卷标题")
	private Integer questionId;


}
