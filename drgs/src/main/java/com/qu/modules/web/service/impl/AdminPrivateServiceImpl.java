package com.qu.modules.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.qu.constant.*;
import com.qu.event.AnswerCheckStatisticDetailEvent;
import com.qu.modules.web.dto.AnswerCheckStatisticDetailEventDto;
import com.qu.modules.web.entity.*;
import com.qu.modules.web.mapper.*;
import com.qu.modules.web.param.*;
import com.qu.modules.web.service.IAdminPrivateService;
import com.qu.modules.web.service.IAnswerCheckStatisticDetailService;
import com.qu.modules.web.service.IOptionService;
import com.qu.modules.web.service.ISubjectService;
import com.qu.modules.web.vo.SubjectVo;
import com.qu.util.PriceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.api.vo.ResultFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminPrivateServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements IAdminPrivateService {

    @Resource
    private DynamicTableMapper dynamicTableMapper;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QsubjectMapper qsubjectMapper;

    @Resource
    private ISubjectService subjectService;

    @Resource
    private OptionMapper optionMapper;

    @Autowired
    private IOptionService optionService;

    @Resource
    private QSingleDiseaseTakeMapper qSingleDiseaseTakeMapper;

    @Resource
    private AnswerCheckMapper answerCheckMapper;

    @Resource
    private AnswerMapper answerMapper;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private IAnswerCheckStatisticDetailService answerCheckStatisticDetailService;

    private static String[] parsePatterns = new String[]{"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"};

    @Override
    public boolean updateTableDate(AdminPrivateParam adminPrivateParam) {
        //查出来所有的单病种表
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_SINGLE_DISEASE);
        lambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
//        lambda.ge(Question::getId,98);
        List<Question> questionList = questionMapper.selectList(lambda);
        StringBuffer sqlSelect = new StringBuffer();
        StringBuffer sqlAns = new StringBuffer();
        boolean dateUpdateFlag = false;
        for (Question question : questionList) {
            if (question != null) {
                sqlSelect.append("select * from `");
                sqlSelect.append(question.getTableName());
                sqlSelect.append("`");

//                sqlSelect.append("select * from `drgs_cc` where summary_mapping_table_id ='1651194077516_94DE4BF8-1C1C-4AF5-AA85-C7088899DA7D'");
//                Map<String, String> map = dynamicTableMapper.selectDynamicTableColumn(sqlSelect.toString());
                List<Map<String, String>> dataList = dynamicTableMapper.selectDynamicTableColumnList(sqlSelect.toString());
                for (int i = 0; i < dataList.size(); i++) {
                    Map<String, String> map = dataList.get(i);
                    String summary_mapping_table_id = map.get("summary_mapping_table_id");
                    if (map == null) {
                        log.info("question.getTableName() data is null---questionId-------{},{},{}", question.getTableName(), question.getId(),summary_mapping_table_id);
                        continue;
                    }
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        String k = entry.getKey();
                        String v = String.valueOf(entry.getValue());
                        if (v.contains("T") && v.contains("Z")) {
                            try {
                                DateTime parse = DateUtil.parse(v, parsePatterns);
                                DateTime offset = parse.offset(DateField.HOUR, 8);
                                map.put(k, offset.toString(DatePattern.NORM_DATETIME_PATTERN));
                                dateUpdateFlag = true;
                            } catch (DateException e) {
                                log.info("v.contains T ,but parse error--->{}", v);
                            } catch (Exception e) {
                                log.error("map for exception", e);
                            }

                        }

                    }

                    if (!dateUpdateFlag) {
                        log.info("not dateUpdateFlag , continue question.getTableName() data is null---questionId--summary_mapping_table_id-----{},{},{}", question.getTableName(), question.getId(),summary_mapping_table_id);
                        continue;
                    }

                    sqlAns.append("update `" + question.getTableName() + "` set ");
//                    sqlAns.append("update `drgs_cc` set ");

                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        String k = entry.getKey();
                        String v = String.valueOf(entry.getValue());
                        if (k == null || v == null) {
                            log.info("map key or value is null");
                            continue;
                        }
                        sqlAns.append("`");
                        sqlAns.append(k);
                        sqlAns.append("`");
                        sqlAns.append("=");
                        sqlAns.append("'");
                        sqlAns.append(v);
                        sqlAns.append("'");
                        sqlAns.append(",");
                    }
                    sqlAns.delete(sqlAns.length() - 1, sqlAns.length());
                    sqlAns.append(" where summary_mapping_table_id = '");
                    sqlAns.append(summary_mapping_table_id);
                    sqlAns.append("'");
                    log.info("-----update sqlAns:{}", sqlAns.toString());
                    dynamicTableMapper.updateDynamicTable(sqlAns.toString());

                    QSingleDiseaseTake qSingleDiseaseTake = new QSingleDiseaseTake();
                    qSingleDiseaseTake.setAnswerJson(JSON.toJSONString(map));
                    LambdaUpdateWrapper<QSingleDiseaseTake> qSingleDiseaseTakeLambdaUpdateWrapper = new UpdateWrapper<QSingleDiseaseTake>().lambda();
                    qSingleDiseaseTakeLambdaUpdateWrapper.eq(QSingleDiseaseTake::getSummaryMappingTableId, summary_mapping_table_id);
                    qSingleDiseaseTakeMapper.update(qSingleDiseaseTake, qSingleDiseaseTakeLambdaUpdateWrapper);

                    dateUpdateFlag =false;
                    sqlAns.setLength(0);
                }
            }
            sqlSelect.setLength(0);
        }
        return true;
    }


    @Override
    public boolean updateTableDrugFee(AdminPrivateUpdateTableDrugFeeParam adminPrivateUpdateTableDrugFeeParam) {
        //查出来所有的单病种表
        LambdaQueryWrapper<QSingleDiseaseTake> lambda = new QueryWrapper<QSingleDiseaseTake>().lambda();
        List<QSingleDiseaseTake> qSingleDiseaseTakeList = qSingleDiseaseTakeMapper.selectList(lambda);
        StringBuilder sqlSelect = new StringBuilder();
        boolean dateUpdateFlag = false;
        for (QSingleDiseaseTake qSingleDiseaseTake : qSingleDiseaseTakeList) {
            sqlSelect.setLength(0);
            if (qSingleDiseaseTake != null) {
                String dynamicTableName = qSingleDiseaseTake.getDynamicTableName();
                String summaryMappingTableId = qSingleDiseaseTake.getSummaryMappingTableId();
                if(StringUtils.isAnyBlank(dynamicTableName,summaryMappingTableId)){
                    log.info("continue qSingleDiseaseTake.dynamicTableName or summaryMappingTableId is null---qSingleDiseaseTakeId-------{},{},{}", dynamicTableName, qSingleDiseaseTake.getId(),summaryMappingTableId);
                    continue;
                }
                sqlSelect.append("select * from `");
                sqlSelect.append(dynamicTableName);
                sqlSelect.append("`");
                sqlSelect.append(" where summary_mapping_table_id = '");
                sqlSelect.append(summaryMappingTableId);
                sqlSelect.append("'");
                Map<String,String> map = dynamicTableMapper.selectDynamicTableColumn(sqlSelect.toString());
                if (map == null || map.isEmpty()) {
                    log.info("continue sqlAns map is null---qSingleDiseaseTakeId-------{},{},{}", dynamicTableName, qSingleDiseaseTake.getId(),summaryMappingTableId);
                    continue;
                }

                //住院天数
//                String inHospitalDayString = map.get("CM-4-1");
//                if(StringUtils.isNotBlank(inHospitalDayString)){
//                    if(inHospitalDayString.contains("天")){
//                        inHospitalDayString = inHospitalDayString.replaceAll("天","");
//                    }
//                    qSingleDiseaseTake.setInHospitalDay(Integer.parseInt(inHospitalDayString));
//                }
//
//                String inHospitalFee = map.get("CM-6-1");
//                if(StringUtils.isNotBlank(inHospitalFee)){
//                    qSingleDiseaseTake.setInHospitalFee(inHospitalFee);
//                }
//
//                String operationTreatmentFeeString = map.get("CM-6-13");
//                if(StringUtils.isNotBlank(operationTreatmentFeeString)){
//                    qSingleDiseaseTake.setOperationTreatmentFee(operationTreatmentFeeString);
//                }
//
//                String operationDisposableMaterialFeeString = map.get("CM-6-29");
//                if(StringUtils.isBlank(operationDisposableMaterialFeeString)){
//                    log.info("continue CM-6-29 is null---qSingleDiseaseTakeId-------{},{},{}", dynamicTableName, qSingleDiseaseTake.getId(),summaryMappingTableId);
//                    operationDisposableMaterialFeeString="0";
//                }
//                Integer operationDisposableMaterialFee= PriceUtil.toFenInt(new BigDecimal(operationDisposableMaterialFeeString));
//                String examinationDisposableMaterialFeeString = map.get("CM-6-27");
//                if(StringUtils.isBlank(examinationDisposableMaterialFeeString)){
//                    log.info("continue CM-6-27 is null---qSingleDiseaseTakeId-------{},{},{}", dynamicTableName, qSingleDiseaseTake.getId(),summaryMappingTableId);
//                    examinationDisposableMaterialFeeString="0";
//                }
//                Integer examinationDisposableMaterialFee = PriceUtil.toFenInt(new BigDecimal(examinationDisposableMaterialFeeString));
//                String treatmentDisposableMaterialFeeString = map.get("CM-6-28");
//                if(StringUtils.isBlank(treatmentDisposableMaterialFeeString)){
//                    log.info("continue CM-6-28 is null---qSingleDiseaseTakeId-------{},{},{}", dynamicTableName, qSingleDiseaseTake.getId(),summaryMappingTableId);
//                    treatmentDisposableMaterialFeeString="0";
//                }
//                Integer treatmentDisposableMaterialFee = PriceUtil.toFenInt(new BigDecimal(treatmentDisposableMaterialFeeString));
//                qSingleDiseaseTake.setDisposableConsumable(PriceUtil.toYuanStr(treatmentDisposableMaterialFee+examinationDisposableMaterialFee+operationDisposableMaterialFee));


                String westernMedicineFeeString = map.get("CM-6-18");
                if(StringUtils.isBlank(westernMedicineFeeString)){
                    log.info("continue CM-6-18 is null---qSingleDiseaseTakeId-------{},{},{}", dynamicTableName, qSingleDiseaseTake.getId(),summaryMappingTableId);
                    westernMedicineFeeString="0";
                }
                Integer westernMedicineFee= PriceUtil.toFenInt(new BigDecimal(westernMedicineFeeString));

                String chineseMedicineFeeString = map.get("CM-6-20");
                if(StringUtils.isBlank(chineseMedicineFeeString)){
                    log.info("continue CM-6-18 is null---qSingleDiseaseTakeId-------{},{},{}", dynamicTableName, qSingleDiseaseTake.getId(),summaryMappingTableId);
                    chineseMedicineFeeString="0";
                }
                Integer chineseMedicineFee = PriceUtil.toFenInt(new BigDecimal(chineseMedicineFeeString));

                String chineseHerbalMedicineFeeString = map.get("CM-6-21");
                if(StringUtils.isBlank(chineseHerbalMedicineFeeString)){
                    log.info("continue CM-6-18 is null---qSingleDiseaseTakeId-------{},{},{}", dynamicTableName, qSingleDiseaseTake.getId(),summaryMappingTableId);
                    chineseHerbalMedicineFeeString="0";
                }
                Integer chineseHerbalMedicineFee = PriceUtil.toFenInt(new BigDecimal(chineseHerbalMedicineFeeString));

                qSingleDiseaseTake.setDrugFee(PriceUtil.toYuanStr(westernMedicineFee + chineseMedicineFee + chineseHerbalMedicineFee));
                dateUpdateFlag = qSingleDiseaseTakeMapper.updateById(qSingleDiseaseTake)>0;

                if (!dateUpdateFlag) {
                    log.info("not dateUpdateFlag , continue qSingleDiseaseTake.getTableName() data is null---questionId--summary_mapping_table_id-----{},{},{}", dynamicTableName, qSingleDiseaseTake.getId(),summaryMappingTableId);
                    continue;
                }

            }
        }
        return true;
    }

    @Override
    public Result updateOptionValue(AdminPrivateUpdateOptionValueParam adminPrivateUpdateOptionValueParam) {
        //查出来所有的单病种表
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
//        lambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        lambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        lambda.eq(Question::getTableName, adminPrivateUpdateOptionValueParam.getTableName());
//        lambda.ge(Question::getId,98);
        List<Question> questionList = questionMapper.selectList(lambda);
        if(questionList.isEmpty()){
            return ResultFactory.fail("未找到相关查检表，请确认该检查表类型设置为检查表");
        }

        for (Question question : questionList) {
            //查出来所有题
            LambdaQueryWrapper<Qsubject> qsubjectLambdaQueryWrapper = new QueryWrapper<Qsubject>().lambda();
            qsubjectLambdaQueryWrapper.eq(Qsubject::getQuId,question.getId())
                    .eq(Qsubject::getSubType,QsubjectConstant.SUB_TYPE_CHOICE_SCORE)
                    .eq(Qsubject::getDel, QsubjectConstant.DEL_NORMAL);
            List<Qsubject> subjectList = qsubjectMapper.selectList(qsubjectLambdaQueryWrapper);
            List<Integer> subjectIdList = subjectList.stream().map(Qsubject::getId).collect(Collectors.toList());
            //查出来所有选项有 是 并且分值大于0
            LambdaQueryWrapper<Qoption> qoptionLambdaQueryWrapper = new QueryWrapper<Qoption>().lambda();
            qoptionLambdaQueryWrapper.in(Qoption::getSubId,subjectIdList);
            qoptionLambdaQueryWrapper.eq(Qoption::getDel, QoptionConstant.DEL_NORMAL);
            qoptionLambdaQueryWrapper.eq(Qoption::getOpName,"是");
            qoptionLambdaQueryWrapper.eq(Qoption::getOpValue,"y");
            qoptionLambdaQueryWrapper.gt(Qoption::getOptionScore,0);
            List<Qoption> qoptions = optionMapper.selectList(qoptionLambdaQueryWrapper);
            ArrayList<Qoption> optionUpdateList = Lists.newArrayList();
            for (Qoption qoption : qoptions) {
                LambdaQueryWrapper<Qoption> qoptionLambda = new QueryWrapper<Qoption>().lambda();
                qoptionLambda.eq(Qoption::getSubId,qoption.getSubId());
                qoptionLambda.eq(Qoption::getDel, QoptionConstant.DEL_NORMAL);
                qoptionLambda.eq(Qoption::getOpName,"否");
                qoptionLambda.eq(Qoption::getOpValue,"n");
//                qoptionLambdaQueryWrapper.ge(Qoption::getOptionScore,1);
                List<Qoption> optionListNo = optionMapper.selectList(qoptionLambda);
                if(optionListNo.isEmpty()){
                    continue;
                }
                Qoption optionNo = optionListNo.get(0);
                BigDecimal optionScoreNo = optionNo.getOptionScore();
                BigDecimal optionScoreYes = qoption.getOptionScore();
                qoption.setOptionScore(optionScoreNo);
                optionNo.setOptionScore(optionScoreYes);
                optionUpdateList.add(qoption);
                optionUpdateList.add(optionNo);
            }
            optionService.updateBatchById(optionUpdateList);
        }
        return ResultFactory.success();
    }

    @Override
    public Result updateTableAddDel(AdminPrivateUpdateTableAddDelFeeParam param) {
        //查出来所有的问卷表
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        //        lambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_SINGLE_DISEASE);
        lambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        Integer quId = param.getQuId();
        if(quId!=null && !quId.equals(-1)){
            lambda.ge(Question::getId,quId);
        }

        List<Question> questionList = questionMapper.selectList(lambda);
        if(questionList.isEmpty()){
            return ResultFactory.fail("未找到需要更新的问卷");
        }
        HashSet<Integer> quIdSet = Sets.newLinkedHashSet();
        HashSet<String> quNameSet = Sets.newLinkedHashSet();
        HashSet<Integer> success = Sets.newLinkedHashSet();
        StringBuilder sqlSelect = new StringBuilder();
        for (Question question : questionList) {
            String tableName = question.getTableName();
            if(StringUtils.isBlank(tableName)){
                continue;
            }
            sqlSelect.setLength(0);
            //ALTER TABLE `drgs_stk` ADD COLUMN `del` tinyint(4) NULL DEFAULT 0 COMMENT '0:正常1:已删除' AFTER `summary_mapping_table_id`;
            sqlSelect.append("ALTER TABLE `");
            sqlSelect.append(tableName);
            sqlSelect.append("` ADD COLUMN `del` tinyint(4) NULL DEFAULT 0 COMMENT '0:正常1:已删除' AFTER `summary_mapping_table_id`");
            try {
                dynamicTableMapper.createDynamicTable(sqlSelect.toString());
                success.add(question.getId());
                log.info("question.getId()---------------------------------{}",question.getId());
            }catch (Exception e){
                log.error("问卷quId--add table_answer_status error--quId---->"+question.getId(),e);
                quIdSet.add(question.getId());
                quNameSet.add(question.getQuName());
            }
        }
//        if(CollectionUtil.isNotEmpty(quIdSet)){
//            String join = Joiner.on("、").join(quIdSet);
//            String joinName = Joiner.on("、").join(quNameSet);
//            join = "查询到问卷id为"+join+",名称为"+joinName+"的报错，其他已经验证完毕";
//            return ResultFactory.fail(join);
//        }

        return ResultFactory.success( Joiner.on("、").join(success));
    }


    @Override
    public Result updateTableAddAnswerStatus(AdminPrivateUpdateTableDrugFeeParam param) {
        //查出来所有的检查表
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        lambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
//        Integer quId = param.getQuId();
//        if(quId!=null && !quId.equals(-1)){
//            lambda.ge(Question::getId,quId);
//        }
//        if(quId!=null && quId.equals(-1)){
//            lambda.lt(Question::getId,164);
//        }

        List<Question> questionList = questionMapper.selectList(lambda);
        if(questionList.isEmpty()){
            return ResultFactory.fail("未找到需要更新的问卷");
        }
        StringBuilder sqlSelect = new StringBuilder();
        HashSet<Integer> quIdSet = Sets.newLinkedHashSet();
        HashSet<String> quNameSet = Sets.newLinkedHashSet();
        for (Question question : questionList) {
            sqlSelect.setLength(0);
            String tableName = question.getTableName();
            if(StringUtils.isBlank(tableName)){
                continue;
            }
            sqlSelect.setLength(0);
            //ALTER TABLE `drgs_stk` ADD COLUMN `del` tinyint(4) NULL DEFAULT 0 COMMENT '0:正常1:已删除' AFTER `summary_mapping_table_id`;
            sqlSelect.append("ALTER TABLE `");
            sqlSelect.append(tableName);
            sqlSelect.append("` ADD COLUMN `table_answer_status` int(11) NULL DEFAULT 0 COMMENT '0:草稿1:已提交' AFTER `summary_mapping_table_id`");
            try {
                dynamicTableMapper.createDynamicTable(sqlSelect.toString());
            }catch (Exception e){
                log.error("问卷quId--add table_answer_status error--quId---->"+question.getId(),e);
                quIdSet.add(question.getId());
                quNameSet.add(question.getQuName());
            }
        }
        if(CollectionUtil.isNotEmpty(quIdSet)){
            String join = Joiner.on("、").join(quIdSet);
            String joinName = Joiner.on("、").join(quNameSet);
            join = "查询到问卷id为"+join+",名称为"+joinName+"的报错，其他已经验证完毕";
            return ResultFactory.fail(join);
        }
        return ResultFactory.success();
    }

    @Override
    public Result updateQuestionTableAddAnswerStatus(AdminPrivateUpdateTableDrugFeeParam param) {
        //查出来所有的检查表
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        lambda.ne(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        lambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
//        Integer quId = param.getQuId();
//        if(quId!=null && !quId.equals(-1)){
//            lambda.ge(Question::getId,quId);
//        }
//        if(quId!=null && quId.equals(-1)){
//            lambda.lt(Question::getId,164);
//        }

        List<Question> questionList = questionMapper.selectList(lambda);
        if(questionList.isEmpty()){
            return ResultFactory.fail("未找到需要更新的问卷");
        }
        StringBuilder sqlSelect = new StringBuilder();
        HashSet<Integer> quIdSet = Sets.newLinkedHashSet();
        HashSet<String> quNameSet = Sets.newLinkedHashSet();
        for (Question question : questionList) {
            sqlSelect.setLength(0);
            String tableName = question.getTableName();
            if(StringUtils.isBlank(tableName)){
                continue;
            }
            sqlSelect.setLength(0);
            //ALTER TABLE `drgs_stk` ADD COLUMN `del` tinyint(4) NULL DEFAULT 0 COMMENT '0:正常1:已删除' AFTER `summary_mapping_table_id`;
            sqlSelect.append("ALTER TABLE `");
            sqlSelect.append(tableName);
            sqlSelect.append("` ADD COLUMN `table_answer_status` int(11) NULL DEFAULT 0 COMMENT '0:草稿1:已提交' AFTER `summary_mapping_table_id`");
            try {
                dynamicTableMapper.createDynamicTable(sqlSelect.toString());
            }catch (Exception e){
                log.error("问卷quId--add table_answer_status error--quId---->"+question.getId(),e);
                quIdSet.add(question.getId());
                quNameSet.add(question.getQuName());
            }
        }
        if(CollectionUtil.isNotEmpty(quIdSet)){
            String join = Joiner.on("、").join(quIdSet);
            String joinName = Joiner.on("、").join(quNameSet);
            join = "查询到问卷id为"+join+",名称为"+joinName+"的报错，其他已经验证完毕";
            return ResultFactory.fail(join);
        }
        return ResultFactory.success();
    }

    @Override
    public Result updateQuestionAnswerStatus(AdminPrivateUpdateTableDrugFeeParam param) {

        //查出来所有的Answer
        LambdaQueryWrapper<Answer> lambda = new QueryWrapper<Answer>().lambda();
        lambda.eq(Answer::getDel, AnswerCheckConstant.DEL_NORMAL);
        List<Answer> answerCheckList = answerMapper.selectList(lambda);

        //查出来所有的问卷
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
//        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
//        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_NORMAL);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        List<Question> questionList = questionMapper.selectList(questionLambda);
        Map<Integer, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));


        StringBuffer sqlAns = new StringBuffer();
        HashSet<Integer> quIdSet = Sets.newLinkedHashSet();
        HashSet<String> quNameSet = Sets.newLinkedHashSet();
        for (Answer answer : answerCheckList) {
            sqlAns.setLength(0);
            Integer quId = answer.getQuId();
            if (quId == null) {
                log.info("continue answer.quId is null --answerCheckId-------{}", answer.getId());
                continue;
            }
            Question question = questionMap.get(quId);
            if (question == null || QuestionConstant.DEL_DELETED.equals(question.getDel())) {
                log.info("continue answer.question is null--quId---answerCheckId-------{},{}", quId, answer.getId());
                continue;
            }


            String tableName = question.getTableName();
            String summaryMappingTableId = answer.getSummaryMappingTableId();
            if(StringUtils.isAnyBlank(tableName,summaryMappingTableId)){
                log.info("continue answer.tableName or summaryMappingTableId is null---answerCheckId-------{},{},{}", tableName, answer.getId(),summaryMappingTableId);
                continue;
            }

            sqlAns.append("update `" + question.getTableName() + "` set ");
            sqlAns.append("`table_answer_status`='");
            sqlAns.append(answer.getAnswerStatus());
            sqlAns.append("'");
            sqlAns.append(" where summary_mapping_table_id = '");
            sqlAns.append(answer.getSummaryMappingTableId());
            sqlAns.append("'");
            log.info("-----update sqlAns:{}", sqlAns.toString());
            try {
                dynamicTableMapper.updateDynamicTable(sqlAns.toString());
            }catch (Exception e){
                log.error("问卷quId--add updateAnswerStatus error--answer---->"+answer.getId(),e);
                quIdSet.add(question.getId());
                quNameSet.add(question.getQuName());
            }
        }
        if(CollectionUtil.isNotEmpty(quIdSet)){
            String join = Joiner.on("、").join(quIdSet);
            String joinName = Joiner.on("、").join(quNameSet);
            join = "查询到问卷id为"+join+",名称为"+joinName+"的报错，其他已经验证完毕";
            return ResultFactory.fail(join);
        }
        return ResultFactory.success();
    }

    @Override
    public Result updateQSingleDiseaseTakeAnswerStatus(AdminPrivateUpdateTableDrugFeeParam param) {

        //查出来所有的Answer
        LambdaQueryWrapper<QSingleDiseaseTake> lambda = new QueryWrapper<QSingleDiseaseTake>().lambda();
        lambda.eq(QSingleDiseaseTake::getDel, QSingleDiseaseTakeConstant.DEL_NORMAL);
        List<QSingleDiseaseTake> qSingleDiseaseTakeList = qSingleDiseaseTakeMapper.selectList(lambda);

        //查出来所有的检查表
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
//        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
//        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_NORMAL);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        List<Question> questionList = questionMapper.selectList(questionLambda);
        Map<Integer, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));


        StringBuffer sqlAns = new StringBuffer();
        HashSet<Integer> quIdSet = Sets.newLinkedHashSet();
        HashSet<String> quNameSet = Sets.newLinkedHashSet();
        for (QSingleDiseaseTake qSingleDiseaseTake : qSingleDiseaseTakeList) {
            sqlAns.setLength(0);
            Integer quId = qSingleDiseaseTake.getQuestionId();
            if (quId == null) {
                log.info("continue qSingleDiseaseTake.quId is null --answerCheckId-------{}", qSingleDiseaseTake.getId());
                continue;
            }
            Question question = questionMap.get(quId);
            if (question == null || QuestionConstant.DEL_DELETED.equals(question.getDel())) {
                log.info("continue qSingleDiseaseTake.question is null--quId---answerCheckId-------{},{}", quId, qSingleDiseaseTake.getId());
                continue;
            }


            String tableName = question.getTableName();
            String summaryMappingTableId = qSingleDiseaseTake.getSummaryMappingTableId();
            if(StringUtils.isAnyBlank(tableName,summaryMappingTableId)){
                log.info("continue qSingleDiseaseTake.tableName or summaryMappingTableId is null---answerCheckId-------{},{},{}", tableName, qSingleDiseaseTake.getId(),summaryMappingTableId);
                continue;
            }

            sqlAns.append("update `" + question.getTableName() + "` set ");
            sqlAns.append("`table_answer_status`='");
            sqlAns.append(qSingleDiseaseTake.getAnswerStatus());
            sqlAns.append("'");
            sqlAns.append(" where summary_mapping_table_id = '");
            sqlAns.append(qSingleDiseaseTake.getSummaryMappingTableId());
            sqlAns.append("'");
            log.info("-----update sqlAns:{}", sqlAns.toString());
            try {
                dynamicTableMapper.updateDynamicTable(sqlAns.toString());
            }catch (Exception e){
                log.error("问卷quId--add updateAnswerStatus error--qSingleDiseaseTake---->"+qSingleDiseaseTake.getId(),e);
                quIdSet.add(question.getId());
                quNameSet.add(question.getQuName());
            }
        }
        if(CollectionUtil.isNotEmpty(quIdSet)){
            String join = Joiner.on("、").join(quIdSet);
            String joinName = Joiner.on("、").join(quNameSet);
            join = "查询到问卷id为"+join+",名称为"+joinName+"的报错，其他已经验证完毕";
            return ResultFactory.fail(join);
        }
        return ResultFactory.success();
    }

    @Override
    public Result updateAnswerStatus(AdminPrivateUpdateTableDrugFeeParam param) {

        //查出来所有的AnswerCheck
        LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
        lambda.eq(AnswerCheck::getDel, AnswerCheckConstant.DEL_NORMAL);
        List<AnswerCheck> answerCheckList = answerCheckMapper.selectList(lambda);

        //查出来所有的检查表
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
//        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        List<Question> questionList = questionMapper.selectList(questionLambda);
        Map<Integer, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));


        StringBuffer sqlAns = new StringBuffer();
        HashSet<Integer> quIdSet = Sets.newLinkedHashSet();
        HashSet<String> quNameSet = Sets.newLinkedHashSet();
        for (AnswerCheck answerCheck : answerCheckList) {
            sqlAns.setLength(0);
            Integer quId = answerCheck.getQuId();
            if (quId == null) {
                log.info("continue answerCheck.quId is null --answerCheckId-------{}", answerCheck.getId());
                continue;
            }
            Question question = questionMap.get(quId);
            if (question == null || QuestionConstant.DEL_DELETED.equals(question.getDel())) {
                log.info("continue answerCheck.question is null--quId---answerCheckId-------{},{}", quId, answerCheck.getId());
                continue;
            }


            String tableName = question.getTableName();
            String summaryMappingTableId = answerCheck.getSummaryMappingTableId();
            if(StringUtils.isAnyBlank(tableName,summaryMappingTableId)){
                log.info("continue answerCheck.tableName or summaryMappingTableId is null---answerCheckId-------{},{},{}", tableName, answerCheck.getId(),summaryMappingTableId);
                continue;
            }

            sqlAns.append("update `" + question.getTableName() + "` set ");
            sqlAns.append("`table_answer_status`='");
            sqlAns.append(answerCheck.getAnswerStatus());
            sqlAns.append("'");
            sqlAns.append(" where summary_mapping_table_id = '");
            sqlAns.append(answerCheck.getSummaryMappingTableId());
            sqlAns.append("'");
            log.info("-----update sqlAns:{}", sqlAns.toString());
            try {
                dynamicTableMapper.updateDynamicTable(sqlAns.toString());
            }catch (Exception e){
                log.error("问卷quId--add updateAnswerStatus error--answerCheck---->"+answerCheck.getId(),e);
                quIdSet.add(question.getId());
                quNameSet.add(question.getQuName());
            }
        }
        if(CollectionUtil.isNotEmpty(quIdSet)){
            String join = Joiner.on("、").join(quIdSet);
            String joinName = Joiner.on("、").join(quNameSet);
            join = "查询到问卷id为"+join+",名称为"+joinName+"的报错，其他已经验证完毕";
            return ResultFactory.fail(join);
        }
        return ResultFactory.success();
    }

    @Override
    public Result updateAnswerCheckCaseId(AdminPrivateUpdateTableDrugFeeParam param) {
        //查出来所有的AnswerCheck
        LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
        lambda.eq(AnswerCheck::getDel, AnswerCheckConstant.DEL_NORMAL);
        lambda.isNotNull(AnswerCheck::getCaseId);
        List<AnswerCheck> answerCheckList = answerCheckMapper.selectList(lambda);

        //查出来所有的检查表
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
        //        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        List<Question> questionList = questionMapper.selectList(questionLambda);
        Map<Integer, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));
        StringBuilder sqlSelect = new StringBuilder();
        for (AnswerCheck answerCheck : answerCheckList) {
            Integer quId = answerCheck.getQuId();
            if (quId == null) {
                log.info("continue answerCheck.quId is null --answerCheckId-------{}", answerCheck.getId());
                continue;
            }
            Question question = questionMap.get(quId);
            if (question == null) {
                log.info("continue answerCheck.question is null--quId---answerCheckId-------{},{}", quId, answerCheck.getId());
                continue;
            }

            sqlSelect.setLength(0);

            String tableName = question.getTableName();
            String summaryMappingTableId = answerCheck.getSummaryMappingTableId();
            if(StringUtils.isAnyBlank(tableName,summaryMappingTableId)){
                log.info("continue answerCheck.tableName or summaryMappingTableId is null---answerCheckId-------{},{},{}", tableName, answerCheck.getId(),summaryMappingTableId);
                continue;
            }
            sqlSelect.append("select * from `");
            sqlSelect.append(tableName);
            sqlSelect.append("`");
            sqlSelect.append(" where summary_mapping_table_id = '");
            sqlSelect.append(summaryMappingTableId);
            sqlSelect.append("'");
            Map<String,String> map=null;
            try {
                map = dynamicTableMapper.selectDynamicTableColumn(sqlSelect.toString());
            } catch (Exception e) {
                log.info("continue selectDynamicTableColumn Exception-------{},{},{}", tableName, answerCheck.getId(),summaryMappingTableId);
                log.error(e.getMessage(),e);
                continue;
            }
            if (map == null || map.isEmpty()) {
                log.info("continue sqlAns map is null---answerCheck-------{},{},{}", tableName, answerCheck.getId(),summaryMappingTableId);
                continue;
            }
            if(map.containsKey(AnswerCheckConstant.COLUMN_NAME_CASE_ID)){
                String s = map.get(AnswerCheckConstant.COLUMN_NAME_CASE_ID);
                answerCheck.setCaseId(s);

                answerCheckMapper.updateById(answerCheck);
            }
        }
        return ResultFactory.success();
    }

    @Override
    public Result updateAnswerOutTime(AdminPrivateUpdateTableDrugFeeParam param) {

        //查出来所有的 Question
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
        //        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
//        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        List<Question> questionList = questionMapper.selectList(questionLambda);
        Map<Integer, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));

        for (int i = 0; ; i++) {
            //查出来所有的Answer
            LambdaQueryWrapper<Answer> lambda = new QueryWrapper<Answer>().lambda();
            lambda.eq(Answer::getDel, AnswerConstant.DEL_NORMAL);
            lambda.isNull(Answer::getOutTime);

            lambda.last(" limit 500");
            List<Answer> answerList = answerMapper.selectList(lambda);
            if(answerList.isEmpty()){
                break;
            }

            StringBuilder sqlSelect = new StringBuilder();
            for (Answer answer : answerList) {
                Integer quId = answer.getQuId();
                if (quId == null) {
                    log.info("continue answer.quId is null --answerId-------{}", answer.getId());
                    continue;
                }
                Question question = questionMap.get(quId);
                if (question == null) {
                    log.info("continue answer.question is null--quId---answerId-------{},{}", quId, answer.getId());
                    continue;
                }

                sqlSelect.setLength(0);

                String tableName = question.getTableName();
                String summaryMappingTableId = answer.getSummaryMappingTableId();
                if(StringUtils.isAnyBlank(tableName,summaryMappingTableId)){
                    log.info("continue answer.tableName or summaryMappingTableId is null---answerId-------{},{},{}", tableName, answer.getId(),summaryMappingTableId);
                    continue;
                }
                sqlSelect.append("select * from `");
                sqlSelect.append(tableName);
                sqlSelect.append("`");
                sqlSelect.append(" where summary_mapping_table_id = '");
                sqlSelect.append(summaryMappingTableId);
                sqlSelect.append("'");
                Map<String,String> map=null;
                try {
                    map = dynamicTableMapper.selectDynamicTableColumn(sqlSelect.toString());
                } catch (Exception e) {
                    log.info("continue selectDynamicTableColumn Exception-------{},{},{}", tableName, answer.getId(),summaryMappingTableId);
                    log.error(e.getMessage(),e);
                    continue;
                }
                if (map == null || map.isEmpty()) {
                    log.info("continue sqlAns map is null---answer-------{},{},{}", tableName, answer.getId(),summaryMappingTableId);
                    continue;
                }
                if(map.containsKey(AnswerConstant.COLUMN_NAME_OUT_TIME)){
                    String s = map.get(AnswerConstant.COLUMN_NAME_OUT_TIME);
                    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                    org.joda.time.DateTime dateTime = dateTimeFormatter.parseDateTime(s);
                    answer.setOutTime(dateTime.toDate());

                    answerMapper.updateById(answer);
                }
            }
        }
        return ResultFactory.success();
    }

    @Override
    public Result updateAnswerCheckPassStatus(AdminPrivateUpdateTableDrugFeeParam param) {
        //查出来所有的 Question
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
        //        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        List<Question> questionList = questionMapper.selectList(questionLambda);
        if(CollectionUtil.isEmpty(questionList)){
            return ResultFactory.fail("未查到需要更新的查检表");
        }
        List<Integer> questionIdList = questionList.stream().map(Question::getId).distinct().collect(Collectors.toList());
        Map<Integer, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));

        Integer lastId = 0;
        for (int i = 0; ; i++) {
            //查出来所有的Answer
            LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
            lambda.in(AnswerCheck::getQuId, questionIdList);
            lambda.eq(AnswerCheck::getDel, AnswerConstant.DEL_NORMAL);
            lambda.gt(AnswerCheck::getId, lastId);
            lambda.isNull(AnswerCheck::getPassStatus);
            lambda.last(" limit 500");
            List<AnswerCheck> answerCheckList = answerCheckMapper.selectList(lambda);
            if(answerCheckList.isEmpty()){
                break;
            }

            StringBuilder sqlSelect = new StringBuilder();
            for (AnswerCheck answerCheck : answerCheckList) {
                lastId =  answerCheck.getId();
                Integer quId = answerCheck.getQuId();
                if (quId == null) {
                    log.info("continue answerCheck.quId is null --answerId-------{}", answerCheck.getId());
                    continue;
                }
                Question question = questionMap.get(quId);
                if (question == null) {
                    log.info("continue answerCheck.question is null--quId---answerId-------{},{}", quId, answerCheck.getId());
                    continue;
                }

                sqlSelect.setLength(0);

                String tableName = question.getTableName();
                String summaryMappingTableId = answerCheck.getSummaryMappingTableId();
                if(StringUtils.isAnyBlank(tableName,summaryMappingTableId)){
                    log.info("continue answerCheck.tableName or summaryMappingTableId is null---answerId-------{},{},{}", tableName, answerCheck.getId(),summaryMappingTableId);
                    continue;
                }
                sqlSelect.append("select * from `");
                sqlSelect.append(tableName);
                sqlSelect.append("`");
                sqlSelect.append(" where summary_mapping_table_id = '");
                sqlSelect.append(summaryMappingTableId);
                sqlSelect.append("'");
                Map<String,String> map=null;
                try {
                    map = dynamicTableMapper.selectDynamicTableColumn(sqlSelect.toString());
                } catch (Exception e) {
                    log.info("continue selectDynamicTableColumn Exception-------{},{},{}", tableName, answerCheck.getId(),summaryMappingTableId);
                    log.error(e.getMessage(),e);
                    continue;
                }
                if (map == null || map.isEmpty()) {
                    log.info("continue sqlAns map is null---answerCheck-------{},{},{}", tableName, answerCheck.getId(),summaryMappingTableId);
                    continue;
                }
                if(map.containsKey(AnswerCheckConstant.COLUMN_NAME_CHECKED_PASS_STATUS)){
                    String s = map.get(AnswerCheckConstant.COLUMN_NAME_CHECKED_PASS_STATUS);
                    if(StringUtils.isNotBlank(s)){
                        answerCheck.setPassStatus(s);
                        answerCheckMapper.updateById(answerCheck);
                    }
                }
            }
        }
        return ResultFactory.success();
    }

    @Override
    public Result updateTableAddMark(AdminPrivateUpdateTableDrugFeeParam param) {
        //查出来所有的检查表 Question
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
//        questionLambda.ge(Question::getId, 296);
        List<Question> questionList = questionMapper.selectList(questionLambda);
        if(questionList.isEmpty()){
            return ResultFactory.fail("没有已发布的检查表");
        }
        Date date = new Date();
        HashSet<Integer> quIdSet = Sets.newLinkedHashSet();
        HashSet<String> quNameSet = Sets.newLinkedHashSet();
        for (Question question : questionList) {
            StringBuffer sql = new StringBuffer();
            sql.append("ALTER TABLE `" + question.getTableName() + "` ");
            boolean alterFlag = false;
            //查出来所有题
            LambdaQueryWrapper<Qsubject> qsubjectLambdaQueryWrapper = new QueryWrapper<Qsubject>().lambda();
            qsubjectLambdaQueryWrapper.eq(Qsubject::getQuId,question.getId())
                    .in(Qsubject::getSubType,Lists.newArrayList(QsubjectConstant.SUB_TYPE_CHOICE_SCORE,QsubjectConstant.SUB_TYPE_SINGLE_CHOICE_BOX_SCORE))
                    .eq(Qsubject::getMark,  QsubjectConstant.MARK_CLOSE)
                    .eq(Qsubject::getDel, QsubjectConstant.DEL_NORMAL);
            List<Qsubject> subjectList = qsubjectMapper.selectList(qsubjectLambdaQueryWrapper);
            List<Qsubject> updateQsubjectList = Lists.newArrayList();
            for (Qsubject qsubject : subjectList) {
                String subType = qsubject.getSubType();
                Integer del = qsubject.getDel();
                if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                        || QuestionConstant.DEL_DELETED.equals(del) || QsubjectConstant.MARK_OPEN.equals(qsubject.getMark())) {
                    continue;
                }
                alterFlag = true;
                qsubject.setMark(QsubjectConstant.MARK_OPEN);
                qsubject.setUpdateTime(date);
                updateQsubjectList.add(qsubject);
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
                        .append("' AFTER `")
                        .append(qsubject.getColumnName())
                        .append("`,");
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
                        .append("' AFTER `")
                        .append(qsubject.getColumnName())
                        .append("`,");
            }
            if(alterFlag){
                try {
                    sql.delete(sql.length()-1,sql.length());
                    sql.append(" ; ");
                    dynamicTableMapper.createDynamicTable(sql.toString());
                }catch (Exception e){
                    log.error("问卷quId--add mark error--quId---->"+question.getId(),e);
                    quIdSet.add(question.getId());
                    quNameSet.add(question.getQuName());
                    continue;
                }
                subjectService.updateBatchById(updateQsubjectList);
            }
        }
        if(CollectionUtil.isNotEmpty(quIdSet)){
            String join = Joiner.on("、").join(quIdSet);
            String joinName = Joiner.on("、").join(quNameSet);
            join = "查询到问卷id为"+join+",名称为"+joinName+"的报错，其他已经验证完毕";
            return ResultFactory.fail(join);
        }
        return ResultFactory.success();
    }

    @Override
    public Result updateAnswerCheckStatisticDetail(AdminPrivateUpdateTableDrugFeeParam param) {
        //查出来所有的AnswerCheck
        LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
        lambda.eq(AnswerCheck::getDel, AnswerCheckConstant.DEL_NORMAL);
        lambda.eq(AnswerCheck::getAnswerStatus, AnswerCheckConstant.ANSWER_STATUS_RELEASE);
        List<AnswerCheck> answerCheckList = answerCheckMapper.selectList(lambda);

        //查出来所有的检查表
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        List<Question> questionList = questionMapper.selectList(questionLambda);
        Map<Integer, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));
        HashSet<Integer> quIdSet = Sets.newLinkedHashSet();
        for (AnswerCheck answerCheck : answerCheckList) {
            Question question = questionMap.get(answerCheck.getQuId());
            if (question == null || QuestionConstant.DEL_DELETED.equals(question.getDel())) {
                quIdSet.add(answerCheck.getQuId());
                continue;
            }

            JSONArray answers = (JSONArray)JSONArray.parse(answerCheck.getAnswerJson());
            Map<String, String> mapCache = new HashMap<>();
            for (Object a : answers) {
                JSONObject jsonObject = (JSONObject)a;
                mapCache.put((String)jsonObject.get("subColumnName"), (String)jsonObject.get("subValue"));
            }

            StringBuffer sqlAns = new StringBuffer();
            sqlAns.append("select * from `");
            sqlAns.append(question.getTableName());
            sqlAns.append("` where summary_mapping_table_id ='");
            sqlAns.append(answerCheck.getSummaryMappingTableId());
            sqlAns.append("'");
            Map<String, String> map = dynamicTableMapper.selectDynamicTableColumn(sqlAns.toString());
            if (CollectionUtil.isNotEmpty(map)) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    mapCache.put(entry.getKey(), entry.getValue());
                }
            }

            List<SubjectVo> subjectList = subjectService.selectSubjectAndOptionByQuId(answerCheck.getQuId());

            //保存统计明细
            AnswerCheckStatisticDetailEventDto dto = new AnswerCheckStatisticDetailEventDto();
            dto.setQuestion(question);
            dto.setSubjectList(subjectList);
            dto.setMapCache(mapCache);
            dto.setAnswerCheck(answerCheck);
            dto.setAnswerUser(answerCheck.getCreater());
            dto.setAnswerUserName(answerCheck.getCreaterName());
            dto.setDepId(answerCheck.getCreaterDeptId());
            dto.setDepName(answerCheck.getCreaterDeptName());
            dto.setSource(AnswerCheckConstant.SOURCE_PC);
            AnswerCheckStatisticDetailEvent questionVersionEvent = new AnswerCheckStatisticDetailEvent(this, dto);
            applicationEventPublisher.publishEvent(questionVersionEvent);
        }
        if(CollectionUtil.isNotEmpty(quIdSet)){
            String join = Joiner.on("、").join(quIdSet);
            return ResultFactory.fail(join);

        }
        return ResultFactory.success();
    }


    @Override
    public Result updateAnswerCheckAllTable(AdminPrivateUpdateAnswerCheckAllTableParam param, Boolean tbrFlag) {
        //查出来所有的检查表
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
//        questionLambda.ne(Question::getId, 435);
        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        List<Integer> quId = param.getQuId();
        if(CollectionUtil.isNotEmpty(quId)){
            questionLambda.in(Question::getId, quId);
        }
        List<Question> questionList = questionMapper.selectList(questionLambda);
        if(CollectionUtil.isEmpty(questionList)){
            return ResultFactory.fail("没有查到需要更新的问卷");
        }
        HashSet<Integer> quIdSet = Sets.newLinkedHashSet();
        HashSet<String> quNameSet = Sets.newLinkedHashSet();
        for (Question question : questionList) {
            //查询子表
            StringBuffer sqlAns = new StringBuffer();
            sqlAns.append("select * from `");
            sqlAns.append(question.getTableName());
            sqlAns.append("`");
            List<Map<String, String>> dataList = dynamicTableMapper.selectDynamicTableColumnList(sqlAns.toString());
//            if(CollectionUtil.isEmpty(dataList)){
//                log.info("question.getTableName() 子表数据 getTableName data is null---questionId-------{},{}", question.getTableName(), question.getId());
//                continue;
//            }

            List<String> checkMonthBlankSummaryMappingTableIdList = dataList.stream().filter(m -> StringUtils.isBlank(m.get("check_month"))).map(m -> m.get("summary_mapping_table_id")).filter(Objects::nonNull).distinct().collect(Collectors.toList());

            List<String> summary_mapping_table_id = dataList.stream().filter(m-> StringUtils.isNotBlank(m.get("check_month"))).map(m -> m.get("summary_mapping_table_id")).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            //查询总表
            LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
            lambda.in(AnswerCheck::getQuId, question.getId());
            lambda.eq(AnswerCheck::getDel, AnswerCheckConstant.DEL_NORMAL);
//            lambda.eq(AnswerCheck::getAnswerStatus, AnswerCheckConstant.ANSWER_STATUS_RELEASE);
            if(CollectionUtil.isNotEmpty(summary_mapping_table_id)){
                lambda.notIn(AnswerCheck::getSummaryMappingTableId, summary_mapping_table_id);
            }
            List<AnswerCheck> answerCheckList = answerCheckMapper.selectList(lambda);

            if(CollectionUtil.isEmpty(answerCheckList)){
                log.info("question.getTableName() 总表数据 answerCheckList data is null---questionId-------{},{}", question.getTableName(), question.getId());
                continue;
            }
            //将总表数据放入子表
            for (AnswerCheck answerCheck : answerCheckList) {

                JSONArray answers = (JSONArray)JSONArray.parse(answerCheck.getAnswerJson());
                Map<String, String> mapCache = new HashMap<>();
                for (Object a : answers) {
                    JSONObject jsonObject = (JSONObject)a;
                    String subValue = (String)jsonObject.get("subValue");
                    subValue = subValue.replaceAll("'","’");
                    mapCache.put((String)jsonObject.get("subColumnName"), subValue);
                }

                List<SubjectVo> subjectList = subjectService.selectSubjectAndOptionByQuId(question.getId());

                if(checkMonthBlankSummaryMappingTableIdList.contains(answerCheck.getSummaryMappingTableId())){
                    //更新
                    StringBuffer sqlAnsUpdate = new StringBuffer();
                    sqlAnsUpdate.append("update `" + question.getTableName() + "` set ");
                    for (int i = 0; i < subjectList.size(); i++) {
                        SubjectVo qsubjectDynamicTable = subjectList.get(i);
                        String subType = qsubjectDynamicTable.getSubType();
                        Integer del = qsubjectDynamicTable.getDel();
                        if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                                || QuestionConstant.DEL_DELETED.equals(del) || mapCache.get(qsubjectDynamicTable.getColumnName()) == null
                            /*|| StringUtils.isBlank(mapCache.get(qsubjectDynamicTable.getColumnName()))*/) {
                            continue;
                        }
                        sqlAnsUpdate.append("`");
                        sqlAnsUpdate.append(qsubjectDynamicTable.getColumnName());
                        sqlAnsUpdate.append("`");
                        sqlAnsUpdate.append("=");
                        sqlAnsUpdate.append("'");
                        sqlAnsUpdate.append(mapCache.get(qsubjectDynamicTable.getColumnName()));
                        sqlAnsUpdate.append("'");
                        sqlAnsUpdate.append(",");

                        if (QsubjectConstant.MARK_OPEN.equals(qsubjectDynamicTable.getMark())) {
                            String columnNameMark = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark");
                            if (org.apache.commons.lang.StringUtils.isNotBlank(columnNameMark)) {
                                sqlAnsUpdate.append("`");
                                sqlAnsUpdate.append(qsubjectDynamicTable.getColumnName());
                                sqlAnsUpdate.append("_mark");
                                sqlAnsUpdate.append("`");
                                sqlAnsUpdate.append("=");
                                sqlAnsUpdate.append("'");
                                sqlAnsUpdate.append(columnNameMark);
                                sqlAnsUpdate.append("'");
                                sqlAnsUpdate.append(",");
                            }
                            String columnNameMarkImg = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark_img");
                            if (org.apache.commons.lang.StringUtils.isNotBlank(columnNameMarkImg)) {
                                sqlAnsUpdate.append("`");
                                sqlAnsUpdate.append(qsubjectDynamicTable.getColumnName());
                                sqlAnsUpdate.append("_mark_img");
                                sqlAnsUpdate.append("`");
                                sqlAnsUpdate.append("=");
                                sqlAnsUpdate.append("'");
                                sqlAnsUpdate.append(columnNameMarkImg);
                                sqlAnsUpdate.append("'");
                                sqlAnsUpdate.append(",");
                            }

                        }

                    }
                    if(tbrFlag){
                        sqlAnsUpdate.append("`tbrid`='");
                        sqlAnsUpdate.append(answerCheck.getCreater());
                        sqlAnsUpdate.append("',");
                        sqlAnsUpdate.append("`tbksmc`='");
                        sqlAnsUpdate.append(answerCheck.getCreaterDeptName());
                        sqlAnsUpdate.append("',");
                        sqlAnsUpdate.append("`tbksdm`='");
                        sqlAnsUpdate.append(answerCheck.getCreaterDeptId());
                        sqlAnsUpdate.append("',");
                    }
                    sqlAnsUpdate.append("`tbrxm`='");
                    sqlAnsUpdate.append(answerCheck.getCreaterName());
                    sqlAnsUpdate.append("',");
                    sqlAnsUpdate.append("`table_answer_status`='");
                    sqlAnsUpdate.append(answerCheck.getAnswerStatus());
                    sqlAnsUpdate.append("'");
                    sqlAnsUpdate.append(" where summary_mapping_table_id = '");
                    sqlAnsUpdate.append(answerCheck.getSummaryMappingTableId());
                    sqlAnsUpdate.append("'");
                    log.info("answerCheck-----update sqlAnsUpdate:{}", sqlAnsUpdate.toString());
                    dynamicTableMapper.updateDynamicTable(sqlAnsUpdate.toString());
                    continue;
                }
                //插入子表
                StringBuffer sqlAnsInsert = new StringBuffer();


                sqlAnsInsert.append("insert into `" + question.getTableName() + "` (");
                for (int i = 0; i < subjectList.size(); i++) {
                    SubjectVo qsubjectDynamicTable = subjectList.get(i);
                    String subType = qsubjectDynamicTable.getSubType();
                    Integer del = qsubjectDynamicTable.getDel();
                    if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                            || QuestionConstant.DEL_DELETED.equals(del) || mapCache.get(qsubjectDynamicTable.getColumnName()) == null
                            || org.apache.commons.lang.StringUtils.isBlank(mapCache.get(qsubjectDynamicTable.getColumnName()))) {
                        continue;
                    }

                    sqlAnsInsert.append("`");
                    sqlAnsInsert.append(qsubjectDynamicTable.getColumnName());
                    sqlAnsInsert.append("`");
                    sqlAnsInsert.append(",");

                    if (QsubjectConstant.MARK_OPEN.equals(qsubjectDynamicTable.getMark())) {
                        String columnNameMark = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark");
                        if (org.apache.commons.lang.StringUtils.isNotBlank(columnNameMark)) {
                            sqlAnsInsert.append("`");
                            sqlAnsInsert.append(qsubjectDynamicTable.getColumnName());
                            sqlAnsInsert.append("_mark");
                            sqlAnsInsert.append("`");
                            sqlAnsInsert.append(",");
                        }

                        String columnNameMarkImg = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark_img");
                        if (org.apache.commons.lang.StringUtils.isNotBlank(columnNameMarkImg)) {
                            sqlAnsInsert.append("`");
                            sqlAnsInsert.append(qsubjectDynamicTable.getColumnName());
                            sqlAnsInsert.append("_mark_img");
                            sqlAnsInsert.append("`");
                            sqlAnsInsert.append(",");
                        }

                    }
                }
                if(tbrFlag){
                    sqlAnsInsert.append("`tbrid`,");
                    sqlAnsInsert.append("`tbksmc`,");
                    sqlAnsInsert.append("`tbksdm`,");
                }
                sqlAnsInsert.append("`tbrxm`,");
                sqlAnsInsert.append("`table_answer_status`,");
                sqlAnsInsert.append("`summary_mapping_table_id`");
//                sqlAnsInsert.delete(sqlAnsInsert.length()-1,sqlAnsInsert.length());
                sqlAnsInsert.append(") values (");
                for (int i = 0; i < subjectList.size(); i++) {
                    SubjectVo qsubjectDynamicTable = subjectList.get(i);
                    String subType = qsubjectDynamicTable.getSubType();
                    Integer del = qsubjectDynamicTable.getDel();
                    if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                            || QuestionConstant.DEL_DELETED.equals(del) || mapCache.get(qsubjectDynamicTable.getColumnName()) == null
                            || org.apache.commons.lang.StringUtils.isBlank(mapCache.get(qsubjectDynamicTable.getColumnName()))) {
                        continue;
                    }
                    sqlAnsInsert.append("'");
                    sqlAnsInsert.append(mapCache.get(qsubjectDynamicTable.getColumnName()));
                    sqlAnsInsert.append("',");


                    if (QsubjectConstant.MARK_OPEN.equals(qsubjectDynamicTable.getMark())) {
                        String columnNameMark = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark");
                        if (org.apache.commons.lang.StringUtils.isNotBlank(columnNameMark)) {
                            sqlAnsInsert.append("'");
                            sqlAnsInsert.append(columnNameMark);
                            sqlAnsInsert.append("',");
                        }

                        String columnNameMarkImg = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark_img");
                        if (org.apache.commons.lang.StringUtils.isNotBlank(columnNameMarkImg)) {
                            sqlAnsInsert.append("'");
                            sqlAnsInsert.append(columnNameMarkImg);
                            sqlAnsInsert.append("',");
                        }
                    }
                }
                if(tbrFlag){
                    sqlAnsInsert.append("'");
                    sqlAnsInsert.append(answerCheck.getCreater());
                    sqlAnsInsert.append("',");

                    sqlAnsInsert.append("'");
                    sqlAnsInsert.append(answerCheck.getCreaterDeptName());
                    sqlAnsInsert.append("',");

                    sqlAnsInsert.append("'");
                    sqlAnsInsert.append(answerCheck.getCreaterDeptId());
                    sqlAnsInsert.append("',");
                }

                sqlAnsInsert.append("'");
                sqlAnsInsert.append(answerCheck.getCreaterName());
                sqlAnsInsert.append("',");

                sqlAnsInsert.append("'");
                sqlAnsInsert.append(answerCheck.getAnswerStatus());
                sqlAnsInsert.append("',");

                sqlAnsInsert.append("'");
                sqlAnsInsert.append(answerCheck.getSummaryMappingTableId());
                sqlAnsInsert.append("'");
