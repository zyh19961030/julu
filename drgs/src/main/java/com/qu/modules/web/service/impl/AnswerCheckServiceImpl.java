package com.qu.modules.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
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
import com.qu.event.AnswerCheckStatisticDetailEvent;
import com.qu.exporter.AnswerCheckeDetailExporter;
import com.qu.modules.web.dto.AnswerCheckStatisticDetailEventDto;
import com.qu.modules.web.entity.*;
import com.qu.modules.web.mapper.AnswerCheckMapper;
import com.qu.modules.web.mapper.DynamicTableMapper;
import com.qu.modules.web.mapper.QsubjectMapper;
import com.qu.modules.web.mapper.QuestionMapper;
import com.qu.modules.web.param.*;
import com.qu.modules.web.pojo.Data;
import com.qu.modules.web.pojo.JsonRootBean;
import com.qu.modules.web.request.AnswerCheckListRequest;
import com.qu.modules.web.request.CheckQuestionHistoryStatisticDetailListExportRequest;
import com.qu.modules.web.request.CheckQuestionHistoryStatisticDetailListRequest;
import com.qu.modules.web.request.CheckQuestionHistoryStatisticRecordListRequest;
import com.qu.modules.web.service.*;
import com.qu.modules.web.vo.*;
import com.qu.util.ExcelExportUtil;
import com.qu.util.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.api.vo.ResultBetter;
import org.jeecg.common.api.vo.ResultBetterFactory;
import org.jeecg.common.api.vo.ResultFactory;
import org.jeecg.common.util.UUIDGenerator;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 检查表问卷总表
 * @Author: jeecg-boot
 * @Date: 2022-07-30
 * @Version: V1.0
 */
@Slf4j
@Service
public class AnswerCheckServiceImpl extends ServiceImpl<AnswerCheckMapper, AnswerCheck> implements IAnswerCheckService {

    @Value("${system.tokenUrl}")
    private String tokenUrl;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QsubjectMapper qsubjectMapper;

    @Autowired
    private DynamicTableMapper dynamicTableMapper;

    @Autowired
    private ITbUserService tbUserService;
    @Autowired
    private ITbDepService tbDepService;

    @Autowired
    private ICheckDetailSetService checkDetailSetService;

    @Autowired
    private ISubjectService subjectService;

    @Autowired
    private IQuestionCheckedDeptService questionCheckedDeptService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private IAnswerCheckStatisticDetailService answerCheckStatisticDetailService;

    @Autowired
    private IQsubjectVersionService qsubjectVersionService;

    @Autowired
    private IQoptionVersionService qoptionVersionService;

    @Autowired
    private IQuestionVersionService questionVersionService;


    @Override
    public IPage<AnswerCheckVo> checkQuestionFillInList(AnswerCheckListRequest request, Integer pageNo, Integer pageSize, Integer answerStatus) {
        Page<AnswerCheck> page = new Page<>(pageNo, pageSize);

        LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new QueryWrapper<Question>().lambda();
        questionLambdaQueryWrapper.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambdaQueryWrapper.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        questionLambdaQueryWrapper.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        if (request != null && StringUtils.isNotBlank(request.getQuName())) {
            questionLambdaQueryWrapper.like(Question::getQuName, request.getQuName());
        }
        if (request != null && StringUtils.isNotBlank(request.getUserDeptId())) {
            questionLambdaQueryWrapper.like(Question::getDeptIds, request.getUserDeptId());
        }
        List<Question> questionList = questionMapper.selectList(questionLambdaQueryWrapper);
        List<Integer> questionSearchIdList = questionList.stream().map(Question::getId).distinct().collect(Collectors.toList());
        if (questionSearchIdList.isEmpty()) {
            return new Page<>();
        }

        LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
        lambda.eq(AnswerCheck::getDel, AnswerCheckConstant.DEL_NORMAL);
        if (!questionSearchIdList.isEmpty()) {
            lambda.in(AnswerCheck::getQuId, questionSearchIdList);
        }
        if (answerStatus != null) {
            lambda.eq(AnswerCheck::getAnswerStatus, answerStatus);
        }
        if (request != null && StringUtils.isNotBlank(request.getDeptId())) {
            lambda.eq(AnswerCheck::getCheckedDept, request.getDeptId());
        }
        if (request != null && StringUtils.isNotBlank(request.getUserId())) {
            lambda.eq(AnswerCheck::getCreater, request.getUserId());
        }
        if (request != null && StringUtils.isNotBlank(request.getCreaterDeptId())) {
            lambda.eq(AnswerCheck::getCreaterDeptId, request.getCreaterDeptId());
        }
        if (request != null && request.getStartDate() != null) {
            lambda.ge(AnswerCheck::getUpdateTime, request.getStartDate());
        }
        if (request != null && request.getEndDate() != null) {
            Date endDate = new DateTime(request.getEndDate()).plusDays(1).toDate();
            lambda.le(AnswerCheck::getUpdateTime, endDate);
        }
        if (request != null && StringUtils.isNotBlank(request.getCheckMonth()) && !"null".equals(request.getCheckMonth())) {
            lambda.eq(AnswerCheck::getCheckMonth, request.getCheckMonth());
        }

        lambda.orderByAsc(AnswerCheck::getAnswerStatus);
        lambda.orderByDesc(AnswerCheck::getAnswerTime);
        IPage<AnswerCheck> answerCheckIPage = this.page(page, lambda);
        List<AnswerCheck> answerCheckList = answerCheckIPage.getRecords();
        if (answerCheckList.isEmpty()) {
            return new Page<>();
        }

        List<Integer> questionIdList = answerCheckList.stream().map(AnswerCheck::getQuId).distinct().collect(Collectors.toList());
        List<Question> answerQuestionList = questionMapper.selectBatchIds(questionIdList);
        Map<Integer, Question> questionMap = answerQuestionList.stream().collect(Collectors.toMap(Question::getId, q -> q));

        List<String> checkedDeptIdList = answerCheckList.stream().map(AnswerCheck::getCheckedDept).distinct().collect(Collectors.toList());
        List<TbDep> checkedDeptList = tbDepService.listByIdList(checkedDeptIdList);
        Map<String, TbDep> checkedDeptListMap = checkedDeptList.stream().collect(Collectors.toMap(TbDep::getId, t -> t));

        List<AnswerCheckVo> answerCheckVoList = answerCheckList.stream().map(answerCheck -> {
            AnswerCheckVo answerCheckVo = new AnswerCheckVo();
            BeanUtils.copyProperties(answerCheck, answerCheckVo);
            answerCheckVo.setQuName(questionMap.get(answerCheck.getQuId()).getQuName());
            TbDep tbDep = checkedDeptListMap.get(answerCheck.getCheckedDept());
            if(tbDep!=null){
                answerCheckVo.setCheckedDeptName(tbDep.getDepname());
            }
            return answerCheckVo;
        }).collect(Collectors.toList());

        IPage<AnswerCheckVo> answerCheckPage = new Page<>(pageNo, pageSize);
        answerCheckPage.setTotal(answerCheckIPage.getTotal());
        answerCheckPage.setRecords(answerCheckVoList);
        return answerCheckPage;
    }

