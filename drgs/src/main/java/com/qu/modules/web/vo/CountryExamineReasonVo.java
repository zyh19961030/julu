package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @auther zhangyihao
 * @date 2021-12-24 14:47
 */
public class CountryExamineReasonVo {

    @ApiModelProperty(value = "题目名称")
    private String subjectName;

    @ApiModelProperty(value = "题目答案")
    private String answer;

    @ApiModelProperty(value = "出错原因")
    private String examineReason;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExamineReason() {
        return examineReason;
    }

    public void setExamineReason(String examineReason) {
        this.examineReason = examineReason;
    }
}
