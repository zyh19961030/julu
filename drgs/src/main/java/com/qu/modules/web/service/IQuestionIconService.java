package com.qu.modules.web.service;

import com.qu.modules.web.entity.QuestionIcon;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 问卷图标表
 * @Author: jeecg-boot
 * @Date:   2022-09-05
 * @Version: V1.0
 */
public interface IQuestionIconService extends IService<QuestionIcon> {

    List<QuestionIcon> queryPageList();


}
