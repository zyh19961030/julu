package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qu.modules.web.entity.DrugRulesOption;
import com.qu.modules.web.mapper.DrugRulesOptionMapper;
import com.qu.modules.web.service.IDrugRulesOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

/**
 * @Description: 药品规则答案表
 * @Author: jeecg-boot
 * @Date:   2021-09-12
 * @Version: V1.0
 */
@Service
public class DrugRulesOptionServiceImpl extends ServiceImpl<DrugRulesOptionMapper, DrugRulesOption> implements IDrugRulesOptionService {

    @Autowired
    DrugRulesOptionMapper drugRulesOptionMapper;

    @Override
    public List<DrugRulesOption> queryOption(Integer subjectId) {
        LambdaQueryWrapper<DrugRulesOption> lambda = new QueryWrapper<DrugRulesOption>().lambda();
        lambda.eq(DrugRulesOption::getDrugRulesSubjectId, subjectId);
        lambda.eq(DrugRulesOption::getDel, 0);
        List<DrugRulesOption> list = this.list(lambda);
        return list;
    }

    @Override
    public Integer queryOptionIdById(Integer id) {
        Integer optionId = drugRulesOptionMapper.queryOptionIdById(id);
        return optionId;
    }

    @Override
    public int updateOptionBySubjectId(Integer subjectId, Date date) {
        return drugRulesOptionMapper.updateOptionBySubjectId(subjectId, date);
    }

    @Override
    public Integer querySubjectIdById(Integer id) {
        Integer subjectId = drugRulesOptionMapper.querySubjectIdById(id);
        return subjectId;
    }
}
