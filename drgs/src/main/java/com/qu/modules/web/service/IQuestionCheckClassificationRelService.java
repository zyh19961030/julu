package com.qu.modules.web.service;

import com.qu.modules.web.entity.QuestionCheckClassificationRel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 查检表分类与查检表关联表
 * @Author: jeecg-boot
 * @Date:   2023-04-27
 * @Version: V1.0
 */
public interface IQuestionCheckClassificationRelService extends IService<QuestionCheckClassificationRel> {

    List<QuestionCheckClassificationRel> selectByQuestionCheckClassification(List<Integer> questionCheckClassificationIdList);

    List<QuestionCheckClassificationRel> selectByQuestionCheckClassificationAndQuestionIdList(List<Integer> questionIdList, List<Integer> questionCheckClassificationIdList);

}