//                sqlAnsInsert.delete(sqlAnsInsert.length()-1,sqlAnsInsert.length());

                sqlAnsInsert.append(")");
                log.info("updateAnswerCheckAllTable---answerCheck-----insert sqlAnsInsert:{}", sqlAnsInsert.toString());
                try {
                    dynamicTableMapper.insertDynamicTable(sqlAnsInsert.toString());
                }catch (Exception e){
                    log.error("问卷quId--add table_answer_status error--quId---->"+question.getId(),e);
                    quIdSet.add(question.getId());
                    quNameSet.add(question.getQuName());
                }
            }
        }

        if(CollectionUtil.isNotEmpty(quIdSet)){
            String join = Joiner.on("、").join(quIdSet);
            String joinName = Joiner.on("、").join(quNameSet);
            join = "处理检查表总表与子表数据不一致问题-查询到问卷id为"+join+",名称为"+joinName+"的报错，其他已经验证完毕";
            return ResultFactory.fail(join);
        }

        return ResultFactory.success();
    }

    @Override
    public Result updateRegisterAnswerCheckAllTable(AdminPrivateUpdateAnswerAllTableParam param, Boolean tbrFlag) {
        //查出来所有的检查表
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_REGISTER);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        List<Integer> quId = param.getQuId();
        if(CollectionUtil.isNotEmpty(quId)){
            questionLambda.in(Question::getId, quId);
        }
        List<Question> questionList = questionMapper.selectList(questionLambda);
        if(CollectionUtil.isEmpty(questionList)){
            return ResultFactory.fail("没有查到需要更新的问卷");
        }
        HashSet<Integer> quIdSet = Sets.newHashSet();
        HashSet<String> quNameSet = Sets.newHashSet();
        for (Question question : questionList) {
            //查询子表
            StringBuffer sqlAns = new StringBuffer();
            sqlAns.append("select * from `");
            sqlAns.append(question.getTableName());
            sqlAns.append("`");
            List<Map<String, String>> dataList = dynamicTableMapper.selectDynamicTableColumnList(sqlAns.toString());
//            if(CollectionUtil.isEmpty(dataList)){
//                log.info("question.getTableName() 子表数据 getTableName data is null---questionId-------{},{}", question.getTableName(), question.getId());
//                continue;
//            }

            List<String> checkMonthBlankSummaryMappingTableIdList = dataList.stream().filter(m -> StringUtils.isBlank(m.get("check_month")))
                    .map(m -> m.get("summary_mapping_table_id")).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<String> summary_mapping_table_id = dataList.stream().filter(m-> StringUtils.isNotBlank(m.get("check_month")))
                    .map(m -> m.get("summary_mapping_table_id")).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if("a".equals(param.getType())){
                summary_mapping_table_id = Lists.newArrayList();
            }

            //查询总表
            LambdaQueryWrapper<Answer> lambda = new QueryWrapper<Answer>().lambda();
            lambda.in(Answer::getQuId, question.getId());
            lambda.eq(Answer::getDel, AnswerConstant.DEL_NORMAL);
//            lambda.eq(Answer::getAnswerStatus, AnswerConstant.ANSWER_STATUS_RELEASE);
            if(CollectionUtil.isNotEmpty(summary_mapping_table_id)){
                lambda.notIn(Answer::getSummaryMappingTableId, summary_mapping_table_id);
            }
            List<Answer> answerList = answerMapper.selectList(lambda);

            if(CollectionUtil.isEmpty(answerList)){
                log.info("updateRegisterAnswerCheckAllTable--question.getTableName() 总表数据 answerList data is null---questionId-------{},{}", question.getTableName(), question.getId());
                continue;
            }
            //将总表数据放入子表
            for (Answer answer : answerList) {

                JSONArray answers = (JSONArray)JSONArray.parse(answer.getAnswerJson());
                Map<String, String> mapCache = new HashMap<>();
                for (Object a : answers) {
                    JSONObject jsonObject = (JSONObject)a;
                    String subValue = (String)jsonObject.get("subValue");
                    subValue = subValue.replaceAll("'","’");
                    mapCache.put((String)jsonObject.get("subColumnName"), subValue);
                }

                List<SubjectVo> subjectList = subjectService.selectSubjectAndOptionByQuId(question.getId());

                if(checkMonthBlankSummaryMappingTableIdList.contains(answer.getSummaryMappingTableId())){
                    //更新
                    StringBuffer sqlAnsUpdate = new StringBuffer();
                    sqlAnsUpdate.append("update `" + question.getTableName() + "` set ");
                    for (int i = 0; i < subjectList.size(); i++) {
                        SubjectVo qsubjectDynamicTable = subjectList.get(i);
                        String subType = qsubjectDynamicTable.getSubType();
                        Integer del = qsubjectDynamicTable.getDel();
                        if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                                || QuestionConstant.DEL_DELETED.equals(del) || mapCache.get(qsubjectDynamicTable.getColumnName()) == null
                            /*|| StringUtils.isBlank(mapCache.get(qsubjectDynamicTable.getColumnName()))*/) {
                            continue;
                        }
                        sqlAnsUpdate.append("`");
                        sqlAnsUpdate.append(qsubjectDynamicTable.getColumnName());
                        sqlAnsUpdate.append("`");
                        sqlAnsUpdate.append("=");
                        sqlAnsUpdate.append("'");
                        sqlAnsUpdate.append(mapCache.get(qsubjectDynamicTable.getColumnName()));
                        sqlAnsUpdate.append("'");
                        sqlAnsUpdate.append(",");

                        if (QsubjectConstant.MARK_OPEN.equals(qsubjectDynamicTable.getMark())) {
                            String columnNameMark = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark");
                            if (org.apache.commons.lang.StringUtils.isNotBlank(columnNameMark)) {
                                sqlAnsUpdate.append("`");
                                sqlAnsUpdate.append(qsubjectDynamicTable.getColumnName());
                                sqlAnsUpdate.append("_mark");
                                sqlAnsUpdate.append("`");
                                sqlAnsUpdate.append("=");
                                sqlAnsUpdate.append("'");
                                sqlAnsUpdate.append(columnNameMark);
                                sqlAnsUpdate.append("'");
                                sqlAnsUpdate.append(",");
                            }
                            String columnNameMarkImg = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark_img");
                            if (org.apache.commons.lang.StringUtils.isNotBlank(columnNameMarkImg)) {
                                sqlAnsUpdate.append("`");
                                sqlAnsUpdate.append(qsubjectDynamicTable.getColumnName());
                                sqlAnsUpdate.append("_mark_img");
                                sqlAnsUpdate.append("`");
                                sqlAnsUpdate.append("=");
                                sqlAnsUpdate.append("'");
                                sqlAnsUpdate.append(columnNameMarkImg);
                                sqlAnsUpdate.append("'");
                                sqlAnsUpdate.append(",");
                            }

                        }

                    }
                    if(tbrFlag){
                        sqlAnsUpdate.append("`tbrid`='");
                        sqlAnsUpdate.append(answer.getCreater());
                        sqlAnsUpdate.append("',");
                        sqlAnsUpdate.append("`tbksmc`='");
                        sqlAnsUpdate.append(answer.getCreaterDeptname());
                        sqlAnsUpdate.append("',");
                        sqlAnsUpdate.append("`tbksdm`='");
                        sqlAnsUpdate.append(answer.getCreaterDeptid());
                        sqlAnsUpdate.append("',");
                    }
                    sqlAnsUpdate.append("`tbrxm`='");
                    sqlAnsUpdate.append(answer.getCreaterName());
                    sqlAnsUpdate.append("',");
                    sqlAnsUpdate.append("`table_answer_status`='");
                    sqlAnsUpdate.append(answer.getAnswerStatus());
                    sqlAnsUpdate.append("'");
                    sqlAnsUpdate.append(" where summary_mapping_table_id = '");
                    sqlAnsUpdate.append(answer.getSummaryMappingTableId());
                    sqlAnsUpdate.append("'");
                    log.info("answer-----update sqlAnsUpdate:{}", sqlAnsUpdate.toString());
                    dynamicTableMapper.updateDynamicTable(sqlAnsUpdate.toString());
                    continue;
                }
                //插入子表
                StringBuffer sqlAnsInsert = new StringBuffer();


                sqlAnsInsert.append("insert into `" + question.getTableName() + "` (");
                for (int i = 0; i < subjectList.size(); i++) {
                    SubjectVo qsubjectDynamicTable = subjectList.get(i);
                    String subType = qsubjectDynamicTable.getSubType();
                    Integer del = qsubjectDynamicTable.getDel();
                    if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                            || QuestionConstant.DEL_DELETED.equals(del) || mapCache.get(qsubjectDynamicTable.getColumnName()) == null
                            || org.apache.commons.lang.StringUtils.isBlank(mapCache.get(qsubjectDynamicTable.getColumnName()))) {
                        continue;
                    }

                    sqlAnsInsert.append("`");
                    sqlAnsInsert.append(qsubjectDynamicTable.getColumnName());
                    sqlAnsInsert.append("`");
                    sqlAnsInsert.append(",");

                    if (QsubjectConstant.MARK_OPEN.equals(qsubjectDynamicTable.getMark())) {
                        String columnNameMark = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark");
                        if (org.apache.commons.lang.StringUtils.isNotBlank(columnNameMark)) {
                            sqlAnsInsert.append("`");
                            sqlAnsInsert.append(qsubjectDynamicTable.getColumnName());
                            sqlAnsInsert.append("_mark");
                            sqlAnsInsert.append("`");
                            sqlAnsInsert.append(",");
                        }

                        String columnNameMarkImg = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark_img");
                        if (org.apache.commons.lang.StringUtils.isNotBlank(columnNameMarkImg)) {
                            sqlAnsInsert.append("`");
                            sqlAnsInsert.append(qsubjectDynamicTable.getColumnName());
                            sqlAnsInsert.append("_mark_img");
                            sqlAnsInsert.append("`");
                            sqlAnsInsert.append(",");
                        }

                    }
                }
                if(tbrFlag){
                    sqlAnsInsert.append("`tbrid`,");
                    sqlAnsInsert.append("`tbksmc`,");
                    sqlAnsInsert.append("`tbksdm`,");
                }
                sqlAnsInsert.append("`tbrxm`,");
                sqlAnsInsert.append("`table_answer_status`,");
                sqlAnsInsert.append("`summary_mapping_table_id`");
