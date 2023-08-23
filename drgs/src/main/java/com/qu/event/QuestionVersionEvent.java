package com.qu.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class QuestionVersionEvent extends ApplicationEvent {

    private Integer quId;


    public QuestionVersionEvent(Object source, Integer quId) {
        super(source);
        this.quId = quId;
    }

}
