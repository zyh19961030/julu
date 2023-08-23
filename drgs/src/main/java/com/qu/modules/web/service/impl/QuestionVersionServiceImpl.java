package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qu.constant.QoptionConstant;
import com.qu.constant.QuestionConstant;
import com.qu.modules.web.entity.*;
import com.qu.modules.web.mapper.QuestionVersionMapper;
import com.qu.modules.web.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Description: 问卷版本表
 * @Author: jeecg-boot
 * @Date:   2022-09-25
 * @Version: V1.0
 */
@Service
public class QuestionVersionServiceImpl extends ServiceImpl<QuestionVersionMapper, QuestionVersion> implements IQuestionVersionService {


    @Lazy
    @Resource
    private IQuestionService questionService;

    @Resource
    private ISubjectService subjectService;

    @Resource
    private IOptionService optionService;

    @Resource
    private IQsubjectVersionService subjectVersionService;

    @Resource
    private IQoptionVersionService optionVersionService;

    @Override
    public QuestionVersion selectByQuestionAndVersion(Integer quId, String questionVersionNumber) {
        LambdaQueryWrapper<QuestionVersion> lambda = new QueryWrapper<QuestionVersion>().lambda();
        lambda.in(QuestionVersion::getQuId,quId);
        lambda.in(QuestionVersion::getQuestionVersion, questionVersionNumber);
        lambda.in(QuestionVersion::getDel, QoptionConstant.DEL_NORMAL);
        List<QuestionVersion> questionVersions = this.list(lambda);
        return questionVersions.isEmpty()?null:questionVersions.get(0);
    }

    @Override
    public void saveQuestionVersion(Integer quId) {
        Question question = questionService.getById(quId);
        if (question == null || QuestionConstant.DEL_DELETED.equals(question.getDel())) {
            return;
        }

        Date date = new Date();
        QuestionVersion questionVersion = new QuestionVersion();
        BeanUtils.copyProperties(question,questionVersion);
        questionVersion.setQuId(quId);
        questionVersion.setCurrentCreateTime(date);
        this.save(questionVersion);
        String questionVersionId = questionVersion.getId();

        List<Qsubject> subjectList = subjectService.selectSubjectByQuId(quId);
        List<QsubjectVersion> qsubjectVersionList = Lists.newArrayList();
        HashMap<Integer, String> qsubjectMap = Maps.newHashMap();
        for (Qsubject qsubject : subjectList) {
            QsubjectVersion qsubjectVersion = new QsubjectVersion();
            BeanUtils.copyProperties(qsubject,qsubjectVersion);
            qsubjectVersion.setQuestionVersionId(questionVersionId);
            qsubjectVersion.setQuId(quId);
            qsubjectVersion.setSubjectId(qsubject.getId());
            qsubjectVersion.setCurrentCreateTime(date);
            String qsubjectId = UUID.randomUUID().toString().replaceAll("-", "");
            qsubjectVersion.setId(qsubjectId);
            qsubjectMap.put(qsubject.getId(),qsubjectId);
            qsubjectVersionList.add(qsubjectVersion);
        }
        subjectVersionService.saveBatch(qsubjectVersionList);

        List<QoptionVersion> qoptionVersionList = Lists.newArrayList();
        List<Integer> subjectIdList = subjectList.stream().map(Qsubject::getId).distinct().collect(Collectors.toList());
        List<Qoption> qoptions = optionService.selectBySubjectList(subjectIdList);
        for (Qoption qoption : qoptions) {
            QoptionVersion qoptionVersion = new QoptionVersion();
            BeanUtils.copyProperties(qoption,qoptionVersion);
            qoptionVersion.setQuestionVersionId(questionVersionId);
            qoptionVersion.setQuestionId(quId);
            qoptionVersion.setSubjectId(qoption.getSubId());
            qoptionVersion.setOptionId(qoption.getId());
            qoptionVersion.setCurrentCreateTime(date);

            String qsubjectId = qsubjectMap.get(qoption.getSubId());
            qoptionVersion.setSubjectVersionId(qsubjectId);

            qoptionVersionList.add(qoptionVersion);
        }
        optionVersionService.saveBatch(qoptionVersionList);
    }
}
