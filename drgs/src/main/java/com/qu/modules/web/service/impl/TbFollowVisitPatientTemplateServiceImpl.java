package com.qu.modules.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qu.constant.TbFollowVisitPatientConstant;
import com.qu.constant.TbFollowVisitPatientRecordConstant;
import com.qu.constant.TbFollowVisitPatientTemplateConstant;
import com.qu.constant.TbFollowVisitTemplateConstant;
import com.qu.modules.web.entity.*;
import com.qu.modules.web.mapper.TbFollowVisitPatientTemplateMapper;
import com.qu.modules.web.param.TbFollowVisitPatientTemplateAllPatientListParam;
import com.qu.modules.web.param.TbFollowVisitPatientTemplateGenerateParam;
import com.qu.modules.web.param.TbFollowVisitPatientTemplateListParam;
import com.qu.modules.web.service.*;
import com.qu.modules.web.vo.TbFollowVisitPatientTemplateAllPatientListVo;
import com.qu.modules.web.vo.TbFollowVisitPatientTemplateHistoryVo;
import com.qu.modules.web.vo.TbFollowVisitPatientTemplateInfoVo;
import com.qu.modules.web.vo.TbFollowVisitPatientTemplateListVo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 随访患者模板总记录表
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
@Service
public class TbFollowVisitPatientTemplateServiceImpl extends ServiceImpl<TbFollowVisitPatientTemplateMapper, TbFollowVisitPatientTemplate> implements ITbFollowVisitPatientTemplateService {

    @Autowired
    private ITbFollowVisitTemplateService tbFollowVisitTemplateService;

    @Autowired
    private ITbFollowVisitTemplateDiseaseService tbFollowVisitTemplateDiseaseService;

    @Autowired
    private ITbFollowVisitTemplateCycleService tbFollowVisitTemplateCycleService;

    @Autowired
    private ITbFollowVisitPatientService tbFollowVisitPatientService;

    @Lazy
    @Autowired
    private ITbFollowVisitPatientRecordService tbFollowVisitPatientRecordService;

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private ITbDepService tbDepService;


