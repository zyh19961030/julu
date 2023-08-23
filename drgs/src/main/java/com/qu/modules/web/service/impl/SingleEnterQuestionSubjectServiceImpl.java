package com.qu.modules.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.qu.constant.Constant;
import com.qu.modules.web.entity.SingleEnterQuestionSubject;
import com.qu.modules.web.mapper.SingleEnterQuestionSubjectMapper;
import com.qu.modules.web.service.ISingleEnterQuestionSubjectService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 录入表单填报题目表
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Service
public class SingleEnterQuestionSubjectServiceImpl extends ServiceImpl<SingleEnterQuestionSubjectMapper, SingleEnterQuestionSubject> implements ISingleEnterQuestionSubjectService {

    @Override
    public List<SingleEnterQuestionSubject> selectBySingleEnterQuestionIdList(List<Integer> singleEnterQuestionIdList) {
        if(CollectionUtil.isEmpty(singleEnterQuestionIdList)){
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<SingleEnterQuestionSubject> lambda = new QueryWrapper<SingleEnterQuestionSubject>().lambda();
        lambda.in(SingleEnterQuestionSubject::getEnterQuestionId,singleEnterQuestionIdList);
        lambda.in(SingleEnterQuestionSubject::getDel, Constant.DEL_NORMAL);
        return this.list(lambda);
    }

    @Override
    public List<SingleEnterQuestionSubject> selectBySingleEnterQuestionId(Integer singleEnterQuestionId) {
        if(singleEnterQuestionId==null){
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<SingleEnterQuestionSubject> lambda = new QueryWrapper<SingleEnterQuestionSubject>().lambda();
        lambda.eq(SingleEnterQuestionSubject::getEnterQuestionId,singleEnterQuestionId);
        lambda.in(SingleEnterQuestionSubject::getDel, Constant.DEL_NORMAL);
        return this.list(lambda);
    }
}
