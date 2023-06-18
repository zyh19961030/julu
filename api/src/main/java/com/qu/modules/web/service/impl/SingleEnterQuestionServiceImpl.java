package com.qu.modules.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qu.constant.*;
import com.qu.modules.web.entity.*;
import com.qu.modules.web.mapper.DynamicTableMapper;
import com.qu.modules.web.mapper.SingleEnterQuestionMapper;
import com.qu.modules.web.param.*;
import com.qu.modules.web.pojo.Data;
import com.qu.modules.web.pojo.JsonRootBean;
import com.qu.modules.web.service.*;
import com.qu.modules.web.vo.*;
import com.qu.util.HttpClient;
import com.qu.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.ResultBetter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 录入表单表
 * @Author: jeecg-boot
 * @Date: 2023-05-24
 * @Version: V1.0
 */
@Slf4j
@Service
public class SingleEnterQuestionServiceImpl extends ServiceImpl<SingleEnterQuestionMapper, SingleEnterQuestion> implements ISingleEnterQuestionService {

    @Autowired
    private ISingleEnterQuestionColumnService singleEnterQuestionColumnService;

    @Autowired
    private ISingleEnterQuestionSubjectService singleEnterQuestionSubjectService;

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private ISubjectService subjectService;

    @Resource
    private DynamicTableMapper dynamicTableMapper;

    @Value("${system.tokenUrl}")
    private String tokenUrl;

    @Autowired
    private ITbDepService tbDepService;

    @Autowired
    private IAnswerService answerService;

    @Autowired
    private ITbDataService tbDataService;



    @Override
    public ResultBetter add(SingleEnterQuestionAddParam param) {

        Integer questionId = param.getQuestionId();
//        SingleEnterQuestion selectByQuestionId = this.selectByQuestionId(questionId);
//        if(Objects.nonNull(selectByQuestionId) && Constant.DEL_NORMAL.equals(selectByQuestionId.getDel())){
//            return ResultBetter.error("已存在该登记表,无法添加");
//        }
        SingleEnterQuestion singleEnterQuestion = new SingleEnterQuestion();
        singleEnterQuestion.setQuestionId(questionId);
        singleEnterQuestion.setDel(Constant.DEL_NORMAL);
        Date date = new Date();
        singleEnterQuestion.setCreateTime(date);
        singleEnterQuestion.setUpdateTime(date);
        singleEnterQuestion.setName(param.getName());
        Integer selectSubjectId = param.getSelectSubjectId();
        if(Objects.nonNull(selectSubjectId)){
            Qsubject qsubject = subjectService.querySubjectById(selectSubjectId);
            singleEnterQuestion.setSubjectId(selectSubjectId);
            if(Objects.nonNull(qsubject)){
                singleEnterQuestion.setSubjectColumnName(qsubject.getColumnName());
            }
        }
        this.save(singleEnterQuestion);

        //保存展示列
        List<Integer> columnIdList = param.getColumnId();
        if (CollectionUtil.isNotEmpty(columnIdList)) {
            List<SingleEnterQuestionColumn> saveColumnList = columnIdList.stream().map(columnId -> {
                SingleEnterQuestionColumn column = new SingleEnterQuestionColumn();
                column.setEnterQuestionId(singleEnterQuestion.getId());
                column.setSubjectId(columnId);
                column.setDel(Constant.DEL_NORMAL);
                column.setCreateTime(date);
                column.setUpdateTime(date);
                return column;
            }).collect(Collectors.toList());
            singleEnterQuestionColumnService.saveBatch(saveColumnList);
        }
        //保存填报题目
        List<Integer> subjectIdList = param.getSubjectId();
        if (CollectionUtil.isNotEmpty(subjectIdList)) {
            List<SingleEnterQuestionSubject> saveSubjectList = subjectIdList.stream().map(columnId -> {
                SingleEnterQuestionSubject subject = new SingleEnterQuestionSubject();
                subject.setEnterQuestionId(singleEnterQuestion.getId());
                subject.setSubjectId(columnId);
                subject.setDel(Constant.DEL_NORMAL);
                subject.setCreateTime(date);
                subject.setUpdateTime(date);
                return subject;
            }).collect(Collectors.toList());
            singleEnterQuestionSubjectService.saveBatch(saveSubjectList);
        }
        return ResultBetter.ok();
    }

    private SingleEnterQuestion selectByQuestionId(Integer questionId) {
        LambdaQueryWrapper<SingleEnterQuestion> lambda = new QueryWrapper<SingleEnterQuestion>().lambda();
        lambda.eq(SingleEnterQuestion::getQuestionId, questionId);
        lambda.eq(SingleEnterQuestion::getDel, Constant.DEL_NORMAL);
        List<SingleEnterQuestion> list = this.list(lambda);
        return list.isEmpty()?null:list.get(0);
    }

