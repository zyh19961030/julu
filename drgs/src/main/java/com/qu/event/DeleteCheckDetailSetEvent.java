package com.qu.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class DeleteCheckDetailSetEvent extends ApplicationEvent {

    private Integer quId;


    public DeleteCheckDetailSetEvent(Object source, Integer quId) {
        super(source);
        this.quId = quId;
    }

}
