package com.qu.modules.web.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.api.vo.ResultBetter;
import org.jeecg.common.api.vo.ResultFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qu.constant.AnswerCheckConstant;
import com.qu.constant.AnswerCheckUserSetConstant;
import com.qu.constant.AnswerConstant;
import com.qu.constant.Constant;
import com.qu.constant.QSingleDiseaseTakeConstant;
import com.qu.constant.QoptionConstant;
import com.qu.constant.QsubjectConstant;
import com.qu.constant.QuestionCheckClassificationConstant;
import com.qu.constant.QuestionCheckedDeptConstant;
import com.qu.constant.QuestionConstant;
import com.qu.constant.TbDataConstant;
import com.qu.constant.TbInspectStatsTemplateDepConstant;
import com.qu.event.DeleteCheckDetailSetEvent;
import com.qu.event.QuestionVersionEvent;
import com.qu.modules.web.dto.SubjectCopyVo;
import com.qu.modules.web.entity.Answer;
import com.qu.modules.web.entity.AnswerCheck;
import com.qu.modules.web.entity.AnswerCheckUserSet;
import com.qu.modules.web.entity.QSingleDiseaseTake;
import com.qu.modules.web.entity.Qoption;
import com.qu.modules.web.entity.QoptionVersion;
import com.qu.modules.web.entity.Qsubject;
import com.qu.modules.web.entity.QsubjectVersion;
import com.qu.modules.web.entity.Question;
import com.qu.modules.web.entity.QuestionCheckClassification;
import com.qu.modules.web.entity.QuestionCheckClassificationRel;
import com.qu.modules.web.entity.QuestionCheckedDept;
import com.qu.modules.web.entity.QuestionVersion;
import com.qu.modules.web.entity.TbCheckProject;
import com.qu.modules.web.entity.TbData;
import com.qu.modules.web.entity.TbDep;
import com.qu.modules.web.entity.TbInspectStatsTemplateDep;
import com.qu.modules.web.entity.TbUserAuxiliaryDep;
import com.qu.modules.web.entity.TqmsQuotaCategory;
import com.qu.modules.web.mapper.DynamicTableMapper;
import com.qu.modules.web.mapper.QuestionMapper;
import com.qu.modules.web.mapper.TqmsQuotaCategoryMapper;
import com.qu.modules.web.param.CheckQuestionHistoryStatisticDeptListParam;
import com.qu.modules.web.param.CopyQuestionParam;
import com.qu.modules.web.param.IdParam;
import com.qu.modules.web.param.QSingleDiseaseTakeStatisticAnalysisByDeptConditionParam;
import com.qu.modules.web.param.QueryCheckQuestionClassificationByUpdateParam;
import com.qu.modules.web.param.QuestionAgainReleaseParam;
import com.qu.modules.web.param.QuestionCheckClassificationParam;
import com.qu.modules.web.param.QuestionCheckParam;
import com.qu.modules.web.param.QuestionCheckedDepParam;
import com.qu.modules.web.param.QuestionEditParam;
import com.qu.modules.web.param.QuestionListParam;
import com.qu.modules.web.param.QuestionParam;
import com.qu.modules.web.param.QuestionQueryByIdParam;
import com.qu.modules.web.param.SelectCheckedDeptIdsParam;
import com.qu.modules.web.param.SelectResponsibilityUserIdsParam;
import com.qu.modules.web.param.UpdateCategoryIdParam;
import com.qu.modules.web.param.UpdateCheckProjectIdParam;
import com.qu.modules.web.param.UpdateCheckedDeptIdsParam;
import com.qu.modules.web.param.UpdateDeptIdsParam;
import com.qu.modules.web.param.UpdateQuestionIconParam;
import com.qu.modules.web.param.UpdateResponsibilityUserIdsParam;
import com.qu.modules.web.param.UpdateTemplateIdParam;
import com.qu.modules.web.param.UpdateWriteFrequencyIdsParam;
import com.qu.modules.web.pojo.Data;
import com.qu.modules.web.pojo.TbUser;
import com.qu.modules.web.service.IAnswerCheckService;
import com.qu.modules.web.service.IAnswerCheckUserSetService;
import com.qu.modules.web.service.IAnswerService;
import com.qu.modules.web.service.IOptionService;
import com.qu.modules.web.service.IQSingleDiseaseTakeService;
import com.qu.modules.web.service.IQoptionVersionService;
import com.qu.modules.web.service.IQsubjectVersionService;
import com.qu.modules.web.service.IQuestionCheckClassificationRelService;
import com.qu.modules.web.service.IQuestionCheckClassificationService;
import com.qu.modules.web.service.IQuestionCheckedDeptService;
import com.qu.modules.web.service.IQuestionService;
import com.qu.modules.web.service.IQuestionVersionService;
import com.qu.modules.web.service.ISingleEnterQuestionService;
import com.qu.modules.web.service.ISubjectService;
import com.qu.modules.web.service.ITbCheckProjectService;
import com.qu.modules.web.service.ITbDataService;
import com.qu.modules.web.service.ITbDepService;
import com.qu.modules.web.service.ITbInspectStatsTemplateDepService;
import com.qu.modules.web.service.ITbUserAuxiliaryDepService;
import com.qu.modules.web.service.ITbUserService;
import com.qu.modules.web.vo.CheckQuestionHistoryStatisticClassificationVo;
import com.qu.modules.web.vo.CheckQuestionHistoryStatisticDeptListDeptVo;
import com.qu.modules.web.vo.CheckQuestionHistoryStatisticVo;
import com.qu.modules.web.vo.CheckQuestionParameterSetListVo;
import com.qu.modules.web.vo.QuestionAndCategoryPageVo;
import com.qu.modules.web.vo.QuestionAndCategoryVo;
import com.qu.modules.web.vo.QuestionCheckClassificationVo;
import com.qu.modules.web.vo.QuestionCheckVo;
import com.qu.modules.web.vo.QuestionMiniAppPageVo;
import com.qu.modules.web.vo.QuestionMonthQuarterYearCreateListVo;
import com.qu.modules.web.vo.QuestionNameVo;
import com.qu.modules.web.vo.QuestionPageVo;
import com.qu.modules.web.vo.QuestionPatientCreateListVo;
import com.qu.modules.web.vo.QuestionSetColumnAllVo;
import com.qu.modules.web.vo.QuestionSetColumnChooseVo;
import com.qu.modules.web.vo.QuestionSetColumnVo;
import com.qu.modules.web.vo.QuestionStatisticsCheckVo;
import com.qu.modules.web.vo.QuestionVo;
import com.qu.modules.web.vo.SubjectVo;
import com.qu.modules.web.vo.ViewNameVo;
import com.qu.util.DeptUtil;
import com.qu.util.IntegerUtil;
import com.qu.util.StringUtil;
import com.qu.util.VersionNumberUtil;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 问卷表
 * @Author: jeecg-boot
 * @Date: 2021-03-19
 * @Version: V1.0
 */
@Slf4j
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Resource
    private QuestionMapper questionMapper;

