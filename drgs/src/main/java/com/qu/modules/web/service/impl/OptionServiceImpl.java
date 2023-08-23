package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.constant.QoptionConstant;
import com.qu.modules.web.entity.Qoption;
import com.qu.modules.web.mapper.OptionMapper;
import com.qu.modules.web.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 选项表
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
@Service
public class OptionServiceImpl extends ServiceImpl<OptionMapper, Qoption> implements IOptionService {

    @Autowired
    OptionMapper optionMapper;

    @Override
    public List<Qoption> queryOptionBySubId(Integer subId) {
        List<Qoption> qoptions = optionMapper.selectQoptionBySubId(subId);
        return qoptions;
    }

    @Override
    public List<Qoption> selectBySubjectList(List<Integer> subjectIdList) {
        LambdaQueryWrapper<Qoption> lambda = new QueryWrapper<Qoption>().lambda();
        lambda.in(Qoption::getSubId,subjectIdList);
        lambda.in(Qoption::getDel, QoptionConstant.DEL_NORMAL);
        lambda.orderByAsc(Qoption::getOpOrder);
        return this.list(lambda);
    }
}
