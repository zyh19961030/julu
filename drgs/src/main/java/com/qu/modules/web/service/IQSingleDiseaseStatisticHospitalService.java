package com.qu.modules.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.QSingleDiseaseStatisticHospital;
import com.qu.modules.web.param.QSingleDiseaseTakeReportQuantityRankingParam;
import com.qu.modules.web.param.QSingleDiseaseTakeReportStatisticParam;
import com.qu.modules.web.param.QSingleDiseaseTakeStatisticAnalysisParam;
import com.qu.modules.web.vo.QSingleDiseaseTakeReportQuantityRankingVo;
import com.qu.modules.web.vo.QSingleDiseaseTakeReportStatisticPageVo;
import com.qu.modules.web.vo.QSingleDiseaseTakeStatisticAnalysisVo;

import java.util.List;

/**
 * @Description: 单病种统计院级表
 * @Author: jeecg-boot
 * @Date:   2022-04-21
 * @Version: V1.0
 */
public interface IQSingleDiseaseStatisticHospitalService extends IService<QSingleDiseaseStatisticHospital> {


    QSingleDiseaseTakeReportStatisticPageVo allSingleDiseaseReportStatistic(QSingleDiseaseTakeReportStatisticParam qSingleDiseaseTakeReportStatisticParam, Integer pageNo, Integer pageSize, String deptId, String type);

    List<QSingleDiseaseTakeReportQuantityRankingVo> singleDiseaseReportQuantityRanking(QSingleDiseaseTakeReportQuantityRankingParam qSingleDiseaseTakeReportQuantityRankingParam);

    List<QSingleDiseaseTakeReportQuantityRankingVo> singleDiseaseReportWriteRanking(QSingleDiseaseTakeReportQuantityRankingParam qSingleDiseaseTakeReportQuantityRankingParam);

    List<QSingleDiseaseTakeStatisticAnalysisVo> singleDiseaseStatisticAnalysis(QSingleDiseaseTakeStatisticAnalysisParam qSingleDiseaseTakeStatisticAnalysisParam);
}
