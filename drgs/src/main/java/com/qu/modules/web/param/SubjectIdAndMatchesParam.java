package com.qu.modules.web.param;

import io.swagger.annotations.ApiModelProperty;

public class SubjectIdAndMatchesParam {
    @ApiModelProperty(value = "问题id")
    private Integer subjectId;

    @ApiModelProperty(value = "匹配规则标识")
    private Integer matches;

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getMatches() {
        return matches;
    }

    public void setMatches(Integer matches) {
        this.matches = matches;
    }
}
