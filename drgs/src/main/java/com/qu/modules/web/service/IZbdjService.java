package com.qu.modules.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.Question;
import org.jeecg.common.api.vo.Result;

public interface IZbdjService extends IService<Question> {
    Result<Boolean> batchGeneerate(String tableName);
}
