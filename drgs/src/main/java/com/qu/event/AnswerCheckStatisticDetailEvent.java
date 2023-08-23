package com.qu.event;

import com.qu.modules.web.dto.AnswerCheckStatisticDetailEventDto;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class AnswerCheckStatisticDetailEvent extends ApplicationEvent {

    private AnswerCheckStatisticDetailEventDto answerCheckStatisticDetailEventDto;

    public AnswerCheckStatisticDetailEvent(Object source, AnswerCheckStatisticDetailEventDto answerCheckStatisticDetailEventDto) {
        super(source);
        this.answerCheckStatisticDetailEventDto = answerCheckStatisticDetailEventDto;
    }

}
