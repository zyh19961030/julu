package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.qu.constant.QSingleDiseaseTakeConstant;
import com.qu.constant.QuestionConstant;
import com.qu.modules.web.entity.QSingleDiseaseStatisticDept;
import com.qu.modules.web.entity.QSingleDiseaseStatisticHospital;
import com.qu.modules.web.entity.Question;
import com.qu.modules.web.entity.TbDep;
import com.qu.modules.web.mapper.QSingleDiseaseStatisticDeptMapper;
import com.qu.modules.web.mapper.QSingleDiseaseTakeMapper;
import com.qu.modules.web.mapper.QuestionMapper;
import com.qu.modules.web.param.*;
import com.qu.modules.web.service.IQSingleDiseaseStatisticDeptService;
import com.qu.modules.web.service.IQSingleDiseaseStatisticHospitalService;
import com.qu.modules.web.service.ITbDepService;
import com.qu.modules.web.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 单病种统计表
 * @Author: jeecg-boot
 * @Date:   2022-04-12
 * @Version: V1.0
 */
@Service
public class QSingleDiseaseStatisticDeptServiceImpl extends ServiceImpl<QSingleDiseaseStatisticDeptMapper, QSingleDiseaseStatisticDept> implements IQSingleDiseaseStatisticDeptService {

    @Autowired
    private QSingleDiseaseTakeMapper qSingleDiseaseTakeMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private ITbDepService tbDepService;

    @Autowired
    private IQSingleDiseaseStatisticHospitalService qSingleDiseaseStatisticHospitalService;


    @Override
    public void processData(String startDateString, String endDateString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM");
        DateTime dateTime = null;
        if (StringUtils.isBlank(startDateString)) {
            Date date = this.baseMapper.selectMinOutTime();
            dateTime = new DateTime(date);
        } else {
            dateTime = dateTimeFormatter.parseDateTime(startDateString);
        }
        DateTime startOfDay = dateTime.dayOfMonth().withMinimumValue();
        DateTime endDay = dateTime.plusMonths(1);

        DateTime endDateTime = dateTimeFormatter.parseDateTime(endDateString).dayOfMonth().withMinimumValue();
        List<Question> questionList = getQuestionList();

        for (int i = 0;true; i++) {
            if(i>0){
                startOfDay = startOfDay.plusMonths(1);
                endDay = endDay.plusMonths(1);
            }
            if(startOfDay.isAfter(endDateTime)){
                break;
            }
            Date startDate = startOfDay.toDate();
            Date endDate = endDay.toDate();
            saveDeptStatistic(startDate, endDate,questionList);
            saveHospitalStatistic(startDate, endDate,questionList);
        }
    }

    @Override
    public void statisticLastMonth() {
        DateTime dateTime = new DateTime();
        DateTime startOfDay = dateTime.minusMonths(1).dayOfMonth().withMinimumValue();
        DateTime endDay = dateTime.plusMonths(1);

        Date startDate = startOfDay.toDate();
        Date endDate = endDay.toDate();
        List<Question> questionList = getQuestionList();
        saveDeptStatistic(startDate, endDate,questionList);
        saveHospitalStatistic(startDate, endDate,questionList);
    }