    @Override
    public IPage<CheckQuestionHistoryStatisticRecordListVo> checkQuestionHistoryStatisticRecordList(CheckQuestionHistoryStatisticRecordListRequest recordListRequest, Integer pageNo, Integer pageSize) {
        Page<AnswerCheck> page = new Page<>(pageNo, pageSize);

        LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new QueryWrapper<Question>().lambda();
        questionLambdaQueryWrapper.eq(Question::getId, recordListRequest.getQuId());
        questionLambdaQueryWrapper.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambdaQueryWrapper.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        questionLambdaQueryWrapper.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        String deptId = recordListRequest.getDeptId();
        if (StringUtils.isNotBlank(deptId)) {
            questionLambdaQueryWrapper.like(Question::getDeptIds, deptId);
        }
        List<Question> questionList = questionMapper.selectList(questionLambdaQueryWrapper);
        List<Integer> questionSearchIdList = questionList.stream().map(Question::getId).distinct().collect(Collectors.toList());
        if (questionSearchIdList.isEmpty()) {
            return new Page<>();
        }
        String checkedDeptId = recordListRequest.getCheckedDeptId();
        if (StringUtils.isNotBlank(checkedDeptId)) {
            //被检查科室
            List<QuestionCheckedDept> questionCheckedDeptList = questionCheckedDeptService.selectCheckedDeptByQuIdAndDeptId(recordListRequest.getQuId(), checkedDeptId);
            if (questionCheckedDeptList.isEmpty()) {
                return new Page<>();
            }
        }

        LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
        lambda.in(AnswerCheck::getQuId, questionSearchIdList);
        lambda.eq(AnswerCheck::getAnswerStatus, AnswerCheckConstant.ANSWER_STATUS_RELEASE);
        lambda.eq(AnswerCheck::getDel, AnswerCheckConstant.DEL_NORMAL);

        if (recordListRequest.getStartDate() != null) {
            lambda.ge(AnswerCheck::getAnswerTime, recordListRequest.getStartDate());
        }

        if (recordListRequest.getEndDate() != null) {
            Date endDate = new DateTime(recordListRequest.getEndDate()).plusDays(1).toDate();
            lambda.le(AnswerCheck::getAnswerTime, endDate);
        }

        String checkMonth = recordListRequest.getCheckMonth();
        if(StringUtils.isNotBlank(checkMonth)){
            lambda.eq(AnswerCheck::getCheckMonth, checkMonth);
        }

        if (StringUtils.isNotBlank(checkedDeptId)) {
            lambda.eq(AnswerCheck::getCheckedDept, checkedDeptId);
        }

        if (StringUtils.isNotBlank(deptId)) {
            lambda.eq(AnswerCheck::getCreaterDeptId, deptId);
        }

        String selfDeptId = recordListRequest.getSelfDeptId();
        if (StringUtils.isNotBlank(selfDeptId)) {
            lambda.eq(AnswerCheck::getCheckedDept, selfDeptId);
            lambda.eq(AnswerCheck::getCreaterDeptId, selfDeptId);
        }

        lambda.orderByDesc(AnswerCheck::getAnswerTime);
        IPage<AnswerCheck> answerCheckIPage = this.page(page, lambda);
        List<AnswerCheck> answerCheckList = answerCheckIPage.getRecords();
        if (answerCheckList.isEmpty()) {
            return new Page<>();
        }

        List<Integer> questionIdList = answerCheckList.stream().map(AnswerCheck::getQuId).distinct().collect(Collectors.toList());
        List<Question> answerQuestionList = questionMapper.selectBatchIds(questionIdList);
        Map<Integer, Question> questionMap = answerQuestionList.stream().collect(Collectors.toMap(Question::getId, q -> q));

        List<String> checkedDeptIdList = answerCheckList.stream().map(AnswerCheck::getCheckedDept).distinct().collect(Collectors.toList());
        List<TbDep> checkedDeptList = tbDepService.listByIdList(checkedDeptIdList);
        Map<String, TbDep> checkedDeptListMap = checkedDeptList.stream().collect(Collectors.toMap(TbDep::getId, t -> t));

        List<CheckQuestionHistoryStatisticRecordListVo> answerCheckVoList = answerCheckList.stream().map(answerCheck -> {
            CheckQuestionHistoryStatisticRecordListVo vo = new CheckQuestionHistoryStatisticRecordListVo();
            BeanUtils.copyProperties(answerCheck, vo);
            vo.setQuName(questionMap.get(answerCheck.getQuId()).getQuName());
            TbDep tbDep = checkedDeptListMap.get(answerCheck.getCheckedDept());
            if(tbDep!=null){
                vo.setCheckedDeptName(tbDep.getDepname());
            }

            return vo;
        }).collect(Collectors.toList());

        IPage<CheckQuestionHistoryStatisticRecordListVo> answerCheckPage = new Page<>(pageNo, pageSize);
        answerCheckPage.setTotal(answerCheckIPage.getTotal());
        answerCheckPage.setRecords(answerCheckVoList);
        return answerCheckPage;
    }

    @Override
    public Result answer(String cookie, AnswerCheckAddParam answerCheckAddParam) {
        //解析token
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
        if (StringUtils.isNotBlank(creater_deptid)) {
            tbDep = tbDepService.getById(creater_deptid);
        }
        return getResult(answerCheckAddParam, creater, creater_name, tbDep.getId(), tbDep.getDepname(),AnswerCheckConstant.SOURCE_PC);
    }


    @Override
    public Result answerByMiniApp(AnswerCheckMiniAppParam answerMiniAppParam) {
        String userId = answerMiniAppParam.getUserId();
        TbUser tbUser = tbUserService.getById(userId);
        if (StringUtils.isBlank(userId) || tbUser == null) {
            return ResultFactory.error("userId参数错误");
        }
        AnswerCheckAddParam answerCheckAddParam = new AnswerCheckAddParam();
        BeanUtils.copyProperties(answerMiniAppParam, answerCheckAddParam);
        if (answerMiniAppParam.getId() != null && NumberUtil.isNumber(answerMiniAppParam.getId())) {
            answerCheckAddParam.setId(Integer.parseInt(answerMiniAppParam.getId()));
        }
        String deptId = answerMiniAppParam.getDeptId();
        TbDep tbDep;
        if (StringUtils.isNotBlank(deptId)) {
            tbDep = tbDepService.getById(deptId);
        } else {
            tbDep = tbDepService.getById(tbUser.getDepid());
        }

        return getResult(answerCheckAddParam, tbUser.getId(), tbUser.getUsername(), tbDep.getId(), tbDep.getDepname(),AnswerCheckConstant.SOURCE_MINIAPP);
    }

