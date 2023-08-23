package com.qu.modules.web.param;

import com.qu.modules.web.vo.PurposeAndActionVo;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class DrugRulesRelationsListParam {
    @ApiModelProperty(value = "答案id")
    private Integer optionId;

    @ApiModelProperty(value = "用药目的和物理作用id集合")
    private List<PurposeAndActionVo> purposeAndActionVos;

    @ApiModelProperty(value = "区分标记")
    private int type;

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public List<PurposeAndActionVo> getPurposeAndActionVos() {
        return purposeAndActionVos;
    }

    public void setPurposeAndActionVos(List<PurposeAndActionVo> purposeAndActionVos) {
        this.purposeAndActionVos = purposeAndActionVos;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
