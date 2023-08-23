package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.qu.constant.TbFollowVisitTemplateConstant;
import com.qu.modules.web.entity.*;
import com.qu.modules.web.mapper.TbFollowVisitTemplateMapper;
import com.qu.modules.web.param.TbFollowVisitTemplateAddOrUpdateParam;
import com.qu.modules.web.param.TbFollowVisitTemplateCycleAddParam;
import com.qu.modules.web.param.TbFollowVisitTemplateListParam;
import com.qu.modules.web.pojo.Data;
import com.qu.modules.web.service.*;
import com.qu.modules.web.vo.TbFollowVisitTemplateCycleInfoVo;
import com.qu.modules.web.vo.TbFollowVisitTemplateInfoVo;
import com.qu.modules.web.vo.TbFollowVisitTemplateListVo;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.ResultBetter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 随访模板表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Service
public class TbFollowVisitTemplateServiceImpl extends ServiceImpl<TbFollowVisitTemplateMapper, TbFollowVisitTemplate> implements ITbFollowVisitTemplateService {

    @Lazy
    @Autowired
    private ITbUserService tbUserService;

    @Autowired
    private ITbFollowVisitTemplateDiseaseService tbFollowVisitTemplateDiseaseService;

    @Autowired
    private ITbFollowVisitTemplateCycleService tbFollowVisitTemplateCycleService;

    @Autowired
    private IZbCodeConfigService zbCodeConfigService;

    @Autowired
    private IQuestionService questionService;

