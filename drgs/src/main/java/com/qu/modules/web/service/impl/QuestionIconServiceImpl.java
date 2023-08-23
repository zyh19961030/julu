package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.constant.QuestionIconConstant;
import com.qu.modules.web.entity.QuestionIcon;
import com.qu.modules.web.mapper.QuestionIconMapper;
import com.qu.modules.web.service.IQuestionIconService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 问卷图标表
 * @Author: jeecg-boot
 * @Date:   2022-09-05
 * @Version: V1.0
 */
@Service
public class QuestionIconServiceImpl extends ServiceImpl<QuestionIconMapper, QuestionIcon> implements IQuestionIconService {

    @Override
    public List<QuestionIcon> queryPageList() {
        LambdaQueryWrapper<QuestionIcon> lambda = new QueryWrapper<QuestionIcon>().lambda();
        lambda.eq(QuestionIcon::getType, QuestionIconConstant.TYPE_CHECK).eq(QuestionIcon::getDel,QuestionIconConstant.DEL_NORMAL);
        return this.list(lambda);
    }
}
