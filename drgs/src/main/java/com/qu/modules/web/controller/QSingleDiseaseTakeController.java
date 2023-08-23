package com.qu.modules.web.controller;

import com.qu.modules.web.service.IQSingleDiseaseTakeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "单病种总表")
@RestController
@RequestMapping("/business/drgs")
public class QSingleDiseaseTakeController {
    @Autowired
    private IQSingleDiseaseTakeService qSingleDiseaseTakeService;


    /**
     * 单病种视图接收
     */
    @AutoLog(value = "单病种视图接收")
    @ApiOperation(value = "单病种视图接收", notes = "单病种视图接收")
    @GetMapping(value = "/getdzbxx")
    public void getdzbxx() {
        qSingleDiseaseTakeService.getGetdzbxxInsertDrgs();
    }

}
