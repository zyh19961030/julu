package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.qu.constant.QuestionConstant;
import com.qu.modules.web.dto.QSingleDiseaseTakeReportStatisticDto;
import com.qu.modules.web.entity.QSingleDiseaseStatisticHospital;
import com.qu.modules.web.entity.Question;
import com.qu.modules.web.mapper.QSingleDiseaseStatisticHospitalMapper;
import com.qu.modules.web.mapper.QuestionMapper;
import com.qu.modules.web.param.QSingleDiseaseTakeReportQuantityRankingParam;
import com.qu.modules.web.param.QSingleDiseaseTakeReportStatisticParam;
import com.qu.modules.web.param.QSingleDiseaseTakeStatisticAnalysisParam;
import com.qu.modules.web.service.IQSingleDiseaseStatisticHospitalService;
import com.qu.modules.web.vo.*;
import com.qu.util.DeptUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 单病种统计院级表
 * @Author: jeecg-boot
 * @Date:   2022-04-21
 * @Version: V1.0
 */
@Service
public class QSingleDiseaseStatisticHospitalServiceImpl extends ServiceImpl<QSingleDiseaseStatisticHospitalMapper, QSingleDiseaseStatisticHospital> implements IQSingleDiseaseStatisticHospitalService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public QSingleDiseaseTakeReportStatisticPageVo allSingleDiseaseReportStatistic(QSingleDiseaseTakeReportStatisticParam qSingleDiseaseTakeReportStatisticParam, Integer pageNo, Integer pageSize, String deptId, String type) {

