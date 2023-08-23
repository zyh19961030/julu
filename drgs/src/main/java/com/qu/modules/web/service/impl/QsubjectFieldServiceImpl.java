package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.modules.web.entity.QsubjectField;
import com.qu.modules.web.mapper.QsubjectFieldMapper;
import com.qu.modules.web.service.IQsubjectFieldService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 题目字段表
 * @Author: jeecg-boot
 * @Date: 2021-05-10
 * @Version: V1.0
 */
@Service
public class QsubjectFieldServiceImpl extends ServiceImpl<QsubjectFieldMapper, QsubjectField> implements IQsubjectFieldService {
    @Override
    public List<QsubjectField> getBySubjectName(String subjectName) {
        QueryWrapper<QsubjectField> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_name", subjectName);
        List<QsubjectField> qsubjectFields = this.baseMapper.selectList(queryWrapper);
        return qsubjectFields;
    }
}
