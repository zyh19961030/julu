package com.qu.modules.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.QSingleDiseaseTake;
import com.qu.modules.web.entity.TbDep;
import com.qu.modules.web.param.*;
import com.qu.modules.web.vo.*;

import java.util.List;
import java.util.Map;


public interface IQSingleDiseaseTakeService extends IService<QSingleDiseaseTake> {

    void getGetdzbxxInsertDrgs();

}