//    @Autowired
//    private QsubjectMapper subjectMapper;

    @Lazy
    @Resource
    private ISubjectService subjectService;

    @Autowired
    private IOptionService optionService;

    @Resource
    private DynamicTableMapper dynamicTableMapper;

    @Resource
    private TqmsQuotaCategoryMapper tqmsQuotaCategoryMapper;

    @Autowired
    private ITbDepService tbDepService;

    @Autowired
    private ITbInspectStatsTemplateDepService tbInspectStatsTemplateDepService;

    @Autowired
    private IQuestionCheckedDeptService questionCheckedDeptService;

    @Lazy
    @Autowired
    private IQSingleDiseaseTakeService qSingleDiseaseTakeService;

    @Autowired
    private IAnswerCheckService answerCheckService;

    @Autowired
    private IAnswerService answerService;

    @Autowired
    private IQuestionVersionService questionVersionService;

    @Autowired
    private IQsubjectVersionService qsubjectVersionService;

    @Autowired
    private IQoptionVersionService qoptionVersionService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
	private ITbDataService tbDataService;

    @Autowired
    private ITbUserService tbUserService;

    @Autowired
    private ITbUserAuxiliaryDepService tbUserAuxiliaryDepService;
    @Lazy
    @Autowired
    private IAnswerCheckUserSetService answerCheckUserSetService;

    @Autowired
    private ITbCheckProjectService tbCheckProjectService;

    @Autowired
    private IQuestionCheckClassificationService questionCheckClassificationService;

    @Autowired
    private IQuestionCheckClassificationRelService questionCheckClassificationRelService;

    @Lazy
    @Autowired
    private ISingleEnterQuestionService singleEnterQuestionService;




    @Override
    public Question saveQuestion(QuestionParam questionParam, TbUser tbUser) {
        Question question = new Question();
        try {
            BeanUtils.copyProperties(questionParam, question);
            question.setQuStatus(QuestionConstant.QU_STATUS_DRAFT);
            question.setQuStop(QuestionConstant.QU_STOP_NORMAL);
            question.setDel(QuestionConstant.DEL_NORMAL);
            Date date = new Date();
            String userId = tbUser.getId();
            question.setCreater(userId);
            question.setCreateTime(date);
            question.setUpdater(userId);
            question.setUpdateTime(date);
            question.setQuestionVersion(QuestionConstant.QUESTION_VERSION_DEFAULT);
            question.setTraceabilityStatus(QuestionConstant.TRACEABILITY_STATUS_NO_GENERATE);
            questionMapper.insert(question);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return question;
    }

    @Override
    public QuestionVo queryById(Integer id) {
        QuestionVo questionVo = new QuestionVo();

        Question question = questionMapper.selectById(id);
        if(question==null){
            return questionVo;
        }
        BeanUtils.copyProperties(question, questionVo);
//        List<Qsubject> subjectList = subjectMapper.selectSubjectByQuId(id);
        List<Qsubject> subjectList = subjectService.selectSubjectByQuId(id);
        if(subjectList.isEmpty()){
            return questionVo;
        }

        List<Integer> subjectIdList = subjectList.stream().map(Qsubject::getId).distinct().collect(Collectors.toList());
        List<Qoption> qoptions = optionService.selectBySubjectList(subjectIdList);

        Map<Integer, ArrayList<Qoption>> optionMap = qoptions.stream().collect(Collectors.toMap(Qoption::getSubId, Lists::newArrayList, (ArrayList<Qoption> k1, ArrayList<Qoption> k2) -> {
            k1.addAll(k2);
            return k1;
        }));

        List<SubjectVo> subjectVoList = new ArrayList<>();
        ArrayList<Qoption> optionEmptyList = Lists.newArrayList();
        for (Qsubject subject : subjectList) {
            SubjectVo subjectVo = new SubjectVo();
            BeanUtils.copyProperties(subject, subjectVo);
            ArrayList<Qoption> qoptionsList = optionMap.get(subject.getId());
            subjectVo.setOptionList(qoptionsList==null?optionEmptyList:qoptionsList);
            subjectVoList.add(subjectVo);
        }
        assembleSubject(questionVo, subjectVoList);

        return questionVo;
    }

    @Override
    public QuestionVo queryByIdNew(QuestionQueryByIdParam param,Integer type) {
        QuestionVo questionVo = new QuestionVo();
        Integer quId = param.getQuId();
        Integer answerCheckId = param.getDataId();
        Question question = questionMapper.selectById(quId);
        if(question==null){
            return questionVo;
        }

        boolean dataFlag = false;
        String answerQuestionVersionNumber = "";
        if(type.equals(QuestionConstant.CATEGORY_TYPE_NORMAL)){
            Answer answer = answerService.getById(answerCheckId);
            //        if (answer == null || AnswerConstant.DEL_DELETED.equals(answer.getDel())) {
            //            return questionVo;
            //        }
            if(answer==null){
                dataFlag = true;
            }else{
                answerQuestionVersionNumber = answer.getQuestionVersion();
            }
        }else if(type.equals(QuestionConstant.CATEGORY_TYPE_CHECK)){
            AnswerCheck answerCheck = answerCheckService.getById(answerCheckId);
//            if (answerCheck == null || AnswerCheckConstant.DEL_DELETED.equals(answerCheck.getDel())) {
//                return questionVo;
//            }
            if(answerCheck==null){
                dataFlag = true;
            }else{
                answerQuestionVersionNumber = answerCheck.getQuestionVersion();
            }
        }else if(type.equals(QuestionConstant.CATEGORY_TYPE_SINGLE_DISEASE)){
            QSingleDiseaseTake qSingleDiseaseTake = qSingleDiseaseTakeService.getById(answerCheckId);
//            if (qSingleDiseaseTake == null  || QSingleDiseaseTakeConstant.DEL_DELETED.equals(qSingleDiseaseTake.getDel())) {
//                return questionVo;
//            }
            if(qSingleDiseaseTake==null){
                dataFlag = true;
            }else{
                answerQuestionVersionNumber = qSingleDiseaseTake.getQuestionVersion();
            }
        }

        List<SubjectVo> subjectVoList = Lists.newArrayList();
        String questionVersionNumber = question.getQuestionVersion();
        //查询历史问卷版本
        //判断数据问卷版本是不是最新的版本
        if ( dataFlag || StringUtils.isBlank(answerQuestionVersionNumber) || answerQuestionVersionNumber.equals(questionVersionNumber)) {
            //最新版本取question表
            BeanUtils.copyProperties(question, questionVo);
            questionVo.setQuestionVersion(question.getQuestionVersion());
            questionVo.setQuestionVersionData(answerQuestionVersionNumber);

            List<Qsubject> subjectList = subjectService.selectSubjectByQuId(quId);
            if (subjectList.isEmpty()) {
                return questionVo;
            }
            List<Integer> subjectIdList = subjectList.stream().map(Qsubject::getId).distinct().collect(Collectors.toList());
            List<Qoption> qoptions = optionService.selectBySubjectList(subjectIdList);
            Map<Integer, ArrayList<Qoption>> optionMap = qoptions.stream().collect(Collectors.toMap(Qoption::getSubId, Lists::newArrayList, (ArrayList<Qoption> k1, ArrayList<Qoption> k2) -> {
                k1.addAll(k2);
                return k1;
            }));

            ArrayList<Qoption> optionEmptyList = Lists.newArrayList();
            for (Qsubject subject : subjectList) {
                SubjectVo subjectVo = new SubjectVo();
                BeanUtils.copyProperties(subject, subjectVo);
                ArrayList<Qoption> qoptionsList = optionMap.get(subject.getId());
                subjectVo.setOptionList(qoptionsList == null ? optionEmptyList : qoptionsList);
                subjectVoList.add(subjectVo);
            }
        } else {
            //历史版本取question_version表
            QuestionVersion questionVersion = questionVersionService.selectByQuestionAndVersion(quId, answerQuestionVersionNumber);
            if (questionVersion == null) {
                return questionVo;
            }

            BeanUtils.copyProperties(questionVersion, questionVo);
            questionVo.setId(questionVersion.getQuId());
            questionVo.setQuestionVersion(question.getQuestionVersion());
            questionVo.setCategoryType(question.getCategoryType());
            questionVo.setCategoryId(question.getCategoryId());
            questionVo.setWriteFrequency(question.getWriteFrequency());
            questionVo.setQuestionVersionData(answerQuestionVersionNumber);

            String questionVersionId = questionVersion.getId();
            List<QsubjectVersion> subjectVersionList = qsubjectVersionService.selectSubjectVersionByQuIdAndVersion(quId, questionVersionId);
            if (subjectVersionList.isEmpty()) {
                return questionVo;
            }

            List<Integer> subjectVersionIdList = subjectVersionList.stream().map(QsubjectVersion::getSubjectId).distinct().collect(Collectors.toList());
            List<QoptionVersion> qoptionVersions = qoptionVersionService.selectOptionVersionByQuIdAndVersion(questionVersionId, subjectVersionIdList);

            Map<Integer, ArrayList<Qoption>> optionMap = qoptionVersions.stream().collect(Collectors.toMap(QoptionVersion::getSubjectId, qoptionVersion -> {
                        Qoption qoption = new Qoption();
                        BeanUtils.copyProperties(qoptionVersion, qoption);
                        qoption.setId(qoptionVersion.getOptionId());
                        return Lists.newArrayList(qoption);
                    },
                    (ArrayList<Qoption> k1, ArrayList<Qoption> k2) -> {
                        k1.addAll(k2);
                        return k1;
                    }));
            ArrayList<Qoption> optionEmptyList = Lists.newArrayList();
            for (QsubjectVersion subjectVersion : subjectVersionList) {
                SubjectVo subjectVo = new SubjectVo();
                BeanUtils.copyProperties(subjectVersion, subjectVo);
                subjectVo.setId(subjectVersion.getSubjectId());

                ArrayList<Qoption> qoptionsList = optionMap.get(subjectVersion.getSubjectId());
                subjectVo.setOptionList(qoptionsList == null ? optionEmptyList : qoptionsList);
                subjectVoList.add(subjectVo);
            }
        }
        assembleSubject(questionVo, subjectVoList);

        return questionVo;
    }

    private QuestionVo getQuestionVo(QuestionVo questionVo, Question question, Object answerData, String answerQuestionVersionNumber) {
        List<SubjectVo> subjectVoList = Lists.newArrayList();
        String questionVersionNumber = question.getQuestionVersion();
        Integer quId = question.getId();
        //查询历史问卷版本
        //判断数据问卷版本是不是最新的版本
        if ( answerData==null|| answerQuestionVersionNumber == null || answerQuestionVersionNumber.equals(questionVersionNumber)) {
            //最新版本取question表
            BeanUtils.copyProperties(question, questionVo);
            questionVo.setQuestionVersion(question.getQuestionVersion());
            questionVo.setQuestionVersionData(answerQuestionVersionNumber);

            List<Qsubject> subjectList = subjectService.selectSubjectByQuId(quId);
            if (subjectList.isEmpty()) {
                return questionVo;
            }
            List<Integer> subjectIdList = subjectList.stream().map(Qsubject::getId).distinct().collect(Collectors.toList());
            List<Qoption> qoptions = optionService.selectBySubjectList(subjectIdList);
            Map<Integer, ArrayList<Qoption>> optionMap = qoptions.stream().collect(Collectors.toMap(Qoption::getSubId, Lists::newArrayList, (ArrayList<Qoption> k1, ArrayList<Qoption> k2) -> {
                k1.addAll(k2);
                return k1;
            }));

            ArrayList<Qoption> optionEmptyList = Lists.newArrayList();
            for (Qsubject subject : subjectList) {
                SubjectVo subjectVo = new SubjectVo();
                BeanUtils.copyProperties(subject, subjectVo);
                ArrayList<Qoption> qoptionsList = optionMap.get(subject.getId());
                subjectVo.setOptionList(qoptionsList == null ? optionEmptyList : qoptionsList);
                subjectVoList.add(subjectVo);
            }
        } else {
            //历史版本取question_version表
            QuestionVersion questionVersion = questionVersionService.selectByQuestionAndVersion(quId, answerQuestionVersionNumber);
            if (questionVersion == null) {
                return questionVo;
            }

            BeanUtils.copyProperties(questionVersion, questionVo);
            questionVo.setId(questionVersion.getQuId());
            questionVo.setQuestionVersion(question.getQuestionVersion());
            questionVo.setQuestionVersionData(answerQuestionVersionNumber);

            String questionVersionId = questionVersion.getId();
            List<QsubjectVersion> subjectVersionList = qsubjectVersionService.selectSubjectVersionByQuIdAndVersion(quId, questionVersionId);
            if (subjectVersionList.isEmpty()) {
                return questionVo;
            }

            List<Integer> subjectVersionIdList = subjectVersionList.stream().map(QsubjectVersion::getSubjectId).distinct().collect(Collectors.toList());
            List<QoptionVersion> qoptionVersions = qoptionVersionService.selectOptionVersionByQuIdAndVersion(questionVersionId, subjectVersionIdList);

            Map<Integer, ArrayList<Qoption>> optionMap = qoptionVersions.stream().collect(Collectors.toMap(QoptionVersion::getSubjectId, qoptionVersion -> {
                        Qoption qoption = new Qoption();
                        BeanUtils.copyProperties(qoptionVersion, qoption);
                        qoption.setId(qoptionVersion.getOptionId());
                        return Lists.newArrayList(qoption);
                    },
                    (ArrayList<Qoption> k1, ArrayList<Qoption> k2) -> {
                        k1.addAll(k2);
                        return k1;
                    }));
            ArrayList<Qoption> optionEmptyList = Lists.newArrayList();
            for (QsubjectVersion subjectVersion : subjectVersionList) {
                SubjectVo subjectVo = new SubjectVo();
                BeanUtils.copyProperties(subjectVersion, subjectVo);
                subjectVo.setId(subjectVersion.getSubjectId());

                ArrayList<Qoption> qoptionsList = optionMap.get(subjectVersion.getSubjectId());
                subjectVo.setOptionList(qoptionsList == null ? optionEmptyList : qoptionsList);
                subjectVoList.add(subjectVo);
            }
        }
        assembleSubject(questionVo, subjectVoList);

        return questionVo;
    }

    @Override
    public QuestionVo answerCheckQueryById(QuestionQueryByIdParam param) {
        QuestionVo questionVo = new QuestionVo();
        Integer quId = param.getQuId();
        Integer answerCheckId = param.getDataId();
        Question question = questionMapper.selectById(quId);
        if (question == null) {
            return questionVo;
        }
        AnswerCheck answerCheck = answerCheckService.getById(answerCheckId);
        if (answerCheck == null || AnswerCheckConstant.DEL_DELETED.equals(answerCheck.getDel())) {
            return questionVo;
        }
        return getQuestionVo(questionVo, question, answerCheck, answerCheck.getQuestionVersion());
    }

    @Override
    public QuestionVo singleDiseaseQueryById(QuestionQueryByIdParam param) {
        QuestionVo questionVo = new QuestionVo();
        Integer quId = param.getQuId();
        Integer answerCheckId = param.getDataId();
        Question question = questionMapper.selectById(quId);
        if (question == null) {
            return questionVo;
        }
        QSingleDiseaseTake qSingleDiseaseTake = qSingleDiseaseTakeService.getById(answerCheckId);
        if (qSingleDiseaseTake == null  || QSingleDiseaseTakeConstant.DEL_DELETED.equals(qSingleDiseaseTake.getDel())) {
            return questionVo;
        }
        return getQuestionVo(questionVo, question, qSingleDiseaseTake, qSingleDiseaseTake.getQuestionVersion());
    }

    private void assembleSubject(QuestionVo questionVo, List<SubjectVo> subjectVoList) {
        //开始组装分组题逻辑
        //先缓存
        Map<Integer, SubjectVo> mapCache = new HashMap<>();
        for (SubjectVo subjectVo : subjectVoList) {
            mapCache.put(subjectVo.getId(), subjectVo);
        }
        //开始算
        StringBuffer groupIdsAll = new StringBuffer();
        for (SubjectVo subjectVo : subjectVoList) {
            //如果是分组题

            if (subjectVo.getSubType().equals(QsubjectConstant.SUB_TYPE_GROUP) || subjectVo.getSubType().equals(QsubjectConstant.SUB_TYPE_GROUP_SCORE)) {
                String groupIds = subjectVo.getGroupIds();//包含题号
                if (null != groupIds) {
                    String[] gids = groupIds.split(",");
                    List<SubjectVo> subjectVoGroupList = new ArrayList<>();
                    for (String subId : gids) {
                        groupIdsAll.append(subId);
                        groupIdsAll.append(",");
                        if (!StringUtil.isEmpty(subId)) {
                            SubjectVo svo = mapCache.get(Integer.parseInt(subId));
                            if(Objects.nonNull(svo)){
                                subjectVoGroupList.add(svo);
                            }
                        }
                    }
                    //设置到分组题对象列表
                    subjectVo.setSubjectVoList(subjectVoGroupList);
                }
            }
        }
        //删除在subjectVoList集合中删除groupIdsAll包含的题
        if (groupIdsAll.length() != 0) {
            String removeIds = groupIdsAll.toString();
            String[] remIds = removeIds.split(",");
            if (null != remIds && remIds.length > 0) {
                for (int i = 0; i < subjectVoList.size(); i++) {
                    //for (SubjectVo subjectVo : subjectVoList) {
                    SubjectVo subjectVo = subjectVoList.get(i);
                    Integer nowId = subjectVo.getId();
                    for (String remId : remIds) {
                        if (!IntegerUtil.isNull(nowId) && !StringUtil.isEmpty(remId)) {
                            if (nowId == Integer.parseInt(remId)) {
                                subjectVoList.remove(i);//移除
                                i--;
                            }
                        }
                    }

                }
            }
        }
        questionVo.setSubjectVoList(subjectVoList);
    }


    @Override
    public List<ViewNameVo> queryByViewName(QuestionCheckedDepParam param) {
        StringBuffer sql =new StringBuffer();
        String viewName = param.getViewName();
        if("checked_dep".equals(viewName)){
            sql.append("select * from `");
            sql.append(viewName);
            Integer quId = param.getQuId();
            if(quId!=null){
                sql.append("` where qu_id = ");
                sql.append(quId);
            }
        }else{
            sql.append("select * from `");
            sql.append(viewName);
            sql.append("`");
        }
        try {
            List<ViewNameVo> vo = dynamicTableMapper.selectViewName(sql.toString());
            return vo;
        } catch (Exception e) {
            if( e.getMessage().contains("Table") &&  e.getMessage().contains("doesn't exist")) {
                return Lists.newArrayList();
            }
        }
        return Lists.newArrayList();
    }

    @Override
    public Question updateQuestionById(QuestionEditParam questionEditParam, TbUser tbUser) {
        Question question = new Question();
        try {
            BeanUtils.copyProperties(questionEditParam, question);
            String userId = tbUser.getId();
            question.setUpdater(userId);
            question.setUpdateTime(new Date());
            questionMapper.updateById(question);
            question = questionMapper.selectById(questionEditParam.getId());
        } catch (Exception e) {
            question = null;
            log.error(e.getMessage(), e);
        }
        return question;
    }

    @Override
    public ResultBetter<Boolean> release(IdParam idParam, TbUser tbUser) {
        Question question = questionMapper.selectById(idParam.getId());

        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE `" + question.getTableName() + "` (");
        sql.append("`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',");
//      List<Qsubject> subjectList = subjectMapper.selectSubjectByQuId(questionEditParam.getId());
        List<Qsubject> subjectList = subjectService.selectSubjectByQuId(question.getId());
        for (Qsubject qsubject : subjectList) {
            Integer limitWords = qsubject.getLimitWords();
            if(limitWords==null || limitWords==0){
                limitWords=50;
            }
            String subType = qsubject.getSubType();
            Integer del = qsubject.getDel();
            if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType) || QuestionConstant.DEL_DELETED.equals(del)) {
                continue;
            }
//            if (QsubjectConstant.SUB_TYPE_DEPT_USER.equals(subType)) {
//                sql.append("`")
//                        .append("a_question_dept")
//                        .append("` ")
//                        .append(qsubject.getColumnTypeDatabase() == null ? "varchar" : qsubject.getColumnTypeDatabase())
//                        .append("(")
//                        .append(limitWords)
//                        .append(") COMMENT '")
//                        .append(qsubject.getSubName())
//                        .append("的科室")
//                        .append("',");
//                sql.append("`")
//                        .append("a_question_dept_user")
//                        .append("` ")
//                        .append(qsubject.getColumnTypeDatabase() == null ? "varchar" : qsubject.getColumnTypeDatabase())
//                        .append("(")
//                        .append(limitWords)
//                        .append(") COMMENT '")
//                        .append(qsubject.getSubName())
//                        .append("的科室中人员")
//                        .append("',");
//                continue;
//            }

            sql.append("`")
                    .append(qsubject.getColumnName())
                    .append("` ")
                    .append(qsubject.getColumnTypeDatabase()==null?"varchar":qsubject.getColumnTypeDatabase())
                    .append("(")
                    .append(limitWords)
                    .append(") COMMENT '")
                    .append(qsubject.getId())
                    .append("',");

            if (QsubjectConstant.MARK_OPEN.equals(qsubject.getMark())) {
                sql.append("`")
                        .append(qsubject.getColumnName())
                        .append("_mark")
                        .append("` ")
                        .append(QsubjectConstant.MARK_TYPE)
                        .append("(")
                        .append(QsubjectConstant.MARK_LENGTH)
                        .append(") COMMENT '")
                        .append(qsubject.getId())
                        .append("的痕迹")
                        .append("',");
                sql.append("`")
                        .append(qsubject.getColumnName())
                        .append("_mark_img")
                        .append("` ")
                        .append(QsubjectConstant.MARK_TYPE)
                        .append("(")
                        .append(QsubjectConstant.MARK_LENGTH)
                        .append(") COMMENT '")
                        .append(qsubject.getId())
                        .append("的痕迹图片")
                        .append("',");
            }
        }
//        if(QuestionConstant.CATEGORY_TYPE_CHECK.equals(question.getCategoryType())){
//            sql.append(" `tbrid` varchar(128) NULL COMMENT '填报人id',");
//        }
        sql.append(" `tbrxm` varchar(128) NULL COMMENT '填报人名称',");
        sql.append(" `answer_datetime` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '填报时间',");
//        sql.append(" `tbksmc` varchar(128) NULL COMMENT '填报科室名称',");
//        sql.append(" `tbksdm` varchar(128) NULL COMMENT '填报科室代码',");
        sql.append(" `summary_mapping_table_id` varchar(128) NULL COMMENT '对应总表的id，可以当主键',");
        sql.append(" `table_answer_status` int(11) NULL DEFAULT 0 COMMENT '0:草稿1:已提交',");
        sql.append(" `del` tinyint(4) NULL DEFAULT 0 COMMENT '0:正常1:已删除',");
        sql.append(" PRIMARY KEY (`id`)");
        if(subjectList.size()>=50){
            sql.append(") ENGINE=MyISAM DEFAULT CHARSET=utf8;");
        }else{
            sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        }
        try {
            dynamicTableMapper.createDynamicTable(sql.toString());
        } catch (Exception e) {
            if (e.getMessage().contains("Table") && e.getMessage().contains("already exists")) {
                return ResultBetter.error("发布失败，表名重复!");
            }
            return ResultBetter.error("发布失败");
        }

        DeleteCheckDetailSetEvent event = new DeleteCheckDetailSetEvent(this, question.getId());
        applicationEventPublisher.publishEvent(event);

        //保存问卷版本
        QuestionVersionEvent questionVersionEvent = new QuestionVersionEvent(this, question.getId());
        applicationEventPublisher.publishEvent(questionVersionEvent);

        String userId = tbUser.getId();
        question.setUpdater(userId);
        question.setUpdateTime(new Date());
        question.setQuStatus(QuestionConstant.QU_STATUS_RELEASE);
        questionMapper.updateById(question);
        return ResultBetter.ok();
    }

    @Override
    public boolean removeQuestionById(Integer id, TbUser tbUser) {
        boolean delFlag = true;
        try {
            Question question = new Question();
            question.setId(id);
            String userId = tbUser.getId();
            question.setDel(QuestionConstant.DEL_DELETED);
            question.setUpdater(userId);
            question.setUpdateTime(new Date());
            questionMapper.updateById(question);
        } catch (Exception e) {
            delFlag = false;
            log.error(e.getMessage(), e);
        }
        return delFlag;
    }

    @Override
    public QuestionAndCategoryPageVo queryPageList(QuestionListParam questionListParam, Integer pageNo, Integer pageSize) {
        QuestionAndCategoryPageVo questionAndCategoryPageVo = new QuestionAndCategoryPageVo();
        QueryWrapper<TqmsQuotaCategory> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("is_single_disease", TqmsQuotaCategoryConstant.IS_SINGLE_DISEASE);
        List<TqmsQuotaCategory> quotaCategoryList = tqmsQuotaCategoryMapper.selectList(queryWrapper);
        Map<Long, String> quotaCategoryMap = quotaCategoryList.stream().collect(Collectors.toMap(TqmsQuotaCategory::getCategoryId, TqmsQuotaCategory::getCategoryName, (k1, k2) -> k1));

        List<TbData> dataList = tbDataService.selectByDataType(TbDataConstant.DATA_TYPE_QUESTION_CHECK_CATEGORY);
        Map<String, String> dataMap = dataList.stream().collect(Collectors.toMap(TbData::getId, TbData::getValue, (k1, k2) -> k1));

        Page<Question> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        if (StringUtils.isNotBlank(questionListParam.getQuName())) {
            lambda.like(Question::getQuName, questionListParam.getQuName());
        }
        if (Objects.nonNull(questionListParam.getCategoryType())) {
            lambda.eq(Question::getCategoryType, questionListParam.getCategoryType());
        }
        if (StringUtils.isNotBlank(questionListParam.getCategoryId())) {
            lambda.eq(Question::getCategoryId, questionListParam.getCategoryId());
        }
        if (Objects.nonNull(questionListParam.getWriteFrequency())) {
            lambda.eq(Question::getWriteFrequency, questionListParam.getWriteFrequency());
        }
        if (StringUtils.isNotBlank(questionListParam.getWriteDeptId())) {
            lambda.like(Question::getDeptIds, questionListParam.getWriteDeptId());
        }
        if (StringUtils.isNotBlank(questionListParam.getSeeDeptId())) {
            lambda.like(Question::getSeeDeptIds, questionListParam.getSeeDeptId());
        }
        List<Integer> idListByCheckedDeptId = Lists.newArrayList();
        if (StringUtils.isNotBlank(questionListParam.getCheckedDeptId())) {
            List<QuestionCheckedDept> questionCheckedDeptList = questionCheckedDeptService.selectCheckedDeptByDeptId(questionListParam.getCheckedDeptId(), null, QuestionCheckedDeptConstant.TYPE_CHECKED_DEPT);
            idListByCheckedDeptId = questionCheckedDeptList.stream().map(QuestionCheckedDept::getQuId).collect(Collectors.toList());
        }
        if(CollectionUtil.isNotEmpty(idListByCheckedDeptId)){
            lambda.in(Question::getId, idListByCheckedDeptId);
        }
        if (Objects.nonNull(questionListParam.getQuStatus())) {
            lambda.eq(Question::getQuStatus, questionListParam.getQuStatus());
        }

        lambda.in(Question::getDel, QuestionConstant.DEL_NORMAL);
        lambda.orderByDesc(Question::getCreateTime);
        IPage<Question> questionIPage = this.page(page, lambda);
        List<Question> questionList = questionIPage.getRecords();

        List<String> checkProjectIdList = questionList.stream().map(Question::getCheckProjectId).distinct().collect(Collectors.toList());
        Map<String, TbCheckProject> tbCheckProjectMap = Maps.newHashMap();
        if(CollectionUtil.isNotEmpty(checkProjectIdList)){
            Collection<TbCheckProject> tbCheckProjects = tbCheckProjectService.listByIds(checkProjectIdList);
            tbCheckProjectMap = tbCheckProjects.stream().collect(Collectors.toMap(TbCheckProject::getId, Function.identity()));
        }

        ArrayList<QuestionAndCategoryVo> resList = Lists.newArrayList();
        for (Question question : questionList) {
            QuestionAndCategoryVo questionAndCategoryVo = new QuestionAndCategoryVo();
            BeanUtils.copyProperties(question,questionAndCategoryVo);
            Integer categoryType = questionAndCategoryVo.getCategoryType();
            if(QuestionConstant.CATEGORY_TYPE_REGISTER.equals(categoryType)){
                questionAndCategoryVo.setCategoryName("登记表");
            }else{
                String categoryId = questionAndCategoryVo.getCategoryId();
                if (StringUtils.isNotBlank(categoryId)) {
                    if(QuestionConstant.CATEGORY_TYPE_SINGLE_DISEASE.equals(categoryType)){
                        questionAndCategoryVo.setCategoryName(quotaCategoryMap.get(Long.parseLong(categoryId)));
                    }else if(QuestionConstant.CATEGORY_TYPE_CHECK.equals(categoryType)){
                        questionAndCategoryVo.setCategoryName(dataMap.get(categoryId));
                    }
                }
            }
            String checkProjectId = question.getCheckProjectId();
            if(StringUtils.isNotBlank(checkProjectId)){
                TbCheckProject tbCheckProject = tbCheckProjectMap.get(checkProjectId);
                if(Objects.nonNull(tbCheckProject)){
                    questionAndCategoryVo.setCheckProjectName(tbCheckProject.getName());
                }
            }

            resList.add(questionAndCategoryVo);
        }
        questionAndCategoryPageVo.setTotal(questionIPage.getTotal());
        questionAndCategoryPageVo.setQuestionList(resList);
        return questionAndCategoryPageVo;
    }

    @Override
    public QuestionPageVo questionFillInList(QuestionParam questionParam, Integer pageNo, Integer pageSize) {
        QuestionPageVo questionPageVo = new QuestionPageVo();
        Map<String, Object> params = new HashMap<>();
        params.put("quName", questionParam.getQuName());
        params.put("quDesc", questionParam.getQuDesc());
        params.put("startRow", (pageNo - 1) * pageSize);
        params.put("pageSize", pageSize);
        int total = questionMapper.questionFillInListCount(params);
        List<Question> questionList = questionMapper.questionFillInList(params);
        questionPageVo.setTotal(total);
        questionPageVo.setQuestionList(questionList);
        return questionPageVo;
    }


    @Override
    public IPage<QuestionCheckVo> checkQuestionList(QuestionCheckParam questionCheckParam, Integer pageNo, Integer pageSize, Data data) {
        String quName = questionCheckParam.getQuName();
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
//        String deptId = data.getTbUser().getDepId();
        String deptId = data.getTbUser().getDepId();
        String positionId = data.getTbUser().getPositionId();
        String userId = data.getTbUser().getId();
        lambda.like(Question::getDeptIds,deptId);
        boolean checkPositionFlag = checkPosition(quName, deptId,positionId,userId, lambda);
        if(checkPositionFlag){
            return new Page<>();
        }
        Page<Question> page = new Page<>(pageNo, pageSize);
        IPage<Question> iPage = this.page(page,lambda);
        List<Question> questionList = iPage.getRecords();
        if(questionList.isEmpty()){
            return new Page<>();
        }

        List<QuestionCheckVo> answerPatientFillingInVos = questionList.stream().map(q -> {
            QuestionCheckVo vo = new QuestionCheckVo();
            BeanUtils.copyProperties(q,vo);
            return vo;
        }).collect(Collectors.toList());

        IPage<QuestionCheckVo> questionCheckPageVoPage = new Page<>(pageNo,pageSize);
        questionCheckPageVoPage.setRecords(answerPatientFillingInVos);
        questionCheckPageVoPage.setTotal(iPage.getTotal());
        return questionCheckPageVoPage;
    }

    @Override
    public List<QuestionCheckClassificationVo> checkQuestionClassificationList(QuestionCheckParam questionCheckParam, Data data) {
        String deptId = data.getTbUser().getDepId();
        String positionId = data.getTbUser().getPositionId();
        String userId = data.getTbUser().getId();

        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.like(Question::getDeptIds,deptId);
        boolean checkPositionFlag = checkPosition(questionCheckParam.getQuName(), deptId,positionId,userId, lambda);
        if(checkPositionFlag){
            return Lists.newArrayList();
        }

        List<Question> questionList = this.list(lambda);
        if(questionList.isEmpty()){
            return Lists.newArrayList();
        }

        //查分类
        List<QuestionCheckClassification> questionCheckClassificationList = questionCheckClassificationService.selectByUserId(userId);
        if(CollectionUtil.isEmpty(questionCheckClassificationList)){
            List<QuestionCheckClassificationVo> resList = questionList.stream().map(q -> {
                QuestionCheckClassificationVo vo = new QuestionCheckClassificationVo();
                vo.setQuId(q.getId());
                vo.setIcon(q.getIcon());
                vo.setQuName(q.getQuName());
                vo.setType(QuestionCheckClassificationConstant.QUESTION_CHECK_CLASSIFICATION_VO_TYPE_QUESTION);
                return vo;
            }).collect(Collectors.toList());
            return resList;
        }

        List<Integer> questionCheckClassificationIdList = questionCheckClassificationList.stream().map(QuestionCheckClassification::getId).collect(Collectors.toList());
        List<Integer> questionIdList = questionList.stream().map(Question::getId).distinct().collect(Collectors.toList());

        Map<Integer, QuestionCheckClassification> questionCheckClassificationMap = questionCheckClassificationList.stream().collect(Collectors.toMap(QuestionCheckClassification::getId, Function.identity()));

        //查分类关联关系
        List<QuestionCheckClassificationRel> questionCheckClassificationRelList = questionCheckClassificationRelService
                .selectByQuestionCheckClassificationAndQuestionIdList(questionIdList,questionCheckClassificationIdList);
        if(CollectionUtil.isEmpty(questionCheckClassificationRelList)){
            List<Integer> questionCheckClassificationRelClassificationIdList = questionCheckClassificationRelList.stream().map(QuestionCheckClassificationRel::getQuestionCheckClassificationId).distinct().collect(Collectors.toList());
            List<QuestionCheckClassificationVo> resList = questionCheckClassificationRelClassificationIdList.stream().map(classificationRel -> {
                QuestionCheckClassificationVo vo = new QuestionCheckClassificationVo();
                QuestionCheckClassification classification = questionCheckClassificationMap.get(classificationRel);
                if(Objects.nonNull(classification)){
                    vo.setQuestionCheckClassificationId(classification.getId());
                    vo.setQuestionCheckClassificationName(classification.getName());
                    vo.setType(QuestionCheckClassificationConstant.QUESTION_CHECK_CLASSIFICATION_VO_TYPE_CLASSIFICATION);
                }
                return vo;
            }).collect(Collectors.toList());
            List<QuestionCheckClassificationVo> questionResList = questionList.stream().map(q -> {
                QuestionCheckClassificationVo vo = new QuestionCheckClassificationVo();
                vo.setQuId(q.getId());
                vo.setIcon(q.getIcon());
                vo.setQuName(q.getQuName());
                vo.setType(QuestionCheckClassificationConstant.QUESTION_CHECK_CLASSIFICATION_VO_TYPE_QUESTION);
                return vo;
            }).collect(Collectors.toList());
            resList.addAll(questionResList);
            return resList;
        }

        List<Integer> questionCheckClassificationRelQuestionIdList = questionCheckClassificationRelList.stream().map(QuestionCheckClassificationRel::getQuestionId).distinct().collect(Collectors.toList());
        questionList.removeIf(q->questionCheckClassificationRelQuestionIdList.contains(q.getId()));

        List<Integer> questionCheckClassificationRelClassificationIdList = questionCheckClassificationRelList.stream().map(QuestionCheckClassificationRel::getQuestionCheckClassificationId).distinct().collect(Collectors.toList());
        List<QuestionCheckClassificationVo> resList = questionCheckClassificationRelClassificationIdList.stream().map(classificationRel -> {
            QuestionCheckClassificationVo vo = new QuestionCheckClassificationVo();
            QuestionCheckClassification classification = questionCheckClassificationMap.get(classificationRel);
            if(Objects.nonNull(classification)){
                vo.setQuestionCheckClassificationId(classification.getId());
                vo.setQuestionCheckClassificationName(classification.getName());
                vo.setType(QuestionCheckClassificationConstant.QUESTION_CHECK_CLASSIFICATION_VO_TYPE_CLASSIFICATION);
            }
            return vo;
        }).collect(Collectors.toList());
        List<QuestionCheckClassificationVo> questionResList = questionList.stream().map(q -> {
            QuestionCheckClassificationVo vo = new QuestionCheckClassificationVo();
            vo.setQuId(q.getId());
            vo.setIcon(q.getIcon());
            vo.setQuName(q.getQuName());
            vo.setType(QuestionCheckClassificationConstant.QUESTION_CHECK_CLASSIFICATION_VO_TYPE_QUESTION);
            return vo;
        }).collect(Collectors.toList());
        resList.addAll(questionResList);
        return resList;
    }

    @Override
    public List<QuestionCheckVo> checkQuestionClassificationSubsetList(QuestionCheckClassificationParam param, Data data) {
        String deptId = data.getTbUser().getDepId();
        String positionId = data.getTbUser().getPositionId();
        String userId = data.getTbUser().getId();

        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.like(Question::getDeptIds,deptId);
        boolean checkPositionFlag = checkPosition(param.getQuName(), deptId,positionId,userId, lambda);
        if(checkPositionFlag){
            return Lists.newArrayList();
        }

        //查分类
        List<QuestionCheckClassificationRel> questionCheckClassificationRelList = questionCheckClassificationRelService.selectByQuestionCheckClassification(Lists.newArrayList(param.getQuestionCheckClassificationId()));
        if(CollectionUtil.isEmpty(questionCheckClassificationRelList)){
            return Lists.newArrayList();
        }

        List<Integer> questionCheckClassificationRelIdList = questionCheckClassificationRelList.stream().map(QuestionCheckClassificationRel::getQuestionId).distinct().collect(Collectors.toList());
        lambda.in(Question::getId,questionCheckClassificationRelIdList);

        List<Question> questionList =  this.list(lambda);
        if(questionList.isEmpty()){
            return Lists.newArrayList();
        }

        List<QuestionCheckVo> answerPatientFillingInVos = questionList.stream().map(q -> {
            QuestionCheckVo vo = new QuestionCheckVo();
            BeanUtils.copyProperties(q,vo);
            return vo;
        }).collect(Collectors.toList());

        return answerPatientFillingInVos;
    }

    @Override
    public List<QuestionNameVo> queryCheckQuestionClassification(String name, Data data) {
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        if(StringUtils.isNotBlank(name)){
            lambda.in(Question::getQuName,name);
        }
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getCategoryType,QuestionConstant.CATEGORY_TYPE_CHECK);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        String userId = data.getTbUser().getId();
        String deptId = data.getTbUser().getDepId();
        String positionId = data.getTbUser().getPositionId();

        lambda.like(Question::getDeptIds,deptId);

        boolean checkPositionFlag = checkPosition(name, deptId,positionId,userId, lambda);
        if(checkPositionFlag){
            return Lists.newArrayList();
        }

        List<QuestionCheckClassification> questionCheckClassificationList = questionCheckClassificationService.selectByUserId(userId);
        if(CollectionUtil.isNotEmpty(questionCheckClassificationList)){
            List<Integer> questionCheckClassificationIdList = questionCheckClassificationList.stream().map(QuestionCheckClassification::getId).collect(Collectors.toList());
            List<QuestionCheckClassificationRel> questionCheckClassificationRelList = questionCheckClassificationRelService.selectByQuestionCheckClassification(questionCheckClassificationIdList);
            if(CollectionUtil.isNotEmpty(questionCheckClassificationRelList)){
                List<Integer> questionIdList = questionCheckClassificationRelList.stream().map(QuestionCheckClassificationRel::getQuestionId).distinct().collect(Collectors.toList());
                lambda.notIn(Question::getId,questionIdList);
            }
        }

        List<Question> list = this.list(lambda);
        List<QuestionNameVo> resList = list.stream().map(q -> {
            QuestionNameVo vo = new QuestionNameVo();
            BeanUtils.copyProperties(q, vo);
            return vo;
        }).collect(Collectors.toList());
        return resList;
    }

    @Override
    public List<QuestionNameVo> queryCheckQuestionClassificationByUpdate(QueryCheckQuestionClassificationByUpdateParam param, Data data) {
        String name = param.getQuName();
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        if(StringUtils.isNotBlank(name)){
            lambda.in(Question::getQuName,name);
        }
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getCategoryType,QuestionConstant.CATEGORY_TYPE_CHECK);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        String userId = data.getTbUser().getId();
        String deptId = data.getTbUser().getDepId();
        String positionId = data.getTbUser().getPositionId();

        lambda.like(Question::getDeptIds,deptId);

        boolean checkPositionFlag = checkPosition(name, deptId,positionId,userId, lambda);
        if(checkPositionFlag){
            return Lists.newArrayList();
        }

        List<QuestionCheckClassification> questionCheckClassificationList = questionCheckClassificationService.selectByUserIdNotEqualId(userId,param.getQuestionCheckClassificationId());
        if(CollectionUtil.isNotEmpty(questionCheckClassificationList)){
            List<Integer> questionCheckClassificationIdList = questionCheckClassificationList.stream().map(QuestionCheckClassification::getId).collect(Collectors.toList());
            List<QuestionCheckClassificationRel> questionCheckClassificationRelList = questionCheckClassificationRelService.selectByQuestionCheckClassification(questionCheckClassificationIdList);
            if(CollectionUtil.isNotEmpty(questionCheckClassificationRelList)){
                List<Integer> questionIdList = questionCheckClassificationRelList.stream().map(QuestionCheckClassificationRel::getQuestionId).distinct().collect(Collectors.toList());
                lambda.notIn(Question::getId,questionIdList);
            }
        }

        List<Question> list = this.list(lambda);
        List<QuestionNameVo> resList = list.stream().map(q -> {
            QuestionNameVo vo = new QuestionNameVo();
            BeanUtils.copyProperties(q, vo);
            return vo;
        }).collect(Collectors.toList());
        return resList;
    }

    @Override
    public List<QuestionStatisticsCheckVo> statisticsCheckList(QuestionCheckParam questionCheckParam) {
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        String quName = questionCheckParam.getQuName();
        if(StringUtils.isNotBlank(quName)){
            lambda.like(Question::getQuName, quName);
        }
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getCategoryType,QuestionConstant.CATEGORY_TYPE_CHECK);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        List<Question> questionList = this.list(lambda);
        if(questionList.isEmpty()){
            return Lists.newArrayList();
        }

        List<QuestionStatisticsCheckVo> statisticsCheckList = questionList.stream().map(q -> {
            QuestionStatisticsCheckVo vo = new QuestionStatisticsCheckVo();
            BeanUtils.copyProperties(q,vo);
            return vo;
        }).collect(Collectors.toList());

        return statisticsCheckList;
    }

    @Override
    public List<CheckQuestionHistoryStatisticVo> checkQuestionHistoryStatisticList(QuestionCheckParam questionCheckParam, Data data) {
        String deptId = data.getTbUser().getDepId();
        String positionId = data.getTbUser().getPositionId();
        String userId = data.getTbUser().getId();
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.like(Question::getSeeDeptIds,deptId);
//        lambda.like(Question::getDeptIds,deptId);
        boolean checkPositionFlag = checkPosition(questionCheckParam.getQuName(), deptId,positionId,userId, lambda);
        if(checkPositionFlag){
            return Lists.newArrayList();
        }

        List<Question> list = this.list(lambda);
        if(list.isEmpty()){
            return Lists.newArrayList();
        }

        List<String> questionIds = list.stream().map(q-> String.valueOf(q.getId())).collect(Collectors.toList());
        //查询是否显示后面三个类型
        List<TbInspectStatsTemplateDep> templateDepList= tbInspectStatsTemplateDepService.selectByQuestionIds(deptId,questionIds);
        Map<String, ArrayList<TbInspectStatsTemplateDep>> templateDepMap = templateDepList.stream().collect(
                Collectors.toMap(TbInspectStatsTemplateDep::getQuId, Lists::newArrayList, (ArrayList<TbInspectStatsTemplateDep> k1, ArrayList<TbInspectStatsTemplateDep> k2) -> {
            k1.addAll(k2);
            return k1;
        }));

        List<CheckQuestionHistoryStatisticVo> checkQuestionHistoryStatisticVos = list.stream().map(q -> {
            CheckQuestionHistoryStatisticVo vo = new CheckQuestionHistoryStatisticVo();
            BeanUtils.copyProperties(q,vo);
            ArrayList<TbInspectStatsTemplateDep> tbInspectStatsTemplateDepList = templateDepMap.get(String.valueOf(q.getId()));
            vo.setDeptStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
            vo.setDefectStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
            vo.setCategoryStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
            if(tbInspectStatsTemplateDepList!=null && !tbInspectStatsTemplateDepList.isEmpty()){
                for (TbInspectStatsTemplateDep tbInspectStatsTemplateDep : tbInspectStatsTemplateDepList) {
                    if(TbInspectStatsTemplateDepConstant.TYPE_DEPT.equals(tbInspectStatsTemplateDep.getType())){
                        vo.setDeptStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                    }else if(TbInspectStatsTemplateDepConstant.TYPE_DEFECT.equals(tbInspectStatsTemplateDep.getType())){
                        vo.setDefectStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                    }else if(TbInspectStatsTemplateDepConstant.TYPE_CATEGORY.equals(tbInspectStatsTemplateDep.getType())){
                        vo.setCategoryStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                    }
                }
            }
            return vo;
        }).collect(Collectors.toList());
        return checkQuestionHistoryStatisticVos;
    }

    @Override
    public List<CheckQuestionHistoryStatisticClassificationVo> checkQuestionHistoryStatisticClassificationList(QuestionCheckParam questionCheckParam, Data data) {
        String deptId = data.getTbUser().getDepId();
        String positionId = data.getTbUser().getPositionId();
        String userId = data.getTbUser().getId();

        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.like(Question::getSeeDeptIds,deptId);
        boolean checkPositionFlag = checkPosition(questionCheckParam.getQuName(), deptId,positionId,userId, lambda);
        if(checkPositionFlag){
            return Lists.newArrayList();
        }

        List<Question> questionList = this.list(lambda);
        if(questionList.isEmpty()){
            return Lists.newArrayList();
        }

        List<String> questionIds = questionList.stream().map(q-> String.valueOf(q.getId())).collect(Collectors.toList());
        //查询是否显示后面三个类型
        List<TbInspectStatsTemplateDep> templateDepList= tbInspectStatsTemplateDepService.selectByQuestionIds(deptId,questionIds);
        Map<String, ArrayList<TbInspectStatsTemplateDep>> templateDepMap = templateDepList.stream().collect(
                Collectors.toMap(TbInspectStatsTemplateDep::getQuId, Lists::newArrayList, (ArrayList<TbInspectStatsTemplateDep> k1, ArrayList<TbInspectStatsTemplateDep> k2) -> {
                    k1.addAll(k2);
                    return k1;
                }));

        //查分类
        List<QuestionCheckClassification> questionCheckClassificationList = questionCheckClassificationService.selectByUserId(userId);
        if(CollectionUtil.isEmpty(questionCheckClassificationList)){
            List<CheckQuestionHistoryStatisticClassificationVo> resList = questionList.stream().map(q -> {
                CheckQuestionHistoryStatisticClassificationVo vo = new CheckQuestionHistoryStatisticClassificationVo();
                vo.setQuId(q.getId());
                vo.setIcon(q.getIcon());
                vo.setQuName(q.getQuName());
                vo.setType(QuestionCheckClassificationConstant.QUESTION_CHECK_CLASSIFICATION_VO_TYPE_QUESTION);

                ArrayList<TbInspectStatsTemplateDep> tbInspectStatsTemplateDepList = templateDepMap.get(String.valueOf(q.getId()));
                vo.setDeptStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
                vo.setDefectStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
                vo.setCategoryStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
                if(tbInspectStatsTemplateDepList!=null && !tbInspectStatsTemplateDepList.isEmpty()){
                    for (TbInspectStatsTemplateDep tbInspectStatsTemplateDep : tbInspectStatsTemplateDepList) {
                        if(TbInspectStatsTemplateDepConstant.TYPE_DEPT.equals(tbInspectStatsTemplateDep.getType())){
                            vo.setDeptStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                        }else if(TbInspectStatsTemplateDepConstant.TYPE_DEFECT.equals(tbInspectStatsTemplateDep.getType())){
                            vo.setDefectStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                        }else if(TbInspectStatsTemplateDepConstant.TYPE_CATEGORY.equals(tbInspectStatsTemplateDep.getType())){
                            vo.setCategoryStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                        }
                    }
                }

                return vo;
            }).collect(Collectors.toList());
            return resList;
        }

        List<Integer> questionCheckClassificationIdList = questionCheckClassificationList.stream().map(QuestionCheckClassification::getId).collect(Collectors.toList());
        List<Integer> questionIdList = questionList.stream().map(Question::getId).distinct().collect(Collectors.toList());

        Map<Integer, QuestionCheckClassification> questionCheckClassificationMap = questionCheckClassificationList.stream().collect(Collectors.toMap(QuestionCheckClassification::getId, Function.identity()));

        //查分类关联关系
        List<QuestionCheckClassificationRel> questionCheckClassificationRelList = questionCheckClassificationRelService
                .selectByQuestionCheckClassificationAndQuestionIdList(questionIdList,questionCheckClassificationIdList);
        if(CollectionUtil.isEmpty(questionCheckClassificationRelList)){

            List<Integer> questionCheckClassificationRelClassificationIdList = questionCheckClassificationRelList.stream().map(QuestionCheckClassificationRel::getQuestionCheckClassificationId).distinct().collect(Collectors.toList());
            List<CheckQuestionHistoryStatisticClassificationVo> resList = questionCheckClassificationRelClassificationIdList.stream().map(classificationRel -> {
                CheckQuestionHistoryStatisticClassificationVo vo = new CheckQuestionHistoryStatisticClassificationVo();
                QuestionCheckClassification classification = questionCheckClassificationMap.get(classificationRel);
                if(Objects.nonNull(classification)){
                    vo.setQuestionCheckClassificationId(classification.getId());
                    vo.setQuestionCheckClassificationName(classification.getName());
                    vo.setType(QuestionCheckClassificationConstant.QUESTION_CHECK_CLASSIFICATION_VO_TYPE_CLASSIFICATION);
                }
                return vo;
            }).collect(Collectors.toList());

            List<CheckQuestionHistoryStatisticClassificationVo> questionResList = questionList.stream().map(q -> {
                CheckQuestionHistoryStatisticClassificationVo vo = new CheckQuestionHistoryStatisticClassificationVo();
                vo.setQuId(q.getId());
                vo.setIcon(q.getIcon());
                vo.setQuName(q.getQuName());
                vo.setType(QuestionCheckClassificationConstant.QUESTION_CHECK_CLASSIFICATION_VO_TYPE_QUESTION);

                ArrayList<TbInspectStatsTemplateDep> tbInspectStatsTemplateDepList = templateDepMap.get(String.valueOf(q.getId()));
                vo.setDeptStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
                vo.setDefectStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
                vo.setCategoryStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
                if(tbInspectStatsTemplateDepList!=null && !tbInspectStatsTemplateDepList.isEmpty()){
                    for (TbInspectStatsTemplateDep tbInspectStatsTemplateDep : tbInspectStatsTemplateDepList) {
                        if(TbInspectStatsTemplateDepConstant.TYPE_DEPT.equals(tbInspectStatsTemplateDep.getType())){
                            vo.setDeptStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                        }else if(TbInspectStatsTemplateDepConstant.TYPE_DEFECT.equals(tbInspectStatsTemplateDep.getType())){
                            vo.setDefectStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                        }else if(TbInspectStatsTemplateDepConstant.TYPE_CATEGORY.equals(tbInspectStatsTemplateDep.getType())){
                            vo.setCategoryStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                        }
                    }
                }

                return vo;
            }).collect(Collectors.toList());
            resList.addAll(questionResList);
            return resList;
        }

        List<Integer> questionCheckClassificationRelQuestionIdList = questionCheckClassificationRelList.stream().map(QuestionCheckClassificationRel::getQuestionId).distinct().collect(Collectors.toList());
        questionList.removeIf(q->questionCheckClassificationRelQuestionIdList.contains(q.getId()));

        List<Integer> questionCheckClassificationRelClassificationIdList = questionCheckClassificationRelList.stream().map(QuestionCheckClassificationRel::getQuestionCheckClassificationId).distinct().collect(Collectors.toList());
        List<CheckQuestionHistoryStatisticClassificationVo> resList = questionCheckClassificationRelClassificationIdList.stream().map(classificationRel -> {
            CheckQuestionHistoryStatisticClassificationVo vo = new CheckQuestionHistoryStatisticClassificationVo();
            QuestionCheckClassification classification = questionCheckClassificationMap.get(classificationRel);
            if(Objects.nonNull(classification)){
                vo.setQuestionCheckClassificationId(classification.getId());
                vo.setQuestionCheckClassificationName(classification.getName());
                vo.setType(QuestionCheckClassificationConstant.QUESTION_CHECK_CLASSIFICATION_VO_TYPE_CLASSIFICATION);
            }
            return vo;
        }).collect(Collectors.toList());
        List<CheckQuestionHistoryStatisticClassificationVo> questionResList = questionList.stream().map(q -> {
            CheckQuestionHistoryStatisticClassificationVo vo = new CheckQuestionHistoryStatisticClassificationVo();
            vo.setQuId(q.getId());
            vo.setIcon(q.getIcon());
            vo.setQuName(q.getQuName());
            vo.setType(QuestionCheckClassificationConstant.QUESTION_CHECK_CLASSIFICATION_VO_TYPE_QUESTION);

            ArrayList<TbInspectStatsTemplateDep> tbInspectStatsTemplateDepList = templateDepMap.get(String.valueOf(q.getId()));
            vo.setDeptStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
            vo.setDefectStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
            vo.setCategoryStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
            if(tbInspectStatsTemplateDepList!=null && !tbInspectStatsTemplateDepList.isEmpty()){
                for (TbInspectStatsTemplateDep tbInspectStatsTemplateDep : tbInspectStatsTemplateDepList) {
                    if(TbInspectStatsTemplateDepConstant.TYPE_DEPT.equals(tbInspectStatsTemplateDep.getType())){
                        vo.setDeptStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                    }else if(TbInspectStatsTemplateDepConstant.TYPE_DEFECT.equals(tbInspectStatsTemplateDep.getType())){
                        vo.setDefectStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                    }else if(TbInspectStatsTemplateDepConstant.TYPE_CATEGORY.equals(tbInspectStatsTemplateDep.getType())){
                        vo.setCategoryStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                    }
                }
            }

            return vo;
        }).collect(Collectors.toList());
        resList.addAll(questionResList);

        return resList;
    }

    @Override
    public List<CheckQuestionHistoryStatisticVo> checkQuestionHistoryStatisticClassificationSubsetList(QuestionCheckClassificationParam param, Data data) {
        String deptId = data.getTbUser().getDepId();
        String positionId = data.getTbUser().getPositionId();
        String userId = data.getTbUser().getId();

        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.like(Question::getSeeDeptIds,deptId);
        boolean checkPositionFlag = checkPosition(param.getQuName(), deptId,positionId,userId, lambda);
        if(checkPositionFlag){
            return Lists.newArrayList();
        }

        //查分类
        List<QuestionCheckClassificationRel> questionCheckClassificationRelList = questionCheckClassificationRelService.selectByQuestionCheckClassification(Lists.newArrayList(param.getQuestionCheckClassificationId()));
        if(CollectionUtil.isEmpty(questionCheckClassificationRelList)){
            return Lists.newArrayList();
        }

        List<Integer> questionCheckClassificationRelIdList = questionCheckClassificationRelList.stream().map(QuestionCheckClassificationRel::getQuestionId).distinct().collect(Collectors.toList());
        lambda.in(Question::getId,questionCheckClassificationRelIdList);

        List<Question> questionList = this.list(lambda);
        if(questionList.isEmpty()){
            return Lists.newArrayList();
        }


        List<String> questionIds = questionList.stream().map(q-> String.valueOf(q.getId())).collect(Collectors.toList());
        //查询是否显示后面三个类型
        List<TbInspectStatsTemplateDep> templateDepList= tbInspectStatsTemplateDepService.selectByQuestionIds(deptId,questionIds);
        Map<String, ArrayList<TbInspectStatsTemplateDep>> templateDepMap = templateDepList.stream().collect(
                Collectors.toMap(TbInspectStatsTemplateDep::getQuId, Lists::newArrayList, (ArrayList<TbInspectStatsTemplateDep> k1, ArrayList<TbInspectStatsTemplateDep> k2) -> {
                    k1.addAll(k2);
                    return k1;
                }));

        List<CheckQuestionHistoryStatisticVo> checkQuestionHistoryStatisticVos = questionList.stream().map(q -> {
            CheckQuestionHistoryStatisticVo vo = new CheckQuestionHistoryStatisticVo();
            BeanUtils.copyProperties(q,vo);
            ArrayList<TbInspectStatsTemplateDep> tbInspectStatsTemplateDepList = templateDepMap.get(String.valueOf(q.getId()));
            vo.setDeptStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
            vo.setDefectStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
            vo.setCategoryStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
            if(tbInspectStatsTemplateDepList!=null && !tbInspectStatsTemplateDepList.isEmpty()){
                for (TbInspectStatsTemplateDep tbInspectStatsTemplateDep : tbInspectStatsTemplateDepList) {
                    if(TbInspectStatsTemplateDepConstant.TYPE_DEPT.equals(tbInspectStatsTemplateDep.getType())){
                        vo.setDeptStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                    }else if(TbInspectStatsTemplateDepConstant.TYPE_DEFECT.equals(tbInspectStatsTemplateDep.getType())){
                        vo.setDefectStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                    }else if(TbInspectStatsTemplateDepConstant.TYPE_CATEGORY.equals(tbInspectStatsTemplateDep.getType())){
                        vo.setCategoryStatisticShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
                    }
                }
            }
            return vo;
        }).collect(Collectors.toList());
        return checkQuestionHistoryStatisticVos;
    }

    @Override
    public List<CheckQuestionParameterSetListVo> checkQuestionParameterSetList(QuestionCheckParam questionCheckParam, Data data) {
        String quName = questionCheckParam.getQuName();
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
//        String deptId = data.getTbUser().getDepId();
        String deptId = data.getTbUser().getDepId();
        String positionId = data.getTbUser().getPositionId();
        String userId = data.getTbUser().getId();
        lambda.like(Question::getDeptIds,deptId);
        boolean checkPositionFlag = checkPosition(quName, deptId,positionId,userId, lambda);
        if(checkPositionFlag){
            return Lists.newArrayList();
        }
        List<Question> list = this.list(lambda);
        if(list.isEmpty()){
            return Lists.newArrayList();
        }

        List<String> questionIds = list.stream().map(q-> String.valueOf(q.getId())).collect(Collectors.toList());
        //查询是否显示 设置统计图形
        List<TbInspectStatsTemplateDep> templateDepList= tbInspectStatsTemplateDepService.selectDeptStatisticsByQuestionIds(deptId,questionIds);
        Map<String, ArrayList<TbInspectStatsTemplateDep>> templateDepMap = templateDepList.stream().collect(
                Collectors.toMap(TbInspectStatsTemplateDep::getQuId, Lists::newArrayList, (ArrayList<TbInspectStatsTemplateDep> k1, ArrayList<TbInspectStatsTemplateDep> k2) -> {
                    k1.addAll(k2);
                    return k1;
                }));

        return list.stream().map(q -> {
            CheckQuestionParameterSetListVo vo = new CheckQuestionParameterSetListVo();
            BeanUtils.copyProperties(q,vo);
            ArrayList<TbInspectStatsTemplateDep> tbInspectStatsTemplateDepList = templateDepMap.get(String.valueOf(q.getId()));
            vo.setStatisticsChartShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_NO);
            if(tbInspectStatsTemplateDepList!=null && !tbInspectStatsTemplateDepList.isEmpty()){
                vo.setStatisticsChartShow(TbInspectStatsTemplateDepConstant.SHOW_TYPE_YES);
            }
            return vo;
        }).collect(Collectors.toList());

    }

    private boolean checkPosition(String quName, String deptId,String positionId,String userId, LambdaQueryWrapper<Question> lambda) {
        if(StringUtils.isNotBlank(quName)){
            lambda.like(Question::getQuName, quName);
        }
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getCategoryType,QuestionConstant.CATEGORY_TYPE_CHECK);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);

        //判断职位
        if(StringUtils.isBlank(positionId)){
            return  true;
        }
        if(Constant.POSITION_ID_LCKSZR.equals(positionId)){
            lambda.and(w->w.eq(Question::getCategoryId, Constant.QUESTION_CHECK_CATEGORY_YL)
                    .or().eq(Question::getCategoryId, Constant.QUESTION_CHECK_CATEGORY_YS_YL)
                    .or().eq(Question::getCategoryId, Constant.QUESTION_CHECK_CATEGORY_YG));
        }else if(Constant.POSITION_ID_LCKSZKY.equals(positionId) ){
            lambda.and(w->w.eq(Question::getCategoryId, Constant.QUESTION_CHECK_CATEGORY_YL)
                    .or().eq(Question::getCategoryId, Constant.QUESTION_CHECK_CATEGORY_YS_YL)
                    .or().eq(Question::getCategoryId, Constant.QUESTION_CHECK_CATEGORY_YG));
            //分配给自己的查检表
            List<QuestionCheckedDept> questionCheckedDeptList = questionCheckedDeptService.selectCheckedDeptByDeptId(userId, deptId, QuestionCheckedDeptConstant.TYPE_RESPONSIBILITY_USER);
            if(questionCheckedDeptList.isEmpty()){
                return  true;
            }
            List<Integer> quIdList = questionCheckedDeptList.stream().map(QuestionCheckedDept::getQuId).distinct().collect(Collectors.toList());
            if(quIdList.isEmpty()){
                return  true;
            }
            lambda.in(Question::getId,quIdList);
        }else if(Constant.POSITION_ID_LCKHSZ.equals(positionId)){
            lambda.and(w->w.eq(Question::getCategoryId, Constant.QUESTION_CHECK_CATEGORY_HL)
                    .or().eq(Question::getCategoryId, Constant.QUESTION_CHECK_CATEGORY_YG)
                    .or().eq(Question::getCategoryId, Constant.QUESTION_CHECK_CATEGORY_YS_HL));
        }else if( Constant.POSITION_ID_LCKSZKYHL.equals(positionId) ){
            //分配给自己的查检表
            lambda.and(w->w.eq(Question::getCategoryId, Constant.QUESTION_CHECK_CATEGORY_HL)
                    .or().eq(Question::getCategoryId, Constant.QUESTION_CHECK_CATEGORY_YG)
                    .or().eq(Question::getCategoryId, Constant.QUESTION_CHECK_CATEGORY_YS_HL));
            List<QuestionCheckedDept> questionCheckedDeptList = questionCheckedDeptService.selectCheckedDeptByDeptId(userId, deptId, QuestionCheckedDeptConstant.TYPE_RESPONSIBILITY_USER);
            if(questionCheckedDeptList.isEmpty()){
                return  true;
            }
            List<Integer> quIdList = questionCheckedDeptList.stream().map(QuestionCheckedDept::getQuId).distinct().collect(Collectors.toList());
            if(quIdList.isEmpty()){
                return  true;
            }
            lambda.in(Question::getId,quIdList);
        }else if( Constant.POSITION_ID_ZNKSZKY.equals(positionId) ){
            //分配给自己的查检表
            List<QuestionCheckedDept> questionCheckedDeptList = questionCheckedDeptService.selectCheckedDeptByDeptId(userId, deptId, QuestionCheckedDeptConstant.TYPE_RESPONSIBILITY_USER);
            if(questionCheckedDeptList.isEmpty()){
                return  true;
            }
            List<Integer> quIdList = questionCheckedDeptList.stream().map(QuestionCheckedDept::getQuId).distinct().collect(Collectors.toList());
            if(quIdList.isEmpty()){
                return  true;
            }
            lambda.in(Question::getId,quIdList);
        }else if( Constant.POSITION_ID_YJKSZKY.equals(positionId) || Constant.POSITION_ID_YJKSZKYHL.equals(positionId) ){
            //分配给自己的查检表
            List<QuestionCheckedDept> questionCheckedDeptList = questionCheckedDeptService.selectCheckedDeptByDeptId(userId, deptId, QuestionCheckedDeptConstant.TYPE_RESPONSIBILITY_USER);
            if(questionCheckedDeptList.isEmpty()){
                return  true;
            }
            List<Integer> quIdList = questionCheckedDeptList.stream().map(QuestionCheckedDept::getQuId).distinct().collect(Collectors.toList());
            if(quIdList.isEmpty()){
                return  true;
            }
            lambda.in(Question::getId,quIdList);
        }
        return false;
    }





    @Override
    public List<CheckQuestionHistoryStatisticDeptListDeptVo> checkQuestionHistoryStatisticInspectedDeptList(CheckQuestionHistoryStatisticDeptListParam deptListParam, Data data) {
        String deptId = data.getTbUser().getDepId();
        TbDep tbDep = new TbDep();
        if (org.apache.commons.lang.StringUtils.isNotBlank(deptId)) {
            tbDep = tbDepService.getById(deptId);
        }
        String type = tbDep.getType();
        Integer quId = deptListParam.getQuId();
        List<QuestionCheckedDept> questionCheckedDeptList = questionCheckedDeptService.selectCheckedDeptByQuId(quId, null, QuestionCheckedDeptConstant.TYPE_CHECKED_DEPT);
        if(questionCheckedDeptList.isEmpty()){
            return Lists.newArrayList();
        }
        List<String> deptIdList = questionCheckedDeptList.stream().map(QuestionCheckedDept::getDeptId).collect(Collectors.toList());
        LambdaQueryWrapper<TbDep> tbDepLambda = new QueryWrapper<TbDep>().lambda();
        tbDepLambda.eq(TbDep::getIsdelete, Constant.IS_DELETE_NO);
        tbDepLambda.in(TbDep::getId,deptIdList);
        if(DeptUtil.isStaff(type)){
            //职能科室-上级督查 返回被检查科室关联表
        }else{
            //临床科室-上级督查 返回被检查科室关联表中的自己
            tbDepLambda.eq(TbDep::getId,deptId);
        }
        List<TbDep> list = tbDepService.list(tbDepLambda);
        List<CheckQuestionHistoryStatisticDeptListDeptVo> resList = list.stream().map(dep -> {
            CheckQuestionHistoryStatisticDeptListDeptVo vo = new CheckQuestionHistoryStatisticDeptListDeptVo();
            BeanUtils.copyProperties(dep, vo);
            vo.setDepartmentId(dep.getId());
            vo.setDepartmentName(dep.getDepname());
            return vo;
        }).collect(Collectors.toList());
        return resList;
    }

    @Override
    public List<CheckQuestionHistoryStatisticDeptListDeptVo> checkQuestionHistoryStatisticDeptList(CheckQuestionHistoryStatisticDeptListParam deptListParam, Data data) {
        Integer quId = deptListParam.getQuId();
        Question byId = this.getById(quId);
        ArrayList<CheckQuestionHistoryStatisticDeptListDeptVo> emptyList = Lists.newArrayList();
        if(byId==null){
            return emptyList;
        }
        String deptIds = byId.getDeptIds();
        if(StringUtils.isBlank(deptIds)){
            return emptyList;
        }
        ArrayList<String> deptIdList = Lists.newArrayList(deptIds.split(","));
        if(CollectionUtil.isEmpty(deptIdList)){
            return emptyList;
        }

        LambdaQueryWrapper<TbDep> tbDepLambda = new QueryWrapper<TbDep>().lambda();
        tbDepLambda.eq(TbDep::getType, Constant.DEPT_STAFF);
//        if(DeptUtil.isStaff(type)){
//            //职能科室-上级督查 返回被检查科室关联表
//        }else{
//            //临床科室-上级督查
//            String deptId = data.getTbUser().getDepId();
//            tbDepLambda.eq(TbDep::getId,deptId);
//        }
        tbDepLambda.eq(TbDep::getIsdelete, Constant.IS_DELETE_NO);
        tbDepLambda.in(TbDep::getId,deptIdList);
        List<TbDep> list = tbDepService.list(tbDepLambda);
        List<CheckQuestionHistoryStatisticDeptListDeptVo> resList = list.stream().map(dep -> {
            CheckQuestionHistoryStatisticDeptListDeptVo vo = new CheckQuestionHistoryStatisticDeptListDeptVo();
            BeanUtils.copyProperties(dep, vo);
            vo.setDepartmentId(dep.getId());
            vo.setDepartmentName(dep.getDepname());
            return vo;
        }).collect(Collectors.toList());
        return resList;
    }

    @Override
    public List<CheckQuestionHistoryStatisticDeptListDeptVo> checkQuestionHistoryStatisticSelfDeptList(CheckQuestionHistoryStatisticDeptListParam deptListParam, Data data) {
        Integer quId = deptListParam.getQuId();
        Question byId = this.getById(quId);
        ArrayList<CheckQuestionHistoryStatisticDeptListDeptVo> emptyList = Lists.newArrayList();
        if(byId==null){
            return emptyList;
        }
        String deptIds = byId.getDeptIds();
        if(StringUtils.isBlank(deptIds)){
            return emptyList;
        }
        ArrayList<String> deptIdList = Lists.newArrayList(deptIds.split(","));
        if(CollectionUtil.isEmpty(deptIdList)){
            return emptyList;
        }
        String deptId = data.getTbUser().getDepId();

        TbDep tbDep = new TbDep();
        if (org.apache.commons.lang.StringUtils.isNotBlank(deptId)) {
            tbDep = tbDepService.getById(deptId);
        }String type = tbDep.getType();
        LambdaQueryWrapper<TbDep> tbDepLambda = new QueryWrapper<TbDep>().lambda();
        tbDepLambda.and(w->w.eq(TbDep::getType, Constant.DEPT_CLINICAL).or().eq(TbDep::getType, Constant.DEP_MEDICAL_TECH));
        if(DeptUtil.isStaff(type)){
            //职能科室-科室自查
        }else{
            //临床科室-科室自查
            tbDepLambda.eq(TbDep::getId,deptId);
        }
        tbDepLambda.eq(TbDep::getIsdelete, Constant.IS_DELETE_NO);
        tbDepLambda.in(TbDep::getId,deptIdList);
        List<TbDep> list = tbDepService.list(tbDepLambda);
        List<CheckQuestionHistoryStatisticDeptListDeptVo> resList = list.stream().map(dep -> {
            CheckQuestionHistoryStatisticDeptListDeptVo vo = new CheckQuestionHistoryStatisticDeptListDeptVo();
            BeanUtils.copyProperties(dep, vo);
            vo.setDepartmentId(dep.getId());
            vo.setDepartmentName(dep.getDepname());
            return vo;
        }).collect(Collectors.toList());
        return resList;
    }

    @Override
    public void updateDeptIdsParam(UpdateDeptIdsParam updateDeptIdsParam) {
        String[] quIds = updateDeptIdsParam.getQuIds();
        String[] deptIds = updateDeptIdsParam.getDeptIds();
        if (quIds != null && deptIds != null) {
            StringBuffer deptid = new StringBuffer();
            for (String did : deptIds) {
                deptid.append(did);
                deptid.append(",");
            }
            ///更新
            for (String qid : quIds) {
                Question question = new Question();
                question.setId(Integer.parseInt(qid));
                question.setDeptIds(deptid.toString());
                question.setUpdateTime(new Date());
                questionMapper.updateById(question);
            }
        }
    }

    @Override
    public void updateSeeDeptIdsParam(UpdateDeptIdsParam updateDeptIdsParam) {
        String[] quIds = updateDeptIdsParam.getQuIds();
        String[] deptIds = updateDeptIdsParam.getDeptIds();
        if (quIds != null && deptIds != null) {
            StringBuffer deptid = new StringBuffer();
            for (String did : deptIds) {
                deptid.append(did);
                deptid.append(",");
            }
            ///更新
            for (String qid : quIds) {
                Question question = new Question();
                question.setId(Integer.parseInt(qid));
                question.setSeeDeptIds(deptid.toString());
                question.setUpdateTime(new Date());
                questionMapper.updateById(question);
            }
        }
    }



    @Override
    public void updateCheckedDeptIdsParam(UpdateCheckedDeptIdsParam updateCheckedDeptIdsParam) {
        Integer quId = updateCheckedDeptIdsParam.getQuId();
        Question byId = this.getById(quId);
        if(byId==null){
            return;
        }

        String[] deptIds = updateCheckedDeptIdsParam.getDeptIds();

        Date date = new Date();
        ArrayList<QuestionCheckedDept> addList = Lists.newArrayList();
        for (String deptId : deptIds) {
            QuestionCheckedDept questionCheckedDept = new QuestionCheckedDept();
            questionCheckedDept.setQuId(quId);
            questionCheckedDept.setDeptId(deptId);
            questionCheckedDept.setCreateTime(date);
            questionCheckedDept.setUpdateTime(date);
            questionCheckedDept.setType(QuestionCheckedDeptConstant.TYPE_CHECKED_DEPT);
            addList.add(questionCheckedDept);
        }
        questionCheckedDeptService.deleteCheckedDeptByQuId(quId,QuestionCheckedDeptConstant.TYPE_CHECKED_DEPT,null);
        questionCheckedDeptService.saveBatch(addList);
    }

    @Override
    public List<String> selectCheckedDeptIdsParam(SelectCheckedDeptIdsParam selectCheckedDeptIdsParam) {
        Integer quId = selectCheckedDeptIdsParam.getQuId();
        Question byId = this.getById(quId);
        if(byId==null || AnswerConstant.DEL_DELETED.equals(byId.getDel())){
            return Lists.newArrayList();
        }

        List<QuestionCheckedDept> questionCheckedDeptList = questionCheckedDeptService.selectCheckedDeptByQuId(quId, null, QuestionCheckedDeptConstant.TYPE_CHECKED_DEPT);
        return questionCheckedDeptList.stream().map(QuestionCheckedDept::getDeptId).collect(Collectors.toList());
    }

    @Override
    public void updateResponsibilityUserIdsParam(String deptId, UpdateResponsibilityUserIdsParam param) {
        Integer quId = param.getQuId();
        Question byId = this.getById(quId);
        if(byId==null){
            return;
        }

        String[] userIds = param.getUserIds();

        Date date = new Date();
        ArrayList<QuestionCheckedDept> addList = Lists.newArrayList();
        for (String userId : userIds) {
            QuestionCheckedDept questionCheckedDept = new QuestionCheckedDept();
            questionCheckedDept.setQuId(quId);
            questionCheckedDept.setDeptId(userId);
            questionCheckedDept.setCreateTime(date);
            questionCheckedDept.setUpdateTime(date);
            questionCheckedDept.setType(QuestionCheckedDeptConstant.TYPE_RESPONSIBILITY_USER);
            questionCheckedDept.setUserDeptId(deptId);
            addList.add(questionCheckedDept);
        }
        questionCheckedDeptService.deleteCheckedDeptByQuId(quId,QuestionCheckedDeptConstant.TYPE_RESPONSIBILITY_USER,deptId);
        questionCheckedDeptService.saveBatch(addList);
    }

    @Override
    public List<String> selectResponsibilityUserIdsParam(String deptId, SelectResponsibilityUserIdsParam param) {
        Integer quId = param.getQuId();
        Question byId = this.getById(quId);
        if(byId==null || AnswerConstant.DEL_DELETED.equals(byId.getDel())){
            return Lists.newArrayList();
        }

        List<QuestionCheckedDept> questionCheckedDeptList = questionCheckedDeptService.selectCheckedDeptByQuId(quId,deptId,QuestionCheckedDeptConstant.TYPE_RESPONSIBILITY_USER );
        return questionCheckedDeptList.stream().map(QuestionCheckedDept::getDeptId).collect(Collectors.toList());
    }

    @Override
    public QuestionVo queryPersonById(Integer id) {
        QuestionVo questionVo = new QuestionVo();
        try {
            Question question = questionMapper.selectById(id);
            BeanUtils.copyProperties(question, questionVo);
//            List<Qsubject> subjectList = subjectMapper.selectPersonSubjectByQuId(id);
            List<Qsubject> subjectList = subjectService.selectPersonSubjectByQuId(id);
            List<SubjectVo> subjectVoList = new ArrayList<>();
            for (Qsubject subject : subjectList) {
                SubjectVo subjectVo = new SubjectVo();
                BeanUtils.copyProperties(subject, subjectVo);
                List<Qoption> qoptionList = optionService.queryOptionBySubId(subject.getId());
                subjectVo.setOptionList(qoptionList);
                subjectVoList.add(subjectVo);
            }
            //开始组装分组题逻辑
            //先缓存
            Map<Integer, SubjectVo> mapCache = new HashMap<>();
            for (SubjectVo subjectVo : subjectVoList) {
                mapCache.put(subjectVo.getId(), subjectVo);
            }
            //开始算
            StringBuffer groupIdsAll = new StringBuffer();
            for (SubjectVo subjectVo : subjectVoList) {
                //如果是分组题
                if (subjectVo.getSubType().equals(QsubjectConstant.SUB_TYPE_GROUP) || subjectVo.getSubType().equals(QsubjectConstant.SUB_TYPE_GROUP_SCORE)) {
                    String groupIds = subjectVo.getGroupIds();//包含题号
                    if (null != groupIds) {
                        String[] gids = groupIds.split(",");
                        List<SubjectVo> subjectVoGroupList = new ArrayList<>();
                        for (String subId : gids) {
                            groupIdsAll.append(subId);
                            groupIdsAll.append(",");
                            SubjectVo svo = mapCache.get(Integer.parseInt(subId));
                            subjectVoGroupList.add(svo);
                        }
                        //设置到分组题对象列表
                        subjectVo.setSubjectVoList(subjectVoGroupList);
                    }
                }
            }
            //删除在subjectVoList集合中删除groupIdsAll包含的题
            if (groupIdsAll.length() != 0) {
                String removeIds = groupIdsAll.toString();
                String[] remIds = removeIds.split(",");
                if (null != remIds && remIds.length > 0) {
                    for (int i = 0; i < subjectVoList.size(); i++) {
                        //for (SubjectVo subjectVo : subjectVoList) {
                        SubjectVo subjectVo = subjectVoList.get(i);
                        Integer nowId = subjectVo.getId();
                        for (String remId : remIds) {
                            if (nowId == Integer.parseInt(remId)) {
                                subjectVoList.remove(i);//移除
                                i--;
                            }
                        }

                    }
                }
            }
            questionVo.setSubjectVoList(subjectVoList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return questionVo;
    }

    @Override
    public void updateCategoryIdParam(UpdateCategoryIdParam updateCategoryIdParam) {
        Integer[] quIds = updateCategoryIdParam.getQuId();
        String[] categoryIds = updateCategoryIdParam.getCategoryId();
        Integer categoryType = updateCategoryIdParam.getCategoryType();
        if (quIds != null && categoryIds != null) {
            StringBuffer categoryId = new StringBuffer();
            if (categoryIds.length == 1) {
                categoryId.append(categoryIds[0]==null?"":categoryIds[0]);
            } else {
                for (String cid : categoryIds) {
                    categoryId.append(cid);
                    categoryId.append(",");
                }
            }
            //更新
            for (Integer qid : quIds) {
                Question question = new Question();
                question.setId(qid);
                question.setCategoryId(categoryId.toString());
                question.setCategoryType(categoryType);
                question.setUpdateTime(new Date());
                questionMapper.updateById(question);
            }
        }
    }

    @Override
    public void updateTemplateIdIdParam(UpdateTemplateIdParam param) {
        Integer quId = param.getQuId();
        String templateId = param.getTemplateId();
        Question byId = this.getById(quId);
        if(byId==null){
            return;
        }
        byId.setTemplateId(templateId);
        byId.setUpdateTime(new Date());
        this.updateById(byId);
    }

    @Override
    public void updateCheckProjectIdParam(UpdateCheckProjectIdParam param) {
        List<Integer> quIds = param.getQuId();
        String checkProjectId = param.getCheckProjectId();
        String checkProjectType = param.getCheckProjectType();

        Question emptyEntity = new Question();
        LambdaUpdateWrapper<Question> lambda = new UpdateWrapper<Question>().lambda();
        lambda.in(Question::getId, quIds)
                .eq(Question::getDel, QuestionConstant.DEL_NORMAL)
                .set(Question::getCheckProjectId, checkProjectId)
                .set(Question::getCheckProjectType, checkProjectType);
        this.update(emptyEntity, lambda);
    }

    @Override
    public Boolean againRelease(QuestionAgainReleaseParam questionAgainreleaseParam) {
        try {
            Question question = questionMapper.selectById(questionAgainreleaseParam.getId());
            //查看有没有保存这个版本
            Integer id = question.getId();
            String questionVersionNumber = question.getQuestionVersion();
            QuestionVersion questionVersion = questionVersionService.selectByQuestionAndVersion(id, questionVersionNumber);
            if (questionVersion == null) {
                questionVersionService.saveQuestionVersion(id);
            }

            question.setQuestionVersion(VersionNumberUtil.autoUpgradeVersion(questionVersionNumber));
            this.updateById(question);

            List<Integer> subjectIds = questionAgainreleaseParam.getSubjectIds();
            List<Integer> markSubjectIds = questionAgainreleaseParam.getMarkSubjectIds();
            if(CollectionUtil.isEmpty(subjectIds) && CollectionUtil.isEmpty(markSubjectIds)){
                //保存问卷版本
                QuestionVersionEvent questionVersionEvent = new QuestionVersionEvent(this, id);
                applicationEventPublisher.publishEvent(questionVersionEvent);
                return true;
            }

            DeleteCheckDetailSetEvent event = new DeleteCheckDetailSetEvent(this, id);
            applicationEventPublisher.publishEvent(event);

            StringBuffer sql = new StringBuffer();
            sql.append("ALTER TABLE `" + question.getTableName() + "` ");
            boolean alterFlag = false;
            if(CollectionUtil.isNotEmpty(subjectIds)){
//            List<Qsubject> subjectList = subjectMapper.selectBatchIds(subjectIds);
                Collection<Qsubject> subjectList = subjectService.listByIds(subjectIds);
                for (Qsubject qsubject : subjectList) {
                    Integer limitWords = qsubject.getLimitWords();
                    if (limitWords == null || limitWords == 0) {
                        limitWords = 50;
                    }
                    String subType = qsubject.getSubType();
                    Integer del = qsubject.getDel();
                    if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType) || QuestionConstant.DEL_DELETED.equals(del)) {
                        continue;
                    }
                    alterFlag = true;
                    sql.append(" ADD COLUMN ");
                    sql.append("`")
                            .append(qsubject.getColumnName())
                            .append("` ")
                            .append(qsubject.getColumnTypeDatabase()==null?"varchar":qsubject.getColumnTypeDatabase())
                            .append("(")
                            .append(limitWords)
                            .append(") NULL COMMENT '")
                            .append(qsubject.getId())
                            .append("',");
                    if (QsubjectConstant.MARK_OPEN.equals(qsubject.getMark())) {
                        sql.append(" ADD COLUMN `")
                                .append(qsubject.getColumnName())
                                .append("_mark")
                                .append("` ")
                                .append(QsubjectConstant.MARK_TYPE)
                                .append("(")
                                .append(QsubjectConstant.MARK_LENGTH)
                                .append(") COMMENT '")
                                .append(qsubject.getId())
                                .append("的痕迹")
                                .append("',");
                        sql.append(" ADD COLUMN `")
                                .append(qsubject.getColumnName())
                                .append("_mark_img")
                                .append("` ")
                                .append(QsubjectConstant.MARK_TYPE)
                                .append("(")
                                .append(QsubjectConstant.MARK_LENGTH)
                                .append(") COMMENT '")
                                .append(qsubject.getId())
                                .append("的痕迹图片")
                                .append("',");
                    }
                }
            }

            if(CollectionUtil.isNotEmpty(markSubjectIds)){
                Collection<Qsubject> markSubjectList = subjectService.listByIds(markSubjectIds);
                for (Qsubject qsubject : markSubjectList) {
                    String subType = qsubject.getSubType();
                    Integer del = qsubject.getDel();
                    if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType) || QuestionConstant.DEL_DELETED.equals(del) || QsubjectConstant.MARK_OPEN.equals(qsubject.getMarkHistory())) {
                        continue;
                    }
                    alterFlag = true;
                    if (QsubjectConstant.MARK_OPEN.equals(qsubject.getMark())) {
                        sql.append(" ADD COLUMN `")
                                .append(qsubject.getColumnName())
                                .append("_mark")
                                .append("` ")
                                .append(QsubjectConstant.MARK_TYPE)
                                .append("(")
                                .append(QsubjectConstant.MARK_LENGTH)
                                .append(") COMMENT '")
                                .append(qsubject.getId())
                                .append("的痕迹")
                                .append("',");
                        sql.append(" ADD COLUMN `")
                                .append(qsubject.getColumnName())
                                .append("_mark_img")
                                .append("` ")
                                .append(QsubjectConstant.MARK_TYPE)
                                .append("(")
                                .append(QsubjectConstant.MARK_LENGTH)
                                .append(") COMMENT '")
                                .append(qsubject.getId())
                                .append("的痕迹图片")
                                .append("',");
                    }
                }
            }

            if(alterFlag){
                sql.delete(sql.length()-1,sql.length());
                sql.append(" ; ");
                dynamicTableMapper.createDynamicTable(sql.toString());
            }
            //保存问卷版本
            QuestionVersionEvent questionVersionEvent = new QuestionVersionEvent(this, id);
            applicationEventPublisher.publishEvent(questionVersionEvent);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Result<?> generateTraceability(String id) {
        try {
            Question question = questionMapper.selectById(id);
            if (question == null || QuestionConstant.DEL_DELETED.equals(question.getDel())) {
                return ResultFactory.error("问卷异常");
            }
            if(QuestionConstant.TRACEABILITY_STATUS_GENERATED.equals(question.getTraceabilityStatus())){
                return ResultFactory.error("已有溯源表");
            }

            StringBuffer sql = new StringBuffer();
            sql.append("CREATE TABLE `" + question.getTableName() + "_sy` (");
            sql.append("`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',");
//                List<Qsubject> subjectList = subjectMapper.selectSubjectByQuId(questionEditParam.getId());
            List<Qsubject> subjectList = subjectService.selectSubjectByQuId(question.getId());
            for (Qsubject qsubject : subjectList) {
//                Integer limitWords = qsubject.getLimitWords();
//                if(limitWords==null || limitWords==0){
//                    limitWords=50;
//                }
                String subType = qsubject.getSubType();
                Integer del = qsubject.getDel();
                if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType) || QuestionConstant.DEL_DELETED.equals(del)) {
                    continue;
                }
                sql.append("`")
                        .append(qsubject.getColumnName())
                        .append("` text(0) COMMENT '")
                        .append(qsubject.getId())
                        .append("',");

                if (QsubjectConstant.MARK_OPEN.equals(qsubject.getMark())) {
                    sql.append("`")
                            .append(qsubject.getColumnName())
                            .append("_mark")
                            .append("` ")
                            .append(QsubjectConstant.MARK_TYPE)
                            .append("(")
                            .append(QsubjectConstant.MARK_LENGTH)
                            .append(") COMMENT '")
                            .append(qsubject.getId())
                            .append("的痕迹")
                            .append("',");
                    sql.append("`")
                            .append(qsubject.getColumnName())
                            .append("_mark_img")
                            .append("` ")
                            .append(QsubjectConstant.MARK_TYPE)
                            .append("(")
                            .append(QsubjectConstant.MARK_LENGTH)
                            .append(") COMMENT '")
                            .append(qsubject.getId())
                            .append("的痕迹图片")
                            .append("',");
                }
            }
