package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "UpdateCheckProjectIdParam", description = "UpdateCheckProjectIdParam")
public class UpdateCheckProjectIdParam {
    @NotNull(message = "问卷id不能为空")
    @ApiModelProperty(value = "问卷id")
    private List<Integer> quId;
    @NotBlank(message = "检查项目id不能为空")
    @ApiModelProperty(value = "检查项目id")
    private String checkProjectId;
    @NotBlank(message = "检查项目类型不能为空")
    @ApiModelProperty(value = "检查项目类型")
    private String checkProjectType;
}
