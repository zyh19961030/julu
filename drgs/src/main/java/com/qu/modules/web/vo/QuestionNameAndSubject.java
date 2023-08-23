package com.qu.modules.web.vo;

import com.qu.modules.web.entity.DrugRulesSubject;
import io.swagger.annotations.ApiModelProperty;

public class QuestionNameAndSubject {

    @ApiModelProperty(value = "药品规则问卷id")
    private Integer id;

    @ApiModelProperty(value = "问卷名称")
    private String qu_name;

    @ApiModelProperty(value = "问题类")
    private DrugRulesSubject drugRulesSubject;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQu_name() {
        return qu_name;
    }

    public void setQu_name(String qu_name) {
        this.qu_name = qu_name;
    }

    public DrugRulesSubject getDrugRulesSubject() {
        return drugRulesSubject;
    }

    public void setDrugRulesSubject(DrugRulesSubject drugRulesSubject) {
        this.drugRulesSubject = drugRulesSubject;
    }
}