    private List<Question> getQuestionList() {
        //查出所有单病种、所有科室
        LambdaQueryWrapper<Question> questionQueryWrapper = new QueryWrapper<Question>().lambda();
        questionQueryWrapper.eq(Question::getQuStatus, QuestionConstant.QU_STATUS_RELEASE);
        questionQueryWrapper.eq(Question::getQuStop, QuestionConstant.QU_STOP_NORMAL);
        questionQueryWrapper.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_SINGLE_DISEASE);
        questionQueryWrapper.eq(Question::getDel, QuestionConstant.DEL_NORMAL);
        questionQueryWrapper.isNotNull(Question::getDeptIds);
        return questionMapper.selectList(questionQueryWrapper);
    }

    private void saveDeptStatistic(Date startDate, Date endDate, List<Question> questionList) {
        Map<String, Object> params = new HashMap<>();
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            String deptIds = question.getDeptIds();
            if(StringUtils.isBlank(deptIds)){
                continue;
            }
            List<String> deptList = Lists.newArrayList();
            if(deptIds.contains(",")){
                deptList=Arrays.asList(deptIds.split(","));
            }else{
                deptList.add(deptIds);
            }
            List<QSingleDiseaseTakeStatisticDeptVo> resList =Lists.newArrayList();
            for (int j = 0; j < deptList.size(); j++) {
                String deptId = deptList.get(j);

                params.put("categoryId", question.getCategoryId());
                params.put("dept", deptId);
                params.put("dateStart", startDate);
                params.put("dateEnd", endDate);
                List<QSingleDiseaseTakeStatisticDeptVo> singleDiseaseReportStatisticList = qSingleDiseaseTakeMapper.singleDiseaseReportStatistic(params);
                TbDep byId = tbDepService.getById(deptId);
                if(byId==null){
                    continue;
                }
                if (singleDiseaseReportStatisticList.isEmpty()) {
                    QSingleDiseaseTakeStatisticDeptVo qSingleDiseaseTakeStatisticDeptVo = QSingleDiseaseTakeStatisticDeptVo.builder()
                            .categoryId(question.getCategoryId()).dynamicTableName(question.getTableName()).disease(question.getQuName())
                            .tqmsDept(deptId).tqmsDeptName(byId.getDepname()).needReportCount(0).completeReportCount(0).notReportCount(0)
                            .completeReportCountryRate("0").averageInHospitalDay(new BigDecimal("0.0000")).sumInHospitalDay(0)
                            .averageInHospitalFee(new BigDecimal("0.00")).sumInHospitalFee(new BigDecimal("0.00"))
                            .averageDrugFee(new BigDecimal("0.00")).sumDrugFee(new BigDecimal("0.00"))
                            .averageOperationTreatmentFee(new BigDecimal("0.00")).sumOperationTreatmentFee(new BigDecimal("0.00"))
                            .averageDisposableConsumable(new BigDecimal("0.00")).sumDisposableConsumable(new BigDecimal("0.00"))
                            .mortality("0").mortalityCount(0)
                            .operationComplicationRate("0").operationComplicationRateCount(0).build();
                    singleDiseaseReportStatisticList.add(qSingleDiseaseTakeStatisticDeptVo);
                } else {
                    for (int index = 0; index < singleDiseaseReportStatisticList.size(); index++) {
                        QSingleDiseaseTakeStatisticDeptVo qSingleDiseaseTakeReportStatisticVo = singleDiseaseReportStatisticList.get(index);
                        qSingleDiseaseTakeReportStatisticVo.setTqmsDeptName(byId.getDepname());
                        Map<String, Object> countParams = new HashMap<>();
                        countParams.put("categoryId", qSingleDiseaseTakeReportStatisticVo.getCategoryId());
                        countParams.put("dept", qSingleDiseaseTakeReportStatisticVo.getTqmsDept());
                        countParams.put("dateStart", startDate);
                        countParams.put("dateEnd", endDate);
                        countParams.put("status", Lists.newArrayList(QSingleDiseaseTakeConstant.STATUS_COMPLETE));
                        Integer needReportCount = qSingleDiseaseTakeReportStatisticVo.getNeedReportCount();
                        Integer completeReportCount = qSingleDiseaseTakeMapper.countSql(countParams);
                        Integer notReportCount = needReportCount - completeReportCount;
                        qSingleDiseaseTakeReportStatisticVo.setCompleteReportCount(completeReportCount);
                        qSingleDiseaseTakeReportStatisticVo.setNotReportCount(notReportCount);

                        String completeReportCountryRate = numberFormat.format((float) completeReportCount / (float) needReportCount * 100);
                        qSingleDiseaseTakeReportStatisticVo.setCompleteReportCountryRate(completeReportCountryRate);

                        Map<String, Object> map = qSingleDiseaseTakeMapper.countAvgSql(countParams);
                        BigDecimal averageInHospitalDay = (BigDecimal) map.get("averageInHospitalDay");
                        BigDecimal sumInHospitalDay = (BigDecimal) map.get("sumInHospitalDay");
                        BigDecimal averageInHospitalFee = (BigDecimal) map.get("averageInHospitalFee");
                        BigDecimal sumInHospitalFee = (BigDecimal) map.get("sumInHospitalFee");
                        BigDecimal averageDrugFee = (BigDecimal) map.get("averageDrugFee");
                        BigDecimal sumDrugFee = (BigDecimal) map.get("sumDrugFee");
                        BigDecimal averageOperationTreatmentFee = (BigDecimal) map.get("averageOperationTreatmentFee");
                        BigDecimal sumOperationTreatmentFee = (BigDecimal) map.get("sumOperationTreatmentFee");
                        BigDecimal averageDisposableConsumable = (BigDecimal) map.get("averageDisposableConsumable");
                        BigDecimal sumDisposableConsumable = (BigDecimal) map.get("sumDisposableConsumable");
                        qSingleDiseaseTakeReportStatisticVo.setAverageInHospitalDay(averageInHospitalDay);
                        qSingleDiseaseTakeReportStatisticVo.setSumInHospitalDay(sumInHospitalDay.intValue());
                        qSingleDiseaseTakeReportStatisticVo.setAverageInHospitalFee(averageInHospitalFee);
                        qSingleDiseaseTakeReportStatisticVo.setSumInHospitalFee(sumInHospitalFee);
                        qSingleDiseaseTakeReportStatisticVo.setAverageDrugFee(averageDrugFee);
                        qSingleDiseaseTakeReportStatisticVo.setSumDrugFee(sumDrugFee);
                        qSingleDiseaseTakeReportStatisticVo.setAverageOperationTreatmentFee(averageOperationTreatmentFee);
                        qSingleDiseaseTakeReportStatisticVo.setSumOperationTreatmentFee(sumOperationTreatmentFee);
                        qSingleDiseaseTakeReportStatisticVo.setAverageDisposableConsumable(averageDisposableConsumable);
                        qSingleDiseaseTakeReportStatisticVo.setSumDisposableConsumable(sumDisposableConsumable);

                        //查询死亡率和并发症
                        Integer mortalityCount = qSingleDiseaseTakeMapper.countMortalitySql(countParams);
                        String mortalityString = numberFormat.format((float) mortalityCount / (float) needReportCount * 100);
                        qSingleDiseaseTakeReportStatisticVo.setMortality(mortalityString);
                        qSingleDiseaseTakeReportStatisticVo.setMortalityCount(mortalityCount);
                        Integer operationComplicationCount = qSingleDiseaseTakeMapper.countOperationComplicationSql(countParams);
                        String operationComplicationString = numberFormat.format((float) operationComplicationCount / (float) needReportCount * 100);
                        qSingleDiseaseTakeReportStatisticVo.setOperationComplicationRate(operationComplicationString);
                        qSingleDiseaseTakeReportStatisticVo.setOperationComplicationRateCount(operationComplicationCount);

                        qSingleDiseaseTakeReportStatisticVo.setDisease(question.getQuName());
                        qSingleDiseaseTakeReportStatisticVo.setCategoryId(question.getCategoryId());
                    }
                }
                resList.addAll(singleDiseaseReportStatisticList);
            }

            DateTime startOfDay = new DateTime(startDate);
            List<QSingleDiseaseStatisticDept> collect = resList.stream().map(s -> {
                QSingleDiseaseStatisticDept statistic = new QSingleDiseaseStatisticDept();
                BeanUtils.copyProperties(s, statistic);
                statistic.setYear(startOfDay.getYear());
                statistic.setMonth(startOfDay.getMonthOfYear());
                statistic.setYearMonthTitle(startOfDay.toString("yyyy年MM月"));
                statistic.setYearMonthRemark(startOfDay.toString("yyyy-MM"));
                statistic.setSingleDiseaseName(s.getDisease());
                statistic.setAverageInHospitalDay(s.getAverageInHospitalDay().toPlainString());
                statistic.setAverageInHospitalFee(s.getAverageInHospitalFee().toPlainString());
                statistic.setSumInHospitalFee(s.getSumInHospitalFee().toPlainString());
                statistic.setAverageDrugFee(s.getAverageDrugFee().toPlainString());
                statistic.setSumDrugFee(s.getSumDrugFee().toPlainString());
                statistic.setAverageOperationTreatmentFee(s.getAverageOperationTreatmentFee().toPlainString());
                statistic.setSumOperationTreatmentFee(s.getSumOperationTreatmentFee().toPlainString());
                statistic.setAverageDisposableConsumable(s.getAverageDisposableConsumable().toPlainString());
                statistic.setSumDisposableConsumable(s.getSumDisposableConsumable().toPlainString());
                return statistic;
            }).collect(Collectors.toList());

            this.saveBatch(collect);
        }
    }

    private void saveHospitalStatistic(Date startDate, Date endDate, List<Question> questionList) {
        Map<String, Object> params = new HashMap<>();

        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            String deptIds = question.getDeptIds();
            if(StringUtils.isBlank(deptIds)){
                continue;
            }

            List<QSingleDiseaseTakeStatisticDeptVo> resList =Lists.newArrayList();

            params.put("categoryId", question.getCategoryId());
            params.put("dateStart", startDate);
            params.put("dateEnd", endDate);
            List<QSingleDiseaseTakeStatisticDeptVo> singleDiseaseReportStatisticList = qSingleDiseaseTakeMapper.singleDiseaseReportStatistic(params);
            if (singleDiseaseReportStatisticList.isEmpty()) {
                QSingleDiseaseTakeStatisticDeptVo qSingleDiseaseTakeStatisticDeptVo = QSingleDiseaseTakeStatisticDeptVo.builder()
                        .categoryId(question.getCategoryId()).dynamicTableName(question.getTableName()).disease(question.getQuName())
                        .needReportCount(0).completeReportCount(0).notReportCount(0)
                        .completeReportCountryRate("0").averageInHospitalDay(new BigDecimal("0.0000")).sumInHospitalDay(0)
                        .averageInHospitalFee(new BigDecimal("0.00")).sumInHospitalFee(new BigDecimal("0.00"))
                        .averageDrugFee(new BigDecimal("0.00")).sumDrugFee(new BigDecimal("0.00"))
                        .averageOperationTreatmentFee(new BigDecimal("0.00")).sumOperationTreatmentFee(new BigDecimal("0.00"))
                        .averageDisposableConsumable(new BigDecimal("0.00")).sumDisposableConsumable(new BigDecimal("0.00"))
                        .mortality("0").mortalityCount(0)
                        .operationComplicationRate("0").operationComplicationRateCount(0).build();
                singleDiseaseReportStatisticList.add(qSingleDiseaseTakeStatisticDeptVo);
            } else {
                for (int index = 0; index < singleDiseaseReportStatisticList.size(); index++) {
                    QSingleDiseaseTakeStatisticDeptVo qSingleDiseaseTakeReportStatisticVo = singleDiseaseReportStatisticList.get(index);
                    Map<String, Object> countParams = new HashMap<>();
                    countParams.put("categoryId", qSingleDiseaseTakeReportStatisticVo.getCategoryId());
                    countParams.put("dateStart", startDate);
                    countParams.put("dateEnd", endDate);
                    countParams.put("status", Lists.newArrayList(QSingleDiseaseTakeConstant.STATUS_COMPLETE));
                    Integer needReportCount = qSingleDiseaseTakeReportStatisticVo.getNeedReportCount();
                    Integer completeReportCount = qSingleDiseaseTakeMapper.countSql(countParams);
                    Integer notReportCount = needReportCount - completeReportCount;
                    qSingleDiseaseTakeReportStatisticVo.setCompleteReportCount(completeReportCount);
                    qSingleDiseaseTakeReportStatisticVo.setNotReportCount(notReportCount);

                    NumberFormat numberFormat = NumberFormat.getInstance();
                    numberFormat.setMaximumFractionDigits(2);
                    String completeReportCountryRate = numberFormat.format((float) completeReportCount / (float) needReportCount * 100);
                    qSingleDiseaseTakeReportStatisticVo.setCompleteReportCountryRate(completeReportCountryRate);

                    Map<String, Object> map = qSingleDiseaseTakeMapper.countAvgSql(countParams);
                    BigDecimal averageInHospitalDay = (BigDecimal) map.get("averageInHospitalDay");
                    BigDecimal sumInHospitalDay = (BigDecimal) map.get("sumInHospitalDay");
                    BigDecimal averageInHospitalFee = (BigDecimal) map.get("averageInHospitalFee");
                    BigDecimal sumInHospitalFee = (BigDecimal) map.get("sumInHospitalFee");
                    BigDecimal averageDrugFee = (BigDecimal) map.get("averageDrugFee");
                    BigDecimal sumDrugFee = (BigDecimal) map.get("sumDrugFee");
                    BigDecimal averageOperationTreatmentFee = (BigDecimal) map.get("averageOperationTreatmentFee");
                    BigDecimal sumOperationTreatmentFee = (BigDecimal) map.get("sumOperationTreatmentFee");
                    BigDecimal averageDisposableConsumable = (BigDecimal) map.get("averageDisposableConsumable");
                    BigDecimal sumDisposableConsumable = (BigDecimal) map.get("sumDisposableConsumable");
                    qSingleDiseaseTakeReportStatisticVo.setAverageInHospitalDay(averageInHospitalDay);
                    qSingleDiseaseTakeReportStatisticVo.setSumInHospitalDay(sumInHospitalDay.intValue());
                    qSingleDiseaseTakeReportStatisticVo.setAverageInHospitalFee(averageInHospitalFee);
                    qSingleDiseaseTakeReportStatisticVo.setSumInHospitalFee(sumInHospitalFee);
                    qSingleDiseaseTakeReportStatisticVo.setAverageDrugFee(averageDrugFee);
                    qSingleDiseaseTakeReportStatisticVo.setSumDrugFee(sumDrugFee);
                    qSingleDiseaseTakeReportStatisticVo.setAverageOperationTreatmentFee(averageOperationTreatmentFee);
                    qSingleDiseaseTakeReportStatisticVo.setSumOperationTreatmentFee(sumOperationTreatmentFee);
                    qSingleDiseaseTakeReportStatisticVo.setAverageDisposableConsumable(averageDisposableConsumable);
                    qSingleDiseaseTakeReportStatisticVo.setSumDisposableConsumable(sumDisposableConsumable);

                    //查询死亡率和并发症
                    Integer mortalityCount = qSingleDiseaseTakeMapper.countMortalitySql(countParams);
                    String mortalityString = numberFormat.format((float) mortalityCount / (float) needReportCount * 100);
                    qSingleDiseaseTakeReportStatisticVo.setMortality(mortalityString);
                    qSingleDiseaseTakeReportStatisticVo.setMortalityCount(mortalityCount);
                    Integer operationComplicationCount = qSingleDiseaseTakeMapper.countOperationComplicationSql(countParams);
                    String operationComplicationString = numberFormat.format((float) operationComplicationCount / (float) needReportCount * 100);
                    qSingleDiseaseTakeReportStatisticVo.setOperationComplicationRate(operationComplicationString);
                    qSingleDiseaseTakeReportStatisticVo.setOperationComplicationRateCount(operationComplicationCount);

                    qSingleDiseaseTakeReportStatisticVo.setDisease(question.getQuName());
                    qSingleDiseaseTakeReportStatisticVo.setCategoryId(question.getCategoryId());
                }
            }
            resList.addAll(singleDiseaseReportStatisticList);


            DateTime startOfDay = new DateTime(startDate);
            List<QSingleDiseaseStatisticHospital> collect = resList.stream().map(s -> {
                QSingleDiseaseStatisticHospital statistic = new QSingleDiseaseStatisticHospital();
                BeanUtils.copyProperties(s, statistic);
                statistic.setYear(startOfDay.getYear());
                statistic.setMonth(startOfDay.getMonthOfYear());
                statistic.setYearMonthTitle(startOfDay.toString("yyyy年MM月"));
                statistic.setYearMonthRemark(startOfDay.toString("yyyy-MM"));
                statistic.setSingleDiseaseName(s.getDisease());
                statistic.setAverageInHospitalDay(s.getAverageInHospitalDay().toPlainString());
                statistic.setAverageInHospitalFee(s.getAverageInHospitalFee().toPlainString());
                statistic.setSumInHospitalFee(s.getSumInHospitalFee().toPlainString());
                statistic.setAverageDrugFee(s.getAverageDrugFee().toPlainString());
                statistic.setSumDrugFee(s.getSumDrugFee().toPlainString());
                statistic.setAverageOperationTreatmentFee(s.getAverageOperationTreatmentFee().toPlainString());
                statistic.setSumOperationTreatmentFee(s.getSumOperationTreatmentFee().toPlainString());
                statistic.setAverageDisposableConsumable(s.getAverageDisposableConsumable().toPlainString());
                statistic.setSumDisposableConsumable(s.getSumDisposableConsumable().toPlainString());
                return statistic;
            }).collect(Collectors.toList());

            this.qSingleDiseaseStatisticHospitalService.saveBatch(collect);
        }

    }


    @Override
    public List<QSingleDiseaseTakeStatisticDepartmentComparisonVo> singleDiseaseStatisticDepartmentComparison(QSingleDiseaseTakeStatisticAnalysisParam qSingleDiseaseTakeStatisticAnalysisParam) {
//        LambdaQueryWrapper<QSingleDiseaseStatisticDept> lambda = new QueryWrapper<QSingleDiseaseStatisticDept>().lambda();
//        lambda.in(QSingleDiseaseStatisticDept::getCategoryId,qSingleDiseaseTakeStatisticAnalysisParam.getCategoryId());
//        lambda.ge(QSingleDiseaseStatisticDept::getYearMonthRemark,qSingleDiseaseTakeStatisticAnalysisParam.getDateStart());
//        lambda.le(QSingleDiseaseStatisticDept::getYearMonthRemark,qSingleDiseaseTakeStatisticAnalysisParam.getDateEnd());
        List<QSingleDiseaseTakeStatisticDepartmentComparisonVo> qSingleDiseaseStatisticHospitalList = this.baseMapper.singleDiseaseStatisticDepartmentComparison(qSingleDiseaseTakeStatisticAnalysisParam);
//        List<QSingleDiseaseTakeStatisticDepartmentComparisonVo> qSingleDiseaseTakeStatisticAnalysisVoList = qSingleDiseaseStatisticHospitalList.stream().map(e ->
//                QSingleDiseaseTakeStatisticDepartmentComparisonVo.builder()
//                .deptName(e.getTqmsDeptName()).needWriteCount(e.getNeedReportCount()).completeWriteCount(e.getCompleteReportCount()).notWriteCount(e.getNotReportCount())
//                .completeReportCountryRate(String.format("%s%%",e.getCompleteReportCountryRate())).averageInHospitalDay(new BigDecimal(e.getAverageInHospitalDay()))
//                .averageInHospitalFee(new BigDecimal(e.getAverageInHospitalFee()))
//                .complicationRate(String.format("%s%%",e.getOperationComplicationRate())).mortality(String.format("%s%%",e.getMortality())).averageDrugFee(new BigDecimal(e.getAverageDrugFee()))
//                .averageOperationFee(new BigDecimal(e.getAverageOperationTreatmentFee()))
//                .averageDisposableConsumableFee(new BigDecimal(e.getAverageDisposableConsumable())).build())
//            .collect(Collectors.toList());
        return qSingleDiseaseStatisticHospitalList;
    }

    @Override
    public List<QSingleDiseaseTakeStatisticDepartmentComparisonChartVo> singleDiseaseStatisticDepartmentComparisonChart(QSingleDiseaseTakeStatisticDepartmentComparisonChartParam qSingleDiseaseTakeStatisticDepartmentComparisonChartParam) {
        LambdaQueryWrapper<QSingleDiseaseStatisticDept> lambda = new QueryWrapper<QSingleDiseaseStatisticDept>().lambda();
        lambda.in(QSingleDiseaseStatisticDept::getCategoryId,qSingleDiseaseTakeStatisticDepartmentComparisonChartParam.getCategoryId());
        lambda.ge(QSingleDiseaseStatisticDept::getYearMonthRemark,qSingleDiseaseTakeStatisticDepartmentComparisonChartParam.getDateStart());
        lambda.le(QSingleDiseaseStatisticDept::getYearMonthRemark,qSingleDiseaseTakeStatisticDepartmentComparisonChartParam.getDateEnd());
        lambda.orderByAsc(QSingleDiseaseStatisticDept::getYearMonthRemark);
        List<QSingleDiseaseStatisticDept> qSingleDiseaseStatisticHospitalList = this.baseMapper.selectList(lambda);

        String chartType = qSingleDiseaseTakeStatisticDepartmentComparisonChartParam.getChartType();

        Map<String, List<QSingleDiseaseTakeStatisticDepartmentComparisonChartInDeptVo>> tempMap = qSingleDiseaseStatisticHospitalList.stream().map(q -> {
            QSingleDiseaseTakeStatisticDepartmentComparisonChartInDeptVo build = QSingleDiseaseTakeStatisticDepartmentComparisonChartInDeptVo.builder()
                    .deptName(q.getTqmsDeptName()).yearMonthTitle(q.getYearMonthTitle()).build();
            if ("0".equals(chartType)) {
                build.setNumber(String.valueOf(q.getCompleteReportCount()));
            } else if ("1".equals(chartType)) {
                build.setNumber(String.valueOf(q.getAverageInHospitalDay()));
            } else if ("2".equals(chartType)) {
                build.setNumber(String.valueOf(q.getAverageInHospitalFee()));
            } else if ("3".equals(chartType)) {
                build.setNumber(q.getMortality());
            } else if ("4".equals(chartType)) {
                build.setNumber(q.getOperationComplicationRate());
            }
            return build;
        }).collect(Collectors.toMap(QSingleDiseaseTakeStatisticDepartmentComparisonChartInDeptVo::getYearMonthTitle, Lists::newArrayList,
                (List<QSingleDiseaseTakeStatisticDepartmentComparisonChartInDeptVo> n1, List<QSingleDiseaseTakeStatisticDepartmentComparisonChartInDeptVo> n2) -> {
                    n1.addAll(n2);
                    return n1;
                },TreeMap::new));

        List<QSingleDiseaseTakeStatisticDepartmentComparisonChartVo> list = Lists.newArrayList();
        tempMap.forEach((k, v) -> {
            QSingleDiseaseTakeStatisticDepartmentComparisonChartVo build = QSingleDiseaseTakeStatisticDepartmentComparisonChartVo.builder().yearMonth(k).deptList(v).build();
            if ("3".equals(chartType) || "4".equals(chartType)) {
                build.setRateType("%");
            }
            list.add(build);
        });
        return list;
    }

    @Override
    public List<QSingleDiseaseTakeNumberVo> deptSingleDiseaseNumberList3(QSingleDiseaseTakeNumberListParam qSingleDiseaseTakeNumberListParam) {
        LambdaQueryWrapper<QSingleDiseaseStatisticDept> lambda = new QueryWrapper<QSingleDiseaseStatisticDept>().lambda();
        String[] categoryIdList = qSingleDiseaseTakeNumberListParam.getCategoryIdList();
        if( categoryIdList!=null &&  categoryIdList.length>0){
            lambda.in(QSingleDiseaseStatisticDept::getCategoryId,categoryIdList);
        }
        String[] deptList = qSingleDiseaseTakeNumberListParam.getDeptList();
        if( deptList!=null &&  deptList.length>0){
            lambda.in(QSingleDiseaseStatisticDept::getTqmsDept,deptList);
        }
        lambda.ge(QSingleDiseaseStatisticDept::getYearMonthRemark,qSingleDiseaseTakeNumberListParam.getDateStart());
        lambda.le(QSingleDiseaseStatisticDept::getYearMonthRemark,qSingleDiseaseTakeNumberListParam.getDateEnd());
        lambda.orderByAsc(QSingleDiseaseStatisticDept::getYearMonthRemark);
        List<QSingleDiseaseStatisticDept> list = this.baseMapper.selectList(lambda);

        Map<String, List<QSingleDiseaseTakeNumberListInDeptVo>> resMap = list.stream()
                .map(q -> QSingleDiseaseTakeNumberListVo.builder()
                        .disease(q.getSingleDiseaseName()).deptName(q.getTqmsDeptName()).number(String.valueOf(q.getCompleteReportCount())).build())
                .collect(Collectors.toMap(QSingleDiseaseTakeNumberListVo::getDeptName, q -> {
                            QSingleDiseaseTakeNumberListInDeptVo build = QSingleDiseaseTakeNumberListInDeptVo.builder().disease(q.getDisease()).number(q.getNumber()).build();
                            ArrayList<QSingleDiseaseTakeNumberListInDeptVo> qSingleDiseaseTakeNumberListVos = Lists.newArrayList();
                            qSingleDiseaseTakeNumberListVos.add(build);
                            return qSingleDiseaseTakeNumberListVos;
                        },
                        (List<QSingleDiseaseTakeNumberListInDeptVo> n1, List<QSingleDiseaseTakeNumberListInDeptVo> n2) -> {
                            n1.addAll(n2);
                            return n1;
                        }));

        List<QSingleDiseaseTakeNumberVo> resList = Lists.newArrayList();
        resMap.forEach((k,v)->{
            QSingleDiseaseTakeNumberVo build = QSingleDiseaseTakeNumberVo.builder().deptName(k).numberList(v).build();
            resList.add(build);
        });

        return resList;
    }

    @Override
    public QSingleDiseaseTakeReportStatisticPageVo allSingleDiseaseReportStatisticByDept(QSingleDiseaseTakeReportStatisticByDeptParam qSingleDiseaseTakeReportStatisticByDeptParam, Integer pageNo, Integer pageSize, List<String> deptIdList) {
        Map<String, Object> params = new HashMap<>();
        params.put("startRow", (pageNo - 1) * pageSize);
        params.put("pageSize", pageSize);
        params.put("deptId", qSingleDiseaseTakeReportStatisticByDeptParam.getDeptId());
        params.put("deptIdList", deptIdList);
        String dateStart = qSingleDiseaseTakeReportStatisticByDeptParam.getDateStart();
        String dateEnd = qSingleDiseaseTakeReportStatisticByDeptParam.getDateEnd();
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        Integer total = this.baseMapper.allSingleDiseaseReportStatisticByDeptCount(params);
        List<QSingleDiseaseTakeReportStatisticVo> allSingleDiseaseReportStatisticList = this.baseMapper.allSingleDiseaseReportStatisticByDept(params);

        QSingleDiseaseTakeReportStatisticPageVo qSingleDiseaseTakeReportStatisticPageVo = new QSingleDiseaseTakeReportStatisticPageVo();
        qSingleDiseaseTakeReportStatisticPageVo.setTotal(total);
        qSingleDiseaseTakeReportStatisticPageVo.setQSingleDiseaseTakeList(allSingleDiseaseReportStatisticList);
        return qSingleDiseaseTakeReportStatisticPageVo;
    }

    @Override
    public List<QSingleDiseaseTakeStatisticAnalysisVo> singleDiseaseStatisticAnalysisByDept(QSingleDiseaseTakeStatisticAnalysisByDeptParam qSingleDiseaseTakeStatisticAnalysisByDeptParam) {
        LambdaQueryWrapper<QSingleDiseaseStatisticDept> lambda = new QueryWrapper<QSingleDiseaseStatisticDept>().lambda();
        lambda.in(QSingleDiseaseStatisticDept::getCategoryId,qSingleDiseaseTakeStatisticAnalysisByDeptParam.getCategoryId());
        lambda.ge(QSingleDiseaseStatisticDept::getYearMonthRemark,qSingleDiseaseTakeStatisticAnalysisByDeptParam.getDateStart());
        lambda.le(QSingleDiseaseStatisticDept::getYearMonthRemark,qSingleDiseaseTakeStatisticAnalysisByDeptParam.getDateEnd());
        if(StringUtils.isNotBlank(qSingleDiseaseTakeStatisticAnalysisByDeptParam.getDeptId())){
            lambda.eq(QSingleDiseaseStatisticDept::getTqmsDept,qSingleDiseaseTakeStatisticAnalysisByDeptParam.getDeptId());
            List<QSingleDiseaseStatisticDept> qSingleDiseaseStatisticDeptList = this.baseMapper.selectList(lambda);
            List<QSingleDiseaseTakeStatisticAnalysisVo> qSingleDiseaseTakeStatisticAnalysisVoList = qSingleDiseaseStatisticDeptList.stream().map(e -> QSingleDiseaseTakeStatisticAnalysisVo.builder()
                    .categoryId(e.getCategoryId()).yearMonth(e.getYearMonthTitle())
                    .completeReportCountryCount(e.getCompleteReportCount()).averageInHospitalDay(new BigDecimal(e.getAverageInHospitalDay()))
                    .averageInHospitalFee(new BigDecimal(e.getAverageInHospitalFee())).mortality(e.getMortality()).complicationRate(e.getOperationComplicationRate()).build())
                    .collect(Collectors.toList());
            return qSingleDiseaseTakeStatisticAnalysisVoList;
        }else{
            Map<String, Object> params = new HashMap<>();
            params.put("categoryId",qSingleDiseaseTakeStatisticAnalysisByDeptParam.getCategoryId());
            params.put("deptId", qSingleDiseaseTakeStatisticAnalysisByDeptParam.getDeptId());
            String dateStart = qSingleDiseaseTakeStatisticAnalysisByDeptParam.getDateStart();
            String dateEnd = qSingleDiseaseTakeStatisticAnalysisByDeptParam.getDateEnd();
            params.put("dateStart", dateStart);
            params.put("dateEnd", dateEnd);
            return this.baseMapper.singleDiseaseStatisticAnalysisByDept(params);
        }
    }



}
