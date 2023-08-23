package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author
 */
@Data
@ApiModel(value = "SubjectNameVo", description = "SubjectNameVo")
public class SubjectNameVo {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "题目名称")
    private String subName;
}
