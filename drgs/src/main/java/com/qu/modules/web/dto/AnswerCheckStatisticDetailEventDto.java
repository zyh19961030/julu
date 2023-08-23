package com.qu.modules.web.dto;

import com.qu.modules.web.entity.AnswerCheck;
import com.qu.modules.web.entity.Question;
import com.qu.modules.web.vo.SubjectVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "AnswerCheckStatisticDetailEventDto", description = "AnswerCheckStatisticDetailEventDto")
public class AnswerCheckStatisticDetailEventDto {

    private Question question;
    private List<SubjectVo> subjectList;
    private Map<String, String> mapCache;
    private AnswerCheck answerCheck;
    private String answerUser;
    private String answerUserName;
    private String depId;
    private String depName;
    private Integer source;

}