    private Result getResult(AnswerCheckAddParam answerCheckAddParam, String creater, String creater_name, String creater_deptid, String creater_deptname,Integer source) {
        AnswerCheck answerCheck = this.getById(answerCheckAddParam.getId());
        if (answerCheck == null) {
            answerCheck = new AnswerCheck();
        } else {
            if (answerCheck.getAnswerStatus().equals(1)) {
                return ResultFactory.error("该记录已提交,无法更改。");
            }
        }
        Integer quId = answerCheckAddParam.getQuId();
        Question question = questionMapper.selectById(quId);
        if (question == null || QuestionConstant.DEL_DELETED.equals(question.getDel())) {
            return ResultFactory.error("问卷不存在,无法保存。");
        }

        answerCheck.setAnswerJson(JSON.toJSONString(answerCheckAddParam.getAnswers()));
        Integer status = answerCheckAddParam.getStatus();
        answerCheck.setAnswerStatus(status);
        Date date = new Date();
        if (status.equals(AnswerCheckConstant.ANSWER_STATUS_RELEASE)) {
            answerCheck.setSubmitTime(date);
        }
        answerCheck.setCreater(creater);
        answerCheck.setCreaterName(creater_name);
        answerCheck.setCreaterDeptId(creater_deptid);
        answerCheck.setCreaterDeptName(creater_deptname);
        answerCheck.setAnswerTime(date);
        answerCheck.setUpdateTime(date);
        answerCheck.setSource(source);

        Answers[] answers = answerCheckAddParam.getAnswers();
        Map<String, String> mapCache = new HashMap<>();
        for (Answers a : answers) {
            mapCache.put(a.getSubColumnName(), a.getSubValue());
        }

        if (mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECK_TIME)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECK_TIME) != null) {
            String dateString = mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECK_TIME);
            answerCheck.setCheckMonth(dateString);
        }

        if (mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECKED_DEPT)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_DEPT) != null) {
            answerCheck.setCheckedDept(mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_DEPT));
        }

        if (mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECKED_DOCT)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_DOCT) != null) {
            answerCheck.setCheckedDoct(mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_DOCT));
        }

        if (mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECKED_PATIENT_ID)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_PATIENT_ID) != null) {
            answerCheck.setCheckedPatientId(mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_PATIENT_ID));
        }

        if (mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CASE_ID)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CASE_ID) != null) {
            answerCheck.setCaseId(mapCache.get(AnswerCheckConstant.COLUMN_NAME_CASE_ID));
        }

        if (mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECKED_PATIENT_NAME)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_PATIENT_NAME) != null) {
            answerCheck.setCheckedPatientId(mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_PATIENT_NAME));
        }

        if (mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECKED_TOTAL_SCORE)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_TOTAL_SCORE) != null) {
            answerCheck.setTotalScore(mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_TOTAL_SCORE));
        }

        if (mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECKED_PASS_STATUS)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_PASS_STATUS) != null) {
            String s = mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_PASS_STATUS);
            answerCheck.setPassStatus(s);
        }

        boolean insertOrUpdate = answerCheck.getId() != null && answerCheck.getId() != 0;
        if (insertOrUpdate) {
            baseMapper.updateById(answerCheck);
        } else {
            //插入总表
            answerCheck.setQuId(quId);
            answerCheck.setQuestionVersion(question.getQuestionVersion());
            answerCheck.setCreateTime(date);
            answerCheck.setDel(AnswerCheckConstant.DEL_NORMAL);

            String summaryMappingTableId = UUIDGenerator.generateRandomUUIDAndCurrentTimeMillis();
            answerCheck.setSummaryMappingTableId(summaryMappingTableId);
            baseMapper.insert(answerCheck);
        }
        //插入子表
        StringBuffer sqlAns = new StringBuffer();
        String questionVersionString = answerCheck.getQuestionVersion();
        List<SubjectVo> subjectList =Lists.newArrayList();
        if(StringUtils.isBlank(questionVersionString) || questionVersionString.equals(question.getQuestionVersion())){
            subjectList = subjectService.selectSubjectAndOptionByQuId(quId);
        }else{
            //查历史问卷题目
            QuestionVersion questionVersion = questionVersionService.selectByQuestionAndVersion(quId, questionVersionString);
            if(Objects.nonNull(questionVersion)){
                String questionVersionId = questionVersion.getId();
                List<QsubjectVersion> subjectVersionList = qsubjectVersionService.selectSubjectVersionByQuIdAndVersion(quId, questionVersionId);
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
                    subjectList.add(subjectVo);
                }
            }
        }

        if (insertOrUpdate) {
            sqlAns.append("update `" + question.getTableName() + "` set ");
            for (int i = 0; i < subjectList.size(); i++) {
                SubjectVo qsubjectDynamicTable = subjectList.get(i);
                String subType = qsubjectDynamicTable.getSubType();
                Integer del = qsubjectDynamicTable.getDel();
                if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                        || QuestionConstant.DEL_DELETED.equals(del) || mapCache.get(qsubjectDynamicTable.getColumnName()) == null
                    /*|| StringUtils.isBlank(mapCache.get(qsubjectDynamicTable.getColumnName()))*/) {
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

                if (QsubjectConstant.MARK_OPEN.equals(qsubjectDynamicTable.getMark())) {
                    String columnNameMark = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark");
                    if (StringUtils.isNotBlank(columnNameMark)) {
                        sqlAns.append("`");
                        sqlAns.append(qsubjectDynamicTable.getColumnName());
                        sqlAns.append("_mark");
                        sqlAns.append("`");
                        sqlAns.append("=");
                        sqlAns.append("'");
                        sqlAns.append(columnNameMark);
                        sqlAns.append("'");
                        sqlAns.append(",");
                    }
                    String columnNameMarkImg = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark_img");
                    if (StringUtils.isNotBlank(columnNameMarkImg)) {
                        sqlAns.append("`");
                        sqlAns.append(qsubjectDynamicTable.getColumnName());
                        sqlAns.append("_mark_img");
                        sqlAns.append("`");
                        sqlAns.append("=");
                        sqlAns.append("'");
                        sqlAns.append(columnNameMarkImg);
                        sqlAns.append("'");
                        sqlAns.append(",");
                    }

                }

            }
            sqlAns.append("`tbrid`='");
            sqlAns.append(creater);
            sqlAns.append("',");
            sqlAns.append("`tbrxm`='");
            sqlAns.append(creater_name);
            sqlAns.append("',");
            sqlAns.append("`tbksmc`='");
            sqlAns.append(creater_deptname);
            sqlAns.append("',");
            sqlAns.append("`tbksdm`='");
            sqlAns.append(creater_deptid);
            sqlAns.append("',");
            sqlAns.append("`table_answer_status`='");
            sqlAns.append(status);
            sqlAns.append("'");
//                    sqlAns.delete(sqlAns.length()-1,sqlAns.length());
            sqlAns.append(" where summary_mapping_table_id = '");
            sqlAns.append(answerCheck.getSummaryMappingTableId());
            sqlAns.append("'");
            log.info("answerCheck-----update sqlAns:{}", sqlAns.toString());
            dynamicTableMapper.updateDynamicTable(sqlAns.toString());
        } else {
            sqlAns.append("insert into `" + question.getTableName() + "` (");
            for (int i = 0; i < subjectList.size(); i++) {
                SubjectVo qsubjectDynamicTable = subjectList.get(i);
                String subType = qsubjectDynamicTable.getSubType();
                Integer del = qsubjectDynamicTable.getDel();
                if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                        || QuestionConstant.DEL_DELETED.equals(del) || mapCache.get(qsubjectDynamicTable.getColumnName()) == null
                        || StringUtils.isBlank(mapCache.get(qsubjectDynamicTable.getColumnName()))) {
                    continue;
                }

                sqlAns.append("`");
                sqlAns.append(qsubjectDynamicTable.getColumnName());
                sqlAns.append("`");
                sqlAns.append(",");

                if (QsubjectConstant.MARK_OPEN.equals(qsubjectDynamicTable.getMark())) {
                    String columnNameMark = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark");
                    if (StringUtils.isNotBlank(columnNameMark)) {
                        sqlAns.append("`");
                        sqlAns.append(qsubjectDynamicTable.getColumnName());
                        sqlAns.append("_mark");
                        sqlAns.append("`");
                        sqlAns.append(",");
                    }

                    String columnNameMarkImg = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark_img");
                    if (StringUtils.isNotBlank(columnNameMarkImg)) {
                        sqlAns.append("`");
                        sqlAns.append(qsubjectDynamicTable.getColumnName());
                        sqlAns.append("_mark_img");
                        sqlAns.append("`");
                        sqlAns.append(",");
                    }

                }
            }
            sqlAns.append("`tbrid`,");
            sqlAns.append("`tbrxm`,");
            sqlAns.append("`tbksmc`,");
            sqlAns.append("`tbksdm`,");
            sqlAns.append("`table_answer_status`,");
            sqlAns.append("`summary_mapping_table_id`");
//                sqlAns.delete(sqlAns.length()-1,sqlAns.length());
            sqlAns.append(") values (");
            for (int i = 0; i < subjectList.size(); i++) {
                SubjectVo qsubjectDynamicTable = subjectList.get(i);
                String subType = qsubjectDynamicTable.getSubType();
                Integer del = qsubjectDynamicTable.getDel();
                if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                        || QuestionConstant.DEL_DELETED.equals(del) || mapCache.get(qsubjectDynamicTable.getColumnName()) == null
                        || StringUtils.isBlank(mapCache.get(qsubjectDynamicTable.getColumnName()))) {
                    continue;
                }
                sqlAns.append("'");
                sqlAns.append(mapCache.get(qsubjectDynamicTable.getColumnName()));
                sqlAns.append("',");


                if (QsubjectConstant.MARK_OPEN.equals(qsubjectDynamicTable.getMark())) {
                    String columnNameMark = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark");
                    if (StringUtils.isNotBlank(columnNameMark)) {
                        sqlAns.append("'");
                        sqlAns.append(columnNameMark);
                        sqlAns.append("',");
                    }

                    String columnNameMarkImg = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark_img");
                    if (StringUtils.isNotBlank(columnNameMarkImg)) {
                        sqlAns.append("'");
                        sqlAns.append(columnNameMarkImg);
                        sqlAns.append("',");
                    }
                }
            }
            sqlAns.append("'");
            sqlAns.append(creater);
            sqlAns.append("',");

            sqlAns.append("'");
            sqlAns.append(creater_name);
            sqlAns.append("',");

            sqlAns.append("'");
            sqlAns.append(creater_deptname);
            sqlAns.append("',");

            sqlAns.append("'");
            sqlAns.append(creater_deptid);
            sqlAns.append("',");

            sqlAns.append("'");
            sqlAns.append(status);
            sqlAns.append("',");

            sqlAns.append("'");
            sqlAns.append(answerCheck.getSummaryMappingTableId());
            sqlAns.append("'");
