package com.qu.modules.web.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.ResultBetter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.constant.TbFollowVisitPatientConstant;
import com.qu.constant.TbFollowVisitPatientRecordConstant;
import com.qu.constant.TbFollowVisitPatientTemplateConstant;
import com.qu.constant.TbFollowVisitTemplateConstant;
import com.qu.modules.web.entity.Question;
import com.qu.modules.web.entity.TbFollowVisitPatient;
import com.qu.modules.web.entity.TbFollowVisitPatientRecord;
import com.qu.modules.web.entity.TbFollowVisitPatientTemplate;
import com.qu.modules.web.entity.TbFollowVisitTemplate;
import com.qu.modules.web.mapper.TbFollowVisitPatientRecordMapper;
import com.qu.modules.web.param.TbFollowVisitPatientRecordAnswerAfterParam;
import com.qu.modules.web.param.TbFollowVisitPatientRecordListParam;
import com.qu.modules.web.service.IQuestionService;
import com.qu.modules.web.service.ITbFollowVisitPatientRecordService;
import com.qu.modules.web.service.ITbFollowVisitPatientService;
import com.qu.modules.web.service.ITbFollowVisitPatientTemplateService;
import com.qu.modules.web.service.ITbFollowVisitTemplateService;
import com.qu.modules.web.vo.TbFollowVisitPatientRecordHistoryVo;
import com.qu.modules.web.vo.TbFollowVisitPatientRecordListVo;
import com.qu.modules.web.vo.TbFollowVisitPatientRecordVo;

import cn.hutool.core.collection.CollectionUtil;

