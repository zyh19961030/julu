package com.qu.modules.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.api.vo.ResultFactory;
import org.jeecg.common.util.UUIDGenerator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qu.constant.AnswerCheckConstant;
import com.qu.constant.QsubjectConstant;
import com.qu.constant.QuestionConstant;
import com.qu.modules.web.entity.AnswerCheck;
import com.qu.modules.web.entity.Qoption;
import com.qu.modules.web.entity.Qsubject;
import com.qu.modules.web.entity.Question;
import com.qu.modules.web.entity.TbDep;
import com.qu.modules.web.entity.TbUser;
import com.qu.modules.web.mapper.AnswerCheckMapper;
import com.qu.modules.web.mapper.DynamicTableMapper;
import com.qu.modules.web.mapper.QsubjectMapper;
import com.qu.modules.web.mapper.QuestionMapper;
import com.qu.modules.web.param.AnswerCheckAddParam;
import com.qu.modules.web.param.AnswerCheckDetailListParam;
import com.qu.modules.web.param.AnswerCheckListParam;
import com.qu.modules.web.param.AnswerMiniAppParam;
import com.qu.modules.web.param.Answers;
import com.qu.modules.web.param.SingleDiseaseAnswer;
import com.qu.modules.web.pojo.Data;
import com.qu.modules.web.pojo.JsonRootBean;
import com.qu.modules.web.service.IAnswerCheckService;
import com.qu.modules.web.service.ICheckDetailSetService;
import com.qu.modules.web.service.ISubjectService;
import com.qu.modules.web.service.ITbDepService;
import com.qu.modules.web.service.ITbUserService;
import com.qu.modules.web.vo.AnswerCheckDetailListVo;
import com.qu.modules.web.vo.AnswerCheckVo;
import com.qu.modules.web.vo.CheckDetailSetVo;
import com.qu.modules.web.vo.SubjectVo;
import com.qu.util.HttpClient;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 检查表问卷总表
 * @Author: jeecg-boot
 * @Date:   2022-07-30
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


    @Override
    public IPage<AnswerCheckVo> checkQuestionFillInList(AnswerCheckListParam answerCheckListParam, Integer pageNo, Integer pageSize, Integer answerStatus) {
        Page<AnswerCheck> page = new Page<>(pageNo, pageSize);

        LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new QueryWrapper<Question>().lambda();
        questionLambdaQueryWrapper.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        questionLambdaQueryWrapper.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        questionLambdaQueryWrapper.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        if(answerCheckListParam !=null && StringUtils.isNotBlank(answerCheckListParam.getQuName())){
            questionLambdaQueryWrapper.like(Question::getQuName, answerCheckListParam.getQuName());
        }
        List<Question> questionList = questionMapper.selectList(questionLambdaQueryWrapper);
        List<Integer> questionSearchIdList = questionList.stream().map(Question::getId).distinct().collect(Collectors.toList());
        if(questionSearchIdList.isEmpty()){
            return new Page<>();
        }

        LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
        if(!questionSearchIdList.isEmpty()){
            lambda.in(AnswerCheck::getQuId,questionSearchIdList);
        }
        if(answerStatus!=null){
            lambda.eq(AnswerCheck::getAnswerStatus,answerStatus);
        }
        if(answerCheckListParam !=null && answerCheckListParam.getStartDate()!=null){
            lambda.ge(AnswerCheck::getCheckTime, answerCheckListParam.getStartDate());
        }
        if(answerCheckListParam !=null && answerCheckListParam.getEndDate()!=null){
            Date endDate = new DateTime(answerCheckListParam.getEndDate()).plusDays(1).toDate();
            lambda.le(AnswerCheck::getCheckTime, endDate);
        }
        if(answerCheckListParam !=null &&StringUtils.isNotBlank(answerCheckListParam.getDeptId())){
            lambda.eq(AnswerCheck::getCheckedDept, answerCheckListParam.getDeptId());
        }

        lambda.orderByDesc(AnswerCheck::getAnswerTime);
        IPage<AnswerCheck> answerCheckIPage = this.page(page, lambda);
        List<AnswerCheck> answerCheckList = answerCheckIPage.getRecords();
        if(answerCheckList.isEmpty()){
            return new Page<>();
        }

        List<Integer> questionIdList = answerCheckList.stream().map(AnswerCheck::getQuId).distinct().collect(Collectors.toList());
        List<Question> answerQuestionList = questionMapper.selectBatchIds(questionIdList);
        Map<Integer, Question> questionMap = answerQuestionList.stream().collect(Collectors.toMap(Question::getId, q -> q));
        List<AnswerCheckVo> answerCheckVoList = answerCheckList.stream().map(answerCheck -> {
            AnswerCheckVo answerCheckVo = new AnswerCheckVo();
            BeanUtils.copyProperties(answerCheck,answerCheckVo);
            answerCheckVo.setQuName(questionMap.get(answerCheck.getQuId()).getQuName());
            return answerCheckVo;
        }).collect(Collectors.toList());

        IPage<AnswerCheckVo> answerCheckPage = new Page<>(pageNo,pageSize);
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
        String creater_deptname = "";
        if (jsonRootBean != null) {
            if (jsonRootBean.getData() != null) {
                creater = jsonRootBean.getData().getTbUser().getId();
                creater_name = jsonRootBean.getData().getTbUser().getUserName();
                creater_deptid = jsonRootBean.getData().getDeps().get(0).getId();
                creater_deptname = jsonRootBean.getData().getDeps().get(0).getDepName();
            }
        }
        return getResult(answerCheckAddParam, creater, creater_name, creater_deptid, creater_deptname);
    }


    @Override
    public Result answerByMiniApp(AnswerMiniAppParam answerMiniAppParam) {
        String userId = answerMiniAppParam.getUserId();
        TbUser tbUser = tbUserService.getById(userId);
        if(StringUtils.isBlank(userId)  || tbUser==null){
            return ResultFactory.error("userId参数错误");
        }
        AnswerCheckAddParam answerCheckAddParam = new AnswerCheckAddParam();
        BeanUtils.copyProperties(answerMiniAppParam,answerCheckAddParam);
        if(answerMiniAppParam.getId()!=null && NumberUtil.isNumber(answerMiniAppParam.getId())){
            answerCheckAddParam.setId(Integer.parseInt(answerMiniAppParam.getId()));
        }
        TbDep tbDep = tbDepService.getById(tbUser.getDepid());
        return getResult(answerCheckAddParam, tbUser.getId(), tbUser.getUsername(), tbUser.getDepid(), tbDep.getDepname());
    }

    private Result getResult(AnswerCheckAddParam answerCheckAddParam, String creater, String creater_name, String creater_deptid, String creater_deptname) {
        AnswerCheck answerCheck = this.getById(answerCheckAddParam.getId());
        if(answerCheck==null){
            answerCheck = new AnswerCheck();
        }else{
            if(answerCheck.getAnswerStatus().equals(1)){
                return ResultFactory.error("该记录已提交,无法更改。");
            }
        }
        //插入总表
        answerCheck.setQuId(answerCheckAddParam.getQuId());
        answerCheck.setAnswerJson( JSON.toJSONString(answerCheckAddParam.getAnswers()));
        Integer status = answerCheckAddParam.getStatus();
        answerCheck.setAnswerStatus(status);
        Date date = new Date();
        if(status.equals(AnswerCheckConstant.ANSWER_STATUS_RELEASE)){
            answerCheck.setSubmitTime(date);
        }
        answerCheck.setCreater(creater);
        answerCheck.setCreaterName(creater_name);
        answerCheck.setCreaterDeptId(creater_deptid);
        answerCheck.setCreaterDeptName(creater_deptname);
        answerCheck.setAnswerTime(date);

        Answers[] answers = answerCheckAddParam.getAnswers();
        Map<String, String> mapCache = new HashMap<>();
        for (Answers a : answers) {
            mapCache.put(a.getSubColumnName(), a.getSubValue());
        }

        if(mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECK_TIME)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECK_TIME)!=null){
            String dateString = mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECK_TIME);
            Date dateCheckTime = DateUtil.parse(dateString).toJdkDate();
            answerCheck.setCheckTime(dateCheckTime);
        }


        if(mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECKED_DEPT)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_DEPT)!=null){
            answerCheck.setCheckedDept(mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_DEPT));
        }

        if(mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECKED_DOCT)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_DOCT)!=null){
            answerCheck.setCheckedDoct(mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_DOCT));
        }

        if(mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECKED_PATIENT_ID)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_PATIENT_ID)!=null){
            answerCheck.setCheckedPatientId(mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_PATIENT_ID));
        }

        if(mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECKED_PATIENT_NAME)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_PATIENT_NAME)!=null){
            answerCheck.setCheckedPatientId(mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_PATIENT_NAME));
        }


        if(mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECKED_TOTAL_SCORE)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_TOTAL_SCORE)!=null){
            answerCheck.setTotalScore(mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_TOTAL_SCORE));
        }

        if(mapCache.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECKED_PASS_STATUS)
                && mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_PASS_STATUS)!=null){
            String s = mapCache.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_PASS_STATUS);
            if(StringUtils.isNotBlank(s) && NumberUtil.isNumber(s)){
                answerCheck.setPassStatus(Integer.parseInt(s));
            }
        }

        boolean insertOrUpdate = answerCheck.getId() != null && answerCheck.getId() != 0;
        if (insertOrUpdate) {
            answerCheck.setUpdateTime(date);
            baseMapper.updateById(answerCheck);
        }else{
            answerCheck.setCreateTime(date);
            answerCheck.setUpdateTime(date);
            answerCheck.setDel(AnswerCheckConstant.DEL_NORMAL);

            String summaryMappingTableId = UUIDGenerator.generateRandomUUIDAndCurrentTimeMillis();
            answerCheck.setSummaryMappingTableId(summaryMappingTableId);
            baseMapper.insert(answerCheck);
        }
        //插入子表
        StringBuffer sqlAns = new StringBuffer();
        Question question = questionMapper.selectById(answerCheckAddParam.getQuId());
        if (question != null) {
            if (insertOrUpdate) {

                sqlAns.append("update `" + question.getTableName() + "` set ");
                List<Qsubject> subjectList = qsubjectMapper.selectSubjectByQuId(answerCheckAddParam.getQuId());
                for (int i = 0; i < subjectList.size(); i++) {
                    Qsubject qsubjectDynamicTable = subjectList.get(i);
                    String subType = qsubjectDynamicTable.getSubType();
                    Integer del = qsubjectDynamicTable.getDel();
                    if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                            || QuestionConstant.DEL_DELETED.equals(del) || mapCache.get(qsubjectDynamicTable.getColumnName())==null
                            /*|| StringUtils.isBlank(mapCache.get(qsubjectDynamicTable.getColumnName()))*/  ) {
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
                        if(StringUtils.isNotBlank(columnNameMark)){
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
                        if(StringUtils.isNotBlank(columnNameMarkImg)){
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
                sqlAns.append("`tbksmc`='");
                sqlAns.append(creater_deptname);
                sqlAns.append("',");
                sqlAns.append("`tbksdm`='");
                sqlAns.append(creater_deptid);
                sqlAns.append("'");
//                    sqlAns.delete(sqlAns.length()-1,sqlAns.length());
                sqlAns.append(" where summary_mapping_table_id = '");
                sqlAns.append(answerCheck.getSummaryMappingTableId());
                sqlAns.append("'");
                log.info("answerCheck-----update sqlAns:{}", sqlAns.toString());
                dynamicTableMapper.updateDynamicTable(sqlAns.toString());
            }else{
                sqlAns.append("insert into `" + question.getTableName() + "` (");

                List<Qsubject> subjectList = qsubjectMapper.selectSubjectByQuId(answerCheckAddParam.getQuId());
                for (int i = 0; i < subjectList.size(); i++) {
                    Qsubject qsubjectDynamicTable = subjectList.get(i);
                    String subType = qsubjectDynamicTable.getSubType();
                    Integer del = qsubjectDynamicTable.getDel();
                    if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                            || QuestionConstant.DEL_DELETED.equals(del) || mapCache.get(qsubjectDynamicTable.getColumnName())==null
                            || StringUtils.isBlank(mapCache.get(qsubjectDynamicTable.getColumnName())) ) {
                        continue;
                    }

                    Qsubject qsubject = subjectList.get(i);
                    sqlAns.append("`");
                    sqlAns.append(qsubject.getColumnName());
                    sqlAns.append("`");
                    sqlAns.append(",");

                    if (QsubjectConstant.MARK_OPEN.equals(qsubjectDynamicTable.getMark())) {
                        String columnNameMark = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark");
                        if(StringUtils.isNotBlank(columnNameMark)){
                            sqlAns.append("`");
                            sqlAns.append(qsubject.getColumnName());
                            sqlAns.append("_mark");
                            sqlAns.append("`");
                            sqlAns.append(",");
                        }

                        String columnNameMarkImg = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark_img");
                        if(StringUtils.isNotBlank(columnNameMarkImg)){
                            sqlAns.append("`");
                            sqlAns.append(qsubject.getColumnName());
                            sqlAns.append("_mark_img");
                            sqlAns.append("`");
                            sqlAns.append(",");
                        }

                    }
                }
                sqlAns.append("`tbksmc`,");
                sqlAns.append("`tbksdm`,");
                sqlAns.append("`summary_mapping_table_id`");
//                sqlAns.delete(sqlAns.length()-1,sqlAns.length());
                sqlAns.append(") values (");
                for (int i = 0; i < subjectList.size(); i++) {
                    Qsubject qsubjectDynamicTable = subjectList.get(i);
                    String subType = qsubjectDynamicTable.getSubType();
                    Integer del = qsubjectDynamicTable.getDel();
                    if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                            || QuestionConstant.DEL_DELETED.equals(del) || mapCache.get(qsubjectDynamicTable.getColumnName())==null
                            || StringUtils.isBlank(mapCache.get(qsubjectDynamicTable.getColumnName()))) {
                        continue;
                    }
                    sqlAns.append("'");
                    sqlAns.append(mapCache.get(qsubjectDynamicTable.getColumnName()));
                    sqlAns.append("',");


                    if (QsubjectConstant.MARK_OPEN.equals(qsubjectDynamicTable.getMark())) {
                        String columnNameMark = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark");
                        if(StringUtils.isNotBlank(columnNameMark)){
                            sqlAns.append("'");
                            sqlAns.append(columnNameMark);
                            sqlAns.append("',");
                        }

                        String columnNameMarkImg = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark_img");
                        if(StringUtils.isNotBlank(columnNameMarkImg)){
                            sqlAns.append("'");
                            sqlAns.append(columnNameMarkImg);
                            sqlAns.append("',");
                        }
                    }
                }
                sqlAns.append("'");
                sqlAns.append(creater_deptname);
                sqlAns.append("',");

                sqlAns.append("'");
                sqlAns.append(creater_deptid);
                sqlAns.append("',");

                sqlAns.append("'");
                sqlAns.append(answerCheck.getSummaryMappingTableId());
                sqlAns.append("'");
//                sqlAns.delete(sqlAns.length()-1,sqlAns.length());

                sqlAns.append(")");
                log.info("answerCheck-----insert sqlAns:{}", sqlAns.toString());
                dynamicTableMapper.insertDynamicTable(sqlAns.toString());
            }
        }

        return ResultFactory.success();
    }



    @Override
    public AnswerCheck queryById(String id) {
        AnswerCheck answerCheck = this.getById(id);
        if (answerCheck == null) {
            return null;
        }

        String answerJson = (String) answerCheck.getAnswerJson();
        List<SingleDiseaseAnswer> singleDiseaseAnswerList = JSON.parseArray(answerJson, SingleDiseaseAnswer.class);
        Map<String, SingleDiseaseAnswer> mapCache = new HashMap<>();
        if(singleDiseaseAnswerList!=null && !singleDiseaseAnswerList.isEmpty()){
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
            Map<String,String> map = dynamicTableMapper.selectDynamicTableColumn(sqlAns.toString());
//            String s = "select * from q_single_disease_take where id =20 ";
//            Map<String, String> map = dynamicTableMapper.selectDynamicTableColumn(s);
            if(map==null){
                return answerCheck;
            }
            for (Map.Entry<String, String> entry : map.entrySet()) {
                QueryWrapper<Qsubject> wrapper = new QueryWrapper<Qsubject>();
                if("id".equals(entry.getKey())){
                    continue;
                }
                wrapper.eq("column_name", entry.getKey());
                wrapper.eq("qu_id", question.getId());
                wrapper.eq("del", "0");
                Qsubject qsubject = qsubjectMapper.selectOne(wrapper);
                if(qsubject==null){
                    continue;
                }
                SingleDiseaseAnswer singleDiseaseAnswer = new SingleDiseaseAnswer();
                singleDiseaseAnswer.setSubColumnName(qsubject.getColumnName());
                singleDiseaseAnswer.setSubValue(String.valueOf(entry.getValue()));
                mapCache.put(qsubject.getColumnName(), singleDiseaseAnswer);
            }
            List<SingleDiseaseAnswer> resList = new ArrayList<>(mapCache.values());
            answerCheck.setAnswerJson( JSON.toJSONString(resList));
        }

        return answerCheck;
    }

    @Override
    public AnswerCheckDetailListVo detailList(AnswerCheckDetailListParam answerCheckDetailListParam, Data data, Integer pageNo, Integer pageSize) {
        //查询显示列
        Integer quId = answerCheckDetailListParam.getQuId();
        List<CheckDetailSetVo> checkDetailSet = checkDetailSetService.queryByQuestionId(quId,data.getTbUser().getId());
        //查询题目
        List<SubjectVo> subjectList = subjectService.selectSubjectAndOptionByQuId(quId);
        Map<Integer, SubjectVo> subjectMap = subjectList.stream().collect(Collectors.toMap(SubjectVo::getId, q -> q));
        //表头
        List<LinkedHashMap<String,Object>> fieldItems = Lists.newArrayList();
        setItems(checkDetailSet, subjectMap, fieldItems);
        //数据
        Page<AnswerCheck> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
        lambda.eq(AnswerCheck::getQuId,answerCheckDetailListParam.getQuId());
        lambda.eq(AnswerCheck::getCreaterDeptId,data.getTbUser().getDepId());
        lambda.eq(AnswerCheck::getDel, AnswerCheckConstant.DEL_NORMAL);
        String checkMonth = answerCheckDetailListParam.getCheckMonth();
        if(StringUtils.isNotBlank(checkMonth)){
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM");
            DateTime dateTime = dateTimeFormatter.parseDateTime(checkMonth);
            DateTime dateTimeMonthStart = dateTime.dayOfMonth().withMinimumValue();
            DateTime dateTimeMonthEnd = dateTime.dayOfMonth().withMaximumValue();
            lambda.ge(AnswerCheck::getCheckTime, dateTimeMonthStart.toDate());
            lambda.le(AnswerCheck::getCheckTime, dateTimeMonthEnd.toDate());
        }
        lambda.orderByDesc(AnswerCheck::getAnswerTime);
        IPage<AnswerCheck> answerCheckIPage = this.page(page, lambda);
        List<AnswerCheck> answerCheckList = answerCheckIPage.getRecords();
        List<LinkedHashMap<String,Object>> detailDataList = Lists.newArrayList();
        if(answerCheckList.isEmpty()){
//            return ResultFactory.success(AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).total(0).build());
            return AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).total(0).build();
        }
        List<String> summaryMappingTableIdList = answerCheckList.stream().map(AnswerCheck::getSummaryMappingTableId).collect(Collectors.toList());
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
            setList(checkDetailSet,subjectMap,dataItemMap,detailDataList);
        }
        AnswerCheckDetailListVo build = AnswerCheckDetailListVo.builder().fieldItems(fieldItems).detailDataList(detailDataList).total(answerCheckIPage.getTotal()).build();
