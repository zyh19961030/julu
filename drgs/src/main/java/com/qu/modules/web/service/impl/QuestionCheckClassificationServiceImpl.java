package com.qu.modules.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.ResultBetter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.qu.constant.Constant;
import com.qu.modules.web.entity.Question;
import com.qu.modules.web.entity.QuestionCheckClassification;
import com.qu.modules.web.entity.QuestionCheckClassificationRel;
import com.qu.modules.web.mapper.QuestionCheckClassificationMapper;
import com.qu.modules.web.param.QuestionCheckClassificationAddParam;
import com.qu.modules.web.param.QuestionCheckClassificationUpdateParam;
import com.qu.modules.web.pojo.Data;
import com.qu.modules.web.service.IQuestionCheckClassificationRelService;
import com.qu.modules.web.service.IQuestionCheckClassificationService;
import com.qu.modules.web.service.IQuestionService;
import com.qu.modules.web.vo.ClassificationQuestionVo;
import com.qu.modules.web.vo.QuestionCheckClassificationListVo;

import cn.hutool.core.collection.CollectionUtil;

/**
 * @Description: 查检表分类表
 * @Author: jeecg-boot
 * @Date:   2023-04-27
 * @Version: V1.0
 */
@Service
public class QuestionCheckClassificationServiceImpl extends ServiceImpl<QuestionCheckClassificationMapper, QuestionCheckClassification> implements IQuestionCheckClassificationService {

    @Autowired
    private IQuestionCheckClassificationRelService questionCheckClassificationRelService;

    @Lazy
    @Autowired
    private IQuestionService questionService;

    @Override
    public List<QuestionCheckClassification> selectByUserId(String userId) {
        LambdaQueryWrapper<QuestionCheckClassification> lambda = new QueryWrapper<QuestionCheckClassification>().lambda();
        lambda.eq(QuestionCheckClassification::getUserId,userId);
        lambda.eq(QuestionCheckClassification::getDel, Constant.DEL_NORMAL);
        return this.list(lambda);
    }

    @Override
    public List<QuestionCheckClassification> selectByUserIdNotEqualId(String userId,Integer questionCheckClassificationId) {
        LambdaQueryWrapper<QuestionCheckClassification> lambda = new QueryWrapper<QuestionCheckClassification>().lambda();
        lambda.eq(QuestionCheckClassification::getUserId,userId);
        lambda.ne(QuestionCheckClassification::getId,questionCheckClassificationId);
        lambda.eq(QuestionCheckClassification::getDel, Constant.DEL_NORMAL);
        return this.list(lambda);
    }