        QSingleDiseaseTakeReportStatisticPageVo qSingleDiseaseTakeReportStatisticPageVo = new QSingleDiseaseTakeReportStatisticPageVo();

        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_SINGLE_DISEASE);
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        if(DeptUtil.isClinical(type)){
            lambda.like(Question::getDeptIds,deptId);
        }else{
            lambda.like(Question::getSeeDeptIds,deptId);
        }
        //科室匹配 按医生填报查询-本科室单病种上报记录-全院单病种上报统计-科室单病种上报统计-单病种指标统计-病种名称筛选条件
        List<Question> questionList = questionMapper.selectList(lambda);
        ArrayList<String> questionCategoryIdList = Lists.newArrayList();
        if(questionList.isEmpty()){
            qSingleDiseaseTakeReportStatisticPageVo.setTotal(0L);
            qSingleDiseaseTakeReportStatisticPageVo.setQSingleDiseaseTakeList(Lists.newArrayList());
            return qSingleDiseaseTakeReportStatisticPageVo;
        }else{
            for (Question question : questionList) {
                questionCategoryIdList.add(question.getCategoryId());
            }
        }
        List<String> categoryIdList = Lists.newArrayList();
        String[] categoryId = qSingleDiseaseTakeReportStatisticParam.getCategoryId();
        if (categoryId == null || categoryId.length<=0) {
            categoryIdList = questionCategoryIdList;
        } else {
            HashSet<String> categoryIdParamSet = Sets.newHashSet(categoryId);
            HashSet<String> categoryIdQuestionSet = Sets.newHashSet(questionCategoryIdList);

            categoryIdList = getIntersectionSetByGuava(categoryIdParamSet, categoryIdQuestionSet);
        }

        if(categoryIdList.isEmpty()){
            qSingleDiseaseTakeReportStatisticPageVo.setTotal(0L);
            qSingleDiseaseTakeReportStatisticPageVo.setQSingleDiseaseTakeList(Lists.newArrayList());
            return qSingleDiseaseTakeReportStatisticPageVo;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("startRow", (pageNo - 1) * pageSize);
        params.put("pageSize", pageSize);
        params.put("categoryId", categoryIdList);
        String dateStart = qSingleDiseaseTakeReportStatisticParam.getDateStart();
        String dateEnd = qSingleDiseaseTakeReportStatisticParam.getDateEnd();
//        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM");
//        DateTime startDateTime = dateTimeFormatter.parseDateTime(dateStart);
//        startDateTime = startDateTime.dayOfMonth().withMinimumValue();
//        DateTime endDateTime = dateTimeFormatter.parseDateTime(dateEnd);
//        endDateTime = endDateTime.dayOfMonth().withMaximumValue().plusDays(1);

        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        Integer total = this.baseMapper.allSingleDiseaseReportStatisticCount(params);
        List<QSingleDiseaseTakeReportStatisticDto> allSingleDiseaseReportStatisticList = this.baseMapper.allSingleDiseaseReportStatistic(params);
        ArrayList<QSingleDiseaseTakeReportStatisticVo> resList = Lists.newArrayList();

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        for (QSingleDiseaseTakeReportStatisticDto qSingleDiseaseTakeReportStatisticDto : allSingleDiseaseReportStatisticList) {
            QSingleDiseaseTakeReportStatisticVo vo = new QSingleDiseaseTakeReportStatisticVo();
            BeanUtils.copyProperties(qSingleDiseaseTakeReportStatisticDto,vo);

            Integer completeReportCount = qSingleDiseaseTakeReportStatisticDto.getCompleteReportCount();
            if(completeReportCount==0){
                vo.setCompleteReportCountryRate("0.00%");
                vo.setAverageInHospitalDay("0.0000");
                vo.setAverageInHospitalFee("0.00");
                vo.setAverageDrugFee("0.00");
                vo.setAverageOperationTreatmentFee("0.00");
                vo.setAverageDisposableConsumable("0.00");
                vo.setMortality("0.00%");
                vo.setOperationComplicationRate("0.00%");
            }else{
                Integer needReportCount = qSingleDiseaseTakeReportStatisticDto.getNeedReportCount();
                String completeReportCountryRate = numberFormat.format((float) completeReportCount / (float) needReportCount * 100);
                vo.setCompleteReportCountryRate(completeReportCountryRate+"%");

                Integer sumInHospitalDay = qSingleDiseaseTakeReportStatisticDto.getSumInHospitalDay();
                String averageInHospitalDay = numberFormat.format((float) sumInHospitalDay / (float) completeReportCount);
                vo.setAverageInHospitalDay(averageInHospitalDay);

                BigDecimal sumInHospitalFee = qSingleDiseaseTakeReportStatisticDto.getSumInHospitalFee();
                sumInHospitalFee=sumInHospitalFee.divide(new BigDecimal(completeReportCount),2,BigDecimal.ROUND_HALF_UP);
                String averageInHospitalFee = sumInHospitalFee.toPlainString();
                vo.setAverageInHospitalFee(averageInHospitalFee);

                BigDecimal sumDrugFee = qSingleDiseaseTakeReportStatisticDto.getSumDrugFee();
                sumDrugFee=sumDrugFee.divide(new BigDecimal(completeReportCount),2,BigDecimal.ROUND_HALF_UP);
                String averageDrugFee = sumDrugFee.toPlainString();
                vo.setAverageDrugFee(averageDrugFee);

                BigDecimal sumOperationTreatmentFee = qSingleDiseaseTakeReportStatisticDto.getSumOperationTreatmentFee();
                sumOperationTreatmentFee=sumOperationTreatmentFee.divide(new BigDecimal(completeReportCount),2,BigDecimal.ROUND_HALF_UP);
                String averageOperationTreatmentFee = sumOperationTreatmentFee.toPlainString();
                vo.setAverageOperationTreatmentFee(averageOperationTreatmentFee);

                BigDecimal sumDisposableConsumable = qSingleDiseaseTakeReportStatisticDto.getSumDisposableConsumable();
                sumDisposableConsumable=sumDisposableConsumable.divide(new BigDecimal(completeReportCount),2,BigDecimal.ROUND_HALF_UP);
                String averageDisposableConsumable = sumDisposableConsumable.toPlainString();
                vo.setAverageDisposableConsumable(averageDisposableConsumable);

                Integer mortalityCount = qSingleDiseaseTakeReportStatisticDto.getMortalityCount();
                if(mortalityCount==0){
                    vo.setMortality("0.00%");
                }else{
                    String mortalityString = numberFormat.format((float) mortalityCount / (float) completeReportCount * 100);
                    vo.setMortality(mortalityString+"%");
                }

                Integer operationComplicationCount = qSingleDiseaseTakeReportStatisticDto.getOperationComplicationRateCount();
                if(operationComplicationCount==0){
                    vo.setOperationComplicationRate("0.00%");
                }else{
                    String operationComplicationString = numberFormat.format((float) operationComplicationCount / (float) completeReportCount * 100);
                    vo.setOperationComplicationRate(operationComplicationString+"%");
                }
            }


            resList.add(vo);
        }

        qSingleDiseaseTakeReportStatisticPageVo.setTotal(total);
        qSingleDiseaseTakeReportStatisticPageVo.setQSingleDiseaseTakeList(resList);
        return qSingleDiseaseTakeReportStatisticPageVo;
    }

    private static List<String> getIntersectionSetByGuava(Set<String> setOne, Set<String> setTwo) {
        Set<String> differenceSet = Sets.intersection(setOne, setTwo);
        return Lists.newArrayList(differenceSet);
    }