    @Override
    public IPage<TbFollowVisitPatientTemplateListVo> queryPageList(Page<TbFollowVisitPatientTemplate> page, TbFollowVisitPatientTemplateListParam param) {
        String name = param.getName();
        List<Integer> templateIdList = null;
        if(StringUtils.isNotBlank(name)){
            //查询随访模板
            LambdaQueryWrapper<TbFollowVisitTemplate> templateLambdaQueryWrapper = new QueryWrapper<TbFollowVisitTemplate>().lambda();
            templateLambdaQueryWrapper.like(TbFollowVisitTemplate::getName,name);
            templateLambdaQueryWrapper.eq(TbFollowVisitTemplate::getDelState, TbFollowVisitTemplateConstant.DEL_NORMAL);
            List<TbFollowVisitTemplate> templateList = tbFollowVisitTemplateService.list(templateLambdaQueryWrapper);
            if(CollectionUtil.isEmpty(templateList)){
                return new Page<>();
            }
            templateIdList = templateList.stream().map(TbFollowVisitTemplate::getId).distinct().collect(Collectors.toList());
        }

        String patientName = param.getPatientName();
        List<Integer> patientIdList = null;
        if(StringUtils.isNotBlank(patientName)){
            //查询随访模板
            LambdaQueryWrapper<TbFollowVisitPatient> patientLambdaQueryWrapper = new QueryWrapper<TbFollowVisitPatient>().lambda();
            patientLambdaQueryWrapper.like(TbFollowVisitPatient::getName,patientName);
            List<TbFollowVisitPatient> patientList = tbFollowVisitPatientService.list(patientLambdaQueryWrapper);
            if(CollectionUtil.isEmpty(patientList)){
                return new Page<>();
            }
            patientIdList = patientList.stream().map(TbFollowVisitPatient::getId).distinct().collect(Collectors.toList());
        }

        String diagnosis = param.getDiagnosis();
        List<Integer> diagnosisPatientIdList = null;
        if(StringUtils.isNotBlank(diagnosis)){
            //查询随访模板
            LambdaQueryWrapper<TbFollowVisitPatient> patientLambdaQueryWrapper = new QueryWrapper<TbFollowVisitPatient>().lambda();
            patientLambdaQueryWrapper.like(TbFollowVisitPatient::getDiagnosis,diagnosis);
            List<TbFollowVisitPatient> patientList = tbFollowVisitPatientService.list(patientLambdaQueryWrapper);
            if(CollectionUtil.isEmpty(patientList)){
                return new Page<>();
            }
            diagnosisPatientIdList = patientList.stream().map(TbFollowVisitPatient::getId).distinct().collect(Collectors.toList());
        }

        LambdaQueryWrapper<TbFollowVisitPatientTemplate> lambda = new QueryWrapper<TbFollowVisitPatientTemplate>().lambda();
        if(CollectionUtil.isNotEmpty(templateIdList)){
            lambda.in(TbFollowVisitPatientTemplate::getFollowVisitTemplateId,templateIdList);
        }
        if(CollectionUtil.isNotEmpty(patientIdList)){
            lambda.in(TbFollowVisitPatientTemplate::getFollowVisitPatientId,patientIdList);
        }
        if(CollectionUtil.isNotEmpty(diagnosisPatientIdList)){
            lambda.in(TbFollowVisitPatientTemplate::getFollowVisitPatientId,diagnosisPatientIdList);
        }
        Date startCreateTime = param.getStartCreateTime();
        if(startCreateTime!=null){
            lambda.ge(TbFollowVisitPatientTemplate::getCreateTime,startCreateTime);
        }
        Date endCreateTime = param.getEndCreateTime();
        if(endCreateTime!=null){
            lambda.lt(TbFollowVisitPatientTemplate::getCreateTime,endCreateTime);
        }
        Integer status = param.getStatus();
        if(status!=null && status>0){
            if(status.equals(TbFollowVisitPatientTemplateConstant.STATUS_STOP)){
                lambda.in(TbFollowVisitPatientTemplate::getStatus, Lists.newArrayList(TbFollowVisitPatientTemplateConstant.STATUS_STOP,TbFollowVisitPatientTemplateConstant.STATUS_AGAIN_HOSPITAL_STOP));
            }else{
                lambda.eq(TbFollowVisitPatientTemplate::getStatus,status);
            }
        }
        IPage<TbFollowVisitPatientTemplate> iPage = this.page(page, lambda);
        List<TbFollowVisitPatientTemplate> records = iPage.getRecords();
        if(CollectionUtil.isEmpty(records)){
            return new Page<>();
        }

        List<Integer> recordPatientIdList = records.stream().map(TbFollowVisitPatientTemplate::getFollowVisitPatientId).distinct().collect(Collectors.toList());
        Collection<TbFollowVisitPatient> tbFollowVisitPatientList = tbFollowVisitPatientService.listByIds(recordPatientIdList);
        Map<Integer, TbFollowVisitPatient> followVisitPatientMap = tbFollowVisitPatientList.stream().collect(Collectors.toMap(TbFollowVisitPatient::getId, Function.identity()));

        List<Integer> recordTemplateIdList = records.stream().map(TbFollowVisitPatientTemplate::getFollowVisitTemplateId).distinct().collect(Collectors.toList());
        Collection<TbFollowVisitTemplate> tbFollowVisitTemplateList = tbFollowVisitTemplateService.listByIds(recordTemplateIdList);
        Map<Integer, TbFollowVisitTemplate> followVisitTemplateMap = tbFollowVisitTemplateList.stream().collect(Collectors.toMap(TbFollowVisitTemplate::getId, Function.identity()));

        List<Integer> patientTemplateIdList = records.stream().map(TbFollowVisitPatientTemplate::getId).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<TbFollowVisitPatientRecord> patientRecordLambdaQueryWrapper = new QueryWrapper<TbFollowVisitPatientRecord>().lambda();
        patientRecordLambdaQueryWrapper.in(TbFollowVisitPatientRecord::getFollowVisitPatientTemplateId,patientTemplateIdList);
        patientRecordLambdaQueryWrapper.eq(TbFollowVisitPatientRecord::getStatus, TbFollowVisitPatientRecordConstant.STATUS_COMPLETED);
        List<TbFollowVisitPatientRecord> patientRecordList = tbFollowVisitPatientRecordService.list(patientRecordLambdaQueryWrapper);
        Map<Integer, List<TbFollowVisitPatientRecord>> patientRecordMap = patientRecordList.stream().collect(Collectors.toMap(TbFollowVisitPatientRecord::getFollowVisitPatientTemplateId, Lists::newArrayList,
                (List<TbFollowVisitPatientRecord> n1, List<TbFollowVisitPatientRecord> n2) -> {
                    n1.addAll(n2);
                    return n1;
                }));

        LambdaQueryWrapper<TbFollowVisitPatientRecord> followVisitPatientRecordLambdaQueryWrapper = new QueryWrapper<TbFollowVisitPatientRecord>().lambda();
        followVisitPatientRecordLambdaQueryWrapper.in(TbFollowVisitPatientRecord::getFollowVisitPatientTemplateId,patientTemplateIdList);
        followVisitPatientRecordLambdaQueryWrapper.eq(TbFollowVisitPatientRecord::getStatus, TbFollowVisitPatientRecordConstant.STATUS_WAIT);
        followVisitPatientRecordLambdaQueryWrapper.orderByDesc(TbFollowVisitPatientRecord::getFollowVisitNumber);
        List<TbFollowVisitPatientRecord> nextPatientRecordList = tbFollowVisitPatientRecordService.list(followVisitPatientRecordLambdaQueryWrapper);
        Map<Integer, List<TbFollowVisitPatientRecord>> nextPatientRecordMap = nextPatientRecordList.stream().collect(Collectors.toMap(TbFollowVisitPatientRecord::getFollowVisitPatientTemplateId, Lists::newArrayList,
                (List<TbFollowVisitPatientRecord> n1, List<TbFollowVisitPatientRecord> n2) -> {
                    n1.addAll(n2);
                    return n1;
                }));

        List<TbFollowVisitPatientTemplateListVo> resList = records.stream().map(r -> {
            TbFollowVisitPatientTemplateListVo vo = new TbFollowVisitPatientTemplateListVo();
            BeanUtils.copyProperties(r, vo);
            TbFollowVisitPatient tbFollowVisitPatient = followVisitPatientMap.get(r.getFollowVisitPatientId());
            if(Objects.nonNull(tbFollowVisitPatient)){
                BeanUtils.copyProperties(tbFollowVisitPatient,vo);
                vo.setPatientName(tbFollowVisitPatient.getName());
            }
            TbFollowVisitTemplate tbFollowVisitTemplate = followVisitTemplateMap.get(r.getFollowVisitTemplateId());
            if(Objects.nonNull(tbFollowVisitTemplate)){
                vo.setFollowVisitTemplate(tbFollowVisitTemplate.getName());
            }
            List<TbFollowVisitPatientRecord> patientRecordVoList = patientRecordMap.get(r.getId());
            if(Objects.nonNull(patientRecordVoList)){
                vo.setAlreadyFollowVisitCountNumber(patientRecordVoList.size());
            }else{
                vo.setAlreadyFollowVisitCountNumber(0);
            }

            List<TbFollowVisitPatientRecord> nextPatientRecordVoList = nextPatientRecordMap.get(r.getId());
            if(CollectionUtil.isNotEmpty(nextPatientRecordVoList)){
                TbFollowVisitPatientRecord tbFollowVisitPatientRecord = nextPatientRecordVoList.get(0);
                vo.setFollowVisitTime(tbFollowVisitPatientRecord.getFollowVisitTime());
            }

            vo.setId(r.getId());
            return vo;
        }).collect(Collectors.toList());

        Page<TbFollowVisitPatientTemplateListVo> resPage = new Page<>();
        resPage.setRecords(resList);
        resPage.setTotal(iPage.getTotal());
        resPage.setCurrent(iPage.getCurrent());
        return resPage;
    }

