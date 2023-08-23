package com.qu.modules.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qu.constant.QoptionConstant;
import com.qu.constant.QuestionConstant;
import com.qu.modules.web.dto.SubjectDto;
import com.qu.modules.web.entity.Qoption;
import com.qu.modules.web.entity.QoptionTemp;
import com.qu.modules.web.entity.Qsubject;
import com.qu.modules.web.entity.QsubjectTemp;
import com.qu.modules.web.entity.Question;
import com.qu.modules.web.entity.QuestionTemp;
import com.qu.modules.web.mapper.QuestionTempMapper;
import com.qu.modules.web.param.IdParam;
import com.qu.modules.web.service.IOptionService;
import com.qu.modules.web.service.IQoptionTempService;
import com.qu.modules.web.service.IQsubjectTempService;
import com.qu.modules.web.service.IQuestionService;
import com.qu.modules.web.service.IQuestionTempService;
import com.qu.modules.web.service.ISubjectService;

/**
 * @Description: 问卷临时表
 * @Author: jeecg-boot
 * @Date: 2022-10-22
 * @Version: V1.0
 */
@Service
public class QuestionTempServiceImpl extends ServiceImpl<QuestionTempMapper, QuestionTemp> implements IQuestionTempService {

    @Autowired
    private IQuestionService questionService;

    @Lazy
    @Resource
    private ISubjectService subjectService;

    @Autowired
    private IOptionService optionService;

    @Autowired
    private IQoptionTempService optionTempService;

    @Autowired
    private IQsubjectTempService subjectTempService;


    @Override
    public Result<?> copyTemp(IdParam idParam) {
        String tempId = idParam.getId();
        QuestionTemp questionTemp = this.getById(tempId);
        if (questionTemp == null || QuestionConstant.DEL_DELETED.equals(questionTemp.getDel())) {
            return Result.error("问卷不存在");
        }
        //查询临时表题目和临时表选项
        LambdaQueryWrapper<QsubjectTemp> qsubjectTempLambda = new QueryWrapper<QsubjectTemp>().lambda();
        qsubjectTempLambda.eq(QsubjectTemp::getQuId,tempId);
        qsubjectTempLambda.eq(QsubjectTemp::getDel,QuestionConstant.DEL_NORMAL);
        qsubjectTempLambda.orderByAsc(QsubjectTemp::getOrderNum);
        List<QsubjectTemp> subjectTempList = subjectTempService.list(qsubjectTempLambda);
        if(subjectTempList.isEmpty()){
            return Result.error("问卷没有找到题目");
        }

        List<Integer> subjectIdList = subjectTempList.stream().map(QsubjectTemp::getId).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<QoptionTemp> qoptionTempLambda = new QueryWrapper<QoptionTemp>().lambda();
        qoptionTempLambda.in(QoptionTemp::getSubId,subjectIdList);
        qoptionTempLambda.in(QoptionTemp::getDel, QoptionConstant.DEL_NORMAL);
        qoptionTempLambda.orderByAsc(QoptionTemp::getOpOrder);
        List<QoptionTemp> qoptionTemps = optionTempService.list(qoptionTempLambda);
        Map<Integer, ArrayList<QoptionTemp>> optionTempMap = qoptionTemps.stream().collect(
                Collectors.toMap(QoptionTemp::getSubId, Lists::newArrayList, (ArrayList<QoptionTemp> k1, ArrayList<QoptionTemp> k2) -> {
            k1.addAll(k2);
            return k1;
        }));

        List<SubjectDto> subjectVoList = new ArrayList<>();
        ArrayList<QoptionTemp> optionEmptyList = Lists.newArrayList();
        for (QsubjectTemp qsubjectTemp : subjectTempList) {
            SubjectDto subjectDto = new SubjectDto();
            BeanUtils.copyProperties(qsubjectTemp, subjectDto);

            ArrayList<QoptionTemp> qoptionsList = optionTempMap.get(qsubjectTemp.getId());
            subjectDto.setOptionList(qoptionsList==null?optionEmptyList:qoptionsList);
            subjectVoList.add(subjectDto);
        }

        //保存问卷
        Question question = new Question();
        BeanUtils.copyProperties(questionTemp, question);
        question.setId(null);
        question.setQuStatus(QuestionConstant.QU_STATUS_DRAFT);
        question.setQuestionVersion(QuestionConstant.QUESTION_VERSION_DEFAULT);
        question.setTraceabilityStatus(QuestionConstant.TRACEABILITY_STATUS_NO_GENERATE);
        questionService.save(question);

        //保存题目
        HashMap<Integer, Integer> subjectIdMap = Maps.newHashMap();
        List<Qsubject> qsubjectAddList =Lists.newArrayList();
        List<Qoption> optionAddList =Lists.newArrayList();
        for (SubjectDto subjectDto : subjectVoList) {
            Qsubject qsubject = new Qsubject();
            BeanUtils.copyProperties(subjectDto, qsubject);
            qsubject.setId(null);
            qsubject.setQuId(question.getId());
//            qsubject.setJumpLogic(subjectDto.getJumpLogic());
//            qsubject.setSpecialJumpLogic(subjectDto.getSpecialJumpLogic());
//            qsubject.setGroupIds(subjectDto.getGroupIds());
//            qsubject.setChoiceSubjectId(subjectDto.getChoiceSubjectId());
            subjectService.save(qsubject);
            qsubjectAddList.add(qsubject);
            subjectIdMap.put(subjectDto.getId(),qsubject.getId());

            List<QoptionTemp> optionList = subjectDto.getOptionList();
            for (QoptionTemp qoptionTemp : optionList) {
                Qoption qoption = new Qoption();
                BeanUtils.copyProperties(qoptionTemp, qoption);
                qoption.setId(null);
                qoption.setSubId(qsubject.getId());
//                qoption.setJumpLogic(qoptionTemp.getJumpLogic());
//                qoption.setSpecialJumpLogic(qoptionTemp.getSpecialJumpLogic());
                optionAddList.add(qoption);
            }
        }
        optionService.saveBatch(optionAddList);

        //处理分组
        List<Qsubject> qsubjectUpdateList =Lists.newArrayList();
        for (Qsubject qsubject : qsubjectAddList) {
            String groupIds = qsubject.getGroupIds();
            if(StringUtils.isNotBlank(groupIds)){
                ArrayList<String> groupIdList = Lists.newArrayList(groupIds.split(","));
                ArrayList<Integer> groupNewIdList = Lists.newArrayList();
                for (String s : groupIdList) {
                    int i = Integer.parseInt(s);
                    if(subjectIdMap.containsKey(i)){
                        groupNewIdList.add(subjectIdMap.get(i));
                    }
                }
                qsubject.setGroupIds(Joiner.on(",").join(groupNewIdList));
                qsubjectUpdateList.add(qsubject);
            }
        }
        if(!qsubjectUpdateList.isEmpty()){
            subjectService.updateBatchById(qsubjectUpdateList);
        }

        return Result.ok();
    }


}
