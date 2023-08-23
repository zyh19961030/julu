package com.qu.modules.web.param;

import com.qu.modules.web.entity.DrugRulesQuestion;
import com.qu.modules.web.entity.DrugRulesSubject;
import io.swagger.annotations.ApiModelProperty;

public class QuestionAndSubjectParam {
    @ApiModelProperty(value = "问卷")
    private DrugRulesQuestion drugRulesQuestion;

    @ApiModelProperty(value = "问题")
    private DrugRulesSubject drugRulesSubject;

    public DrugRulesQuestion getDrugRulesQuestion() {
        return drugRulesQuestion;
    }

    public void setDrugRulesQuestion(DrugRulesQuestion drugRulesQuestion) {
        this.drugRulesQuestion = drugRulesQuestion;
    }

    public DrugRulesSubject getDrugRulesSubject() {
        return drugRulesSubject;
    }

    public void setDrugRulesSubject(DrugRulesSubject drugRulesSubject) {
        this.drugRulesSubject = drugRulesSubject;
    }
}
