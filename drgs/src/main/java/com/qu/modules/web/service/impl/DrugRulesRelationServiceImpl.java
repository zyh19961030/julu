package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qu.modules.web.entity.DrugReceiveHis;
import com.qu.modules.web.entity.DrugRulesRelation;
import com.qu.modules.web.mapper.DrugRulesRelationMapper;
import com.qu.modules.web.service.IDrugRulesRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 药品规则关联表
 * @Author: jeecg-boot
 * @Date:   2021-09-10
 * @Version: V1.0
 */
@Service
public class DrugRulesRelationServiceImpl extends ServiceImpl<DrugRulesRelationMapper, DrugRulesRelation> implements IDrugRulesRelationService {

    @Autowired
    DrugRulesRelationMapper drugRulesRelationMapper;

    @Override
    public List<DrugRulesRelation> ifExist(Integer optionId) {
        List<DrugRulesRelation> list = drugRulesRelationMapper.ifExist(optionId);
        return list;
    }

    @Override
    public int delete(Integer optionId) {
        int i = drugRulesRelationMapper.delete(optionId);
        return i;
    }

    @Override
    public List<DrugRulesRelation> queryByOptionId(Integer optionId, Integer subject_type) {
        LambdaQueryWrapper<DrugRulesRelation> lambda = new QueryWrapper<DrugRulesRelation>().lambda();
        lambda.eq(DrugRulesRelation::getDrugRulesOptionId, optionId);
        lambda.eq(DrugRulesRelation::getSubject_type, subject_type);
        List<DrugRulesRelation> list = this.list(lambda);
        return list;
    }
}