//                sqlAnsInsert.delete(sqlAnsInsert.length()-1,sqlAnsInsert.length());
                sqlAnsInsert.append(") values (");
                for (int i = 0; i < subjectList.size(); i++) {
                    SubjectVo qsubjectDynamicTable = subjectList.get(i);
                    String subType = qsubjectDynamicTable.getSubType();
                    Integer del = qsubjectDynamicTable.getDel();
                    if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                            || QuestionConstant.DEL_DELETED.equals(del) || mapCache.get(qsubjectDynamicTable.getColumnName()) == null
                            || org.apache.commons.lang.StringUtils.isBlank(mapCache.get(qsubjectDynamicTable.getColumnName()))) {
                        continue;
                    }
                    sqlAnsInsert.append("'");
                    sqlAnsInsert.append(mapCache.get(qsubjectDynamicTable.getColumnName()));
                    sqlAnsInsert.append("',");


                    if (QsubjectConstant.MARK_OPEN.equals(qsubjectDynamicTable.getMark())) {
                        String columnNameMark = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark");
                        if (org.apache.commons.lang.StringUtils.isNotBlank(columnNameMark)) {
                            sqlAnsInsert.append("'");
                            sqlAnsInsert.append(columnNameMark);
                            sqlAnsInsert.append("',");
                        }

                        String columnNameMarkImg = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark_img");
                        if (org.apache.commons.lang.StringUtils.isNotBlank(columnNameMarkImg)) {
                            sqlAnsInsert.append("'");
                            sqlAnsInsert.append(columnNameMarkImg);
                            sqlAnsInsert.append("',");
                        }
                    }
                }
                if(tbrFlag){
                    sqlAnsInsert.append("'");
                    sqlAnsInsert.append(answer.getCreater());
                    sqlAnsInsert.append("',");

                    sqlAnsInsert.append("'");
                    sqlAnsInsert.append(answer.getCreaterDeptname());
                    sqlAnsInsert.append("',");

                    sqlAnsInsert.append("'");
                    sqlAnsInsert.append(answer.getCreaterDeptid());
                    sqlAnsInsert.append("',");
                }

                sqlAnsInsert.append("'");
                sqlAnsInsert.append(answer.getCreaterName());
                sqlAnsInsert.append("',");

                sqlAnsInsert.append("'");
                sqlAnsInsert.append(answer.getAnswerStatus());
                sqlAnsInsert.append("',");

                sqlAnsInsert.append("'");
                sqlAnsInsert.append(answer.getSummaryMappingTableId());
                sqlAnsInsert.append("'");