/**
 * @Description: 随访患者记录表
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
@Service
public class TbFollowVisitPatientRecordServiceImpl extends ServiceImpl<TbFollowVisitPatientRecordMapper, TbFollowVisitPatientRecord> implements ITbFollowVisitPatientRecordService {


    @Autowired
    private ITbFollowVisitTemplateService tbFollowVisitTemplateService;

    @Autowired
    private ITbFollowVisitPatientService tbFollowVisitPatientService;

    @Lazy
    @Autowired
    private ITbFollowVisitPatientTemplateService tbFollowVisitPatientTemplateService;

    @Autowired
    private IQuestionService questionService;

    @Override
    public IPage<TbFollowVisitPatientRecordListVo> queryPageList(TbFollowVisitPatientRecordListParam param, Page<TbFollowVisitPatientRecord> page) {
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

        String startTime = param.getStartTime();

        Date startDate = null;
        Date endDate = null;

        if(StringUtils.isNotBlank(startTime)){
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM");
            DateTime startDateTime = dateTimeFormatter.parseDateTime(startTime);
            startDate = startDateTime.dayOfMonth().withMinimumValue().toDate();

        }
        String endTime = param.getEndTime();
        if(StringUtils.isNotBlank(endTime)){
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM");
            DateTime endDateTime = dateTimeFormatter.parseDateTime(endTime);
            endDate = endDateTime.dayOfMonth().withMaximumValue().plusDays(1).toDate();
        }

        LambdaQueryWrapper<TbFollowVisitPatientRecord> lambda = new QueryWrapper<TbFollowVisitPatientRecord>().lambda();
        if(CollectionUtil.isNotEmpty(templateIdList)){
            lambda.in(TbFollowVisitPatientRecord::getFollowVisitTemplateId,templateIdList);
        }
        if(CollectionUtil.isNotEmpty(patientIdList)){
            lambda.in(TbFollowVisitPatientRecord::getFollowVisitPatientId,patientIdList);
        }
        if(startDate!=null){
            lambda.ge(TbFollowVisitPatientRecord::getFollowVisitTime,startDate);
        }
        if(endDate!=null){
            lambda.lt(TbFollowVisitPatientRecord::getFollowVisitTime,endDate);
        }
        Integer status = param.getStatus();
        if(status!=null && status>0){
            lambda.eq(TbFollowVisitPatientRecord::getStatus,status);
        }else{
            lambda.ne(TbFollowVisitPatientRecord::getStatus, TbFollowVisitPatientRecordConstant.STATUS_STOP);
        }
        IPage<TbFollowVisitPatientRecord> iPage = this.page(page, lambda);
        List<TbFollowVisitPatientRecord> records = iPage.getRecords();
        if(CollectionUtil.isEmpty(records)){
            return new Page<>();
        }

        List<Integer> recordPatientIdList = records.stream().map(TbFollowVisitPatientRecord::getFollowVisitPatientId).distinct().collect(Collectors.toList());
        Collection<TbFollowVisitPatient> tbFollowVisitPatientList = tbFollowVisitPatientService.listByIds(recordPatientIdList);
        Map<Integer, TbFollowVisitPatient> followVisitPatientMap = tbFollowVisitPatientList.stream().collect(Collectors.toMap(TbFollowVisitPatient::getId, Function.identity()));

        List<Integer> recordTemplateIdList = records.stream().map(TbFollowVisitPatientRecord::getFollowVisitTemplateId).distinct().collect(Collectors.toList());
        Collection<TbFollowVisitTemplate> tbFollowVisitTemplateList = tbFollowVisitTemplateService.listByIds(recordTemplateIdList);
        Map<Integer, TbFollowVisitTemplate> followVisitTemplateMap = tbFollowVisitTemplateList.stream().collect(Collectors.toMap(TbFollowVisitTemplate::getId, Function.identity()));

        List<Integer> questionIdList = records.stream().map(TbFollowVisitPatientRecord::getQuestionId).distinct().collect(Collectors.toList());
        Collection<Question> questionList = questionService.listByIds(questionIdList);
        Map<Integer, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));

        List<TbFollowVisitPatientRecordListVo> resList = records.stream().map(r -> {
            TbFollowVisitPatientRecordListVo vo = new TbFollowVisitPatientRecordListVo();
            BeanUtils.copyProperties(r, vo);
            TbFollowVisitPatient tbFollowVisitPatient = followVisitPatientMap.get(r.getFollowVisitPatientId());
            if(Objects.nonNull(tbFollowVisitPatient)){
                BeanUtils.copyProperties(tbFollowVisitPatient,vo);
                vo.setPatientName(tbFollowVisitPatient.getName());
            }
            TbFollowVisitTemplate tbFollowVisitTemplate = followVisitTemplateMap.get(r.getFollowVisitTemplateId());
            if(Objects.nonNull(tbFollowVisitTemplate)){
                vo.setFollowVisitTemplateName(tbFollowVisitTemplate.getName());
            }
            Question question = questionMap.get(r.getQuestionId());
            if(Objects.nonNull(question)){
                vo.setFollowVisitTemplate(question.getQuName());
            }
            vo.setId(r.getId());
            return vo;
        }).collect(Collectors.toList());

        Page<TbFollowVisitPatientRecordListVo> resPage = new Page<>();
        resPage.setRecords(resList);
        resPage.setTotal(iPage.getTotal());
        resPage.setCurrent(iPage.getCurrent());
        return resPage;
    }

    @Override
    public boolean stopFollowVisit(Integer id) {
        TbFollowVisitPatientRecord byId = this.getById(id);
        if(byId==null){
            return false;
        }
        Integer followVisitPatientTemplateId = byId.getFollowVisitPatientTemplateId();
        TbFollowVisitPatientTemplate patientTemplate = tbFollowVisitPatientTemplateService.getById(followVisitPatientTemplateId);
        if(patientTemplate==null){
            return false;
        }
        patientTemplate.setStatus(TbFollowVisitPatientTemplateConstant.STATUS_STOP);
        tbFollowVisitPatientTemplateService.updateById(patientTemplate);

        TbFollowVisitPatientRecord emptyEntity = new TbFollowVisitPatientRecord();
        LambdaUpdateWrapper<TbFollowVisitPatientRecord> lambda = new UpdateWrapper<TbFollowVisitPatientRecord>().lambda();
        lambda.eq(TbFollowVisitPatientRecord::getFollowVisitPatientTemplateId, followVisitPatientTemplateId)
                .eq(TbFollowVisitPatientRecord::getStatus, TbFollowVisitPatientConstant.STATUS_WAIT)
                .set(TbFollowVisitPatientRecord::getStatus, TbFollowVisitPatientConstant.STATUS_STOP);
        this.update(emptyEntity, lambda);
        return true;
    }

    @Override
    public TbFollowVisitPatientRecordVo startFollowVisit(Integer id) {
        TbFollowVisitPatientRecord byId = this.getById(id);
        if(byId==null){
            return null;
        }
        TbFollowVisitPatientRecordVo vo = new TbFollowVisitPatientRecordVo();
        Integer followVisitPatientId = byId.getFollowVisitPatientId();
        TbFollowVisitPatient patient = tbFollowVisitPatientService.getById(followVisitPatientId);
        BeanUtils.copyProperties(patient,vo);
        vo.setPatientName(patient.getName());
        vo.setId(id);
        vo.setFollowVisitNumber(byId.getFollowVisitNumber());
        vo.setStatus(byId.getStatus());

        Integer followVisitTemplateId = byId.getFollowVisitTemplateId();
        TbFollowVisitTemplate template = tbFollowVisitTemplateService.getById(followVisitTemplateId);
        vo.setFollowVisitTemplateName(template.getName());
        vo.setDateStartType(template.getDateStartType());

        vo.setFollowVisitTime(byId.getFollowVisitTime());
        vo.setFollowVisitPatientTemplateId(byId.getFollowVisitPatientTemplateId());
        vo.setQuestionId(byId.getQuestionId());
        vo.setAnswerId(byId.getAnswerId());

        LambdaUpdateWrapper<TbFollowVisitPatientRecord> lambda = new UpdateWrapper<TbFollowVisitPatientRecord>().lambda();
        lambda.eq(TbFollowVisitPatientRecord::getFollowVisitPatientTemplateId, byId.getFollowVisitPatientTemplateId());
        lambda.eq(TbFollowVisitPatientRecord::getStatus, TbFollowVisitPatientConstant.STATUS_COMPLETED);
        List<TbFollowVisitPatientRecord> list = this.list(lambda);
        List<TbFollowVisitPatientRecordHistoryVo> historyVoList = list.stream().map(r -> {
            TbFollowVisitPatientRecordHistoryVo historyVo = new TbFollowVisitPatientRecordHistoryVo();
            BeanUtils.copyProperties(r, historyVo);
            return historyVo;
        }).collect(Collectors.toList());

        vo.setHistoryRecord(historyVoList);
        return vo;
    }

    @Override
    public ResultBetter<Boolean> answerAfter(TbFollowVisitPatientRecordAnswerAfterParam param) {
        Integer id = param.getId();
        TbFollowVisitPatientRecord byId = this.getById(id);
        if(byId==null){
            return ResultBetter.error("记录错误");
        }
        byId.setAnswerId(param.getAnswerId());
        byId.setStatus(TbFollowVisitPatientConstant.STATUS_COMPLETED);
        this.updateById(byId);
        return ResultBetter.ok();
    }
}
