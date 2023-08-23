package com.qu.modules.web.service;

import com.qu.modules.web.entity.QuestionTemp;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.param.IdParam;
import org.jeecg.common.api.vo.Result;

/**
 * @Description: 问卷临时表
 * @Author: jeecg-boot
 * @Date:   2022-10-22
 * @Version: V1.0
 */
public interface IQuestionTempService extends IService<QuestionTemp> {

    Result<?> copyTemp(IdParam idParam);

}