    @Override
    public IPage<SingleEnterQuestionListVo> queryPageList(Page<SingleEnterQuestion> page, SingleEnterQuestionListParam param) {
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        if (StringUtils.isNotBlank(param.getQuestionName())) {
            lambda.like(Question::getQuName, param.getQuestionName());
        }
        if (StringUtils.isNotBlank(param.getQuestionNameCategoryId())) {
            lambda.eq(Question::getCategoryId, param.getQuestionNameCategoryId());
        }

        if (StringUtils.isNotBlank(param.getWriteDeptId())) {
            lambda.like(Question::getDeptIds, param.getWriteDeptId());
        }
        if (StringUtils.isNotBlank(param.getSeeDeptId())) {
            lambda.like(Question::getSeeDeptIds, param.getSeeDeptId());
        }
        lambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        List<Question> questionList = questionService.list(lambda);
        if (CollectionUtil.isEmpty(questionList)) {
            return new Page<>();
        }
        List<Integer> questionIdList = questionList.stream().map(Question::getId).distinct().collect(Collectors.toList());
        Map<Integer, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));

        LambdaQueryWrapper<SingleEnterQuestion> enterQuestionLambdaQueryWrapper = new QueryWrapper<SingleEnterQuestion>().lambda();
        enterQuestionLambdaQueryWrapper.in(SingleEnterQuestion::getQuestionId, questionIdList);
        enterQuestionLambdaQueryWrapper.eq(SingleEnterQuestion::getDel, Constant.DEL_NORMAL);
        IPage<SingleEnterQuestion> singleEnterQuestionIPage = this.page(page, enterQuestionLambdaQueryWrapper);

        List<SingleEnterQuestion> singleEnterQuestionList = singleEnterQuestionIPage.getRecords();
        if (CollectionUtil.isEmpty(singleEnterQuestionList)) {
            return new Page<>();
        }

        List<TbData> dataList = tbDataService.selectByDataType(TbDataConstant.DATA_TYPE_QUESTION_REGISTER_CATEGORY);
        Map<String, String> dataMap = dataList.stream().collect(Collectors.toMap(TbData::getId, TbData::getValue, (k1, k2) -> k1));

        List<Integer> singleEnterQuestionIdList = singleEnterQuestionList.stream().map(SingleEnterQuestion::getId).distinct().collect(Collectors.toList());
        List<SingleEnterQuestionSubject> singleEnterQuestionSubjectList = singleEnterQuestionSubjectService.selectBySingleEnterQuestionIdList(singleEnterQuestionIdList);
        Map<Integer, List<SingleEnterQuestionSubject>> singleEnterQuestionSubjectMap = singleEnterQuestionSubjectList.stream().collect(Collectors.groupingBy(SingleEnterQuestionSubject::getEnterQuestionId));

        List<Integer> subjectIdList = singleEnterQuestionSubjectList.stream().map(SingleEnterQuestionSubject::getSubjectId).distinct().collect(Collectors.toList());
        List<Qsubject> subjectList = subjectService.selectByIds(subjectIdList);
        Map<Integer, Qsubject> subjectMap = subjectList.stream().collect(Collectors.toMap(Qsubject::getId, Function.identity()));

        ArrayList<SingleEnterQuestionListVo> resList = Lists.newArrayList();
        for (SingleEnterQuestion singleEnterQuestion : singleEnterQuestionList) {
            SingleEnterQuestionListVo vo = new SingleEnterQuestionListVo();
            vo.setId(singleEnterQuestion.getId());
            vo.setName(singleEnterQuestion.getName());
            Integer questionId = singleEnterQuestion.getQuestionId();
            Question question = questionMap.get(questionId);
            vo.setQuestionId(questionId);
            if (Objects.nonNull(question)) {
                vo.setQuestionName(question.getQuName());
                String categoryId = question.getCategoryId();
                if(StringUtils.isNotBlank(categoryId)){
                    String s = dataMap.get(categoryId);
                    vo.setQuestionNameCategoryName(s);
                }
            }
            List<SingleEnterQuestionSubject> singleEnterQuestionSubjects = singleEnterQuestionSubjectMap.get(singleEnterQuestion.getId());
            if (CollectionUtil.isNotEmpty(singleEnterQuestionSubjects)) {
                for (int i = 0; i < singleEnterQuestionSubjects.size(); i++) {
                    SingleEnterQuestionSubject singleEnterQuestionSubject = singleEnterQuestionSubjects.get(i);
                    Qsubject qsubject = subjectMap.get(singleEnterQuestionSubject.getSubjectId());
                    if (Objects.nonNull(qsubject)) {
                        if (i == 0) {
                            vo.setSubjectNameOne(qsubject.getSubName());
                        } else if (i == 1) {
                            vo.setSubjectNameTwo(qsubject.getSubName());
                        } else if (i == 2) {
                            vo.setSubjectNameThree(qsubject.getSubName());
                        } else {
                            break;
                        }
                    }
                }
            }

            resList.add(vo);
        }
        IPage<SingleEnterQuestionListVo> singleEnterQuestionListVoPage = new Page<>(page.getCurrent(), page.getSize());
        singleEnterQuestionListVoPage.setTotal(singleEnterQuestionIPage.getTotal());
        singleEnterQuestionListVoPage.setRecords(resList);
        return singleEnterQuestionListVoPage;
    }

    @Override
    public ResultBetter delete(IdIntegerParam param) {
        Integer id = param.getId();
        SingleEnterQuestion byId = this.getById(id);
        if(Objects.isNull(byId) || Constant.DEL_DELETED.equals(byId.getDel())){
            return ResultBetter.error("数据错误");
        }
        byId.setDel(Constant.DEL_DELETED);
        byId.setUpdateTime(new Date());
        this.updateById(byId);
        return ResultBetter.ok();
    }

    @Override
    public SingleEnterQuestionInfoVo info(String id) {
        SingleEnterQuestion byId = this.getById(id);
        if(Objects.isNull(byId) || Constant.DEL_DELETED.equals(byId.getDel())){
            return null;
        }
        SingleEnterQuestionInfoVo vo = new SingleEnterQuestionInfoVo();
        vo.setId(byId.getId());
        vo.setName(byId.getName());
        vo.setSelectSubjectId(byId.getSubjectId());
        vo.setQuestionId(byId.getQuestionId());
        Integer questionId = byId.getQuestionId();
        Question question = questionService.getById(questionId);
        if(Objects.nonNull(question)){
            vo.setQuestionName(question.getQuName());
        }

        List<SingleEnterQuestionColumn> singleEnterQuestionColumnList =  singleEnterQuestionColumnService.selectBySingleEnterQuestionId(byId.getId());
        List<SingleEnterQuestionSubject> singleEnterQuestionSubjectList =  singleEnterQuestionSubjectService.selectBySingleEnterQuestionId(byId.getId());

        List<Integer> subjectIdList = singleEnterQuestionColumnList.stream().map(SingleEnterQuestionColumn::getSubjectId).distinct().collect(Collectors.toList());
        subjectIdList.addAll( singleEnterQuestionSubjectList.stream().map(SingleEnterQuestionSubject::getSubjectId).distinct().collect(Collectors.toList()));

        List<Qsubject> subjectList = subjectService.selectByIds(subjectIdList);
        Map<Integer, Qsubject> subjectMap = subjectList.stream().collect(Collectors.toMap(Qsubject::getId, Function.identity()));

        List<SingleEnterQuestionInfoSubjectVo> columnList = Lists.newArrayList();
        for (SingleEnterQuestionColumn singleEnterQuestionColumn : singleEnterQuestionColumnList) {
            SingleEnterQuestionInfoSubjectVo singleEnterQuestionInfoSubjectVo = new SingleEnterQuestionInfoSubjectVo();
            columnList.add(singleEnterQuestionInfoSubjectVo);
            Qsubject qsubject = subjectMap.get(singleEnterQuestionColumn.getSubjectId());
            if(Objects.nonNull(qsubject)){
                singleEnterQuestionInfoSubjectVo.setId(qsubject.getId());
                singleEnterQuestionInfoSubjectVo.setSubName(qsubject.getSubName());
            }
        }
        vo.setColumnList(columnList);


        List<SingleEnterQuestionInfoSubjectVo> resSubjectList = Lists.newArrayList();
        for (SingleEnterQuestionSubject singleEnterQuestionSubject : singleEnterQuestionSubjectList) {
            SingleEnterQuestionInfoSubjectVo singleEnterQuestionInfoSubjectVo = new SingleEnterQuestionInfoSubjectVo();
            resSubjectList.add(singleEnterQuestionInfoSubjectVo);
            Qsubject qsubject = subjectMap.get(singleEnterQuestionSubject.getSubjectId());
            if(Objects.nonNull(qsubject)){
                singleEnterQuestionInfoSubjectVo.setId(qsubject.getId());
                singleEnterQuestionInfoSubjectVo.setSubName(qsubject.getSubName());
            }
        }
        vo.setSubjectList(resSubjectList);
        return vo;
    }

    @Override
    public ResultBetter edit(SingleEnterQuestionUpdateParam param) {
        SingleEnterQuestion singleEnterQuestion = this.getById(param.getId());
        if(Objects.isNull(singleEnterQuestion) || Constant.DEL_DELETED.equals(singleEnterQuestion.getDel())){
            return ResultBetter.error("数据错误");
        }

        singleEnterQuestion.setQuestionId(param.getQuestionId());
        singleEnterQuestion.setDel(Constant.DEL_NORMAL);
        Date date = new Date();
        singleEnterQuestion.setUpdateTime(date);
        singleEnterQuestion.setName(param.getName());
        Integer selectSubjectId = param.getSelectSubjectId();
        if(Objects.nonNull(selectSubjectId)){
            Qsubject qsubject = subjectService.querySubjectById(selectSubjectId);
            singleEnterQuestion.setSubjectId(selectSubjectId);
            if(Objects.nonNull(qsubject)){
                singleEnterQuestion.setSubjectColumnName(qsubject.getColumnName());
            }
        }
        this.updateById(singleEnterQuestion);

        //删除列
        LambdaUpdateWrapper<SingleEnterQuestionColumn> lambda = new UpdateWrapper<SingleEnterQuestionColumn>().lambda();
        SingleEnterQuestionColumn emptyEntity = new SingleEnterQuestionColumn();
        lambda.eq(SingleEnterQuestionColumn::getEnterQuestionId, singleEnterQuestion.getId())
                .eq(SingleEnterQuestionColumn::getDel, Constant.DEL_NORMAL)
                .set(SingleEnterQuestionColumn::getDel, Constant.DEL_DELETED)
                .set(SingleEnterQuestionColumn::getUpdateTime, date);
        singleEnterQuestionColumnService.update(emptyEntity, lambda);

        //保存展示列
        List<Integer> columnIdList = param.getColumnId();
        if (CollectionUtil.isNotEmpty(columnIdList)) {
            List<SingleEnterQuestionColumn> saveColumnList = columnIdList.stream().map(columnId -> {
                SingleEnterQuestionColumn column = new SingleEnterQuestionColumn();
                column.setEnterQuestionId(singleEnterQuestion.getId());
                column.setSubjectId(columnId);
                column.setDel(Constant.DEL_NORMAL);
                column.setCreateTime(date);
                column.setUpdateTime(date);
                return column;
            }).collect(Collectors.toList());
            singleEnterQuestionColumnService.saveBatch(saveColumnList);
        }

        //删除填报题目
        LambdaUpdateWrapper<SingleEnterQuestionSubject> subjectLambdaUpdateWrapper = new UpdateWrapper<SingleEnterQuestionSubject>().lambda();
        SingleEnterQuestionSubject subjectEmptyEntity = new SingleEnterQuestionSubject();
        subjectLambdaUpdateWrapper.eq(SingleEnterQuestionSubject::getEnterQuestionId, singleEnterQuestion.getId())
                .eq(SingleEnterQuestionSubject::getDel, Constant.DEL_NORMAL)
                .set(SingleEnterQuestionSubject::getDel, Constant.DEL_DELETED)
                .set(SingleEnterQuestionSubject::getUpdateTime, date);
        singleEnterQuestionSubjectService.update(subjectEmptyEntity, subjectLambdaUpdateWrapper);

        //保存填报题目
        List<Integer> subjectIdList = param.getSubjectId();
        if (CollectionUtil.isNotEmpty(subjectIdList)) {
            List<SingleEnterQuestionSubject> saveSubjectList = subjectIdList.stream().map(columnId -> {
                SingleEnterQuestionSubject subject = new SingleEnterQuestionSubject();
                subject.setEnterQuestionId(singleEnterQuestion.getId());
                subject.setSubjectId(columnId);
                subject.setDel(Constant.DEL_NORMAL);
                subject.setCreateTime(date);
                subject.setUpdateTime(date);
                return subject;
            }).collect(Collectors.toList());
            singleEnterQuestionSubjectService.saveBatch(saveSubjectList);
        }

        return ResultBetter.ok();
    }

    @Override
    public List<SingleEnterQuestion> selectAllByQuestionIdList(List<Integer> questionIdList) {
        if(CollectionUtil.isEmpty(questionIdList)){
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SingleEnterQuestion> enterQuestionLambdaQueryWrapper = new QueryWrapper<SingleEnterQuestion>().lambda();
        enterQuestionLambdaQueryWrapper.in(SingleEnterQuestion::getQuestionId, questionIdList);
        enterQuestionLambdaQueryWrapper.eq(SingleEnterQuestion::getDel, Constant.DEL_NORMAL);
        return this.list(enterQuestionLambdaQueryWrapper);
    }


    @Override
    public List<SingleEnterQuestionQuestionCheckVo> startCheckList(QuestionCheckParam questionCheckParam, Data data) {
        String quName = questionCheckParam.getQuName();
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        if(StringUtils.isNotBlank(quName)){
            lambda.like(Question::getQuName, quName);
        }
//        String deptId = data.getTbUser().getDepId();
        String deptId = data.getTbUser().getDepId();
        String positionId = data.getTbUser().getPositionId();
        String userId = data.getTbUser().getId();
        lambda.like(Question::getDeptIds,deptId);
//        boolean checkPositionFlag = checkPosition(quName, deptId,positionId,userId, lambda);
//        if(checkPositionFlag){
//            return new Page<>();
//        }

        List<Question> questionList = questionService.list(lambda);
        if(questionList.isEmpty()){
            return Lists.newArrayList();
        }

        List<Integer> questionIdList = questionList.stream().map(Question::getId).distinct().collect(Collectors.toList());
        Map<Integer, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));

        List<SingleEnterQuestion> selectAllList= this.selectAllByQuestionIdList(questionIdList);
        if(CollectionUtil.isEmpty(selectAllList)){
            return Lists.newArrayList();
        }

        List<SingleEnterQuestionQuestionCheckVo> answerPatientFillingInVos = selectAllList.stream().map(singleEnterQuestion -> {
            SingleEnterQuestionQuestionCheckVo vo = new SingleEnterQuestionQuestionCheckVo();
            vo.setName(singleEnterQuestion.getName());
            Question question = questionMap.get(singleEnterQuestion.getQuestionId());
            if(Objects.nonNull(question)){
                BeanUtils.copyProperties(question,vo);
                vo.setSingleEnterQuestionId(singleEnterQuestion.getId());
                return vo;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return answerPatientFillingInVos;
    }


    @Override
    public ResultBetter<SingleEnterQuestionEnterQuestionHeadListVo> enterQuestionHeadList(SingleEnterQuestionEnterQuestionHeadListParam param) {
        SingleEnterQuestion singleEnterQuestion = this.getById(param.getSingleEnterQuestionId());
        if(Objects.isNull(singleEnterQuestion) || Constant.DEL_DELETED.equals(singleEnterQuestion.getDel())) {
            return ResultBetter.error("录入表不存在");
        }
        Integer questionId = singleEnterQuestion.getQuestionId();
        Question question = questionService.getById(questionId);
        if(Objects.isNull(question) || QuestionConstant.DEL_DELETED.equals(question.getDel())) {
            return ResultBetter.error("登记表不存在");
        }

        SingleEnterQuestionEnterQuestionHeadListVo vo = new SingleEnterQuestionEnterQuestionHeadListVo();

        List<SingleEnterQuestionColumn> singleEnterQuestionColumnList =  singleEnterQuestionColumnService.selectBySingleEnterQuestionId(singleEnterQuestion.getId());
        List<SingleEnterQuestionSubject> singleEnterQuestionSubjectList =  singleEnterQuestionSubjectService.selectBySingleEnterQuestionId(singleEnterQuestion.getId());

        List<Integer> subjectIdList = singleEnterQuestionColumnList.stream().map(SingleEnterQuestionColumn::getSubjectId).distinct().collect(Collectors.toList());
        subjectIdList.addAll( singleEnterQuestionSubjectList.stream().map(SingleEnterQuestionSubject::getSubjectId).distinct().collect(Collectors.toList()));

        List<Qsubject> subjectList = subjectService.selectByIds(subjectIdList);
        Map<Integer, Qsubject> subjectMap = subjectList.stream().collect(Collectors.toMap(Qsubject::getId, Function.identity()));

        List<SingleEnterQuestionEnterQuestionHeadListDetailVo> columnList = Lists.newArrayList();
        for (SingleEnterQuestionColumn singleEnterQuestionColumn : singleEnterQuestionColumnList) {
            SingleEnterQuestionEnterQuestionHeadListDetailVo singleEnterQuestionInfoSubjectVo = new SingleEnterQuestionEnterQuestionHeadListDetailVo();
            columnList.add(singleEnterQuestionInfoSubjectVo);
            Qsubject qsubject = subjectMap.get(singleEnterQuestionColumn.getSubjectId());
            if(Objects.nonNull(qsubject)){
                singleEnterQuestionInfoSubjectVo.setColumnName(qsubject.getColumnName());
                singleEnterQuestionInfoSubjectVo.setSubName(qsubject.getSubName());
            }
        }
        vo.setColumnList(columnList);


        List<SingleEnterQuestionEnterQuestionHeadListDetailVo> resSubjectList = Lists.newArrayList();
        for (SingleEnterQuestionSubject singleEnterQuestionSubject : singleEnterQuestionSubjectList) {
            SingleEnterQuestionEnterQuestionHeadListDetailVo singleEnterQuestionInfoSubjectVo = new SingleEnterQuestionEnterQuestionHeadListDetailVo();
            resSubjectList.add(singleEnterQuestionInfoSubjectVo);
            Qsubject qsubject = subjectMap.get(singleEnterQuestionSubject.getSubjectId());
            if(Objects.nonNull(qsubject)){
                singleEnterQuestionInfoSubjectVo.setColumnName(qsubject.getColumnName());
                singleEnterQuestionInfoSubjectVo.setSubName(qsubject.getSubName());
            }
        }
        vo.setSubjectList(resSubjectList);

        return ResultBetter.ok(vo);
    }

    @Override
    public IPage<LinkedHashMap<String, Object>> enterQuestionDataList(SingleEnterQuestionEnterQuestionListParam param, Integer pageNo, Integer pageSize) {

        SingleEnterQuestion singleEnterQuestion = this.getById(param.getSingleEnterQuestionId());
        if(Objects.isNull(singleEnterQuestion) || Constant.DEL_DELETED.equals(singleEnterQuestion.getDel())) {
            return new Page<>();
        }
        Integer questionId = singleEnterQuestion.getQuestionId();
        Question question = questionService.getById(questionId);
        if(Objects.isNull(question) || QuestionConstant.DEL_DELETED.equals(question.getDel())) {
            return new Page<>();
        }

        StringBuffer sqlCount = new StringBuffer();
        sqlCount.append("select count(1) from `");
        sqlCount.append(question.getTableName());
        sqlCount.append("` where  del = '0' and table_answer_status = '0' ");
        if(param.getStatus().equals(SingleEnterQuestionConstant.ENTER_QUESTION_DATA_LIST_STATUS_HANDLE)){
            sqlCount.append("and  ");
            sqlCount.append(singleEnterQuestion.getSubjectColumnName());
            sqlCount.append(" = 'y' ");
        }else{
            sqlCount.append("and (");
            sqlCount.append(singleEnterQuestion.getSubjectColumnName());
            sqlCount.append(" = 'y1' or ");
            sqlCount.append(singleEnterQuestion.getSubjectColumnName());
            sqlCount.append(" = 'y2') ");
        }
        log.info("enterQuestionDataList-----select total sqlCount:{}", sqlCount.toString());
        Long total = dynamicTableMapper.countDynamicTable(sqlCount.toString());
        if(total ==null || total<=0){
            return new Page<>();
        }
        if(pageSize>total){
            pageSize = total.intValue();
        }

        List<SubjectVo> subjectList = subjectService.selectSubjectAndOptionByQuId(questionId);
        Map<Integer, SubjectVo> subjectMap = subjectList.stream().collect(Collectors.toMap(SubjectVo::getId, Function.identity()));

        List<SingleEnterQuestionColumn> singleEnterQuestionColumnList =  singleEnterQuestionColumnService.selectBySingleEnterQuestionId(singleEnterQuestion.getId());
        List<SingleEnterQuestionSubject> singleEnterQuestionSubjectList =  singleEnterQuestionSubjectService.selectBySingleEnterQuestionId(singleEnterQuestion.getId());


        StringBuffer sqlSelect = new StringBuffer();
        sqlSelect.append("select * from `");
        sqlSelect.append(question.getTableName());
        sqlSelect.append("` where del = '0' and table_answer_status = '0'  ");
        if(param.getStatus().equals(SingleEnterQuestionConstant.ENTER_QUESTION_DATA_LIST_STATUS_HANDLE)){
            sqlSelect.append("and  ");
            sqlSelect.append(singleEnterQuestion.getSubjectColumnName());
            sqlSelect.append(" = 'y' ");
        }else{
            sqlSelect.append("and (");
            sqlSelect.append(singleEnterQuestion.getSubjectColumnName());
            sqlSelect.append(" = 'y1' or ");
            sqlSelect.append(singleEnterQuestion.getSubjectColumnName());
            sqlSelect.append(" = 'y2') ");
        }
        sqlSelect.append("limit ");
        sqlSelect.append((pageNo - 1) * pageSize);
        sqlSelect.append(",");
        sqlSelect.append(pageSize);
        log.info("enterQuestionDataList-----select data sqlSelect:{}", sqlSelect.toString());
        List<Map<String, String>> dataList = dynamicTableMapper.selectDynamicTableColumnList(sqlSelect.toString());
        List<LinkedHashMap<String, Object>> detailDataList = Lists.newArrayList();
        for (Map<String, String> dataItemMap : dataList) {
            LinkedHashMap<String, Object> valueItem = Maps.newLinkedHashMap();
            valueItem.put("mappingTableId",dataItemMap.get("summary_mapping_table_id"));
            valueItem.put("singleEnterQuestionId",singleEnterQuestion.getId());
            for (SingleEnterQuestionColumn singleEnterQuestionColumn : singleEnterQuestionColumnList) {
                SubjectVo qsubject = subjectMap.get(singleEnterQuestionColumn.getSubjectId());
                if (qsubject == null || QsubjectConstant.SUB_TYPE_TITLE.equals(qsubject.getSubType())) {
                    continue;
                }
                String columnName = qsubject.getColumnName();
                if(StringUtils.isNotBlank(columnName)){
                    valueItem.put(qsubject.getColumnName(), dataItemMap.get(qsubject.getColumnName()));
                }
            }

            for (SingleEnterQuestionSubject singleEnterQuestionSubject : singleEnterQuestionSubjectList) {
                SubjectVo qsubject = subjectMap.get(singleEnterQuestionSubject.getSubjectId());
                if (qsubject == null || QsubjectConstant.SUB_TYPE_TITLE.equals(qsubject.getSubType())) {
                    continue;
                }
                String columnName = qsubject.getColumnName();
                if(StringUtils.isNotBlank(columnName)){
                    String s = dataItemMap.get(qsubject.getColumnName());
                    EnterQuestionDataListCompleteVo completeVo = PojoUtils.map(qsubject, EnterQuestionDataListCompleteVo.class);
                    completeVo.setFillValue(s);
                    valueItem.put(qsubject.getColumnName(), completeVo);
                }
            }
            detailDataList.add(valueItem);
        }
        IPage<LinkedHashMap<String, Object>> singleEnterQuestionListVoPage = new Page<>(pageNo, pageSize);
        singleEnterQuestionListVoPage.setTotal(total);
        singleEnterQuestionListVoPage.setRecords(detailDataList);
        return singleEnterQuestionListVoPage;
    }

    @Override
    public ResultBetter saveData(String cookie, SingleEnterQuestionSaveDataParam saveParam) {
        //解析token
        return getResultBetter(cookie, saveParam.getMappingTableId(),saveParam.getAnswers(),AnswerConstant.SINGLE_ENTER_QUESTION_ANSWER_STATUS_SAVE,saveParam.getSingleEnterQuestionId());
    }

    private ResultBetter getResultBetter(String cookie, String mappingTableId,Answers[] answers,Integer type,Integer singleEnterQuestionId) {
        String res = HttpClient.doPost(tokenUrl, cookie, null);
        JsonRootBean jsonRootBean = JSON.parseObject(res, JsonRootBean.class);
        String creater = "";
        String creater_name = "";
        String creater_deptid = "";
        if (jsonRootBean != null) {
            if (jsonRootBean.getData() != null) {
                creater = jsonRootBean.getData().getTbUser().getId();
                creater_name = jsonRootBean.getData().getTbUser().getUserName();
                creater_deptid = jsonRootBean.getData().getTbUser().getDepId();
            }
        }
        TbDep tbDep = new TbDep();
        if (org.apache.commons.lang.StringUtils.isNotBlank(creater_deptid)) {
            tbDep = tbDepService.getById(creater_deptid);
        }

        Answer answer = answerService.selectBySummaryMappingTableId(mappingTableId);

        if(answer==null){
            return ResultBetter.error("数据不存在。");
        }else{
//            if(AnswerConstant.ANSWER_STATUS_RELEASE.equals(answer.getAnswerStatus())){
//                return ResultBetter.error("该记录已提交,无法更改。");
//            }
        }

        Integer quId = answer.getQuId();
        Question question = questionService.getById(quId);
        if (question == null || QuestionConstant.DEL_DELETED.equals(question.getDel())) {
            return ResultBetter.error("问卷不存在,无法保存。");
        }

        SingleEnterQuestion singleEnterQuestion = this.getById(singleEnterQuestionId);
        if(Objects.isNull(singleEnterQuestion) || Constant.DEL_DELETED.equals(singleEnterQuestion.getDel())) {
            return ResultBetter.error("录入表不存在");
        }

        //插入总表
        answer.setAnswerStatus(AnswerConstant.ANSWER_STATUS_RELEASE);
        Date date = new Date();
        answer.setSubmitTime(date);
        answer.setCreater(creater);
        answer.setCreaterName(creater_name);
        answer.setCreaterDeptid(creater_deptid);
        answer.setCreaterDeptname(tbDep.getDepname());
        answer.setAnswerTime(date);

        Map<String, String> mapCache = new HashMap<>();
        for (Answers a : answers) {
            mapCache.put(a.getSubColumnName(), a.getSubValue());
        }

        if(mapCache.containsKey(AnswerConstant.COLUMN_NAME_TH_MONTH)
                && mapCache.get(AnswerConstant.COLUMN_NAME_TH_MONTH)!=null){
            answer.setQuestionAnswerTime(mapCache.get(AnswerConstant.COLUMN_NAME_TH_MONTH));
        }
        if(mapCache.containsKey(AnswerConstant.COLUMN_NAME_TH_QUARTER)
                && mapCache.get(AnswerConstant.COLUMN_NAME_TH_QUARTER)!=null){
            answer.setQuestionAnswerTime(mapCache.get(AnswerConstant.COLUMN_NAME_TH_QUARTER));
        }
        if(mapCache.containsKey(AnswerConstant.COLUMN_NAME_TH_YEAR)
                && mapCache.get(AnswerConstant.COLUMN_NAME_TH_YEAR)!=null){
            answer.setQuestionAnswerTime(mapCache.get(AnswerConstant.COLUMN_NAME_TH_YEAR));
        }

        if(mapCache.containsKey(AnswerConstant.COLUMN_NAME_CASE_ID)
                && mapCache.get(AnswerConstant.COLUMN_NAME_CASE_ID)!=null){
            answer.setHospitalInNo(mapCache.get(AnswerConstant.COLUMN_NAME_CASE_ID));
        }

        if(mapCache.containsKey(AnswerConstant.COLUMN_NAME_PATIENT_NAME)
                && mapCache.get(AnswerConstant.COLUMN_NAME_PATIENT_NAME)!=null){
            answer.setPatientName(mapCache.get(AnswerConstant.COLUMN_NAME_PATIENT_NAME));
        }

        if(mapCache.containsKey(AnswerConstant.COLUMN_NAME_PATIENT_NAME_LOWER_CASE)
                && mapCache.get(AnswerConstant.COLUMN_NAME_PATIENT_NAME_LOWER_CASE)!=null){
            answer.setPatientName(mapCache.get(AnswerConstant.COLUMN_NAME_PATIENT_NAME_LOWER_CASE));
        }

        if(mapCache.containsKey(AnswerConstant.COLUMN_NAME_AGE)
                && mapCache.get(AnswerConstant.COLUMN_NAME_AGE)!=null){
            answer.setAge(Integer.parseInt(mapCache.get(AnswerConstant.COLUMN_NAME_AGE)));
        }

        if(mapCache.containsKey(AnswerConstant.COLUMN_NAME_AGE_LOWER_CASE)
                && mapCache.get(AnswerConstant.COLUMN_NAME_AGE_LOWER_CASE)!=null){
            answer.setAge(Integer.parseInt(mapCache.get(AnswerConstant.COLUMN_NAME_AGE_LOWER_CASE)));
        }

        if(mapCache.containsKey(AnswerConstant.COLUMN_NAME_IN_TIME)
                && mapCache.get(AnswerConstant.COLUMN_NAME_IN_TIME)!=null){
            String dateInTimeString = mapCache.get(AnswerConstant.COLUMN_NAME_IN_TIME);
            Date dateInTime = DateUtil.parse(dateInTimeString).toJdkDate();
            answer.setInTime(dateInTime);
        }

        if(mapCache.containsKey(AnswerConstant.COLUMN_NAME_OUT_TIME)
                && mapCache.get(AnswerConstant.COLUMN_NAME_OUT_TIME)!=null){
            String dateOutTimeString = mapCache.get(AnswerConstant.COLUMN_NAME_OUT_TIME);
            Date dateInTime = DateUtil.parse(dateOutTimeString).toJdkDate();
            answer.setOutTime(dateInTime);
        }

        List<Qsubject> subjectList = subjectService.selectSubjectByQuId(question.getId());

        answer.setUpdateTime(date);
        answerService.updateById(answer);
        //保存痕迹相关
        answerService.saveAnswerMark(mapCache,subjectList,answer,question,creater,AnswerConstant.SOURCE_PC);
        //查

        //更新子表
        StringBuffer sqlAns = new StringBuffer();
        sqlAns.append("update `").append(question.getTableName()).append( "` set ");
        for (int i = 0; i < subjectList.size(); i++) {
            Qsubject qsubjectDynamicTable = subjectList.get(i);
            String subType = qsubjectDynamicTable.getSubType();
            Integer del = qsubjectDynamicTable.getDel();
            if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                    || QuestionConstant.DEL_DELETED.equals(del) || mapCache.get(qsubjectDynamicTable.getColumnName())==null
                    || StringUtils.isBlank(mapCache.get(qsubjectDynamicTable.getColumnName()))) {
                continue;
            }
            sqlAns.append("`");
            sqlAns.append(qsubjectDynamicTable.getColumnName());
            sqlAns.append("`");
            sqlAns.append("=");
            sqlAns.append("'");
            sqlAns.append(mapCache.get(qsubjectDynamicTable.getColumnName()));
            sqlAns.append("'");
            sqlAns.append(",");
        }
        if(type.equals(AnswerConstant.SINGLE_ENTER_QUESTION_ANSWER_STATUS_SAVE)){
            sqlAns.append("`");
            sqlAns.append(singleEnterQuestion.getSubjectColumnName());
            sqlAns.append("`");
            sqlAns.append(" = 'y1',");
        }else if(type.equals(AnswerConstant.SINGLE_ENTER_QUESTION_ANSWER_STATUS_AMENDMENT)){
            sqlAns.append("`");
            sqlAns.append(singleEnterQuestion.getSubjectColumnName());
            sqlAns.append("`");
            sqlAns.append(" = 'y2',");
        }else{
            sqlAns.append("`");
            sqlAns.append(singleEnterQuestion.getSubjectColumnName());
            sqlAns.append("`");
            sqlAns.append(" = 'y9',");
        }

        sqlAns.append("`tbksmc`='");
        sqlAns.append(tbDep.getDepname());
        sqlAns.append("',");
        sqlAns.append("`tbksdm`='");
        sqlAns.append(creater_deptid);
        sqlAns.append("'");
        //                    sqlAns.delete(sqlAns.length()-1,sqlAns.length());
        sqlAns.append(" where summary_mapping_table_id = '");
        sqlAns.append(answer.getSummaryMappingTableId());
        sqlAns.append("'");
        log.info("SingleEnterQuestionSaveParam-----update sqlAns:{}", sqlAns.toString());
        dynamicTableMapper.updateDynamicTable(sqlAns.toString());


        AnswerIdVo vo = new AnswerIdVo();
        vo.setAnswerId(answer.getId());
        return ResultBetter.ok(vo);
    }

    @Override
    public ResultBetter amendmentSaveData(String cookie, SingleEnterQuestionAmendmentSaveDataParam amendmentSaveDataParam) {
        return getResultBetter(cookie, amendmentSaveDataParam.getMappingTableId(),amendmentSaveDataParam.getAnswers(),AnswerConstant.SINGLE_ENTER_QUESTION_ANSWER_STATUS_AMENDMENT,amendmentSaveDataParam.getSingleEnterQuestionId());
    }
}