    @Override
    public boolean stopFollowVisit(Integer id) {
        TbFollowVisitPatientTemplate byId = this.getById(id);
        if(byId==null){
            return false;
        }
        Date date = new Date();
        byId.setStatus(TbFollowVisitPatientTemplateConstant.STATUS_STOP);
        byId.setUpdateTime(date);
        this.updateById(byId);

        TbFollowVisitPatientRecord emptyEntity = new TbFollowVisitPatientRecord();
        LambdaUpdateWrapper<TbFollowVisitPatientRecord> lambda = new UpdateWrapper<TbFollowVisitPatientRecord>().lambda();
        lambda.eq(TbFollowVisitPatientRecord::getFollowVisitPatientTemplateId, id)
                .eq(TbFollowVisitPatientRecord::getStatus, TbFollowVisitPatientConstant.STATUS_WAIT)
                .set(TbFollowVisitPatientRecord::getStatus, TbFollowVisitPatientConstant.STATUS_STOP);
        tbFollowVisitPatientRecordService.update(emptyEntity, lambda);
        return true;
    }

    @Override
    public TbFollowVisitPatientTemplateInfoVo info(Integer id) {
        TbFollowVisitPatientTemplate byId = this.getById(id);
        if(byId==null){
            return null;
        }
        TbFollowVisitPatientTemplateInfoVo vo = new TbFollowVisitPatientTemplateInfoVo();

        TbFollowVisitPatient patient = tbFollowVisitPatientService.getById(byId.getFollowVisitPatientId());
        BeanUtils.copyProperties(patient,vo);
        vo.setPatientName(patient.getName());

        Integer followVisitTemplateId = byId.getFollowVisitTemplateId();
        TbFollowVisitTemplate template = tbFollowVisitTemplateService.getById(followVisitTemplateId);
        if(template!=null && template.getDelState().equals(TbFollowVisitTemplateConstant.DEL_NORMAL)){
            vo.setFollowVisitTemplate(template.getName());
            vo.setDateStartType(template.getDateStartType());
        }
        vo.setFollowVisitCountNumber(byId.getFollowVisitCountNumber());

        LambdaQueryWrapper<TbFollowVisitPatientRecord> patientRecordLambdaQueryWrapper = new QueryWrapper<TbFollowVisitPatientRecord>().lambda();
        patientRecordLambdaQueryWrapper.eq(TbFollowVisitPatientRecord::getFollowVisitPatientTemplateId,id);
//        patientRecordLambdaQueryWrapper.eq(TbFollowVisitPatientRecord::getStatus, TbFollowVisitPatientRecordConstant.STATUS_COMPLETED);
        List<TbFollowVisitPatientRecord> patientRecordList = tbFollowVisitPatientRecordService.list(patientRecordLambdaQueryWrapper);
        if(CollectionUtil.isNotEmpty(patientRecordList)){
            long count = patientRecordList.stream().filter(r -> TbFollowVisitPatientRecordConstant.STATUS_COMPLETED.equals(r.getStatus())).count();
            vo.setAlreadyFollowVisitCountNumber(count);
        }else{
            vo.setAlreadyFollowVisitCountNumber(0L);
        }


        List<Integer> questionIdList = patientRecordList.stream().map(TbFollowVisitPatientRecord::getQuestionId).distinct().collect(Collectors.toList());
        Map<Integer, Question> questionMap = Maps.newHashMap();
        if(CollectionUtil.isNotEmpty(questionIdList)){
            Collection<Question> questionList = questionService.listByIds(questionIdList);
            questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));
        }
        List<TbFollowVisitPatientTemplateHistoryVo> historyVoList = Lists.newArrayList();
        for (TbFollowVisitPatientRecord record : patientRecordList) {
            TbFollowVisitPatientTemplateHistoryVo historyVo = new TbFollowVisitPatientTemplateHistoryVo();
            BeanUtils.copyProperties(record,historyVo);
            Question question = questionMap.get(record.getQuestionId());
            if(Objects.nonNull(question)){
                historyVo.setQuestionName(question.getQuName());
            }
            historyVoList.add(historyVo);
        }
        vo.setHistoryRecord(historyVoList);