//                sqlAns.delete(sqlAns.length()-1,sqlAns.length());

            sqlAns.append(")");
            log.info("answerCheck-----insert sqlAns:{}", sqlAns.toString());
            dynamicTableMapper.insertDynamicTable(sqlAns.toString());
        }

        if (status.equals(AnswerCheckConstant.ANSWER_STATUS_RELEASE)) {
            //保存统计明细
            AnswerCheckStatisticDetailEventDto dto = new AnswerCheckStatisticDetailEventDto();
            dto.setQuestion(question);
            dto.setSubjectList(subjectList);
            dto.setMapCache(mapCache);
            dto.setAnswerCheck(answerCheck);
            dto.setAnswerUser(creater);
            dto.setAnswerUserName(creater_name);
            dto.setDepId(creater_deptid);
            dto.setDepName(creater_deptname);
            dto.setSource(source);
            AnswerCheckStatisticDetailEvent questionVersionEvent = new AnswerCheckStatisticDetailEvent(this, dto);
            applicationEventPublisher.publishEvent(questionVersionEvent);
        }

        AnswerCheckIdVo vo = new AnswerCheckIdVo();
        vo.setAnswerCheckId(answerCheck.getId());
        return ResultFactory.success(vo);
    }


    @Override
    public AnswerCheck queryById(String id) {
        AnswerCheck answerCheck = this.getById(id);
        if(answerCheck==null || AnswerCheckConstant.DEL_DELETED.equals(answerCheck.getDel())){
            return null;
        }

        String answerJson = (String) answerCheck.getAnswerJson();
        List<SingleDiseaseAnswer> singleDiseaseAnswerList = JSON.parseArray(answerJson, SingleDiseaseAnswer.class);
        Map<String, SingleDiseaseAnswer> mapCache = new HashMap<>();
        if (singleDiseaseAnswerList != null && !singleDiseaseAnswerList.isEmpty()) {
            for (SingleDiseaseAnswer a : singleDiseaseAnswerList) {
                mapCache.put(a.getSubColumnName(), a);
            }
        }
        Question question = questionMapper.selectById(answerCheck.getQuId());

        StringBuffer sqlAns = new StringBuffer();
        if (question != null) {
            sqlAns.append("select * from `");
            sqlAns.append(question.getTableName());
            sqlAns.append("` where summary_mapping_table_id ='");
            sqlAns.append(answerCheck.getSummaryMappingTableId());
            sqlAns.append("'");
            Map<String, String> map = dynamicTableMapper.selectDynamicTableColumn(sqlAns.toString());
//            String s = "select * from q_single_disease_take where id =20 ";
//            Map<String, String> map = dynamicTableMapper.selectDynamicTableColumn(s);
            if (map == null) {
                return answerCheck;
            }
            for (Map.Entry<String, String> entry : map.entrySet()) {
                QueryWrapper<Qsubject> wrapper = new QueryWrapper<Qsubject>();
                if ("id".equals(entry.getKey())) {
                    continue;
                }
                wrapper.eq("column_name", entry.getKey());
                wrapper.eq("qu_id", question.getId());
                wrapper.eq("del", "0");
                Qsubject qsubject = qsubjectMapper.selectOne(wrapper);
                if (qsubject == null) {
                    continue;
                }
                SingleDiseaseAnswer singleDiseaseAnswer = new SingleDiseaseAnswer();
                singleDiseaseAnswer.setSubColumnName(qsubject.getColumnName());
                singleDiseaseAnswer.setSubValue(String.valueOf(entry.getValue()));
                mapCache.put(qsubject.getColumnName(), singleDiseaseAnswer);
            }
            List<SingleDiseaseAnswer> resList = new ArrayList<>(mapCache.values());
            answerCheck.setAnswerJson(JSON.toJSONString(resList));
        }

        return answerCheck;
    }

    @Override
    public AnswerCheckDetailListVo detailList(AnswerCheckDetailListParam answerCheckDetailListParam, Data data, Integer pageNo, Integer pageSize) {
        String depId = data.getTbUser().getDepId();
        //查询显示列
        Integer quId = answerCheckDetailListParam.getQuId();
        List<CheckDetailSetVo> checkDetailSet = checkDetailSetService.queryByQuestionId(quId, data.getTbUser().getId());
        //查询题目
        List<SubjectVo> subjectList = subjectService.selectSubjectAndOptionByQuId(quId);
        Map<Integer, SubjectVo> subjectMap = subjectList.stream().collect(Collectors.toMap(SubjectVo::getId, q -> q));
        //表头
        List<LinkedHashMap<String, Object>> fieldItems = Lists.newArrayList();
        LinkedHashMap<String, Object> fieldItemCheckDept = Maps.newLinkedHashMap();
        fieldItems.add(fieldItemCheckDept);
        fieldItemCheckDept.put("fieldTxt", "检查科室");
        fieldItemCheckDept.put("fieldId", "checkDept");
        setItems(checkDetailSet, subjectMap, fieldItems);
        //数据
        Page<AnswerCheck> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
        lambda.eq(AnswerCheck::getQuId, answerCheckDetailListParam.getQuId());
        //科室权限
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
        questionLambda.eq(Question::getId, answerCheckDetailListParam.getQuId());
        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        questionLambda.like(Question::getDeptIds, depId);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        List<Question> questionList = questionMapper.selectList(questionLambda);
        if (questionList.isEmpty()) {
            lambda.and(w -> w.eq(AnswerCheck::getCreaterDeptId, depId).or().eq(AnswerCheck::getCheckedDept, depId));
        }
        lambda.eq(AnswerCheck::getDel, AnswerCheckConstant.DEL_NORMAL);
        String checkMonth = answerCheckDetailListParam.getCheckMonth();
        if (StringUtils.isNotBlank(checkMonth)) {
            lambda.eq(AnswerCheck::getCheckMonth, checkMonth);
        }
        lambda.orderByDesc(AnswerCheck::getAnswerTime);
        IPage<AnswerCheck> answerCheckIPage = this.page(page, lambda);
        List<AnswerCheck> answerCheckList = answerCheckIPage.getRecords();
        List<LinkedHashMap<String, Object>> detailDataList = Lists.newArrayList();
        if (answerCheckList.isEmpty()) {
//            return ResultFactory.success(AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).total(0).build());
            return AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).total(0).build();
        }
        List<String> summaryMappingTableIdList = answerCheckList.stream().map(AnswerCheck::getSummaryMappingTableId).collect(Collectors.toList());
        Map<String, AnswerCheck> answerCheckMap = answerCheckList.stream().collect(Collectors.toMap(AnswerCheck::getSummaryMappingTableId, a -> a));
        //查子表
        Question question = questionMapper.selectById(quId);
        StringBuffer sqlSelect = new StringBuffer();
        sqlSelect.append("select * from `");
        sqlSelect.append(question.getTableName());
        sqlSelect.append("`");
        sqlSelect.append(" where summary_mapping_table_id in (");
        for (String summaryMappingTableId : summaryMappingTableIdList) {
            sqlSelect.append("'");
            sqlSelect.append(summaryMappingTableId);
            sqlSelect.append("'");
            sqlSelect.append(",");
        }
        sqlSelect.delete(sqlSelect.length() - 1, sqlSelect.length());
        sqlSelect.append(")");
        List<Map<String, String>> dataList = dynamicTableMapper.selectDynamicTableColumnList(sqlSelect.toString());
//        Map<String, Map<String,String>> dataMap = dataList.stream().collect(Collectors.toMap(m-> m.get("summary_mapping_table_id"), m -> m));
        for (Map<String, String> dataItemMap : dataList) {
            LinkedHashMap<String, Object> stringObjectLinkedHashMap = setList(checkDetailSet, subjectMap, dataItemMap, answerCheckMap);
            detailDataList.add(stringObjectLinkedHashMap);
        }
        AnswerCheckDetailListVo build = AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).total(answerCheckIPage.getTotal()).build();
