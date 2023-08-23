package com.qu.event;


import com.qu.modules.web.service.IQuestionVersionService;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class QuestionVersionEventListener implements ApplicationListener<QuestionVersionEvent> {

    @Resource
    private IQuestionVersionService questionVersionService;


    @Async
    @Override
    public void onApplicationEvent(QuestionVersionEvent event) {
        Integer quId = event.getQuId();
        questionVersionService.saveQuestionVersion(quId);
    }


}