//                sqlAnsInsert.delete(sqlAnsInsert.length()-1,sqlAnsInsert.length());

                sqlAnsInsert.append(")");
                log.info("updateRegisterAnswerCheckAllTable---answer-----insert sqlAnsInsert:{}", sqlAnsInsert.toString());
                try {
                    dynamicTableMapper.insertDynamicTable(sqlAnsInsert.toString());
                }catch (Exception e){
                    log.error("updateRegisterAnswerCheckAllTable--问卷quId--add table_answer_status error--quId---->"+question.getId(),e);
                    quIdSet.add(question.getId());
                    quNameSet.add(question.getQuName());
                }
            }
        }

        if(CollectionUtil.isNotEmpty(quIdSet)){
            String join = Joiner.on("、").join(quIdSet);
            String joinName = Joiner.on("、").join(quNameSet);
            join = "处理检查表总表与子表数据不一致问题-查询到问卷id为"+join+",名称为"+joinName+"的报错，其他已经验证完毕";
            return ResultFactory.fail(join);
        }

        return ResultFactory.success();
    }

    @Override
    public Result updateAnswerCheckStatisticDetailBySubtable(AdminPrivateUpdateAnswerCheckAllTableParam param) {
        //查出来所有的检查表
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
//        questionLambda.ne(Question::getId, 435);
        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        List<Integer> quId = param.getQuId();
        if(CollectionUtil.isNotEmpty(quId)){
            questionLambda.in(Question::getId, quId);
        }
        List<Question> questionList = questionMapper.selectList(questionLambda);
        if(CollectionUtil.isEmpty(questionList)){
            return ResultFactory.fail("没有查到需要统计的问卷");
        }
        HashSet<Integer> quIdSet = Sets.newLinkedHashSet();
        HashSet<String> quNameSet = Sets.newLinkedHashSet();
        for (Question question : questionList) {
            //查询总表
            LambdaQueryWrapper<AnswerCheck> lambda = new QueryWrapper<AnswerCheck>().lambda();
            lambda.in(AnswerCheck::getQuId, question.getId());
            lambda.eq(AnswerCheck::getDel, AnswerCheckConstant.DEL_NORMAL);
            lambda.eq(AnswerCheck::getAnswerStatus, AnswerCheckConstant.ANSWER_STATUS_RELEASE);
            List<AnswerCheck> answerCheckList = answerCheckMapper.selectList(lambda);

            if(CollectionUtil.isEmpty(answerCheckList)){
                log.info("question.getTableName() 总表数据 answerCheckList data is null---questionId-------{},{}", question.getTableName(), question.getId());
                continue;
            }

            List<String> summaryMappingTableIdAnswerCheckList = answerCheckList.stream().map(AnswerCheck::getSummaryMappingTableId).filter(Objects::nonNull).distinct().collect(Collectors.toList());

            //查询子表
            StringBuffer sqlAns = new StringBuffer();
            sqlAns.append("select * from `");
            sqlAns.append(question.getTableName());
            sqlAns.append("` where summary_mapping_table_id in (");
            for (String s : summaryMappingTableIdAnswerCheckList) {
                sqlAns.append("'");
                sqlAns.append(s);
                sqlAns.append("',");
            }
            sqlAns.delete(sqlAns.length()-1,sqlAns.length());
            sqlAns.append(")");
            List<Map<String, String>> dataList = dynamicTableMapper.selectDynamicTableColumnList(sqlAns.toString());
            if(CollectionUtil.isEmpty(dataList)){
                log.info("question.getTableName() 子表数据 getTableName data is null---questionId-------{},{}", question.getTableName(), question.getId());
                continue;
            }
            Map<String, Map<String, String>> dataMap = dataList.stream().collect(Collectors.toMap(m->m.get("summary_mapping_table_id"), Function.identity()));

            List<SubjectVo> subjectList = subjectService.selectSubjectAndOptionByQuId(question.getId());
            Map<String, SubjectVo> subjectMap = subjectList.stream().filter(s-> QuestionConstant.DEL_NORMAL.equals(s.getDel()) && StringUtils.isNotBlank(s.getColumnName())).collect(Collectors.toMap(SubjectVo::getColumnName, Function.identity()));

            //将子表数据放入统计
            for (AnswerCheck answerCheck : answerCheckList) {
                try {
                    Map<String, String> stringStringMap = dataMap.get(answerCheck.getSummaryMappingTableId());
                    if(CollectionUtil.isEmpty(stringStringMap)){
                        log.info("question.getTableName() 子表数据 CollectionUtil.isEmpty(stringStringMap) data is null---questionIdName,questionId,answerCheckId-------{},{},{}", question.getTableName(), question.getId(),answerCheck.getId());
                        continue;
                    }
                    //查询统计表有没有数据，有数据则跳过这条记录
                    LambdaQueryWrapper<AnswerCheckStatisticDetail> answerCheckStatisticDetailLambdaQueryWrapper = new QueryWrapper<AnswerCheckStatisticDetail>().lambda();
                    answerCheckStatisticDetailLambdaQueryWrapper
                            .eq(AnswerCheckStatisticDetail::getAnswerCheckId,answerCheck.getId());
                    List<AnswerCheckStatisticDetail> answerCheckStatisticDetailList = answerCheckStatisticDetailService.list(answerCheckStatisticDetailLambdaQueryWrapper);
                    if(CollectionUtil.isNotEmpty(answerCheckStatisticDetailList)){
                        continue;
                    }

                    JSONArray answers = (JSONArray) JSONArray.parse(answerCheck.getAnswerJson());
                    Map<String, String> mapCache = new HashMap<>();
                    for (Object a : answers) {
                        JSONObject jsonObject = (JSONObject) a;
                        mapCache.put((String) jsonObject.get("subColumnName"), (String) jsonObject.get("subValue"));
                    }

                    for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
                        SubjectVo qsubject = subjectMap.get(entry.getKey());
                        if (qsubject == null) {
                            continue;
                        }
                        mapCache.put(qsubject.getColumnName(), String.valueOf(entry.getValue()));
                    }

                    Integer status = answerCheck.getAnswerStatus();
                    if (status.equals(AnswerCheckConstant.ANSWER_STATUS_RELEASE)) {
                        //保存统计明细
                        AnswerCheckStatisticDetailEventDto dto = new AnswerCheckStatisticDetailEventDto();
                        dto.setQuestion(question);
                        dto.setSubjectList(subjectList);
                        dto.setMapCache(mapCache);
                        dto.setAnswerCheck(answerCheck);
                        dto.setAnswerUser(answerCheck.getCreater());
                        dto.setAnswerUserName(answerCheck.getCreaterName());
                        dto.setDepId(answerCheck.getCreaterDeptId());
                        dto.setDepName(answerCheck.getCreaterDeptName());
                        AnswerCheckStatisticDetailEvent questionVersionEvent = new AnswerCheckStatisticDetailEvent(this, dto);
                        applicationEventPublisher.publishEvent(questionVersionEvent);
                    }
                } catch (Exception e) {
                    log.error("问卷quId--add table_answer_status error--quId---->"+question.getId(),e);
                    quIdSet.add(question.getId());
                    quNameSet.add(question.getQuName());
                }
            }
        }

        if(CollectionUtil.isNotEmpty(quIdSet)){
            String join = Joiner.on("、").join(quIdSet);
            String joinName = Joiner.on("、").join(quNameSet);
            join = "处理查检表统计明细表没有数据问题,子表有数据但统计表没有问题-查询到问卷id为"+join+",名称为"+joinName+"的报错，其他已经验证完毕";
            return ResultFactory.fail(join);
        }

        return ResultFactory.success();
    }

    @Override
    public Result selectQuestionAllTable(AdminPrivateUpdateAnswerCheckAllTableParam param) {
        //查出来所有的问卷
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
//        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        questionLambda.orderByAsc(Question::getId);
        List<Integer> quId = param.getQuId();
        if(CollectionUtil.isNotEmpty(quId)){
            questionLambda.in(Question::getId, quId);
        }
        List<Question> questionList = questionMapper.selectList(questionLambda);
        if(CollectionUtil.isEmpty(questionList)){
            return ResultFactory.fail("没有查到需要查询的问卷");
        }
        HashSet<Integer> quIdSet = Sets.newLinkedHashSet();
        HashSet<Integer> existQuIdSet = Sets.newLinkedHashSet();
        HashSet<String> existQuNameSet = Sets.newLinkedHashSet();
        HashSet<String> quNameSet = Sets.newLinkedHashSet();
        String format = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        for (Question question : questionList) {
            List<SubjectVo> subjectList = subjectService.selectSubjectAndOptionByQuId(question.getId());
            Map<String, SubjectVo> subjectMap = subjectList.stream()
                    .filter(subjectVo -> QsubjectConstant.DEL_NORMAL.equals(subjectVo.getDel()))
                    .filter(subjectVo -> !QuestionConstant.SUB_TYPE_GROUP.equals(subjectVo.getSubType()))
                    .filter(subjectVo -> !QuestionConstant.SUB_TYPE_TITLE.equals(subjectVo.getSubType()))
                    .collect(Collectors.toMap(SubjectVo::getColumnName, q -> q));
            StringBuffer sqlAnsSelect = new StringBuffer();
            sqlAnsSelect.append("select ");
            for (int i = 0; i < subjectList.size(); i++) {
                SubjectVo qsubjectDynamicTable = subjectList.get(i);
                String subType = qsubjectDynamicTable.getSubType();
                Integer del = qsubjectDynamicTable.getDel();
                if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                        || QuestionConstant.DEL_DELETED.equals(del) ) {
                    continue;
                }

                sqlAnsSelect.append("`");
                sqlAnsSelect.append(qsubjectDynamicTable.getColumnName());
                sqlAnsSelect.append("`");
                sqlAnsSelect.append(",");

                if (QsubjectConstant.MARK_OPEN.equals(qsubjectDynamicTable.getMark())) {
                    sqlAnsSelect.append("`");
                    sqlAnsSelect.append(qsubjectDynamicTable.getColumnName());
                    sqlAnsSelect.append("_mark");
                    sqlAnsSelect.append("`");
                    sqlAnsSelect.append(",");

                    sqlAnsSelect.append("`");
                    sqlAnsSelect.append(qsubjectDynamicTable.getColumnName());
                    sqlAnsSelect.append("_mark_img");
                    sqlAnsSelect.append("`");
                    sqlAnsSelect.append(",");
                }
            }
            if(QuestionConstant.CATEGORY_TYPE_CHECK.equals(question.getCategoryType())){
                sqlAnsSelect.append("`tbrid`,");
                sqlAnsSelect.append("`tbrxm`,");
            }
            sqlAnsSelect.append("`tbksmc`,");
            sqlAnsSelect.append("`tbksdm`,");
            sqlAnsSelect.append("`table_answer_status`,");
            sqlAnsSelect.append("`summary_mapping_table_id`");
//                sqlAnsSelect.delete(sqlAnsSelect.length()-1,sqlAnsSelect.length());
            sqlAnsSelect.append(" from `");
            sqlAnsSelect.append(question.getTableName());
            sqlAnsSelect.append("` limit 1 ");
            log.info("updateAnswerCheckAllTable---selectQuestionAllTable-----select sqlAnsSelect:{}", sqlAnsSelect.toString());
            try {
                dynamicTableMapper.selectDynamicTableColumnList(sqlAnsSelect.toString());
            } catch (Exception e) {
                log.error("问卷quId--select selectQuestionAllTable error--quId---->" + question.getId(), e);
                if( e.getMessage().contains("Table") &&  e.getMessage().contains("doesn't exist")) {
                    existQuIdSet.add(question.getId());
                    existQuNameSet.add(question.getQuName());
                }
                alterTable(question, e, subjectMap,sqlAnsSelect.toString(),format);

                quIdSet.add(question.getId());
                quNameSet.add(question.getQuName());
            }
        }
        if(CollectionUtil.isNotEmpty(existQuIdSet)){
            String join = Joiner.on("、").join(existQuIdSet);
            String joinName = Joiner.on("、").join(existQuNameSet);
            join = "查询所有问卷子表数据是否缺失并自动添加缺少字段-查询到问卷id为"+join+",名称为"+joinName+"的报错，已发布但未查到子表，处理后再次执行该方法";
            return ResultFactory.fail(join);
        }
        if(CollectionUtil.isNotEmpty(quIdSet)){
            String join = Joiner.on("、").join(quIdSet);
            String joinName = Joiner.on("、").join(quNameSet);
            join = "查询所有问卷子表数据是否缺失并自动添加缺少字段-查询到问卷id为"+join+",名称为"+joinName+"的报错，已经为报错问卷增加字段，其他已经验证完毕，再次执行该方法";
            return ResultFactory.fail(join);
        }
        return ResultFactory.success();
    }


    @Override
    public Result selectRegisterQuestionAllTable(AdminPrivateUpdateAnswerAllTableParam param) {
        //查出来所有的问卷
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_REGISTER);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        questionLambda.orderByAsc(Question::getId);
        List<Integer> quId = param.getQuId();
        if(CollectionUtil.isNotEmpty(quId)){
            questionLambda.in(Question::getId, quId);
        }
        List<Question> questionList = questionMapper.selectList(questionLambda);
        if(CollectionUtil.isEmpty(questionList)){
            return ResultFactory.fail("没有查到需要查询的问卷");
        }
        HashSet<Integer> quIdSet = Sets.newHashSet();
        HashSet<Integer> existQuIdSet = Sets.newHashSet();
        HashSet<String> existQuNameSet = Sets.newHashSet();
        HashSet<String> quNameSet = Sets.newHashSet();
        String format = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        for (Question question : questionList) {
            List<SubjectVo> subjectList = subjectService.selectSubjectAndOptionByQuId(question.getId());
            Map<String, SubjectVo> subjectMap = subjectList.stream()
                    .filter(subjectVo -> QsubjectConstant.DEL_NORMAL.equals(subjectVo.getDel()))
                    .filter(subjectVo -> !QuestionConstant.SUB_TYPE_GROUP.equals(subjectVo.getSubType()))
                    .filter(subjectVo -> !QuestionConstant.SUB_TYPE_TITLE.equals(subjectVo.getSubType()))
                    .collect(Collectors.toMap(SubjectVo::getColumnName, q -> q));
            StringBuffer sqlAnsSelect = new StringBuffer();
            sqlAnsSelect.append("select ");
            for (int i = 0; i < subjectList.size(); i++) {
                SubjectVo qsubjectDynamicTable = subjectList.get(i);
                String subType = qsubjectDynamicTable.getSubType();
                Integer del = qsubjectDynamicTable.getDel();
                if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                        || QuestionConstant.DEL_DELETED.equals(del) ) {
                    continue;
                }

                sqlAnsSelect.append("`");
                sqlAnsSelect.append(qsubjectDynamicTable.getColumnName());
                sqlAnsSelect.append("`");
                sqlAnsSelect.append(",");

                if (QsubjectConstant.MARK_OPEN.equals(qsubjectDynamicTable.getMark())) {
                    sqlAnsSelect.append("`");
                    sqlAnsSelect.append(qsubjectDynamicTable.getColumnName());
                    sqlAnsSelect.append("_mark");
                    sqlAnsSelect.append("`");
                    sqlAnsSelect.append(",");

                    sqlAnsSelect.append("`");
                    sqlAnsSelect.append(qsubjectDynamicTable.getColumnName());
                    sqlAnsSelect.append("_mark_img");
                    sqlAnsSelect.append("`");
                    sqlAnsSelect.append(",");
                }
            }
            if(QuestionConstant.CATEGORY_TYPE_CHECK.equals(question.getCategoryType())){
                sqlAnsSelect.append("`tbrid`,");
                sqlAnsSelect.append("`tbrxm`,");
            }
            sqlAnsSelect.append("`tbksmc`,");
            sqlAnsSelect.append("`tbksdm`,");
            sqlAnsSelect.append("`table_answer_status`,");
            sqlAnsSelect.append("`summary_mapping_table_id`");