    @Override
    public IPage<TbFollowVisitTemplateListVo> queryPageList(Page<TbFollowVisitTemplate> page, TbFollowVisitTemplateListParam listParam) {
        LambdaQueryWrapper<TbFollowVisitTemplate> lambda = new QueryWrapper<TbFollowVisitTemplate>().lambda();
        if (StringUtils.isNotBlank(listParam.getName())) {
            lambda.like(TbFollowVisitTemplate::getName, listParam.getName());
        }
        if (listParam.getStartCreateTime() != null) {
            lambda.ge(TbFollowVisitTemplate::getCreateTime, listParam.getStartCreateTime());
        }
        if (listParam.getEndCreateTime() != null) {
            lambda.le(TbFollowVisitTemplate::getCreateTime, listParam.getEndCreateTime());
        }
        if (listParam.getStartUpdateTime() != null) {
            lambda.ge(TbFollowVisitTemplate::getUpdateTime, listParam.getStartUpdateTime());
        }
        if (listParam.getEndUpdateTime() != null) {
            lambda.le(TbFollowVisitTemplate::getUpdateTime, listParam.getEndUpdateTime());
        }
        lambda.orderByDesc(TbFollowVisitTemplate::getUpdateTime);
        IPage<TbFollowVisitTemplate> iPage = this.page(page, lambda);
        List<TbFollowVisitTemplate> records = iPage.getRecords();

        List<String> userIdList = records.stream().map(TbFollowVisitTemplate::getCreateUser).collect(Collectors.toList());
        List<TbUser> userList = tbUserService.getByIds(userIdList);
        Map<String, TbUser> userMap = userList.stream().collect(Collectors.toMap(TbUser::getId, Function.identity()));

        List<Integer> templateIdList = records.stream().map(TbFollowVisitTemplate::getId).collect(Collectors.toList());
        LambdaQueryWrapper<TbFollowVisitTemplateDisease> diseaseLambdaQueryWrapper = new QueryWrapper<TbFollowVisitTemplateDisease>().lambda();
        diseaseLambdaQueryWrapper.in(TbFollowVisitTemplateDisease::getFollowVisitTemplateId,templateIdList);
        diseaseLambdaQueryWrapper.eq(TbFollowVisitTemplateDisease::getDelState,TbFollowVisitTemplateConstant.DEL_NORMAL);
        List<TbFollowVisitTemplateDisease> diseaseList = tbFollowVisitTemplateDiseaseService.list(diseaseLambdaQueryWrapper);

        Map<Integer, List<TbFollowVisitTemplateDisease>> diseaseMap = diseaseList.stream().collect(Collectors.toMap(TbFollowVisitTemplateDisease::getFollowVisitTemplateId, Lists::newArrayList,
                (List<TbFollowVisitTemplateDisease> n1, List<TbFollowVisitTemplateDisease> n2) -> {
                    n1.addAll(n2);
                    return n1;
                }));

        List<String> diseaseCodeList = diseaseList.stream().map(TbFollowVisitTemplateDisease::getCode).collect(Collectors.toList());
        LambdaQueryWrapper<ZbCodeConfig> zbCodeConfigLambdaQueryWrapper = new QueryWrapper<ZbCodeConfig>().lambda();
        zbCodeConfigLambdaQueryWrapper.in(ZbCodeConfig::getZbgroup,diseaseCodeList);
        zbCodeConfigLambdaQueryWrapper.eq(ZbCodeConfig::getZblx,3);
        zbCodeConfigLambdaQueryWrapper.groupBy(ZbCodeConfig::getZbgroup);
        List<ZbCodeConfig> zbCodeConfigList = zbCodeConfigService.list(zbCodeConfigLambdaQueryWrapper);
        Map<String, ZbCodeConfig> zbCodeConfigMap = zbCodeConfigList.stream().collect(Collectors.toMap(ZbCodeConfig::getZbgroup, Function.identity()));

        List<TbFollowVisitTemplateListVo> resList = records.stream().map(r -> {
            TbFollowVisitTemplateListVo vo = new TbFollowVisitTemplateListVo();
            BeanUtils.copyProperties(r, vo);
            List<TbFollowVisitTemplateDisease> templateDiseaseList = diseaseMap.get(r.getId());
            vo.setDisease(templateDiseaseList.stream().map(d->{
                ZbCodeConfig zbCodeConfig = zbCodeConfigMap.get(d.getCode());
                if(Objects.nonNull(zbCodeConfig)){
                    return zbCodeConfig.getZbgroupname();
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.joining("、")));
            TbUser tbUser = userMap.get(r.getCreateUser());
            if(Objects.nonNull(tbUser)){
                vo.setCreateUser(tbUser.getLoginname());
            }
            return vo;
        }).collect(Collectors.toList());

        Page<TbFollowVisitTemplateListVo> resPage = new Page<>();
        resPage.setRecords(resList);
        resPage.setTotal(iPage.getTotal());
        resPage.setCurrent(iPage.getCurrent());
        return resPage;
    }

    @Override
    public ResultBetter<Boolean> addOrUpdate(TbFollowVisitTemplateAddOrUpdateParam param, Data data) {
        Integer id = param.getId();
        Date date = new Date();

        //先查查疾病有没有被别的用
        LambdaQueryWrapper<TbFollowVisitTemplateDisease> checkDiseaseLambdaQueryWrapper = new QueryWrapper<TbFollowVisitTemplateDisease>().lambda();
        List<String> diseaseAddParamList = param.getDiseaseList();
        checkDiseaseLambdaQueryWrapper.in(TbFollowVisitTemplateDisease::getCode,diseaseAddParamList);
        if(id!=null && id >0){
            checkDiseaseLambdaQueryWrapper.ne(TbFollowVisitTemplateDisease::getFollowVisitTemplateId,id);
        }
        List<TbFollowVisitTemplateDisease> checkDiseaseList = tbFollowVisitTemplateDiseaseService.list(checkDiseaseLambdaQueryWrapper);
        if(!checkDiseaseList.isEmpty()){
            return ResultBetter.error("该疾病已被其他模板引用，无法添加");
        }

        if(id!=null && id >0){
            TbFollowVisitTemplate byId = this.getById(id);
            BeanUtils.copyProperties(param,byId);
            byId.setUpdateTime(date);
            this.updateById(byId);
        }else{
            TbFollowVisitTemplate template = new TbFollowVisitTemplate();
            BeanUtils.copyProperties(param,template);
            template.setStatus(TbFollowVisitTemplateConstant.STATUS_NORMAL);
            template.setDelState(TbFollowVisitTemplateConstant.DEL_NORMAL);
            template.setCreateUser(data.getTbUser().getId());
            template.setCreateTime(date);
            template.setUpdateTime(date);
            this.save(template);
            id = template.getId();
        }

        //先删除
        LambdaUpdateWrapper<TbFollowVisitTemplateDisease> diseaseLambdaQueryWrapper = new UpdateWrapper<TbFollowVisitTemplateDisease>().lambda();
        diseaseLambdaQueryWrapper.eq(TbFollowVisitTemplateDisease::getFollowVisitTemplateId,id).set(TbFollowVisitTemplateDisease::getDelState,TbFollowVisitTemplateConstant.DEL_DELETED);
        TbFollowVisitTemplateDisease a = new TbFollowVisitTemplateDisease();
        tbFollowVisitTemplateDiseaseService.update(a,diseaseLambdaQueryWrapper);
        Integer finalId = id;
        List<TbFollowVisitTemplateDisease> diseaseList = diseaseAddParamList.stream().map(d -> {
            TbFollowVisitTemplateDisease disease = new TbFollowVisitTemplateDisease();
//            BeanUtils.copyProperties(d, disease);
            disease.setCode(d);
            disease.setFollowVisitTemplateId(finalId);
            disease.setDelState(TbFollowVisitTemplateConstant.DEL_NORMAL);
            disease.setCreateUser(data.getTbUser().getId());
            disease.setCreateTime(date);
            disease.setUpdateTime(date);
            return disease;
        }).collect(Collectors.toList());
        tbFollowVisitTemplateDiseaseService.saveBatch(diseaseList);

        LambdaUpdateWrapper<TbFollowVisitTemplateCycle> cycleLambdaUpdateWrapper = new UpdateWrapper<TbFollowVisitTemplateCycle>().lambda();
        cycleLambdaUpdateWrapper.eq(TbFollowVisitTemplateCycle::getFollowVisitTemplateId,id).set(TbFollowVisitTemplateCycle::getDelState,TbFollowVisitTemplateConstant.DEL_DELETED);
        TbFollowVisitTemplateCycle b = new TbFollowVisitTemplateCycle();
        tbFollowVisitTemplateCycleService.update(b,cycleLambdaUpdateWrapper);

        List<TbFollowVisitTemplateCycleAddParam> cycleAddParamList = param.getCycleList();
        List<TbFollowVisitTemplateCycle> cycleList = cycleAddParamList.stream().map(c -> {
            TbFollowVisitTemplateCycle cycle = new TbFollowVisitTemplateCycle();
            BeanUtils.copyProperties(c, cycle);
            cycle.setFollowVisitTemplateId(finalId);
            cycle.setDelState(TbFollowVisitTemplateConstant.DEL_NORMAL);
            cycle.setCreateUser(data.getTbUser().getId());
            cycle.setCreateTime(date);
            cycle.setUpdateTime(date);
            return cycle;
        }).collect(Collectors.toList());
        tbFollowVisitTemplateCycleService.saveBatch(cycleList);
        return ResultBetter.ok();
    }

    @Override
    public TbFollowVisitTemplateInfoVo info(String id) {
        TbFollowVisitTemplate byId = this.getById(id);
        if(byId==null){
            return null;
        }

        TbFollowVisitTemplateInfoVo vo =new TbFollowVisitTemplateInfoVo();
        BeanUtils.copyProperties(byId,vo);

        LambdaQueryWrapper<TbFollowVisitTemplateDisease> diseaseLambdaQueryWrapper = new QueryWrapper<TbFollowVisitTemplateDisease>().lambda();
        diseaseLambdaQueryWrapper.eq(TbFollowVisitTemplateDisease::getFollowVisitTemplateId,id);
        diseaseLambdaQueryWrapper.eq(TbFollowVisitTemplateDisease::getDelState,TbFollowVisitTemplateConstant.DEL_NORMAL);
        List<TbFollowVisitTemplateDisease> diseaseList = tbFollowVisitTemplateDiseaseService.list(diseaseLambdaQueryWrapper);
//        List<TbFollowVisitTemplateDiseaseInfoVo> resDiseaseList = diseaseList.stream().map(d -> {
//            TbFollowVisitTemplateDiseaseInfoVo diseaseInfoVo = new TbFollowVisitTemplateDiseaseInfoVo();
//            BeanUtils.copyProperties(d, diseaseInfoVo);
//            return diseaseInfoVo;
//        }).collect(Collectors.toList());
//        vo.setDiseaseList(resDiseaseList);
        List<String> diseaseCodeList = diseaseList.stream().map(TbFollowVisitTemplateDisease::getCode).collect(Collectors.toList());
        vo.setDiseaseList(diseaseCodeList);

        LambdaQueryWrapper<TbFollowVisitTemplateCycle> cycleLambdaQueryWrapper = new QueryWrapper<TbFollowVisitTemplateCycle>().lambda();
        cycleLambdaQueryWrapper.eq(TbFollowVisitTemplateCycle::getFollowVisitTemplateId,id);
        cycleLambdaQueryWrapper.eq(TbFollowVisitTemplateCycle::getDelState,TbFollowVisitTemplateConstant.DEL_NORMAL);
        List<TbFollowVisitTemplateCycle> cycleList = tbFollowVisitTemplateCycleService.list(cycleLambdaQueryWrapper);

        List<Integer> questionIdList = cycleList.stream().map(TbFollowVisitTemplateCycle::getQuestionId).distinct().collect(Collectors.toList());
        Collection<Question> questionList = questionService.listByIds(questionIdList);
        Map<Integer, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));

        List<TbFollowVisitTemplateCycleInfoVo> resCycleList = cycleList.stream().map(c -> {
            TbFollowVisitTemplateCycleInfoVo cycleInfoVo = new TbFollowVisitTemplateCycleInfoVo();
            BeanUtils.copyProperties(c, cycleInfoVo);
            Question question = questionMap.get(c.getQuestionId());
            if(Objects.nonNull(question)){
                cycleInfoVo.setQuestionName(question.getQuName());
            }
            return cycleInfoVo;
        }).collect(Collectors.toList());
        vo.setCycleList(resCycleList);
        return vo;
    }

    @Override
    public boolean deactivate(Integer id) {
        TbFollowVisitTemplate byId = this.getById(id);
        if(byId==null){
            return false;
        }
        byId.setStatus(TbFollowVisitTemplateConstant.STATUS_STOP);
        this.updateById(byId);
        return true;
    }
}