//        return ResultFactory.success(build);
        return build;
    }

    private void setItems(List<CheckDetailSetVo> checkDetailSet, Map<Integer, SubjectVo> subjectMap, List<LinkedHashMap<String, Object>> fieldItems) {
        ArrayList<LinkedHashMap<String, Object>> emptyList = Lists.newArrayList();
        for (int i = 0; i < checkDetailSet.size(); i++) {
            CheckDetailSetVo checkDetailSetVo = checkDetailSet.get(i);
            if (!CheckDetailSetConstant.SHOW_TYPE_YES.equals(checkDetailSetVo.getShowType())) {
                continue;
            }

            Integer subjectId = checkDetailSetVo.getSubjectId();
            SubjectVo qsubject = subjectMap.get(subjectId);
            if (qsubject == null || QsubjectConstant.SUB_TYPE_TITLE.equals(qsubject.getSubType())) {
                continue;
            }

            LinkedHashMap<String, Object> fieldItem = Maps.newLinkedHashMap();

            fieldItems.add(fieldItem);
            fieldItem.put("fieldTxt", qsubject.getSubName());
            fieldItem.put("fieldId", qsubject.getColumnName());
            fieldItem.put("fieldSubType", qsubject.getSubType());
            if (checkDetailSetVo.getChildList() != null && !checkDetailSetVo.getChildList().isEmpty()) {
                List<LinkedHashMap<String, Object>> fieldItemsFor = Lists.newArrayList();
                fieldItem.put("fieldChildList", fieldItemsFor);
                setItems(checkDetailSetVo.getChildList(), subjectMap, fieldItemsFor);
            } else {
                fieldItem.put("fieldChildList", emptyList);
            }
        }
    }

    private LinkedHashMap<String, Object> setList(List<CheckDetailSetVo> checkDetailSet, Map<Integer, SubjectVo> subjectMap, Map<String, String> dataItemMap,
                                                  Map<String, AnswerCheck> answerCheckMap) {
        LinkedHashMap<String, Object> valueItem = Maps.newLinkedHashMap();
        AnswerCheck answerCheck = answerCheckMap.get(dataItemMap.get("summary_mapping_table_id"));

        //检查科室填充名称
        String tbksdm = dataItemMap.get("tbksdm");
        if (StringUtils.isNotBlank(tbksdm)) {
            TbDep byId = tbDepService.getById(tbksdm);
            valueItem.put("checkDept", byId.getDepname());
        }

        if (answerCheck == null) {
            valueItem.put("detailDataId", null);
        } else {
            Date submitTime = answerCheck.getSubmitTime();
            if(Objects.nonNull(submitTime)){
                valueItem.put("answerTime", new DateTime(submitTime).toString("yyyy-MM-dd HH:mm:ss"));
            }
            valueItem.put("detailDataId", answerCheck.getId());
        }
        for (int i = 0; i < checkDetailSet.size(); i++) {
            CheckDetailSetVo checkDetailSetVo = checkDetailSet.get(i);
            if (!CheckDetailSetConstant.SHOW_TYPE_YES.equals(checkDetailSetVo.getShowType())) {
                continue;
            }
            Integer subjectId = checkDetailSetVo.getSubjectId();
            SubjectVo qsubject = subjectMap.get(subjectId);
            if (qsubject == null || QsubjectConstant.SUB_TYPE_TITLE.equals(qsubject.getSubType())) {
                continue;
            }

            String columnName = qsubject.getColumnName();
            if ("checked_dept".equals(columnName)) {
                valueItem.put(qsubject.getColumnName(), dataItemMap.get(qsubject.getColumnName()));
                //科室填充名称
                String depId = dataItemMap.get(qsubject.getColumnName());
                if (StringUtils.isNotBlank(depId)) {
                    TbDep byId = tbDepService.getById(depId);
                    valueItem.put(qsubject.getColumnName(), byId.getDepname());
                }
            } else if ("tbrid".equals(columnName)) {
                valueItem.put(qsubject.getColumnName(), dataItemMap.get(qsubject.getColumnName()));
                //填报人
                String userId = dataItemMap.get(qsubject.getColumnName());
                if (StringUtils.isNotBlank(userId)) {
                    TbUser byId = tbUserService.getById(userId);
                    if(Objects.nonNull(byId)){
                        valueItem.put(qsubject.getColumnName(), byId.getUsername());
                    }
                }
            } else if (StringUtils.isNotBlank(columnName)) {
                valueItem.put(qsubject.getColumnName(), dataItemMap.get(qsubject.getColumnName()));
                String value = dataItemMap.get(qsubject.getColumnName());
                if (StringUtils.isNotBlank(value)) {
                    String subType = qsubject.getSubType();
                    if (QsubjectConstant.SUB_TYPE_CHOICE.equals(subType)
                            || QsubjectConstant.SUB_TYPE_SINGLE_CHOICE_BOX.equals(subType)
                            || QsubjectConstant.SUB_TYPE_CHOICE_SCORE.equals(subType)
                            || QsubjectConstant.SUB_TYPE_SINGLE_CHOICE_BOX_SCORE.equals(subType)) {
                        List<Qoption> optionList = qsubject.getOptionList();
                        Map<String, Qoption> optionMap = optionList.stream().collect(Collectors.toMap(Qoption::getOpValue, Function.identity(), (oldData, newData) -> newData));
                        Qoption qoption = optionMap.get(value);
                        if (qoption != null) {
                            valueItem.put(qsubject.getColumnName(), qoption.getOpName());
                        }
                    } else if (QsubjectConstant.SUB_TYPE_HOSPITAL_USER.equals(subType)) {
                        String s = dataItemMap.get(qsubject.getColumnName());
                        if (StringUtils.isNotBlank(s)) {
                            TbUser tbUser = tbUserService.getById(s);
                            if (tbUser != null) {
                                valueItem.put(qsubject.getColumnName(), tbUser.getUsername());
                            }
                        }
                    } else if (QsubjectConstant.SUB_TYPE_RESULT_EVALUATE.equals(subType)) {
                        List<Qoption> optionList = qsubject.getOptionList();
                        Map<String, Qoption> optionMap = optionList.stream().collect(Collectors.toMap(Qoption::getAnswerValue, Function.identity(), (oldData, newData) -> newData));
                        Qoption qoption = optionMap.get(value);
                        if (qoption != null) {
                            valueItem.put(qsubject.getColumnName(), qoption.getAnswerName());
                        }
                    } else if (QsubjectConstant.SUB_TYPE_MULTIPLE_CHOICE.equals(subType)) {
                        List<Qoption> optionList = qsubject.getOptionList();
                        Map<String, List<Qoption>> optionMap = optionList.stream().collect(Collectors.toMap(Qoption::getOpValue, Lists::newArrayList,
                                (List<Qoption> n1, List<Qoption> n2) -> {
                                    n1.addAll(n2);
                                    return n1;
                                }));
                        if (value.contains("$")) {
                            List<String> valueList = Lists.newArrayList(value.split("\\$"));
                            StringBuffer collect = new StringBuffer();
                            for (String o : valueList) {
                                collect.append(optionMap.get(o).stream().map(Qoption::getOpName).collect(Collectors.joining(",")));
                                collect.append(",");
                            }
                            collect.delete(collect.length() - 1, collect.length());
                            valueItem.put(qsubject.getColumnName(), collect);
                        } else {
                            String collect = optionMap.get(value).stream().map(Qoption::getOpName).collect(Collectors.joining(","));
                            valueItem.put(qsubject.getColumnName(), collect);
                        }
                    }

                }
            }
            if (checkDetailSetVo.getChildList() != null && !checkDetailSetVo.getChildList().isEmpty()) {
//                List<LinkedHashMap<String,Object>> valueItemsFor = Lists.newArrayList();
//                valueItem.put("fieldChildList",valueItemsFor);
                LinkedHashMap<String, Object> stringObjectLinkedHashMap = setList(checkDetailSetVo.getChildList(), subjectMap, dataItemMap, answerCheckMap);
                valueItem.putAll(stringObjectLinkedHashMap);
            } else {
//                valueItem.put("fieldChildList",emptyList);
            }
        }
        return valueItem;
    }


    @Override
    public void exportXlsDetailList(AnswerCheckDetailListExportParam answerCheckDetailListExportParam, HttpServletResponse response) {
        String userId = answerCheckDetailListExportParam.getUserId();
        if (StringUtils.isBlank(userId)) {
            return;
        }
        TbUser tbUser = tbUserService.getById(userId);
        String depId = tbUser.getDepid();
        //查询显示列
        Integer quId = answerCheckDetailListExportParam.getQuId();
        List<CheckDetailSetVo> checkDetailSet = checkDetailSetService.queryByQuestionId(quId, tbUser.getId());
        //查询题目
        List<SubjectVo> subjectList = subjectService.selectSubjectAndOptionByQuId(quId);
        Map<Integer, SubjectVo> subjectMap = subjectList.stream().collect(Collectors.toMap(SubjectVo::getId, q -> q));
        //表头
        List<LinkedHashMap<String, Object>> fieldItems = Lists.newArrayList();
        LinkedHashMap<String, Object> fieldItemCheckDept = Maps.newLinkedHashMap();
        fieldItems.add(fieldItemCheckDept);
        fieldItemCheckDept.put("fieldTxt", "检查科室");
        fieldItemCheckDept.put("fieldId", "checkDept");
        setItems(checkDetailSet, subjectMap, fieldItems);
        //数据
        LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
        lambda.eq(AnswerCheck::getQuId, answerCheckDetailListExportParam.getQuId());
        //科室权限
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
        questionLambda.eq(Question::getId, answerCheckDetailListExportParam.getQuId());
        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        questionLambda.like(Question::getDeptIds, depId);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        List<Question> questionList = questionMapper.selectList(questionLambda);
        if (questionList.isEmpty()) {
            lambda.and(w -> w.eq(AnswerCheck::getCreaterDeptId, depId).or().eq(AnswerCheck::getCheckedDept, depId));
        }
        lambda.eq(AnswerCheck::getDel, AnswerCheckConstant.DEL_NORMAL);
        String checkMonth = answerCheckDetailListExportParam.getCheckMonth();
        if (StringUtils.isNotBlank(checkMonth)) {
            lambda.eq(AnswerCheck::getCheckMonth, checkMonth);
        }
        lambda.orderByDesc(AnswerCheck::getAnswerTime);
        List<AnswerCheck> answerCheckList = this.list(lambda);
        List<LinkedHashMap<String, Object>> detailDataList = Lists.newArrayList();
        if (!answerCheckList.isEmpty()) {
            List<String> summaryMappingTableIdList = answerCheckList.stream().map(AnswerCheck::getSummaryMappingTableId).collect(Collectors.toList());
            Map<String, AnswerCheck> answerCheckMap = answerCheckList.stream().collect(Collectors.toMap(AnswerCheck::getSummaryMappingTableId, a -> a));
            //查子表
            Question question = questionMapper.selectById(quId);
            StringBuffer sqlSelect = new StringBuffer();
            sqlSelect.append("select * from `");
            sqlSelect.append(question.getTableName());
            sqlSelect.append("`");
            sqlSelect.append(" where summary_mapping_table_id in (");
            for (String summaryMappingTableId : summaryMappingTableIdList) {
                sqlSelect.append("'");
                sqlSelect.append(summaryMappingTableId);
                sqlSelect.append("'");
                sqlSelect.append(",");
            }
            sqlSelect.delete(sqlSelect.length() - 1, sqlSelect.length());
            sqlSelect.append(")");
            List<Map<String, String>> dataList = dynamicTableMapper.selectDynamicTableColumnList(sqlSelect.toString());
            //        Map<String, Map<String,String>> dataMap = dataList.stream().collect(Collectors.toMap(m-> m.get("summary_mapping_table_id"), m -> m));
            for (Map<String, String> dataItemMap : dataList) {
                LinkedHashMap<String, Object> stringObjectLinkedHashMap = setList(checkDetailSet, subjectMap, dataItemMap, answerCheckMap);
                detailDataList.add(stringObjectLinkedHashMap);
            }
        }
//        fieldItems   detailDataList
        AnswerCheckDetailListVo result = AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).build();
        AnswerCheckeDetailExporter builder = new AnswerCheckeDetailExporter();
        ExcelExportUtil.export(response, builder, result);
    }


    @Override
    public AnswerCheckDetailListVo checkQuestionHistoryStatisticDetailList(CheckQuestionHistoryStatisticDetailListRequest listRequest, Data data, Integer pageNo, Integer pageSize) {
        //查询显示列
        Integer quId = listRequest.getQuId();
        List<CheckDetailSetVo> checkDetailSet = checkDetailSetService.queryByQuestionId(quId, data.getTbUser().getId());
        //查询题目
        List<SubjectVo> subjectList = subjectService.selectSubjectAndOptionByQuId(quId);
        Map<Integer, SubjectVo> subjectMap = subjectList.stream().collect(Collectors.toMap(SubjectVo::getId, q -> q));
        //表头
        List<LinkedHashMap<String, Object>> fieldItems = Lists.newArrayList();
        LinkedHashMap<String, Object> fieldItemCheckDept = Maps.newLinkedHashMap();
        fieldItems.add(fieldItemCheckDept);
        fieldItemCheckDept.put("fieldTxt", "检查科室");
        fieldItemCheckDept.put("fieldId", "checkDept");
        fieldItemCheckDept.put("fieldSubType", 0);
        setItems(checkDetailSet, subjectMap, fieldItems);
        LinkedHashMap<String, Object> fieldItemAnswerTime = Maps.newLinkedHashMap();
        fieldItems.add(fieldItemAnswerTime);
        fieldItemAnswerTime.put("fieldTxt", "检查时间");
        fieldItemAnswerTime.put("fieldId", "answerTime");
        fieldItemAnswerTime.put("fieldSubType", 0);
        //数据
        Page<AnswerCheck> page = new Page<>(pageNo, pageSize);

        LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new QueryWrapper<Question>().lambda();
        questionLambdaQueryWrapper.eq(Question::getId, listRequest.getQuId());
        questionLambdaQueryWrapper.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambdaQueryWrapper.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        questionLambdaQueryWrapper.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        String deptId = listRequest.getDeptId();
        if (StringUtils.isNotBlank(deptId)) {
            questionLambdaQueryWrapper.like(Question::getDeptIds, deptId);
        }
        List<Question> questionList = questionMapper.selectList(questionLambdaQueryWrapper);
        List<Integer> questionSearchIdList = questionList.stream().map(Question::getId).distinct().collect(Collectors.toList());
        List<LinkedHashMap<String, Object>> detailDataList = Lists.newArrayList();
        if (questionSearchIdList.isEmpty()) {
            return AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).total(0).build();
        }
        String checkedDeptId = listRequest.getCheckedDeptId();
        if (StringUtils.isNotBlank(checkedDeptId)) {
            //被检查科室
            List<QuestionCheckedDept> questionCheckedDeptList = questionCheckedDeptService.selectCheckedDeptByQuIdAndDeptId(listRequest.getQuId(), checkedDeptId);
            if (questionCheckedDeptList.isEmpty()) {
                return AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).total(0).build();
            }
        }

        LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
        lambda.eq(AnswerCheck::getQuId, listRequest.getQuId());
        lambda.eq(AnswerCheck::getAnswerStatus, AnswerCheckConstant.ANSWER_STATUS_RELEASE);
        lambda.eq(AnswerCheck::getDel, AnswerCheckConstant.DEL_NORMAL);
        if (listRequest.getStartDate() != null) {
            lambda.ge(AnswerCheck::getAnswerTime, listRequest.getStartDate());
        }

        if (listRequest.getEndDate() != null) {
            Date endDate = new DateTime(listRequest.getEndDate()).plusDays(1).toDate();
            lambda.le(AnswerCheck::getAnswerTime, endDate);
        }

        String checkMonth = listRequest.getCheckMonth();
        if (StringUtils.isNotBlank(checkMonth)) {
            lambda.eq(AnswerCheck::getCheckMonth, checkMonth);
        }

        if (StringUtils.isNotBlank(checkedDeptId)) {
            lambda.eq(AnswerCheck::getCheckedDept, checkedDeptId);
        }

        if (StringUtils.isNotBlank(deptId)) {
            lambda.eq(AnswerCheck::getCreaterDeptId, deptId);
        }

        String selfDeptId = listRequest.getSelfDeptId();
        if (StringUtils.isNotBlank(selfDeptId)) {
            lambda.eq(AnswerCheck::getCheckedDept, selfDeptId);
            lambda.eq(AnswerCheck::getCreaterDeptId, selfDeptId);
        }

        lambda.orderByDesc(AnswerCheck::getAnswerTime);
        IPage<AnswerCheck> answerCheckIPage = this.page(page, lambda);
        List<AnswerCheck> answerCheckList = answerCheckIPage.getRecords();

        if (answerCheckList.isEmpty()) {
//            return ResultFactory.success(AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).total(0).build());
            return AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).total(0).build();
        }
        List<String> summaryMappingTableIdList = answerCheckList.stream().map(AnswerCheck::getSummaryMappingTableId).collect(Collectors.toList());
        Map<String, AnswerCheck> answerCheckMap = answerCheckList.stream().collect(Collectors.toMap(AnswerCheck::getSummaryMappingTableId, a -> a));
        //查子表
        Question question = questionMapper.selectById(quId);
        StringBuffer sqlSelect = new StringBuffer();
        sqlSelect.append("select * from `");
        sqlSelect.append(question.getTableName());
        sqlSelect.append("`");
        sqlSelect.append(" where summary_mapping_table_id in (");
        for (String summaryMappingTableId : summaryMappingTableIdList) {
            sqlSelect.append("'");
            sqlSelect.append(summaryMappingTableId);
            sqlSelect.append("'");
            sqlSelect.append(",");
        }
        sqlSelect.delete(sqlSelect.length() - 1, sqlSelect.length());
        sqlSelect.append(")");
        List<Map<String, String>> dataList = dynamicTableMapper.selectDynamicTableColumnList(sqlSelect.toString());
