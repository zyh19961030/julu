package com.qu.modules.web.param;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class TypeAndReceiveHis {
    @ApiModelProperty(value = "区分标识")
    private Integer type;

    @ApiModelProperty(value = "对应his物理作用")
    private List<String> his;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<String> getHis() {
        return his;
    }

    public void setHis(List<String> his) {
        this.his = his;
    }
}