//            if(QuestionConstant.CATEGORY_TYPE_CHECK.equals(question.getCategoryType())){
//                sql.append(" `tbrid` varchar(128) NULL COMMENT '填报人id',");
//            }
            sql.append(" `tbrxm` varchar(128) NULL COMMENT '填报人名称',");
            sql.append(" `answer_datetime` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '填报时间',");
//            sql.append(" `tbksmc` varchar(128) NULL COMMENT '填报科室名称',");
//            sql.append(" `tbksdm` varchar(128) NULL COMMENT '填报科室代码',");
            sql.append(" `summary_mapping_table_id` varchar(128) NULL COMMENT '对应总表的id，可以当主键',");
            sql.append(" `table_answer_status` int(11) NULL DEFAULT 0 COMMENT '0:草稿1:已提交',");
            sql.append(" `del` tinyint(4) NULL DEFAULT 0 COMMENT '0:正常1:已删除',");
            sql.append(" PRIMARY KEY (`id`)");
            if(subjectList.size()>=50){
                sql.append(") ENGINE=MyISAM DEFAULT CHARSET=utf8;");
            }else{
                sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            }
            dynamicTableMapper.createDynamicTable(sql.toString());
            question.setTraceabilityStatus(QuestionConstant.TRACEABILITY_STATUS_GENERATED);
            this.updateById(question);
            return ResultFactory.success();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResultFactory.fail();
    }

    @Override
    public List<Question> queryQuestionByInput(String name) {
        List<Question> questions = questionMapper.queryQuestionByInput(name);
        return questions;
    }

    @Override
    public void updateWriteFrequencyIdsParam(UpdateWriteFrequencyIdsParam updateWriteFrequencyIdsParam) {
        Integer[] quIds = updateWriteFrequencyIdsParam.getQuId();
        Integer writeFrequency = updateWriteFrequencyIdsParam.getWriteFrequency();
        if (quIds != null && writeFrequency != null) {
            //更新
            for (Integer qid : quIds) {
                Question question = new Question();
                question.setId(qid);
                question.setWriteFrequency(writeFrequency);
                question.setUpdateTime(new Date());
                questionMapper.updateById(question);
            }
        }
    }

    @Override
    public void updateQuestionIcon(UpdateQuestionIconParam updateQuestionIconParam) {
        Integer[] quIds = updateQuestionIconParam.getQuId();
        String url = updateQuestionIconParam.getUrl();
        if (quIds != null && StringUtils.isNotBlank(url)) {
            //更新
            for (Integer qid : quIds) {
                Question question = new Question();
                question.setId(qid);
                question.setIcon(url);
                question.setUpdateTime(new Date());
                questionMapper.updateById(question);
            }
        }
    }

    @Override
    public List<QuestionPatientCreateListVo> patientCreateList(String name, String deptId) {
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        if(StringUtils.isNotBlank(name)){
            lambda.like(Question::getQuName,name);
        }
        lambda.eq(Question::getWriteFrequency,QuestionConstant.WRITE_FREQUENCY_PATIENT_WRITE);
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        lambda.and(wrapper->wrapper.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_REGISTER).or().isNull(Question::getCategoryType));

        //科室匹配 问卷设置科室权限---
        if(StringUtils.isNotBlank(deptId)){
            lambda.like(Question::getDeptIds,deptId);
        }
        List<Question> questions = questionMapper.selectList(lambda);
        List<QuestionPatientCreateListVo> patientCreateListVos = questions.stream().map(q -> {
            QuestionPatientCreateListVo patientCreateListVo = new QuestionPatientCreateListVo();
            patientCreateListVo.setTableName(q.getTableName());
            patientCreateListVo.setQuName(q.getQuName());
            patientCreateListVo.setIcon(q.getIcon());
            patientCreateListVo.setId(q.getId());
            return patientCreateListVo;
        }).collect(Collectors.toList());
        return patientCreateListVos;
    }

    @Override
    public List<QuestionMonthQuarterYearCreateListVo> monthQuarterYearCreateList(String type, String deptId) {
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        if("0".equals(type)){
            lambda.eq(Question::getWriteFrequency,QuestionConstant.WRITE_FREQUENCY_MONTH);
        }else if("1".equals(type)){
            lambda.eq(Question::getWriteFrequency,QuestionConstant.WRITE_FREQUENCY_QUARTER);
        }else if("2".equals(type)){
            lambda.eq(Question::getWriteFrequency,QuestionConstant.WRITE_FREQUENCY_YEAR);
        }else{
            lambda.in(Question::getWriteFrequency,QuestionConstant.WRITE_FREQUENCY_MONTH_QUARTER_YEAR);
        }
        lambda.in(Question::getWriteFrequency,QuestionConstant.WRITE_FREQUENCY_MONTH_QUARTER_YEAR);
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        lambda.eq(Question::getCategoryType,QuestionConstant.CATEGORY_TYPE_REGISTER);
//        lambda.and(wrapper->wrapper.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_REGISTER).or().isNull(Question::getCategoryType));

        //科室匹配 问卷设置科室权限---
        if(StringUtils.isNotBlank(deptId)){
            lambda.like(Question::getDeptIds,deptId);
        }
        List<Question> questions = questionMapper.selectList(lambda);
        List<QuestionMonthQuarterYearCreateListVo> patientCreateListVos = questions.stream().map(q -> {
            QuestionMonthQuarterYearCreateListVo patientCreateListVo = new QuestionMonthQuarterYearCreateListVo();
            patientCreateListVo.setIcon(q.getIcon());
            patientCreateListVo.setQuName(q.getQuName());
            patientCreateListVo.setIcon(q.getIcon());
            patientCreateListVo.setId(q.getId());
            return patientCreateListVo;
        }).collect(Collectors.toList());
        return patientCreateListVos;
    }

    @Override
    public List<QuestionMonthQuarterYearCreateListVo> registerCreateList(String deptId) {
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.in(Question::getWriteFrequency,QuestionConstant.WRITE_FREQUENCY_MONTH_QUARTER_YEAR);
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        lambda.eq(Question::getCategoryType,QuestionConstant.CATEGORY_TYPE_REGISTER);
        //科室匹配 问卷设置科室权限---
        if(StringUtils.isNotBlank(deptId)){
            lambda.like(Question::getDeptIds,deptId);
        }
        List<Question> questions = questionMapper.selectList(lambda);
        List<QuestionMonthQuarterYearCreateListVo> patientCreateListVos = questions.stream().map(q -> {
            QuestionMonthQuarterYearCreateListVo patientCreateListVo = new QuestionMonthQuarterYearCreateListVo();
            patientCreateListVo.setIcon(q.getIcon());
            patientCreateListVo.setQuName(q.getQuName());
            patientCreateListVo.setIcon(q.getIcon());
            patientCreateListVo.setId(q.getId());
            return patientCreateListVo;
        }).collect(Collectors.toList());
        return patientCreateListVos;
    }

    @Override
    public List<TbDep> singleDiseaseStatisticAnalysisByDeptCondition(QSingleDiseaseTakeStatisticAnalysisByDeptConditionParam qSingleDiseaseTakeStatisticAnalysisByDeptConditionParam, String deptId, String type) {
        String categoryId = qSingleDiseaseTakeStatisticAnalysisByDeptConditionParam.getCategoryId();
        List<String>  deptIdList = selectSingleDiseaseDeptIdList(categoryId);
        LambdaQueryWrapper<TbDep> tbDepLambda = new QueryWrapper<TbDep>().lambda();
        tbDepLambda.eq(TbDep::getIsdelete, Constant.IS_DELETE_NO);
        if(DeptUtil.isClinical(type)){
            tbDepLambda.eq(TbDep::getId,deptId);
        }
        tbDepLambda.in(TbDep::getId,deptIdList);
        return tbDepService.list(tbDepLambda);
    }

    @Override
    public List<String> selectSingleDiseaseDeptIdList(String categoryId) {
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getCategoryType,QuestionConstant.CATEGORY_TYPE_SINGLE_DISEASE);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        if(StringUtils.isNotBlank(categoryId)){
            lambda.eq(Question::getCategoryId, categoryId);
        }
        List<Question> questionList = questionMapper.selectList(lambda);
        if(questionList.isEmpty()){
            return Lists.newArrayList();
        }
        List<String> deptIdList = Lists.newArrayList();
        for (Question question : questionList) {
            String deptIds = question.getDeptIds();
            if(StringUtils.isNotBlank(deptIds)){
                deptIdList.addAll(Arrays.asList(deptIds.split(",")));
            }
        }

        return deptIdList.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public IPage<QuestionMiniAppPageVo> queryPageListByMiniApp(String deptId, String userId, Integer pageNo, Integer pageSize) {
        com.qu.modules.web.entity.TbUser tbUser = tbUserService.getById(userId);
        if(org.apache.commons.lang.StringUtils.isBlank(userId)  || tbUser==null){
            return new Page<>();
        }
        String positionId = null;
        if(deptId.equals(tbUser.getDepid())){
            positionId = tbUser.getPositionid();
        }else{
            //查询辅助科室
            TbUserAuxiliaryDep auxiliaryDep = tbUserAuxiliaryDepService.selectByUserIdAndDepId(userId,deptId);
            if(auxiliaryDep==null){
                return new Page<>();
            }
            positionId = auxiliaryDep.getPositionId();
        }
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.like(Question::getDeptIds,deptId);
        boolean checkPositionFlag = checkPosition(null, deptId,positionId,userId, lambda);
        if(checkPositionFlag){
            return new Page<>();
        }
        Page<Question> page = new Page<>(pageNo, pageSize);
        IPage<Question> questionIPage = this.page(page, lambda);
        List<Question> records = questionIPage.getRecords();
        if(records.isEmpty()){
            return new Page<>();
        }

        List<QuestionMiniAppPageVo> resList = records.stream().map(r -> {
            QuestionMiniAppPageVo vo = new QuestionMiniAppPageVo();
            BeanUtils.copyProperties(r, vo);
            return vo;
        }).collect(Collectors.toList());
        IPage<QuestionMiniAppPageVo> questionMiniAppPageVoIPage = new Page<>(pageNo,pageSize);
        questionMiniAppPageVoIPage.setRecords(resList);
        questionMiniAppPageVoIPage.setTotal(questionIPage.getTotal());
        return questionMiniAppPageVoIPage;
    }

    @Override
    public IPage<QuestionMiniAppPageVo> answerQueryPageListByMiniApp(String deptId, String userId, Integer pageNo, Integer pageSize) {

        Page<Question> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.like(Question::getDeptIds,deptId);
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getCategoryType,QuestionConstant.CATEGORY_TYPE_REGISTER);
//        lambda.in(Question::getWriteFrequency,QuestionConstant.WRITE_FREQUENCY_MONTH_QUARTER_YEAR);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        IPage<Question> questionIPage = this.page(page, lambda);
        List<Question> records = questionIPage.getRecords();
        if(records.isEmpty()){
            return new Page<>();
        }
        List<QuestionMiniAppPageVo> resList = records.stream().map(r -> {
            QuestionMiniAppPageVo vo = new QuestionMiniAppPageVo();
            BeanUtils.copyProperties(r, vo);
            return vo;
        }).collect(Collectors.toList());
        IPage<QuestionMiniAppPageVo> questionMiniAppPageVoIPage = new Page<>(pageNo,pageSize);
        questionMiniAppPageVoIPage.setRecords(resList);
        questionMiniAppPageVoIPage.setTotal(questionIPage.getTotal());
        return questionMiniAppPageVoIPage;
    }

    @Override
    public List<SubjectVo> queryQuestionSubjectById(Integer id) {
        Question question = questionMapper.selectById(id);
        if(question==null || AnswerCheckConstant.DEL_DELETED.equals(question.getDel())){
            return null;
        }
//        List<Qsubject> subjectList = subjectMapper.selectSubjectByQuId(id);
        List<Qsubject> subjectList = subjectService.selectSubjectByQuId(id);
        if(subjectList.isEmpty()){
            return null;
        }

        List<SubjectVo> subjectVoList = new ArrayList<>();
        for (Qsubject subject : subjectList) {
            SubjectVo subjectVo = new SubjectVo();
            BeanUtils.copyProperties(subject, subjectVo);
            subjectVoList.add(subjectVo);
        }
        //开始组装分组题逻辑
        //先缓存
        Map<Integer, SubjectVo> mapCache = new HashMap<>();
        for (SubjectVo subjectVo : subjectVoList) {
            mapCache.put(subjectVo.getId(), subjectVo);
        }
        //开始算
        StringBuffer groupIdsAll = new StringBuffer();
        for (SubjectVo subjectVo : subjectVoList) {
            //如果是分组题
            if (subjectVo.getSubType().equals(QsubjectConstant.SUB_TYPE_GROUP) || subjectVo.getSubType().equals(QsubjectConstant.SUB_TYPE_GROUP_SCORE)) {
                String groupIds = subjectVo.getGroupIds();//包含题号
                if (null != groupIds) {
                    String[] gids = groupIds.split(",");
                    List<SubjectVo> subjectVoGroupList = new ArrayList<>();
                    for (String subId : gids) {
                        groupIdsAll.append(subId);
                        groupIdsAll.append(",");
                        if (!StringUtil.isEmpty(subId)) {
                            SubjectVo svo = mapCache.get(Integer.parseInt(subId));
                            subjectVoGroupList.add(svo);
                        }
                    }
                    //设置到分组题对象列表
                    subjectVo.setSubjectVoList(subjectVoGroupList);
                }
            }
        }
        //删除在subjectVoList集合中删除groupIdsAll包含的题
        if (groupIdsAll.length() != 0) {
            String removeIds = groupIdsAll.toString();
            String[] remIds = removeIds.split(",");
            if (null != remIds && remIds.length > 0) {
                for (int i = 0; i < subjectVoList.size(); i++) {
                    //for (SubjectVo subjectVo : subjectVoList) {
                    SubjectVo subjectVo = subjectVoList.get(i);
                    Integer nowId = subjectVo.getId();
                    for (String remId : remIds) {
                        if (!IntegerUtil.isNull(nowId) && !StringUtil.isEmpty(remId)) {
                            if (nowId == Integer.parseInt(remId)) {
                                subjectVoList.remove(i);//移除
                                i--;
                            }
                        }
                    }

                }
            }
        }
        return subjectVoList;
    }


    @Override
    public ResultBetter<QuestionSetColumnVo> setColumn(Data data) {
        String deptId = data.getTbUser().getDepId();
        String positionCode = data.getPositions().get(0).getTbPosition().getPositionCode();

        //问卷
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.eq(Question::getCategoryType,QuestionConstant.CATEGORY_TYPE_CHECK);
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.like(Question::getDeptIds,deptId);
        lambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        if (positionCode.indexOf(Constant.POSITION_CODE_ZNKS) > -1) {
            //		 1、如果登录账号是职能科室（无论是科主任还是科室干事），都返回本科室的全部人员（除科主任以外）和本科室全部填报的查检表
        } else if (positionCode.indexOf(Constant.POSITION_CODE_LCKSZR) > -1) {
            //		 2、如果登录账号是临床科室主任，返回本科的全部医生（职位是LCKSZKY），和本科室全部医疗类型的能填报的查检表
            lambda.eq(Question::getCategoryId, TbDataConstant.DATA_TYPE_QUESTION_CHECK_CATEGORY_YL);
        } else if (positionCode.indexOf(Constant.POSITION_CODE_LCKHSZ) > -1) {
            //		 3、如果登录账号是临床科室护士长，返回本科的全部护士（职位是LCKSZKYHL），和本科室全部护理类型的能填报的查检表
            lambda.eq(Question::getCategoryId, TbDataConstant.DATA_TYPE_QUESTION_CHECK_CATEGORY_HL);
        }else{
            //		 4、其他账号登录，本页面无返回
            return ResultBetter.ok();
        }

        List<Question> questionList = this.list(lambda);
        List<QuestionSetColumnAllVo> answerCheckUserVoList = questionList.stream().map(user -> {
            QuestionSetColumnAllVo vo = new QuestionSetColumnAllVo();
            BeanUtils.copyProperties(user, vo);
            return vo;
        }).collect(Collectors.toList());
        Map<Integer, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));
        //选择的数据
        List<AnswerCheckUserSet> answerCheckUserSetList = answerCheckUserSetService.selectByDeptAndType(deptId,AnswerCheckUserSetConstant.TYPE_COLUMN);
