package com.qu.modules.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.Getdzbxx;

import java.util.HashMap;
import java.util.List;

public interface GetdzbxxService extends IService<Getdzbxx> {

    List<HashMap<Object, Object>> queryDRGIDList(String time_6, String time_7);
}
