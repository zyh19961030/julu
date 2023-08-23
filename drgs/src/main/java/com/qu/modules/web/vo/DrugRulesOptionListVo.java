package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class DrugRulesOptionListVo {
    @ApiModelProperty(value = "答案id")
    private Integer id;

    @ApiModelProperty(value = "答案名称")
    private String name;

    @ApiModelProperty(value = "对应his物理作用")
    private List<String> his;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getHis() {
        return his;
    }

    public void setHis(List<String> his) {
        this.his = his;
    }
}