//        LambdaQueryWrapper<TbFollowVisitPatientRecord> followVisitPatientRecordLambdaQueryWrapper = new QueryWrapper<TbFollowVisitPatientRecord>().lambda();
//        followVisitPatientRecordLambdaQueryWrapper.eq(TbFollowVisitPatientRecord::getFollowVisitPatientTemplateId,id);
//        followVisitPatientRecordLambdaQueryWrapper.eq(TbFollowVisitPatientRecord::getStatus, TbFollowVisitPatientRecordConstant.STATUS_WAIT);
//        followVisitPatientRecordLambdaQueryWrapper.orderByDesc(TbFollowVisitPatientRecord::getFollowVisitNumber);
//        List<TbFollowVisitPatientRecord> nextPatientRecordList = tbFollowVisitPatientRecordService.list(followVisitPatientRecordLambdaQueryWrapper);
//        if(CollectionUtil.isNotEmpty(nextPatientRecordList)){
//            TbFollowVisitPatientRecord tbFollowVisitPatientRecord = nextPatientRecordList.get(0);
//            vo.setFollowVisitTime(tbFollowVisitPatientRecord.getFollowVisitTime());
//        }

        vo.setStatus(byId.getStatus());
        vo.setCreateTime(byId.getCreateTime());

        return vo;
    }

    @Override
    public IPage<TbFollowVisitPatientTemplateAllPatientListVo> allPatientList(Page<TbFollowVisitPatientTemplate> page, TbFollowVisitPatientTemplateAllPatientListParam param) {
        String name = param.getName();
        List<Integer> templateIdList = null;
        if(StringUtils.isNotBlank(name)){
            //查询随访模板
            LambdaQueryWrapper<TbFollowVisitTemplate> templateLambdaQueryWrapper = new QueryWrapper<TbFollowVisitTemplate>().lambda();
            templateLambdaQueryWrapper.like(TbFollowVisitTemplate::getName,name);
            templateLambdaQueryWrapper.eq(TbFollowVisitTemplate::getDelState, TbFollowVisitTemplateConstant.DEL_NORMAL);
            List<TbFollowVisitTemplate> templateList = tbFollowVisitTemplateService.list(templateLambdaQueryWrapper);
            if(CollectionUtil.isEmpty(templateList)){
                return new Page<>();
            }
            templateIdList = templateList.stream().map(TbFollowVisitTemplate::getId).distinct().collect(Collectors.toList());
        }

        String patientName = param.getPatientName();
        List<Integer> patientIdList = null;
        if(StringUtils.isNotBlank(patientName)){
            //查询随访模板
            LambdaQueryWrapper<TbFollowVisitPatient> patientLambdaQueryWrapper = new QueryWrapper<TbFollowVisitPatient>().lambda();
            patientLambdaQueryWrapper.like(TbFollowVisitPatient::getName,patientName);
            List<TbFollowVisitPatient> patientList = tbFollowVisitPatientService.list(patientLambdaQueryWrapper);
            if(CollectionUtil.isEmpty(patientList)){
                return new Page<>();
            }
            patientIdList = patientList.stream().map(TbFollowVisitPatient::getId).distinct().collect(Collectors.toList());
        }

        String diagnosis = param.getDiagnosis();
        List<Integer> diagnosisPatientIdList = null;
        if(StringUtils.isNotBlank(diagnosis)){
            //查询随访模板
            LambdaQueryWrapper<TbFollowVisitPatient> patientLambdaQueryWrapper = new QueryWrapper<TbFollowVisitPatient>().lambda();
            patientLambdaQueryWrapper.like(TbFollowVisitPatient::getDiagnosis,diagnosis);
            List<TbFollowVisitPatient> patientList = tbFollowVisitPatientService.list(patientLambdaQueryWrapper);
            if(CollectionUtil.isEmpty(patientList)){
                return new Page<>();
            }
            diagnosisPatientIdList = patientList.stream().map(TbFollowVisitPatient::getId).distinct().collect(Collectors.toList());
        }

        String deptId = param.getDeptId();
        List<Integer> deptIdList = null;
        if(StringUtils.isNotBlank(deptId)){
            //查询随访模板
            LambdaQueryWrapper<TbFollowVisitPatient> patientLambdaQueryWrapper = new QueryWrapper<TbFollowVisitPatient>().lambda();
            patientLambdaQueryWrapper.like(TbFollowVisitPatient::getDeptId,deptId);
            List<TbFollowVisitPatient> patientList = tbFollowVisitPatientService.list(patientLambdaQueryWrapper);
            if(CollectionUtil.isEmpty(patientList)){
                return new Page<>();
            }
            deptIdList = patientList.stream().map(TbFollowVisitPatient::getId).distinct().collect(Collectors.toList());
        }

        LambdaQueryWrapper<TbFollowVisitPatientTemplate> lambda = new QueryWrapper<TbFollowVisitPatientTemplate>().lambda();
        if(CollectionUtil.isNotEmpty(templateIdList)){
            lambda.in(TbFollowVisitPatientTemplate::getFollowVisitTemplateId,templateIdList);
        }
        if(CollectionUtil.isNotEmpty(patientIdList)){
            lambda.in(TbFollowVisitPatientTemplate::getFollowVisitPatientId,patientIdList);
        }
        if(CollectionUtil.isNotEmpty(deptIdList)){
            lambda.in(TbFollowVisitPatientTemplate::getFollowVisitPatientId,deptIdList);
        }
        if(CollectionUtil.isNotEmpty(diagnosisPatientIdList)){
            lambda.in(TbFollowVisitPatientTemplate::getFollowVisitPatientId,diagnosisPatientIdList);
        }
        Date startCreateTime = param.getStartCreateTime();
        if(startCreateTime!=null){
            lambda.ge(TbFollowVisitPatientTemplate::getCreateTime,startCreateTime);
        }
        Date endCreateTime = param.getEndCreateTime();
        if(endCreateTime!=null){
            lambda.lt(TbFollowVisitPatientTemplate::getCreateTime,endCreateTime);
        }
        Integer status = param.getStatus();
        if(status!=null && status>0){
            if(status.equals(TbFollowVisitPatientTemplateConstant.STATUS_STOP)){
                lambda.in(TbFollowVisitPatientTemplate::getStatus, Lists.newArrayList(TbFollowVisitPatientTemplateConstant.STATUS_STOP,TbFollowVisitPatientTemplateConstant.STATUS_AGAIN_HOSPITAL_STOP));
            }else{
                lambda.eq(TbFollowVisitPatientTemplate::getStatus,status);
            }
        }
        IPage<TbFollowVisitPatientTemplate> iPage = this.page(page, lambda);
        List<TbFollowVisitPatientTemplate> records = iPage.getRecords();
        if(CollectionUtil.isEmpty(records)){
            return new Page<>();
        }

        List<Integer> recordPatientIdList = records.stream().map(TbFollowVisitPatientTemplate::getFollowVisitPatientId).distinct().collect(Collectors.toList());
        Collection<TbFollowVisitPatient> tbFollowVisitPatientList = tbFollowVisitPatientService.listByIds(recordPatientIdList);
        Map<Integer, TbFollowVisitPatient> followVisitPatientMap = tbFollowVisitPatientList.stream().collect(Collectors.toMap(TbFollowVisitPatient::getId, Function.identity()));

        List<String> recordDeptIdList = tbFollowVisitPatientList.stream().map(TbFollowVisitPatient::getDeptId).distinct().collect(Collectors.toList());
        List<TbDep> tbDepList = tbDepService.listByIdList(recordDeptIdList);
        Map<String, TbDep> tbDepMap = tbDepList.stream().collect(Collectors.toMap(TbDep::getId, Function.identity()));

        List<Integer> recordTemplateIdList = records.stream().map(TbFollowVisitPatientTemplate::getFollowVisitTemplateId).distinct().collect(Collectors.toList());
        Collection<TbFollowVisitTemplate> tbFollowVisitTemplateList = tbFollowVisitTemplateService.listByIds(recordTemplateIdList);
        Map<Integer, TbFollowVisitTemplate> followVisitTemplateMap = tbFollowVisitTemplateList.stream().collect(Collectors.toMap(TbFollowVisitTemplate::getId, Function.identity()));

        List<Integer> patientTemplateIdList = records.stream().map(TbFollowVisitPatientTemplate::getId).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<TbFollowVisitPatientRecord> patientRecordLambdaQueryWrapper = new QueryWrapper<TbFollowVisitPatientRecord>().lambda();
        patientRecordLambdaQueryWrapper.in(TbFollowVisitPatientRecord::getFollowVisitPatientTemplateId,patientTemplateIdList);
        patientRecordLambdaQueryWrapper.eq(TbFollowVisitPatientRecord::getStatus, TbFollowVisitPatientRecordConstant.STATUS_COMPLETED);
        List<TbFollowVisitPatientRecord> patientRecordList = tbFollowVisitPatientRecordService.list(patientRecordLambdaQueryWrapper);
        Map<Integer, List<TbFollowVisitPatientRecord>> patientRecordMap = patientRecordList.stream().collect(Collectors.toMap(TbFollowVisitPatientRecord::getFollowVisitPatientTemplateId, Lists::newArrayList,
                (List<TbFollowVisitPatientRecord> n1, List<TbFollowVisitPatientRecord> n2) -> {
                    n1.addAll(n2);
                    return n1;
                }));

        LambdaQueryWrapper<TbFollowVisitPatientRecord> followVisitPatientRecordLambdaQueryWrapper = new QueryWrapper<TbFollowVisitPatientRecord>().lambda();
        followVisitPatientRecordLambdaQueryWrapper.in(TbFollowVisitPatientRecord::getFollowVisitPatientTemplateId,patientTemplateIdList);
        followVisitPatientRecordLambdaQueryWrapper.eq(TbFollowVisitPatientRecord::getStatus, TbFollowVisitPatientRecordConstant.STATUS_WAIT);
        followVisitPatientRecordLambdaQueryWrapper.orderByDesc(TbFollowVisitPatientRecord::getFollowVisitNumber);
        List<TbFollowVisitPatientRecord> nextPatientRecordList = tbFollowVisitPatientRecordService.list(followVisitPatientRecordLambdaQueryWrapper);
        Map<Integer, List<TbFollowVisitPatientRecord>> nextPatientRecordMap = nextPatientRecordList.stream().collect(Collectors.toMap(TbFollowVisitPatientRecord::getFollowVisitPatientTemplateId, Lists::newArrayList,
                (List<TbFollowVisitPatientRecord> n1, List<TbFollowVisitPatientRecord> n2) -> {
                    n1.addAll(n2);
                    return n1;
                }));

        List<TbFollowVisitPatientTemplateAllPatientListVo> resList = records.stream().map(r -> {
            TbFollowVisitPatientTemplateAllPatientListVo vo = new TbFollowVisitPatientTemplateAllPatientListVo();
            BeanUtils.copyProperties(r, vo);
            TbFollowVisitPatient tbFollowVisitPatient = followVisitPatientMap.get(r.getFollowVisitPatientId());
            if(Objects.nonNull(tbFollowVisitPatient)){
                BeanUtils.copyProperties(tbFollowVisitPatient,vo);
                vo.setPatientName(tbFollowVisitPatient.getName());

                TbDep tbDep = tbDepMap.get(tbFollowVisitPatient.getDeptId());
                if(Objects.nonNull(tbDep)){
                    vo.setDeptName(tbDep.getDepname());
                }
            }

            TbFollowVisitTemplate tbFollowVisitTemplate = followVisitTemplateMap.get(r.getFollowVisitTemplateId());
            if(Objects.nonNull(tbFollowVisitTemplate)){
                vo.setFollowVisitTemplate(tbFollowVisitTemplate.getName());
            }
            List<TbFollowVisitPatientRecord> patientRecordVoList = patientRecordMap.get(r.getId());
            if(Objects.nonNull(patientRecordVoList)){
                vo.setAlreadyFollowVisitCountNumber(patientRecordVoList.size());
            }else{
                vo.setAlreadyFollowVisitCountNumber(0);
            }

            List<TbFollowVisitPatientRecord> nextPatientRecordVoList = nextPatientRecordMap.get(r.getId());
            if(CollectionUtil.isNotEmpty(nextPatientRecordVoList)){
                TbFollowVisitPatientRecord tbFollowVisitPatientRecord = nextPatientRecordVoList.get(0);
                vo.setFollowVisitTime(tbFollowVisitPatientRecord.getFollowVisitTime());
            }

            vo.setId(r.getId());
            return vo;
        }).collect(Collectors.toList());

        Page<TbFollowVisitPatientTemplateAllPatientListVo> resPage = new Page<>();
        resPage.setRecords(resList);
        resPage.setTotal(iPage.getTotal());
        resPage.setCurrent(iPage.getCurrent());
        return resPage;
    }

    @Override
    public void generatePatientTemplate(TbFollowVisitPatientTemplateGenerateParam param) {
        String zbgroup = param.getZbgroup();
        Date date = new Date();
        //先查查有没有进行中的随访计划
        LambdaQueryWrapper<TbFollowVisitPatient> patientLambdaQueryWrapper = new QueryWrapper<TbFollowVisitPatient>().lambda();
        patientLambdaQueryWrapper.eq(TbFollowVisitPatient::getName,param.getName());
        patientLambdaQueryWrapper.eq(TbFollowVisitPatient::getCode,param.getZbgroup());
        patientLambdaQueryWrapper.eq(TbFollowVisitPatient::getCaseId,param.getCaseId());
        List<TbFollowVisitPatient> patientList = tbFollowVisitPatientService.list(patientLambdaQueryWrapper);
        if(CollectionUtil.isNotEmpty(patientList)){
            TbFollowVisitPatient patient = patientList.get(0);
            //查模板
            LambdaQueryWrapper<TbFollowVisitPatientTemplate> patientTemplateLambdaQueryWrapper = new QueryWrapper<TbFollowVisitPatientTemplate>().lambda();
            patientTemplateLambdaQueryWrapper.eq(TbFollowVisitPatientTemplate::getFollowVisitPatientId,patient.getId());
            patientTemplateLambdaQueryWrapper.eq(TbFollowVisitPatientTemplate::getStatus,TbFollowVisitPatientTemplateConstant.STATUS_PROGRESS);
            List<TbFollowVisitPatientTemplate> patientTemplateList = this.list(patientTemplateLambdaQueryWrapper);
            if(CollectionUtil.isNotEmpty(patientTemplateList)){
                TbFollowVisitPatientTemplate patientTemplate = patientTemplateList.get(0);
                patientTemplate.setStatus(TbFollowVisitPatientTemplateConstant.STATUS_AGAIN_HOSPITAL_STOP);
                patientTemplate.setUpdateTime(date);
                this.updateById(patientTemplate);

                TbFollowVisitPatientRecord emptyEntity = new TbFollowVisitPatientRecord();
                LambdaUpdateWrapper<TbFollowVisitPatientRecord> lambda = new UpdateWrapper<TbFollowVisitPatientRecord>().lambda();
                lambda.eq(TbFollowVisitPatientRecord::getFollowVisitPatientTemplateId, patientTemplate.getId())
                        .eq(TbFollowVisitPatientRecord::getStatus, TbFollowVisitPatientConstant.STATUS_WAIT)
                        .set(TbFollowVisitPatientRecord::getStatus, TbFollowVisitPatientConstant.STATUS_STOP);
                tbFollowVisitPatientRecordService.update(emptyEntity, lambda);
            }
        }

        //查询随访模板
        LambdaQueryWrapper<TbFollowVisitTemplateDisease> diseaseLambdaQueryWrapper = new QueryWrapper<TbFollowVisitTemplateDisease>().lambda();
        diseaseLambdaQueryWrapper.like(TbFollowVisitTemplateDisease::getCode,zbgroup);
        List<TbFollowVisitTemplateDisease> templateDiseaseList = tbFollowVisitTemplateDiseaseService.list(diseaseLambdaQueryWrapper);
        if(CollectionUtil.isEmpty(templateDiseaseList)){
            return;
        }
        TbFollowVisitTemplateDisease disease = templateDiseaseList.get(0);
        Integer followVisitTemplateId = disease.getFollowVisitTemplateId();
        TbFollowVisitTemplate template = tbFollowVisitTemplateService.getById(followVisitTemplateId);
        if (template == null || template.getDelState().equals(TbFollowVisitTemplateConstant.DEL_DELETED)) {
            return;
        }

        LambdaQueryWrapper<TbFollowVisitTemplateCycle> cycleLambdaQueryWrapper = new QueryWrapper<TbFollowVisitTemplateCycle>().lambda();
        cycleLambdaQueryWrapper.eq(TbFollowVisitTemplateCycle::getFollowVisitTemplateId,followVisitTemplateId);
        cycleLambdaQueryWrapper.eq(TbFollowVisitTemplateCycle::getDelState,TbFollowVisitTemplateConstant.DEL_NORMAL);
        List<TbFollowVisitTemplateCycle> cycleList = tbFollowVisitTemplateCycleService.list(cycleLambdaQueryWrapper);

        TbFollowVisitPatient patient = new TbFollowVisitPatient();
        BeanUtils.copyProperties(param,patient);
        patient.setCode(param.getZbgroup());
        patient.setDelState(TbFollowVisitPatientConstant.DEL_NORMAL);
        patient.setCreateTime(date);
        patient.setUpdateTime(date);
        tbFollowVisitPatientService.save(patient);
        TbFollowVisitPatientTemplate patientTemplate = new TbFollowVisitPatientTemplate();
        patientTemplate.setFollowVisitPatientId(patient.getId());
        patientTemplate.setFollowVisitTemplateId(template.getId());
        patientTemplate.setFollowVisitCountNumber(cycleList.size());
        patientTemplate.setStatus(TbFollowVisitPatientTemplateConstant.STATUS_PROGRESS);
        patientTemplate.setDelState(TbFollowVisitPatientTemplateConstant.DEL_NORMAL);
        patientTemplate.setCreateTime(date);
        patientTemplate.setUpdateTime(date);
        this.save(patientTemplate);

        List<TbFollowVisitPatientRecord> patientRecordList = cycleList.stream().map(c -> {
            TbFollowVisitPatientRecord record = new TbFollowVisitPatientRecord();
            record.setFollowVisitPatientId(patient.getId());
            record.setFollowVisitPatientTemplateId(patientTemplate.getId());
            record.setFollowVisitTemplateId(template.getId());
            record.setQuestionId(c.getQuestionId());
            DateTime dateTime = new DateTime(param.getOutTime());
            Integer dateStartType = c.getDateStartType();
            Integer dateStartNumber = c.getDateStartNumber();
            if(TbFollowVisitPatientRecordConstant.DATE_START_TYPE_DAY.equals(dateStartType)){
                Date followVisitTime = dateTime.plusDays(dateStartNumber).toDate();
                record.setFollowVisitTime(followVisitTime);
            }else if(TbFollowVisitPatientRecordConstant.DATE_START_TYPE_WEEK.equals(dateStartType)){
                Date followVisitTime = dateTime.plusWeeks(dateStartNumber).toDate();
                record.setFollowVisitTime(followVisitTime);
            }else if(TbFollowVisitPatientRecordConstant.DATE_START_TYPE_MONTH.equals(dateStartType)){
                Date followVisitTime = dateTime.plusMonths(dateStartNumber).toDate();
                record.setFollowVisitTime(followVisitTime);
            }else if(TbFollowVisitPatientRecordConstant.DATE_START_TYPE_YEAR.equals(dateStartType)){
                Date followVisitTime = dateTime.plusYears(dateStartNumber).toDate();
                record.setFollowVisitTime(followVisitTime);
            }
            record.setDateStartType(c.getDateStartType());
            record.setFollowVisitNumber(c.getFrequency());
            record.setDateStartNumber(c.getDateStartNumber());
            record.setStatus(TbFollowVisitPatientRecordConstant.STATUS_WAIT);
            record.setDelState(TbFollowVisitPatientRecordConstant.DEL_NORMAL);
            record.setCreateTime(date);
            record.setUpdateTime(date);
            return record;
        }).collect(Collectors.toList());
        tbFollowVisitPatientRecordService.saveBatch(patientRecordList);
    }
}