//        return ResultFactory.success(build);
        return build;
    }

    private void setItems(List<CheckDetailSetVo> checkDetailSet, Map<Integer, SubjectVo> subjectMap, List<LinkedHashMap<String,Object>> fieldItems) {
        for (int i = 0; i < checkDetailSet.size(); i++) {
            LinkedHashMap<String, Object> fieldItem = Maps.newLinkedHashMap();
            ArrayList<LinkedHashMap<String,Object>> emptyList = Lists.newArrayList();
            fieldItems.add(fieldItem);
            CheckDetailSetVo checkDetailSetVo = checkDetailSet.get(i);
            Integer subjectId = checkDetailSetVo.getSubjectId();
            SubjectVo qsubject = subjectMap.get(subjectId);
            if(qsubject==null){
                continue;
            }
            fieldItem.put("fieldTxt",qsubject.getSubName());
            fieldItem.put("fieldId",qsubject.getColumnName());
            if(checkDetailSetVo.getChildList()!=null && !checkDetailSetVo.getChildList().isEmpty()){
                List<LinkedHashMap<String,Object>> fieldItemsFor = Lists.newArrayList();
                fieldItem.put("fieldChildList",fieldItemsFor);
                setItems(checkDetailSetVo.getChildList(), subjectMap, fieldItemsFor);
            }else{
                fieldItem.put("fieldChildList",emptyList);
            }
        }
    }

    private void setList(List<CheckDetailSetVo> checkDetailSet, Map<Integer, SubjectVo> subjectMap,Map<String, String> dataItemMap, List<LinkedHashMap<String, Object>> detailDataList) {
        LinkedHashMap<String, Object> valueItem = Maps.newLinkedHashMap();
        for (int i = 0; i < checkDetailSet.size(); i++) {
            ArrayList<Object> emptyList = Lists.newArrayList();
            CheckDetailSetVo checkDetailSetVo = checkDetailSet.get(i);
            Integer subjectId = checkDetailSetVo.getSubjectId();
            SubjectVo qsubject = subjectMap.get(subjectId);
            if(qsubject==null){
                continue;
            }
            String columnName = qsubject.getColumnName();
            if("checked_dept".equals(columnName)){
                valueItem.put(qsubject.getColumnName(),dataItemMap.get(qsubject.getColumnName()));
                //科室填充名称
                String depId = dataItemMap.get(qsubject.getColumnName());
                if(StringUtils.isNotBlank(depId)){
                    TbDep byId = tbDepService.getById(depId);
                    valueItem.put(qsubject.getColumnName(),byId.getDepname());
                }
            } else if (StringUtils.isNotBlank(columnName)) {
                valueItem.put(qsubject.getColumnName(),dataItemMap.get(qsubject.getColumnName()));
                String value = dataItemMap.get(qsubject.getColumnName());
                if(StringUtils.isNotBlank(value)){
                    String subType = qsubject.getSubType();
                    if (subType.equals(QsubjectConstant.SUB_TYPE_CHOICE)
                            || subType.equals(QsubjectConstant.SUB_TYPE_SINGLE_CHOICE_BOX)
                            || subType.equals(QsubjectConstant.SUB_TYPE_CHOICE_SCORE)
                            || subType.equals(QsubjectConstant.SUB_TYPE_SINGLE_CHOICE_BOX_SCORE)) {
                        List<Qoption> optionList = qsubject.getOptionList();
                        Map<String, Qoption> optionMap = optionList.stream().collect(Collectors.toMap(Qoption::getOpValue, Function.identity(), (oldData, newData) -> newData));
                        Qoption qoption = optionMap.get(value);
                        if(qoption!=null){
                            valueItem.put(qsubject.getColumnName(),qoption.getOpName());
                        }
                    }else if (subType.equals(QsubjectConstant.SUB_TYPE_MULTIPLE_CHOICE)){
                        List<Qoption> optionList = qsubject.getOptionList();
                        Map<String, List<Qoption>> optionMap = optionList.stream().collect(Collectors.toMap(Qoption::getOpValue, Lists::newArrayList,
                                (List<Qoption> n1, List<Qoption> n2)->{
                                    n1.addAll(n2);
                                    return n1;
                                }));
                        String collect = optionMap.get(value).stream().map(Qoption::getOpName).collect(Collectors.joining(","));
                        valueItem.put(qsubject.getColumnName(),collect);
                    }

                }
            }
            if(checkDetailSetVo.getChildList()!=null && !checkDetailSetVo.getChildList().isEmpty()){
                List<LinkedHashMap<String,Object>> valueItemsFor = Lists.newArrayList();
                valueItem.put("fieldChildList",valueItemsFor);
                setList(checkDetailSetVo.getChildList(), subjectMap,dataItemMap, valueItemsFor);
            }else{
                valueItem.put("fieldChildList",emptyList);
            }
        }
        detailDataList.add(valueItem);
    }
}