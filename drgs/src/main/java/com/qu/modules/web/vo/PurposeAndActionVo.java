package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModelProperty;

public class PurposeAndActionVo {
    @ApiModelProperty(value = "用药目的id")
    private Integer medicationPurposeId;

    @ApiModelProperty(value = "物理作用id")
    private Integer drugPhysicalActionId;

    public Integer getMedicationPurposeId() {
        return medicationPurposeId;
    }

    public void setMedicationPurposeId(Integer medicationPurposeId) {
        this.medicationPurposeId = medicationPurposeId;
    }

    public Integer getDrugPhysicalActionId() {
        return drugPhysicalActionId;
    }

    public void setDrugPhysicalActionId(Integer drugPhysicalActionId) {
        this.drugPhysicalActionId = drugPhysicalActionId;
    }
}
