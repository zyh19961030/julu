package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.qu.constant.Constant;
import com.qu.modules.web.entity.SingleEnterQuestionColumn;
import com.qu.modules.web.mapper.SingleEnterQuestionColumnMapper;
import com.qu.modules.web.service.ISingleEnterQuestionColumnService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 录入表单展示列表
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Service
public class SingleEnterQuestionColumnServiceImpl extends ServiceImpl<SingleEnterQuestionColumnMapper, SingleEnterQuestionColumn> implements ISingleEnterQuestionColumnService {

    @Override
    public List<SingleEnterQuestionColumn> selectBySingleEnterQuestionId(Integer singleEnterQuestionId) {
        if(singleEnterQuestionId==null){
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<SingleEnterQuestionColumn> lambda = new QueryWrapper<SingleEnterQuestionColumn>().lambda();
        lambda.eq(SingleEnterQuestionColumn::getEnterQuestionId,singleEnterQuestionId);
        lambda.in(SingleEnterQuestionColumn::getDel, Constant.DEL_NORMAL);
        return this.list(lambda);
    }
}
