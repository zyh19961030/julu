package com.qu.modules.web.param;

import io.swagger.annotations.ApiModelProperty;

public class NameAndTypeParam {
    @ApiModelProperty(value = "搜索栏输入的内容")
    private String name;

    @ApiModelProperty(value = "区分标记")
    private Integer type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
