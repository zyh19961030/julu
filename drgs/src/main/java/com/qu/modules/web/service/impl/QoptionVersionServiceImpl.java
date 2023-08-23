package com.qu.modules.web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.constant.QoptionConstant;
import com.qu.modules.web.entity.QoptionVersion;
import com.qu.modules.web.mapper.QoptionVersionMapper;
import com.qu.modules.web.service.IQoptionVersionService;

/**
 * @Description: 选项版本表
 * @Author: jeecg-boot
 * @Date:   2022-09-25
 * @Version: V1.0
 */
@Service
public class QoptionVersionServiceImpl extends ServiceImpl<QoptionVersionMapper, QoptionVersion> implements IQoptionVersionService {

    @Override
    public List<QoptionVersion> selectOptionVersionByQuIdAndVersion(String questionVersionId, List<Integer> subjectVersionIdList) {
        LambdaQueryWrapper<QoptionVersion> lambda = new QueryWrapper<QoptionVersion>().lambda();
        lambda.in(QoptionVersion::getSubjectId,subjectVersionIdList);
        lambda.eq(QoptionVersion::getQuestionVersionId, questionVersionId);
        lambda.eq(QoptionVersion::getDel, QoptionConstant.DEL_NORMAL);
        lambda.orderByAsc(QoptionVersion::getOpOrder);
        return this.list(lambda);
    }
}