//                sqlAnsSelect.delete(sqlAnsSelect.length()-1,sqlAnsSelect.length());
            sqlAnsSelect.append(" from `");
            sqlAnsSelect.append(question.getTableName());
            sqlAnsSelect.append("` limit 1 ");
            log.info("updateAnswerCheckAllTable---selectQuestionAllTable-----select sqlAnsSelect:{}", sqlAnsSelect.toString());
            try {
                dynamicTableMapper.selectDynamicTableColumnList(sqlAnsSelect.toString());
            } catch (Exception e) {
                log.error("问卷quId--select selectQuestionAllTable error--quId---->" + question.getId(), e);
                if( e.getMessage().contains("Table") &&  e.getMessage().contains("doesn't exist")) {
                    existQuIdSet.add(question.getId());
                    existQuNameSet.add(question.getQuName());
                }
                alterTable(question, e, subjectMap,sqlAnsSelect.toString(),format);

                quIdSet.add(question.getId());
                quNameSet.add(question.getQuName());
            }
        }
        if(CollectionUtil.isNotEmpty(existQuIdSet)){
            String join = Joiner.on("、").join(existQuIdSet);
            String joinName = Joiner.on("、").join(existQuNameSet);
            join = "查询所有问卷子表数据是否缺失并自动添加缺少字段-查询到问卷id为"+join+",名称为"+joinName+"的报错，已发布但未查到子表，处理后再次执行该方法";
            return ResultFactory.fail(join);
        }
        if(CollectionUtil.isNotEmpty(quIdSet)){
            String join = Joiner.on("、").join(quIdSet);
            String joinName = Joiner.on("、").join(quNameSet);
            join = "查询所有问卷子表数据是否缺失并自动添加缺少字段-查询到问卷id为"+join+",名称为"+joinName+"的报错，已经为报错问卷增加字段，其他已经验证完毕，再次执行该方法";
            return ResultFactory.fail(join);
        }
        return ResultFactory.success();
    }

    private void alterTable(Question question, Exception e, Map<String, SubjectVo> subjectMap, String sqlAnsSelect,String format) {
        String message = e.getCause().getMessage();
        //Unknown column 'GLI-2-1-1-3' in 'field list'
        if(message.contains("Unknown column") && message.contains("in 'field list'")){
            String substring = message.substring(message.indexOf("'")+1);
            log.info("substring----->{}",substring);
            String substring1 = substring.substring(0, substring.indexOf("'"));
            log.info("substring1----->{}",substring1);
            if("del".equals(substring1)){
                StringBuffer sqlSelect = new StringBuffer();
                sqlSelect.append("ALTER TABLE `");
                sqlSelect.append(question.getTableName());
                sqlSelect.append("` ADD COLUMN `del` tinyint(4) NULL DEFAULT 0 COMMENT '");
                sqlSelect.append(format);
                sqlSelect.append("_a_0:正常1:已删除' ");
                try {
                    dynamicTableMapper.createDynamicTable(sqlSelect.toString());
                    dynamicTableMapper.selectDynamicTableColumnList(sqlAnsSelect);
                }catch (Exception e1){
                    alterTable(question,e1, subjectMap, sqlAnsSelect,format);
                    log.error("问卷quId--add alterTable del error--quId---->"+question.getId(),e);
                }
            }
            if("tbrid".equals(substring1)){
                StringBuffer sqlSelect = new StringBuffer();
                sqlSelect.append("ALTER TABLE `");
                sqlSelect.append(question.getTableName());
                sqlSelect.append("` ADD COLUMN `tbrid` varchar(128) NULL COMMENT '");
                sqlSelect.append(format);
                sqlSelect.append("_a_填报人id' ");
                try {
                    dynamicTableMapper.createDynamicTable(sqlSelect.toString());
                    dynamicTableMapper.selectDynamicTableColumnList(sqlAnsSelect);
                }catch (Exception e1){
                    alterTable(question,e1, subjectMap, sqlAnsSelect,format);
                    log.error("问卷quId--add alterTable tbrid error--quId---->"+question.getId(),e);
                }
            }
            if("tbrxm".equals(substring1)){
                StringBuffer sqlSelect = new StringBuffer();
                sqlSelect.append("ALTER TABLE `");
                sqlSelect.append(question.getTableName());
                sqlSelect.append("` ADD COLUMN `tbrxm` varchar(128) NULL COMMENT ' ");
                sqlSelect.append(format);
                sqlSelect.append("_a_填报人名称' ");
                try {
                    dynamicTableMapper.createDynamicTable(sqlSelect.toString());
                    dynamicTableMapper.selectDynamicTableColumnList(sqlAnsSelect);
                }catch (Exception e1){
                    alterTable(question,e1, subjectMap, sqlAnsSelect,format);
                    log.error("问卷quId--add alterTable tbrxm error--quId---->"+question.getId(),e);
                }
            }
            if("tbksmc".equals(substring1)){
                StringBuffer sqlSelect = new StringBuffer();
                sqlSelect.append("ALTER TABLE `");
                sqlSelect.append(question.getTableName());
                sqlSelect.append("` ADD COLUMN `tbksmc` varchar(128) NULL COMMENT '");
                sqlSelect.append(format);
                sqlSelect.append("_a_填报科室名称' ");
                try {
                    dynamicTableMapper.createDynamicTable(sqlSelect.toString());
                    dynamicTableMapper.selectDynamicTableColumnList(sqlAnsSelect);
                }catch (Exception e1){
                    alterTable(question,e1, subjectMap, sqlAnsSelect,format);
                    log.error("问卷quId--add alterTable tbrxm error--quId---->"+question.getId(),e);
                }
            }
            if("tbksdm".equals(substring1)){
                StringBuffer sqlSelect = new StringBuffer();
                sqlSelect.append("ALTER TABLE `");
                sqlSelect.append(question.getTableName());
                sqlSelect.append("` ADD COLUMN `tbksdm` varchar(128) NULL COMMENT ' ");
                sqlSelect.append(format);
                sqlSelect.append("_a_填报科室代码' ");
                try {
                    dynamicTableMapper.createDynamicTable(sqlSelect.toString());
                    dynamicTableMapper.selectDynamicTableColumnList(sqlAnsSelect);
                }catch (Exception e1){
                    alterTable(question,e1, subjectMap, sqlAnsSelect,format);
                    log.error("问卷quId--add alterTable tbrxm error--quId---->"+question.getId(),e);
                }
            }
            if("table_answer_status".equals(substring1)){
                StringBuffer sqlSelect = new StringBuffer();
                sqlSelect.append("ALTER TABLE `");
                sqlSelect.append(question.getTableName());
                sqlSelect.append("` ADD COLUMN `table_answer_status` int(11) NULL DEFAULT 0 COMMENT '");
                sqlSelect.append(format);
                sqlSelect.append("_a_0:草稿1:已提交' ");
                try {
                    dynamicTableMapper.createDynamicTable(sqlSelect.toString());
                    dynamicTableMapper.selectDynamicTableColumnList(sqlAnsSelect);
                }catch (Exception e1){
                    alterTable(question,e1, subjectMap, sqlAnsSelect,format);
                    log.error("问卷quId--add alterTable table_answer_status error--quId---->"+question.getId(),e);
                }
            }
            if(substring1.contains("_mark_img")){
                String mark = substring1.substring(0, substring1.indexOf("_mark"));
                SubjectVo subjectVo = subjectMap.get(mark);
                if(Objects.nonNull(subjectVo)){
                    StringBuffer sqlSelect = new StringBuffer();
                    sqlSelect.append("ALTER TABLE `");
                    sqlSelect.append(question.getTableName());
                    sqlSelect.append("` ADD COLUMN `");
                    sqlSelect.append(subjectVo.getColumnName());
                    sqlSelect.append("_mark_img");
                    sqlSelect.append("` ");
                    sqlSelect.append(QsubjectConstant.MARK_TYPE)
                            .append("(")
                            .append(QsubjectConstant.MARK_LENGTH)
                            .append(") COMMENT '")
                            .append(format)
                            .append("_a_")
                            .append(subjectVo.getId())
                            .append("的痕迹图片")
                            .append("'");
                    try {
                        dynamicTableMapper.createDynamicTable(sqlSelect.toString());
                        dynamicTableMapper.selectDynamicTableColumnList(sqlAnsSelect);
                    }catch (Exception e1){
                        alterTable(question,e1, subjectMap, sqlAnsSelect,format);
                        log.error("问卷quId--add alterTable subjectMap error--quId---->"+question.getId(),e);
                    }
                }
            }

            if(substring1.contains("_mark")){
                String mark = substring1.substring(0, substring1.indexOf("_mark"));
                SubjectVo subjectVo = subjectMap.get(mark);
                if(Objects.nonNull(subjectVo)){
                    StringBuffer sqlSelect = new StringBuffer();
                    sqlSelect.append("ALTER TABLE `");
                    sqlSelect.append(question.getTableName());
                    sqlSelect.append("` ADD COLUMN `");
                    sqlSelect.append(subjectVo.getColumnName());
                    sqlSelect.append("_mark");
                    sqlSelect.append("` ");
                    sqlSelect.append(QsubjectConstant.MARK_TYPE)
                            .append("(")
                            .append(QsubjectConstant.MARK_LENGTH)
                            .append(") COMMENT '")
                            .append(format)
                            .append("_a_")
                            .append(subjectVo.getId())
                            .append("的痕迹")
                            .append("'");
                    try {
                        dynamicTableMapper.createDynamicTable(sqlSelect.toString());
                        dynamicTableMapper.selectDynamicTableColumnList(sqlAnsSelect);
                    }catch (Exception e1){
                        alterTable(question,e1, subjectMap, sqlAnsSelect,format);
                        log.error("问卷quId--add alterTable subjectMap error--quId---->"+question.getId(),e);
                    }
                }
            }

            if(subjectMap.containsKey(substring1)){
                SubjectVo subjectVo = subjectMap.get(substring1);
                if(Objects.nonNull(subjectVo)){

                    Integer limitWords = subjectVo.getLimitWords();
                    if (limitWords == null || limitWords == 0) {
                        limitWords = 50;
                    }

                    StringBuffer sqlSelect = new StringBuffer();
                    sqlSelect.append("ALTER TABLE `");
                    sqlSelect.append(question.getTableName());
                    sqlSelect.append("` ADD COLUMN `");
                    sqlSelect.append(subjectVo.getColumnName());
                    sqlSelect.append("` varchar(");
                    sqlSelect.append(limitWords)
                            .append(") NULL COMMENT '")
                            .append(format)
                            .append("_a_")
                            .append(subjectVo.getId())
                            .append("'");
                    try {
                        dynamicTableMapper.createDynamicTable(sqlSelect.toString());
                        dynamicTableMapper.selectDynamicTableColumnList(sqlAnsSelect);
                    }catch (Exception e1){
                        alterTable(question,e1, subjectMap, sqlAnsSelect,format);
                        log.error("问卷quId--add alterTable subjectMap error--quId---->"+question.getId(),e);
                    }
                }
            }
        }
    }

    @Override
    public Result addQuestionSubject(AdminPrivateUpdateTableDrugFeeParam param) {
        //查出来所有的问卷
        LambdaQueryWrapper<Question> questionLambda = new QueryWrapper<Question>().lambda();
        questionLambda.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
//        questionLambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_CHECK);
        questionLambda.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        questionLambda.orderByAsc(Question::getId);
        List<Question> questionList = questionMapper.selectList(questionLambda);
        Date date = new Date();
        for (Question question : questionList) {
            //先查查有没有
            LambdaQueryWrapper<Qsubject> qsubjectLambdaQueryWrapper = new QueryWrapper<Qsubject>().lambda();
            qsubjectLambdaQueryWrapper.eq(Qsubject::getQuId,question.getId())
                    .eq(Qsubject::getSubType,QsubjectConstant.SUB_TYPE_RESULT)
                    .eq(Qsubject::getColumnName,"tbrid")
                    .eq(Qsubject::getDel, QsubjectConstant.DEL_NORMAL);
            List<Qsubject> subjectList = qsubjectMapper.selectList(qsubjectLambdaQueryWrapper);
            if(CollectionUtil.isEmpty(subjectList)){
                //插入
                Qsubject qsubject = new Qsubject();
                qsubject.setColumnName("tbrid");
                qsubject.setColumnType("字符串");
                qsubject.setDisplay(0);
                qsubject.setGrabType("person");
                qsubject.setQuId(question.getId());
                qsubject.setRequired(0);
                qsubject.setSubName("填报人");
                qsubject.setSubType("6");
                qsubject.setTextCheck("none");
                qsubject.setColumnTypeDatabase("varchar");
                //计算题号
                Integer subSumCount = qsubjectMapper.selectSumCount(question.getId());
                if(subSumCount==null){
                    qsubject.setOrderNum(1);
                }else{
                    qsubject.setOrderNum(subSumCount + 1);
                }
                qsubject.setDel(QsubjectConstant.DEL_NORMAL);
                qsubject.setCreateTime(date);
                qsubject.setUpdateTime(date);
                qsubjectMapper.insert(qsubject);
            }

            //先查查有没有
            LambdaQueryWrapper<Qsubject> lambda = new QueryWrapper<Qsubject>().lambda();
            lambda.eq(Qsubject::getQuId,question.getId())
                    .eq(Qsubject::getSubType,QsubjectConstant.SUB_TYPE_RESULT)
                    .eq(Qsubject::getColumnName,"tbksmc")
                    .eq(Qsubject::getDel, QsubjectConstant.DEL_NORMAL);
            List<Qsubject> qsubjects = qsubjectMapper.selectList(lambda);
            if(CollectionUtil.isEmpty(qsubjects)){
                //插入
                Qsubject qsubject = new Qsubject();
                qsubject.setColumnName("tbksmc");
                qsubject.setColumnType("字符串");
                qsubject.setDisplay(0);
                qsubject.setGrabType("person");
                qsubject.setQuId(question.getId());
                qsubject.setRequired(0);
                qsubject.setSubName("填报科室名称");
                qsubject.setSubType("6");
                qsubject.setTextCheck("none");
                qsubject.setColumnTypeDatabase("varchar");
                //计算题号
                Integer subSumCount = qsubjectMapper.selectSumCount(question.getId());
                if(subSumCount==null){
                    qsubject.setOrderNum(1);
                }else{
                    qsubject.setOrderNum(subSumCount + 1);
                }
                qsubject.setDel(QsubjectConstant.DEL_NORMAL);
                qsubject.setCreateTime(date);
                qsubject.setUpdateTime(date);
                qsubjectMapper.insert(qsubject);
            }

            //先查查有没有
            LambdaQueryWrapper<Qsubject> queryWrapper = new QueryWrapper<Qsubject>().lambda();
            queryWrapper.eq(Qsubject::getQuId,question.getId())
                    .eq(Qsubject::getSubType,QsubjectConstant.SUB_TYPE_RESULT)
                    .eq(Qsubject::getColumnName,"tbksdm")
                    .eq(Qsubject::getDel, QsubjectConstant.DEL_NORMAL);
            List<Qsubject> qsubjectList = qsubjectMapper.selectList(queryWrapper);
            if(CollectionUtil.isEmpty(qsubjectList)){
                //插入
                Qsubject qsubject = new Qsubject();
                qsubject.setColumnName("tbksdm");
                qsubject.setColumnType("字符串");
                qsubject.setDisplay(0);
                qsubject.setGrabType("person");
                qsubject.setQuId(question.getId());
                qsubject.setRequired(0);
                qsubject.setSubName("填报科室代码");
                qsubject.setSubType("6");
                qsubject.setTextCheck("none");
                qsubject.setColumnTypeDatabase("varchar");
                //计算题号
                Integer subSumCount = qsubjectMapper.selectSumCount(question.getId());
                if(subSumCount==null){
                    qsubject.setOrderNum(1);
                }else{
                    qsubject.setOrderNum(subSumCount + 1);
                }
                qsubject.setDel(QsubjectConstant.DEL_NORMAL);
                qsubject.setCreateTime(date);
                qsubject.setUpdateTime(date);
                qsubjectMapper.insert(qsubject);
            }
        }

        return ResultFactory.success();
    }





}