//    {
//        Page<QSingleDiseaseStatisticHospital> page = new Page<>(pageNo, pageSize);
//        QSingleDiseaseTakeReportStatisticPageVo qSingleDiseaseTakeReportStatisticPageVo = new QSingleDiseaseTakeReportStatisticPageVo();
//        LambdaQueryWrapper<QSingleDiseaseStatisticHospital> lambda = new QueryWrapper<QSingleDiseaseStatisticHospital>().lambda();
//        String[] categoryIdList = qSingleDiseaseTakeReportStatisticParam.getCategoryId();
//        if(categoryIdList.length>0){
//            lambda.in(QSingleDiseaseStatisticHospital::getCategoryId,qSingleDiseaseTakeReportStatisticParam.getCategoryId());
//        }
//        lambda.ge(QSingleDiseaseStatisticHospital::getYearMonthRemark,qSingleDiseaseTakeReportStatisticParam.getDateStart());
//        lambda.le(QSingleDiseaseStatisticHospital::getYearMonthRemark,qSingleDiseaseTakeReportStatisticParam.getDateEnd());
//        lambda.groupBy()
//        IPage<QSingleDiseaseStatisticHospital> qSingleDiseaseStatisticHospitalIPage = this.page(page, lambda);
//        qSingleDiseaseTakeReportStatisticPageVo.setTotal(qSingleDiseaseStatisticHospitalIPage.getTotal());
//        qSingleDiseaseTakeReportStatisticPageVo.setQSingleDiseaseTakeList(qSingleDiseaseStatisticHospitalIPage.getRecords());
//        return qSingleDiseaseTakeReportStatisticPageVo;
//    }

    @Override
    public List<QSingleDiseaseTakeReportQuantityRankingVo> singleDiseaseReportQuantityRanking(QSingleDiseaseTakeReportQuantityRankingParam qSingleDiseaseTakeReportQuantityRankingParam) {
        return this.baseMapper.singleDiseaseReportQuantityRanking(qSingleDiseaseTakeReportQuantityRankingParam);
    }

    @Override
    public List<QSingleDiseaseTakeReportQuantityRankingVo> singleDiseaseReportWriteRanking(QSingleDiseaseTakeReportQuantityRankingParam qSingleDiseaseTakeReportQuantityRankingParam) {
        return this.baseMapper.singleDiseaseReportWriteRanking(qSingleDiseaseTakeReportQuantityRankingParam);
    }

    @Override
    public List<QSingleDiseaseTakeStatisticAnalysisVo> singleDiseaseStatisticAnalysis(QSingleDiseaseTakeStatisticAnalysisParam qSingleDiseaseTakeStatisticAnalysisParam) {
        LambdaQueryWrapper<QSingleDiseaseStatisticHospital> lambda = new QueryWrapper<QSingleDiseaseStatisticHospital>().lambda();
        lambda.in(QSingleDiseaseStatisticHospital::getCategoryId,qSingleDiseaseTakeStatisticAnalysisParam.getCategoryId());
        lambda.ge(QSingleDiseaseStatisticHospital::getYearMonthRemark,qSingleDiseaseTakeStatisticAnalysisParam.getDateStart());
        lambda.le(QSingleDiseaseStatisticHospital::getYearMonthRemark,qSingleDiseaseTakeStatisticAnalysisParam.getDateEnd());
        List<QSingleDiseaseStatisticHospital> qSingleDiseaseStatisticHospitalList = this.baseMapper.selectList(lambda);
        List<QSingleDiseaseTakeStatisticAnalysisVo> qSingleDiseaseTakeStatisticAnalysisVoList = qSingleDiseaseStatisticHospitalList.stream().map(e -> QSingleDiseaseTakeStatisticAnalysisVo.builder()
                .categoryId(e.getCategoryId()).yearMonth(e.getYearMonthTitle())
                .completeReportCountryCount(e.getCompleteReportCount()).averageInHospitalDay(new BigDecimal(e.getAverageInHospitalDay()))
                .averageInHospitalFee(new BigDecimal(e.getAverageInHospitalFee())).mortality(e.getMortality()).complicationRate(e.getOperationComplicationRate()).build())
            .collect(Collectors.toList());

        return qSingleDiseaseTakeStatisticAnalysisVoList;
    }




}
