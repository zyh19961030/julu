package com.qu.event;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qu.modules.web.entity.CheckDetailSet;
import com.qu.modules.web.service.ICheckDetailSetService;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DeleteCheckDetailSetEventListener implements ApplicationListener<DeleteCheckDetailSetEvent> {

    @Resource
    private ICheckDetailSetService checkDetailSetService;

    @Async
    @Override
    public void onApplicationEvent(DeleteCheckDetailSetEvent event) {
        Integer quId = event.getQuId();
        LambdaQueryWrapper<CheckDetailSet> lambda = new QueryWrapper<CheckDetailSet>().lambda();
        lambda.eq(CheckDetailSet::getQuestionId,quId);
        checkDetailSetService.remove(lambda);
    }
}
