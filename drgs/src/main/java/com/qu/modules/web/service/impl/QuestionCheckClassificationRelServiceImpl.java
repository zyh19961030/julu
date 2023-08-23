package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.modules.web.entity.QuestionCheckClassificationRel;
import com.qu.modules.web.mapper.QuestionCheckClassificationRelMapper;
import com.qu.modules.web.service.IQuestionCheckClassificationRelService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 查检表分类与查检表关联表
 * @Author: jeecg-boot
 * @Date:   2023-04-27
 * @Version: V1.0
 */
@Service
public class QuestionCheckClassificationRelServiceImpl extends ServiceImpl<QuestionCheckClassificationRelMapper, QuestionCheckClassificationRel> implements IQuestionCheckClassificationRelService {

    @Override
    public List<QuestionCheckClassificationRel> selectByQuestionCheckClassification(List<Integer> questionCheckClassificationIdList) {
        LambdaQueryWrapper<QuestionCheckClassificationRel> lambda = new QueryWrapper<QuestionCheckClassificationRel>().lambda();
        lambda.in(QuestionCheckClassificationRel::getQuestionCheckClassificationId,questionCheckClassificationIdList);
        return this.list(lambda);
    }

    @Override
    public List<QuestionCheckClassificationRel> selectByQuestionCheckClassificationAndQuestionIdList(List<Integer> questionIdList, List<Integer> questionCheckClassificationIdList) {
        LambdaQueryWrapper<QuestionCheckClassificationRel> lambda = new QueryWrapper<QuestionCheckClassificationRel>().lambda();
        lambda.in(QuestionCheckClassificationRel::getQuestionId,questionIdList);
        lambda.in(QuestionCheckClassificationRel::getQuestionCheckClassificationId,questionCheckClassificationIdList);
        return this.list(lambda);
    }
}