//        List<Integer> answerCheckUserList = answerCheckUserSetList.stream().map(AnswerCheckUserSet::getQuId).collect(Collectors.toList());
        List<QuestionSetColumnChooseVo> answerCheckUserChooseList = answerCheckUserSetList.stream().map(answerCheckUserSet -> {
            QuestionSetColumnChooseVo vo = new QuestionSetColumnChooseVo();
            Integer quId = answerCheckUserSet.getQuId();
            vo.setId(quId);
            Question question = questionMap.get(quId);
            if(question!=null){
                vo.setQuName(question.getQuName());
            }
            return vo;
        }).collect(Collectors.toList());

        QuestionSetColumnVo build = QuestionSetColumnVo.builder().allData(answerCheckUserVoList).chooseData(answerCheckUserChooseList).build();
        return  ResultBetter.ok(build);
    }

    @Override
    public List<Question> getCheckByIds(List<Integer> quIdList) {
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.in(Question::getId,quIdList);
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getCategoryType,QuestionConstant.CATEGORY_TYPE_CHECK);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        return this.list(lambda);
    }

    @Override
    public Result<?> copyQuestion(CopyQuestionParam copyQuestionParam, Data data) {
        Integer quId = copyQuestionParam.getQuId();
        Question questionOld = this.getById(quId);
        if (questionOld == null || QuestionConstant.DEL_DELETED.equals(questionOld.getDel())) {
            return Result.error("问卷不存在");
        }
        //查询临时表题目和临时表选项
        LambdaQueryWrapper<Qsubject> qsubjectLambda = new QueryWrapper<Qsubject>().lambda();
        qsubjectLambda.eq(Qsubject::getQuId,quId);
        qsubjectLambda.eq(Qsubject::getDel,QuestionConstant.DEL_NORMAL);
        qsubjectLambda.orderByAsc(Qsubject::getOrderNum);
        List<Qsubject> subjectList = subjectService.list(qsubjectLambda);
        if(subjectList.isEmpty()){
            return Result.error("问卷没有找到题目");
        }

        List<Integer> subjectIdList = subjectList.stream().map(Qsubject::getId).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<Qoption> qoptionLambda = new QueryWrapper<Qoption>().lambda();
        qoptionLambda.in(Qoption::getSubId,subjectIdList);
        qoptionLambda.in(Qoption::getDel, QoptionConstant.DEL_NORMAL);
        qoptionLambda.orderByAsc(Qoption::getOpOrder);
        List<Qoption> qoptionList = optionService.list(qoptionLambda);
        Map<Integer, ArrayList<Qoption>> optionMap = qoptionList.stream().collect(
                Collectors.toMap(Qoption::getSubId, Lists::newArrayList, (ArrayList<Qoption> k1, ArrayList<Qoption> k2) -> {
                    k1.addAll(k2);
                    return k1;
                }));

        List<SubjectCopyVo> subjectVoList = new ArrayList<>();
        ArrayList<Qoption> optionEmptyList = Lists.newArrayList();
        for (Qsubject qsubject : subjectList) {
            SubjectCopyVo subjectDto = new SubjectCopyVo();
            BeanUtils.copyProperties(qsubject, subjectDto);

            ArrayList<Qoption> qoptionsList = optionMap.get(qsubject.getId());
            subjectDto.setOptionList(qoptionsList==null?optionEmptyList:qoptionsList);
            subjectVoList.add(subjectDto);
        }

        //保存问卷
        Question question = new Question();
        BeanUtils.copyProperties(questionOld, question);
        question.setQuName(copyQuestionParam.getNewQuestionName());
        question.setQuStatus(QuestionConstant.QU_STATUS_DRAFT);
        question.setTableName(copyQuestionParam.getNewTableName());
        Date date = new Date();
        String userId = data.getTbUser().getId();
        question.setCreater(userId);
        question.setCreateTime(date);
        question.setUpdater(userId);
        question.setUpdateTime(date);
        question.setQuestionVersion(QuestionConstant.QUESTION_VERSION_DEFAULT);
        question.setTraceabilityStatus(QuestionConstant.TRACEABILITY_STATUS_NO_GENERATE);
        question.setId(null);
        this.save(question);

        //保存题目
        HashMap<Integer, Integer> subjectIdMap = Maps.newHashMap();
        List<Qsubject> qsubjectAddList =Lists.newArrayList();
        List<Qoption> optionAddList =Lists.newArrayList();
        for (SubjectCopyVo subjectCopyVo : subjectVoList) {
            Qsubject qsubject = new Qsubject();
            BeanUtils.copyProperties(subjectCopyVo, qsubject);
            qsubject.setId(null);
            qsubject.setQuId(question.getId());
            qsubject.setJumpLogic(subjectCopyVo.getJumpLogic());
            qsubject.setSpecialJumpLogic(subjectCopyVo.getSpecialJumpLogic());
            qsubject.setGroupIds(subjectCopyVo.getGroupIds());
            qsubject.setChoiceSubjectId(subjectCopyVo.getChoiceSubjectId());
            subjectService.save(qsubject);
            qsubjectAddList.add(qsubject);
            subjectIdMap.put(subjectCopyVo.getId(),qsubject.getId());

            List<Qoption> optionList = subjectCopyVo.getOptionList();
            for (Qoption qoptionTemp : optionList) {
                Qoption qoption = new Qoption();
                BeanUtils.copyProperties(qoptionTemp, qoption);
                qoption.setId(null);
                qoption.setSubId(qsubject.getId());
                qoption.setJumpLogic(qoptionTemp.getJumpLogic());
                qoption.setSpecialJumpLogic(qoptionTemp.getSpecialJumpLogic());
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

    @Override
    public List<QuestionNameVo> queryByQuestionName(String name){
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        if(StringUtils.isNotBlank(name)){
            lambda.in(Question::getQuName,name);
        }
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getCategoryType,QuestionConstant.CATEGORY_TYPE_CHECK);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        List<Question> list = this.list(lambda);
        List<QuestionNameVo> resList = list.stream().map(q -> {
            QuestionNameVo vo = new QuestionNameVo();
            BeanUtils.copyProperties(q, vo);
            return vo;
        }).collect(Collectors.toList());
        return resList;
    }

    @Override
    public List<QuestionNameVo> queryCheckQuestion(String name, String deptId) {
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        if(StringUtils.isNotBlank(name)){
            lambda.in(Question::getQuName,name);
        }
        lambda.like(Question::getSeeDeptIds,deptId);
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getCategoryType,QuestionConstant.CATEGORY_TYPE_CHECK);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        List<Question> list = this.list(lambda);
        List<QuestionNameVo> resList = list.stream().map(q -> {
            QuestionNameVo vo = new QuestionNameVo();
            BeanUtils.copyProperties(q, vo);
            return vo;
        }).collect(Collectors.toList());
        return resList;
    }

    @Override
    public List<QuestionNameVo> enterQuestionSelectRegister(String name) {
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        if(StringUtils.isNotBlank(name)){
            lambda.in(Question::getQuName,name);
        }
//        lambda.like(Question::getSeeDeptIds,deptId);
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getCategoryType,QuestionConstant.CATEGORY_TYPE_REGISTER);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        List<Question> list = this.list(lambda);
        List<QuestionNameVo> resList = list.stream().map(q -> {
            QuestionNameVo vo = new QuestionNameVo();
            BeanUtils.copyProperties(q, vo);
            return vo;
        }).collect(Collectors.toList());
        return resList;
    }




}