    @Override
    public List<QuestionCheckClassificationListVo> queryList(Data data) {
        String userId = data.getTbUser().getId();
        LambdaQueryWrapper<QuestionCheckClassification> lambda = new QueryWrapper<QuestionCheckClassification>().lambda();
        lambda.eq(QuestionCheckClassification::getUserId,userId);
        lambda.eq(QuestionCheckClassification::getDel, Constant.DEL_NORMAL);
        List<QuestionCheckClassification> list = this.list(lambda);
        if(CollectionUtil.isEmpty(list)){
            return Lists.newArrayList();
        }

        List<Integer> questionCheckClassificationIdList = list.stream().map(QuestionCheckClassification::getId).collect(Collectors.toList());
        //查分类关联关系
        List<QuestionCheckClassificationRel> questionCheckClassificationRelList = questionCheckClassificationRelService.selectByQuestionCheckClassification(questionCheckClassificationIdList);
        if(CollectionUtil.isEmpty(questionCheckClassificationRelList)){
            List<QuestionCheckClassificationListVo> questionCheckClassificationListVoList = list.stream().map(q -> {
                QuestionCheckClassificationListVo vo = new QuestionCheckClassificationListVo();
                BeanUtils.copyProperties(q,vo);
                return vo;
            }).collect(Collectors.toList());

            return questionCheckClassificationListVoList;
        }

        Map<Integer, List<QuestionCheckClassificationRel>> questionCheckClassificationRelMap = questionCheckClassificationRelList
                .stream()
                .collect(Collectors.toMap(QuestionCheckClassificationRel::getQuestionCheckClassificationId, Lists::newArrayList,
                (List<QuestionCheckClassificationRel> n1, List<QuestionCheckClassificationRel> n2) -> {
                    n1.addAll(n2);
                    return n1;
                }));

        List<Integer> questionIdList = questionCheckClassificationRelList.stream().map(QuestionCheckClassificationRel::getQuestionId).distinct().collect(Collectors.toList());
        List<Question> questionList = questionService.getCheckByIds(questionIdList);
        Map<Integer, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));

        List<QuestionCheckClassificationListVo> questionCheckClassificationListVoList = Lists.newArrayList();
        for (QuestionCheckClassification questionCheckClassification : list) {

            QuestionCheckClassificationListVo vo = new QuestionCheckClassificationListVo();
            BeanUtils.copyProperties(questionCheckClassification,vo);

            List<QuestionCheckClassificationRel> questionCheckClassificationRelListInMap = questionCheckClassificationRelMap.get(questionCheckClassification.getId());

            List<ClassificationQuestionVo> classificationQuestionVoList = Lists.newArrayList();
            if(CollectionUtil.isNotEmpty(questionCheckClassificationRelListInMap)){
                for (QuestionCheckClassificationRel questionCheckClassificationRel : questionCheckClassificationRelListInMap) {
                    ClassificationQuestionVo classificationQuestionVo = new ClassificationQuestionVo();
                    Integer questionId = questionCheckClassificationRel.getQuestionId();
                    Question question = questionMap.get(questionId);
                    if(Objects.nonNull(question)){
                        classificationQuestionVo.setQuId(questionId);
                        classificationQuestionVo.setQuName(question.getQuName());
                        classificationQuestionVoList.add(classificationQuestionVo);
                    }
                }
            }
            vo.setQuestionVoList(classificationQuestionVoList);
            questionCheckClassificationListVoList.add(vo);
        }

        return questionCheckClassificationListVoList;
    }

    @Override
    public void add(QuestionCheckClassificationAddParam param, Data data) {
        String name = param.getName();
        QuestionCheckClassification classification = new QuestionCheckClassification();
        classification.setUserId(data.getTbUser().getId());
        classification.setName(name);
        Date date = new Date();
        classification.setCreateTime(date);
        classification.setUpdateTime(date);
        classification.setDel(Constant.DEL_NORMAL);
        this.save(classification);

        List<Integer> questionIdList = param.getQuestionIdList();
        if(CollectionUtil.isNotEmpty(questionIdList)){
            ArrayList<QuestionCheckClassificationRel> relAddList = Lists.newArrayList();
            for (Integer questionId : questionIdList) {
                QuestionCheckClassificationRel rel = new QuestionCheckClassificationRel();
                rel.setQuestionCheckClassificationId(classification.getId());
                rel.setQuestionId(questionId);
                rel.setCreateTime(date);
                relAddList.add(rel);
            }
            questionCheckClassificationRelService.saveBatch(relAddList);
        }
    }

    @Override
    public ResultBetter<T> edit(QuestionCheckClassificationUpdateParam param, Data data) {
        Integer id = param.getId();
        QuestionCheckClassification byId = this.getById(id);
        if(Objects.isNull(byId) || Constant.DEL_DELETED.equals(byId.getDel())){
            return ResultBetter.error("分类不存在");
        }
        byId.setName(param.getName());
        Date date = new Date();
        byId.setUpdateTime(date);
        byId.setDel(Constant.DEL_NORMAL);
        byId.setUserId(data.getTbUser().getId());
        this.updateById(byId);

        LambdaUpdateWrapper<QuestionCheckClassificationRel> questionCheckClassificationRelLambdaUpdateWrapper = new UpdateWrapper<QuestionCheckClassificationRel>().lambda();
        questionCheckClassificationRelLambdaUpdateWrapper
                .eq(QuestionCheckClassificationRel::getQuestionCheckClassificationId,id);
        questionCheckClassificationRelService.remove(questionCheckClassificationRelLambdaUpdateWrapper);

        List<Integer> questionIdList = param.getQuestionIdList();
        if(CollectionUtil.isNotEmpty(questionIdList)){
            ArrayList<QuestionCheckClassificationRel> relAddList = Lists.newArrayList();
            for (Integer questionId : questionIdList) {
                QuestionCheckClassificationRel rel = new QuestionCheckClassificationRel();
                rel.setQuestionCheckClassificationId(byId.getId());
                rel.setQuestionId(questionId);
                rel.setCreateTime(date);
                relAddList.add(rel);
            }
            questionCheckClassificationRelService.saveBatch(relAddList);
        }
        return ResultBetter.ok();
    }

    @Override
    public ResultBetter<T> delete(String id, Data data) {
        QuestionCheckClassification byId = this.getById(id);
        if(Objects.isNull(byId) || Constant.DEL_DELETED.equals(byId.getDel())){
            return ResultBetter.error("分类不存在");
        }
        Date date = new Date();
        byId.setUpdateTime(date);
        byId.setDel(Constant.DEL_DELETED);
        this.updateById(byId);

        LambdaUpdateWrapper<QuestionCheckClassificationRel> questionCheckClassificationRelLambdaUpdateWrapper = new UpdateWrapper<QuestionCheckClassificationRel>().lambda();
        questionCheckClassificationRelLambdaUpdateWrapper
                .eq(QuestionCheckClassificationRel::getQuestionCheckClassificationId,id);
        questionCheckClassificationRelService.remove(questionCheckClassificationRelLambdaUpdateWrapper);

        return ResultBetter.ok();
    }
}
