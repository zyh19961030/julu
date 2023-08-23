package com.qu.modules.web.vo;

import com.qu.modules.web.entity.DrugRulesQuestion;
import com.qu.modules.web.entity.DrugRulesSubject;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class SearchResultVo {
    @ApiModelProperty(value = "药品规则问卷id")
    private Integer qu_id;

    @ApiModelProperty(value = "问卷id")
    private Integer questionId;

    @ApiModelProperty(value = "问卷名称")
    private String questionName;

    @ApiModelProperty(value = "搜索出所属该问卷的问题集合")
    private List<DrugRulesSubject> drugRulesSubjectList;

    public Integer getQu_id() {
        return qu_id;
    }

    public void setQu_id(Integer qu_id) {
        this.qu_id = qu_id;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public List<DrugRulesSubject> getDrugRulesSubjectList() {
        return drugRulesSubjectList;
    }

    public void setDrugRulesSubjectList(List<DrugRulesSubject> drugRulesSubjectList) {
        this.drugRulesSubjectList = drugRulesSubjectList;
    }
}
