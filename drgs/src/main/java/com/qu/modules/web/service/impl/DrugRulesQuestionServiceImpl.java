package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qu.modules.web.entity.DrugRulesQuestion;
import com.qu.modules.web.mapper.DrugRulesQuestionMapper;
import com.qu.modules.web.service.IDrugRulesQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 药品规则问卷表
 * @Author: jeecg-boot
 * @Date:   2021-09-12
 * @Version: V1.0
 */
@Service
public class DrugRulesQuestionServiceImpl extends ServiceImpl<DrugRulesQuestionMapper, DrugRulesQuestion> implements IDrugRulesQuestionService {

    @Autowired
    DrugRulesQuestionMapper drugRulesQuestionMapper;

    @Override
    public List<DrugRulesQuestion> queryQuestion(String name) {
        List<DrugRulesQuestion> list = new ArrayList<>();
        LambdaQueryWrapper<DrugRulesQuestion> lambda = new QueryWrapper<DrugRulesQuestion>().lambda();
        lambda.eq(DrugRulesQuestion::getDel, 0);
        list = this.list(lambda);
        return list;
    }

    @Override
    public List<DrugRulesQuestion> queryQuestionIfDelById(Integer id) {
        LambdaQueryWrapper<DrugRulesQuestion> lambda = new QueryWrapper<DrugRulesQuestion>().lambda();
        lambda.eq(DrugRulesQuestion::getQuestionId, id);
        lambda.eq(DrugRulesQuestion::getDel, 0);
        List<DrugRulesQuestion> list = this.list(lambda);
        return list;
    }

    @Override
    public List<DrugRulesQuestion> queryQuestionIfExistById(Integer id) {
        LambdaQueryWrapper<DrugRulesQuestion> lambda = new QueryWrapper<DrugRulesQuestion>().lambda();
        lambda.eq(DrugRulesQuestion::getQuestionId, id);
        List<DrugRulesQuestion> list = this.list(lambda);
        return list;
    }

    @Override
    public int updateQuestion(Integer questionId, int del, Date updateTime) {
        int i = drugRulesQuestionMapper.updateQuestion(questionId, del, updateTime);
        return i;
    }

    @Override
    public DrugRulesQuestion queryQuestionsById(Integer id) {
        DrugRulesQuestion drugRulesQuestion = drugRulesQuestionMapper.queryQuestionById(id);
        return drugRulesQuestion;
    }

    @Override
    public List<DrugRulesQuestion> queryById(Integer id) {
        LambdaQueryWrapper<DrugRulesQuestion> lambda = new QueryWrapper<DrugRulesQuestion>().lambda();
        lambda.eq(DrugRulesQuestion::getId, id);
        lambda.eq(DrugRulesQuestion::getDel, 0);
        List<DrugRulesQuestion> list = this.list(lambda);
        return list;
    }
}
