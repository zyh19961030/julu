package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.constant.TbInspectStatsTemplateDepConstant;
import com.qu.modules.web.entity.TbInspectStatsTemplateDep;
import com.qu.modules.web.mapper.TbInspectStatsTemplateDepMapper;
import com.qu.modules.web.service.ITbInspectStatsTemplateDepService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 科室检查统计模板
 * @Author: jeecg-boot
 * @Date:   2022-09-18
 * @Version: V1.0
 */
@Service
public class TbInspectStatsTemplateDepServiceImpl extends ServiceImpl<TbInspectStatsTemplateDepMapper, TbInspectStatsTemplateDep> implements ITbInspectStatsTemplateDepService {


    @Override
    public List<TbInspectStatsTemplateDep> selectByQuestionIds(String deptId, List<String> questionIds) {
        LambdaQueryWrapper<TbInspectStatsTemplateDep> lambda = new QueryWrapper<TbInspectStatsTemplateDep>().lambda();
        lambda.eq(TbInspectStatsTemplateDep::getDeptId, deptId);
        lambda.in(TbInspectStatsTemplateDep::getQuId,questionIds);
        return this.list(lambda);
    }

    @Override
    public List<TbInspectStatsTemplateDep> selectDeptStatisticsByQuestionIds(String deptId, List<String> questionIds) {
        LambdaQueryWrapper<TbInspectStatsTemplateDep> lambda = new QueryWrapper<TbInspectStatsTemplateDep>().lambda();
        lambda.eq(TbInspectStatsTemplateDep::getDeptId, deptId);
        lambda.eq(TbInspectStatsTemplateDep::getType, TbInspectStatsTemplateDepConstant.TYPE_DEPT);
        lambda.in(TbInspectStatsTemplateDep::getQuId,questionIds);
        return this.list(lambda);
    }
}
