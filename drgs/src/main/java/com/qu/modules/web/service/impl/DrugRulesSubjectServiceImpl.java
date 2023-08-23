package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qu.modules.web.entity.DrugRulesQuestion;
import com.qu.modules.web.entity.DrugRulesSubject;
import com.qu.modules.web.mapper.DrugRulesSubjectMapper;
import com.qu.modules.web.service.IDrugRulesSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

/**
 * @Description: 药品规则问题表
 * @Author: jeecg-boot
 * @Date:   2021-09-12
 * @Version: V1.0
 */
@Service
public class DrugRulesSubjectServiceImpl extends ServiceImpl<DrugRulesSubjectMapper, DrugRulesSubject> implements IDrugRulesSubjectService {

    @Autowired
    DrugRulesSubjectMapper drugRulesSubjectMapper;

    @Override
    public List<DrugRulesSubject> querySubject(Integer id) {
        LambdaQueryWrapper<DrugRulesSubject> lambda = new QueryWrapper<DrugRulesSubject>().lambda();
        lambda.eq(DrugRulesSubject::getDrugRulesQuestionId, id);
        lambda.eq(DrugRulesSubject::getDel, 0);
        List<DrugRulesSubject> list = this.list(lambda);
        return list;
    }

    @Override
    public List<DrugRulesSubject> querySubjectByInput(String name) {
        List<DrugRulesSubject> list = drugRulesSubjectMapper.querySubjectByInput(name);
        return list;
    }

    @Override
    public int deleteSubject(Integer id, Date updateTime) {
        int i = drugRulesSubjectMapper.deleteSubject(id, updateTime);
        return i;
    }

    @Override
    public List<DrugRulesSubject> querySubjectByInputAndId(String name, Integer id) {
        List<DrugRulesSubject> list = drugRulesSubjectMapper.querySubjectByInputAndId(name, id);
        return list;
    }

    @Override
    public DrugRulesSubject queryById(Integer id) {
        DrugRulesSubject drugRulesSubject = drugRulesSubjectMapper.queryById(id);
        return drugRulesSubject;
    }

}
