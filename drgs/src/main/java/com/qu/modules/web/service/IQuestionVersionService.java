package com.qu.modules.web.service;

import com.qu.modules.web.entity.QuestionVersion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 问卷版本表
 * @Author: jeecg-boot
 * @Date:   2022-09-25
 * @Version: V1.0
 */
public interface IQuestionVersionService extends IService<QuestionVersion> {

    /**
     * 根据问卷id和问卷版本查询
     * @param quId
     * @param questionVersionNumber
     * @return
     */
    QuestionVersion selectByQuestionAndVersion(Integer quId, String questionVersionNumber);

    void saveQuestionVersion(Integer quId);



}