//        Map<String, Map<String,String>> dataMap = dataList.stream().collect(Collectors.toMap(m-> m.get("summary_mapping_table_id"), m -> m));
        for (Map<String, String> dataItemMap : dataList) {
            LinkedHashMap<String, Object> stringObjectLinkedHashMap = setList(checkDetailSet, subjectMap, dataItemMap, answerCheckMap);
            detailDataList.add(stringObjectLinkedHashMap);
        }
        AnswerCheckDetailListVo build = AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).total(answerCheckIPage.getTotal()).build();
//        return ResultFactory.success(build);
        return build;
    }

    @Override
    public List<CheckQuestionDefectStatisticListVo> checkQuestionDefectStatisticList(CheckQuestionDefectStatisticListParam listParam) {
        List<CheckQuestionDefectStatisticListVo> resList = Lists.newArrayList();

        Integer quId = listParam.getQuId();
        //查询题目
//        List<SubjectVo> subjectList = subjectService.selectSubjectAndOptionByQuId(quId);
//        Map<Integer, SubjectVo> subjectMap = subjectList.stream().collect(Collectors.toMap(SubjectVo::getId, q -> q));
        //数据
        LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new QueryWrapper<Question>().lambda();
        questionLambdaQueryWrapper.eq(Question::getId, listParam.getQuId());
        questionLambdaQueryWrapper.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambdaQueryWrapper.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        questionLambdaQueryWrapper.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        String deptId = listParam.getDeptId();
        if (StringUtils.isNotBlank(deptId)) {
            questionLambdaQueryWrapper.like(Question::getDeptIds, deptId);
        }
        List<Question> questionList = questionMapper.selectList(questionLambdaQueryWrapper);
        List<Integer> questionSearchIdList = questionList.stream().map(Question::getId).distinct().collect(Collectors.toList());
        if (questionSearchIdList.isEmpty()) {
            return resList;
        }
        LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
        lambda.eq(AnswerCheck::getQuId, listParam.getQuId());
        lambda.eq(AnswerCheck::getAnswerStatus, AnswerCheckConstant.ANSWER_STATUS_RELEASE);
        lambda.eq(AnswerCheck::getDel, AnswerCheckConstant.DEL_NORMAL);
        String checkMonth = listParam.getCheckMonth();
        if (StringUtils.isNotBlank(checkMonth)) {
            lambda.eq(AnswerCheck::getCheckMonth, checkMonth);
        }
        String checkedDeptId = listParam.getCheckedDeptId();
        if (StringUtils.isNotBlank(checkedDeptId)) {
            lambda.eq(AnswerCheck::getCheckedDept, checkedDeptId);
        }

        if (StringUtils.isNotBlank(deptId)) {
            lambda.eq(AnswerCheck::getCreaterDeptId, deptId);
        }
        lambda.orderByDesc(AnswerCheck::getAnswerTime);
        List<AnswerCheck> answerCheckList = this.list(lambda);
        if (answerCheckList.isEmpty()) {
            return resList;
        }
        List<String> summaryMappingTableIdList = answerCheckList.stream().map(AnswerCheck::getSummaryMappingTableId).collect(Collectors.toList());
//        Map<String, AnswerCheck> answerCheckMap = answerCheckList.stream().collect(Collectors.toMap(AnswerCheck::getSummaryMappingTableId, a -> a));
        //查子表
        Question question = questionMapper.selectById(quId);
        StringBuffer sqlSelect = new StringBuffer();
        sqlSelect.append("select * from `");
        sqlSelect.append(question.getTableName());
        sqlSelect.append("`");
        sqlSelect.append(" where summary_mapping_table_id in (");
        for (String summaryMappingTableId : summaryMappingTableIdList) {
            sqlSelect.append("'");
            sqlSelect.append(summaryMappingTableId);
            sqlSelect.append("'");
            sqlSelect.append(",");
        }
        sqlSelect.delete(sqlSelect.length() - 1, sqlSelect.length());
        sqlSelect.append(")");
        List<Map<String, String>> dataList = dynamicTableMapper.selectDynamicTableColumnList(sqlSelect.toString());
        List<String> tbksdmIdList = dataList.stream().map(m -> m.get("tbksdm")).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<String> checkedDeptIdList = dataList.stream().map(m -> m.get("checked_dept")).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        tbksdmIdList.addAll(checkedDeptIdList);
        List<TbDep> tbDepList = tbDepService.listByIdList(tbksdmIdList);
        Map<String, TbDep> tbDeptMap = tbDepList.stream().collect(Collectors.toMap(TbDep::getId, t -> t));
        for (Map<String, String> dataItemMap : dataList) {
            CheckQuestionDefectStatisticListVo vo = new CheckQuestionDefectStatisticListVo();
            resList.add(vo);

            vo.setQuId(quId);
            vo.setQuName(question.getQuName());

            //检查科室填充名称
            String tbksdm = dataItemMap.get("tbksdm");
            if (StringUtils.isNotBlank(tbksdm)) {
                TbDep tbDep = tbDeptMap.get(tbksdm);
                vo.setDeptId(tbDep.getId());
                vo.setDeptName(tbDep.getDepname());
            }
            //被检查科室id填充名称
            String checkedDept = dataItemMap.get("checked_dept");
            if (StringUtils.isNotBlank(checkedDept)) {
                TbDep tbDep = tbDeptMap.get(checkedDept);
                vo.setCheckedDept(tbDep.getId());
                vo.setCheckedDeptName(tbDep.getDepname());
            }
            String checkMonthData = dataItemMap.get("check_month");
            if (StringUtils.isNotBlank(checkMonthData)) {
                vo.setCheckMonth(checkMonthData);
            }
            String totalScore = dataItemMap.get("total_score");
            if (StringUtils.isNotBlank(totalScore)) {
                vo.setTotalScore(totalScore);
            }
            String totalFault = dataItemMap.get("total_fault");
            if (StringUtils.isNotBlank(totalFault)) {
                vo.setTotalFault(totalFault);
            }
            String passStatus = dataItemMap.get("pass_status");
            if (StringUtils.isNotBlank(passStatus)) {
                vo.setPassStatus(passStatus);
            }

            //
            Integer defectCount = 0;
            StringBuffer problem = new StringBuffer();
            for (Map.Entry<String, String> entry : dataItemMap.entrySet()) {
                String key = entry.getKey();
                String value = String.valueOf(entry.getValue());
                if(StringUtils.isNotBlank(value) && value.contains("n") && !key.contains("_mark_img") && !key.contains("pass_status")){
                    defectCount++;
                }

                if(StringUtils.isNotBlank(key) && key.contains("_mark") && !key.contains("_mark_img") && !key.contains("pass_status")){
                    problem.append(value);
                    problem.append(";");
                }
            }
            vo.setDefectCount(defectCount);
            vo.setProblem(problem.toString());
        }
        return resList;
    }

    @Override
    public void exportXlsCheckQuestionHistoryStatisticDetailList(CheckQuestionHistoryStatisticDetailListExportRequest exportRequest, HttpServletResponse response) {
        String userId = exportRequest.getUserId();
        if (StringUtils.isBlank(userId)) {
            return;
        }
        TbUser tbUser = tbUserService.getById(userId);
        String depId = tbUser.getDepid();
        //查询显示列
        Integer quId = exportRequest.getQuId();
        List<CheckDetailSetVo> checkDetailSet = checkDetailSetService.queryByQuestionId(quId, tbUser.getId());
        //查询题目
        List<SubjectVo> subjectList = subjectService.selectSubjectAndOptionByQuId(quId);
        Map<Integer, SubjectVo> subjectMap = subjectList.stream().collect(Collectors.toMap(SubjectVo::getId, q -> q));
        //表头
        List<LinkedHashMap<String, Object>> fieldItems = Lists.newArrayList();
        LinkedHashMap<String, Object> fieldItemCheckDept = Maps.newLinkedHashMap();
        fieldItems.add(fieldItemCheckDept);
        fieldItemCheckDept.put("fieldTxt", "检查科室");
        fieldItemCheckDept.put("fieldId", "checkDept");
        setItems(checkDetailSet, subjectMap, fieldItems);
        LinkedHashMap<String, Object> fieldItemAnswerTime = Maps.newLinkedHashMap();
        fieldItems.add(fieldItemAnswerTime);
        fieldItemAnswerTime.put("fieldTxt", "检查时间");
        fieldItemAnswerTime.put("fieldId", "answerTime");
        //数据
        LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new QueryWrapper<Question>().lambda();
        questionLambdaQueryWrapper.eq(Question::getId, exportRequest.getQuId());
        questionLambdaQueryWrapper.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambdaQueryWrapper.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        questionLambdaQueryWrapper.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        String deptId = exportRequest.getDeptId();
        if (StringUtils.isNotBlank(deptId)) {
            questionLambdaQueryWrapper.like(Question::getDeptIds, deptId);
        }
        List<Question> questionList = questionMapper.selectList(questionLambdaQueryWrapper);
        List<Integer> questionSearchIdList = questionList.stream().map(Question::getId).distinct().collect(Collectors.toList());
        List<LinkedHashMap<String, Object>> detailDataList = Lists.newArrayList();
        if (questionSearchIdList.isEmpty()) {
            AnswerCheckDetailListVo result = AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).build();
            AnswerCheckeDetailExporter builder = new AnswerCheckeDetailExporter();
            ExcelExportUtil.export(response, builder, result);
            return;
        }
        String checkedDeptId = exportRequest.getCheckedDeptId();
        if (StringUtils.isNotBlank(checkedDeptId)) {
            //被检查科室
            List<QuestionCheckedDept> questionCheckedDeptList = questionCheckedDeptService.selectCheckedDeptByQuIdAndDeptId(exportRequest.getQuId(), checkedDeptId);
            if (questionCheckedDeptList.isEmpty()) {
                AnswerCheckDetailListVo result = AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).build();
                AnswerCheckeDetailExporter builder = new AnswerCheckeDetailExporter();
                ExcelExportUtil.export(response, builder, result);
                return;
            }
        }

        LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
        lambda.eq(AnswerCheck::getQuId, exportRequest.getQuId());
        lambda.eq(AnswerCheck::getAnswerStatus, AnswerCheckConstant.ANSWER_STATUS_RELEASE);
        lambda.eq(AnswerCheck::getDel, AnswerCheckConstant.DEL_NORMAL);
        if (exportRequest.getStartDate() != null) {
            lambda.ge(AnswerCheck::getAnswerTime, exportRequest.getStartDate());
        }

        if (exportRequest.getEndDate() != null) {
            Date endDate = new DateTime(exportRequest.getEndDate()).plusDays(1).toDate();
            lambda.le(AnswerCheck::getAnswerTime, endDate);
        }

        if (StringUtils.isNotBlank(checkedDeptId)) {
            lambda.eq(AnswerCheck::getCheckedDept, checkedDeptId);
        }

        if (StringUtils.isNotBlank(deptId)) {
            lambda.eq(AnswerCheck::getCreaterDeptId, deptId);
        }

        String selfDeptId = exportRequest.getSelfDeptId();
        if (StringUtils.isNotBlank(selfDeptId)) {
            lambda.eq(AnswerCheck::getCheckedDept, selfDeptId);
            lambda.eq(AnswerCheck::getCreaterDeptId, selfDeptId);
        }

        String checkMonth = exportRequest.getCheckMonth();
        if (StringUtils.isNotBlank(checkMonth)) {
            lambda.eq(AnswerCheck::getCheckMonth, checkMonth);
        }

        lambda.orderByDesc(AnswerCheck::getAnswerTime);
        List<AnswerCheck> answerCheckList = this.list(lambda);
        if (!answerCheckList.isEmpty()) {
            List<String> summaryMappingTableIdList = answerCheckList.stream().map(AnswerCheck::getSummaryMappingTableId).collect(Collectors.toList());
            Map<String, AnswerCheck> answerCheckMap = answerCheckList.stream().collect(Collectors.toMap(AnswerCheck::getSummaryMappingTableId, a -> a));
            //查子表
            Question question = questionMapper.selectById(quId);
            StringBuffer sqlSelect = new StringBuffer();
            sqlSelect.append("select * from `");
            sqlSelect.append(question.getTableName());
            sqlSelect.append("`");
            sqlSelect.append(" where summary_mapping_table_id in (");
            for (String summaryMappingTableId : summaryMappingTableIdList) {
                sqlSelect.append("'");
                sqlSelect.append(summaryMappingTableId);
                sqlSelect.append("'");
                sqlSelect.append(",");
            }
            sqlSelect.delete(sqlSelect.length() - 1, sqlSelect.length());
            sqlSelect.append(")");
            List<Map<String, String>> dataList = dynamicTableMapper.selectDynamicTableColumnList(sqlSelect.toString());
//          Map<String, Map<String,String>> dataMap = dataList.stream().collect(Collectors.toMap(m-> m.get("summary_mapping_table_id"), m -> m));
            for (Map<String, String> dataItemMap : dataList) {
                LinkedHashMap<String, Object> stringObjectLinkedHashMap = setList(checkDetailSet, subjectMap, dataItemMap, answerCheckMap);
                detailDataList.add(stringObjectLinkedHashMap);
            }
//          fieldItems   detailDataList
            AnswerCheckDetailListVo result = AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).build();
            AnswerCheckeDetailExporter builder = new AnswerCheckeDetailExporter();
            ExcelExportUtil.export(response, builder, result);
        }
    }

    @Override
    public ResultBetter checkQuestionRecordDelete(AnswerCheckDeleteParam param, String userId) {
        Integer id = param.getId();
        AnswerCheck byId = this.getById(id);
        if(byId==null || AnswerCheckConstant.DEL_DELETED.equals(byId.getDel())){
            return ResultBetterFactory.fail("记录错误");
        }
        if(byId.getCreater().equals(userId)){
            byId.setDel(AnswerCheckConstant.DEL_DELETED);
            byId.setUpdateTime(new Date());
            Question question = questionMapper.selectById(byId.getQuId());
            if(question==null || QuestionConstant.DEL_DELETED.equals(question.getDel())){
                return ResultBetterFactory.fail("记录的问卷不存在");
            }
            StringBuffer sqlAns = new StringBuffer();
            sqlAns.append("update `" + question.getTableName() + "` set ");
            sqlAns.append("`del`='1' where summary_mapping_table_id = '");
            sqlAns.append(byId.getSummaryMappingTableId());
            sqlAns.append("'");
            log.info("-----del--update sqlAns:{}", sqlAns.toString());
            dynamicTableMapper.updateDynamicTable(sqlAns.toString());

            this.updateById(byId);

            //删除查检表的统计明细表
            LambdaUpdateWrapper<AnswerCheckStatisticDetail> answerCheckStatisticDetailLambdaUpdateWrapper = new UpdateWrapper<AnswerCheckStatisticDetail>().lambda();
            answerCheckStatisticDetailLambdaUpdateWrapper
                    .eq(AnswerCheckStatisticDetail::getAnswerCheckId,id)
                    .set(AnswerCheckStatisticDetail::getDel, AnswerCheckConstant.DEL_DELETED);
            AnswerCheckStatisticDetail a = new AnswerCheckStatisticDetail();
            answerCheckStatisticDetailService.update(a,answerCheckStatisticDetailLambdaUpdateWrapper);

            return ResultBetterFactory.success();
        }

        return ResultBetterFactory.fail("不是记录创建人，无法删除");
    }


    @Override
    public List<CheckQuestionCountStatisticVo> checkQuestionCountStatistic(CheckQuestionCountStatisticParam statisticParam) {
        //查询职能科室
        LambdaQueryWrapper<TbDep> lambda = new QueryWrapper<TbDep>().lambda();
        lambda.eq(TbDep::getStatus, TbDepConstant.STATUS_OPEN);
        lambda.eq(TbDep::getIsdelete, TbDepConstant.IS_DELETE_NO);
        lambda.and(a -> a.eq(TbDep::getType, "bb9b8649a3564c149a27cf13c60c8bb4"));
        List<TbDep> depList = tbDepService.list(lambda);
        List<String> collect = depList.stream().map(TbDep::getId).distinct().collect(Collectors.toList());
        statisticParam.setIdList(collect);

        Date endDate = statisticParam.getEndDate();
        if(endDate!=null){
            statisticParam.setEndDate(new DateTime(endDate).plusDays(1).toDate());
        }

        List<CheckQuestionCountStatisticVo> voList = this.baseMapper.checkQuestionCountStatistic(statisticParam);
        if(CollectionUtil.isEmpty(voList)){
            return Lists.newArrayList();
        }


        List<String> deptIdList = voList.stream().map(CheckQuestionCountStatisticVo::getDeptId).distinct().collect(Collectors.toList());
        List<TbDep> deptList = tbDepService.listByIdList(deptIdList);
        Map<String, TbDep> deptListMap = deptList.stream().collect(Collectors.toMap(TbDep::getId, t -> t));


        List<String> userIdList = voList.stream().map(CheckQuestionCountStatisticVo::getUserId).distinct().collect(Collectors.toList());
        List<TbUser> userList = tbUserService.getByIds(userIdList);
        Map<String, TbUser> userListMap = userList.stream().collect(Collectors.toMap(TbUser::getId, t -> t));

        List<Integer> quIdList = voList.stream().map(CheckQuestionCountStatisticVo::getQuId).distinct().collect(Collectors.toList());
        List<Question> quList = questionMapper.selectBatchIds(quIdList);
        Map<Integer, Question> quListMap = quList.stream().collect(Collectors.toMap(Question::getId, t -> t));


        for (CheckQuestionCountStatisticVo checkQuestionCountStatisticVo : voList) {
            String deptId = checkQuestionCountStatisticVo.getDeptId();
            if(StringUtils.isNotBlank(deptId)){
                TbDep tbDep = deptListMap.get(deptId);
                if(Objects.nonNull(tbDep)){
                    checkQuestionCountStatisticVo.setDeptName(tbDep.getDepname());
                }
            }

            String userId = checkQuestionCountStatisticVo.getUserId();
            if(StringUtils.isNotBlank(userId)){
                TbUser tbUser = userListMap.get(userId);
                if(Objects.nonNull(tbUser)){
                    checkQuestionCountStatisticVo.setUserName(tbUser.getUsername());
                }
            }

            Integer quId = checkQuestionCountStatisticVo.getQuId();
            if(Objects.nonNull(quId)){
                Question question = quListMap.get(quId);
                if(Objects.nonNull(question)){
                    checkQuestionCountStatisticVo.setQuName(question.getQuName());
                }
            }

        }
        return voList;
    }

    @Override
    public List<CheckQuestionCountStatisticDeptListVo> checkQuestionCountStatisticDeptList() {
        //查询拥有填报检查表的科室
        LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new QueryWrapper<Question>().lambda();
        questionLambdaQueryWrapper.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambdaQueryWrapper.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        questionLambdaQueryWrapper.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        List<Question> questionList = questionMapper.selectList(questionLambdaQueryWrapper);
        if(CollectionUtil.isEmpty(questionList)){
            return Lists.newArrayList();
        }
        List<String> deptIdList = Lists.newArrayList();
        for (Question question : questionList) {
            String deptIds = question.getDeptIds();
            if(StringUtils.isNotBlank(deptIds)){
                String[] split = deptIds.split(",");
                deptIdList.addAll(Arrays.asList(split));
            }
        }
        if(CollectionUtil.isEmpty(deptIdList)){
            return Lists.newArrayList();
        }

        LambdaQueryWrapper<TbDep> lambda = new QueryWrapper<TbDep>().lambda();
        lambda.in(TbDep::getId, deptIdList);
        lambda.eq(TbDep::getStatus, TbDepConstant.STATUS_OPEN);
        lambda.eq(TbDep::getIsdelete, TbDepConstant.IS_DELETE_NO);
        lambda.and(a -> a.eq(TbDep::getType, "bb9b8649a3564c149a27cf13c60c8bb4"));
        List<TbDep> depList = tbDepService.list(lambda);

        List<CheckQuestionCountStatisticDeptListVo> resList = depList.stream().map(d -> {
            CheckQuestionCountStatisticDeptListVo vo = new CheckQuestionCountStatisticDeptListVo();
            BeanUtils.copyProperties(d, vo);
            vo.setDepName(d.getDepname());
            return vo;
        }).collect(Collectors.toList());
        return resList;
    }




}
