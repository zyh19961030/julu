package com.qu.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qu.modules.web.entity.QSingleDiseaseTake;
import com.qu.modules.web.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 单病种总表
 * @Author: jeecg-boot
 * @Date: 2021-04-02
 * @Version: V1.0
 */
public interface QSingleDiseaseTakeMapper extends BaseMapper<QSingleDiseaseTake> {

    /*List<QSingleDiseaseTakeVo> singleDiseaseList(@Param("name") String name);*/

    List<QSingleDiseaseTakeStatisticDeptVo> singleDiseaseReportStatistic(Map<String, Object> params);

    Integer countSql(Map<String, Object> params);

    Map<String, Object> countAvgSql(Map<String, Object> countParams);

    Integer countMortalitySql(Map<String, Object> countParams);

    Integer countOperationComplicationSql(Map<String, Object> countParams);

    List<QSingleDiseaseTakeReportStatisticDeptVo> selectDept();

    List<QSingleDiseaseTakeReportStatisticOverviewLineVo> allSingleDiseaseReportStatisticOverviewLine(Map<String, Object> params);

    List<QSingleDiseaseTakeReportStatisticOverviewPieVo> allSingleDiseaseReportStatisticOverviewPie(Map<String, Object> params);

    List<QSingleDiseaseTakeReportStatisticTrendVo> allSingleDiseaseReportStatisticTrend(Map<String, Object> params);

    List<QSingleDiseaseTakeReportStatisticDeptPermutationVo> allSingleDiseaseReportStatisticDeptPermutation(Map<String, Object> params);

    List<QSingleDiseaseTakeReportStatisticSummaryVo> allSingleDiseaseReportStatisticSummary(Map<String, Object> params);

    Integer workbenchReminderNotWriteCount(@Param("dept")String dept);

    Integer workbenchReminderRejectCount(@Param("dept")String dept);

    Integer updateStatusById(Integer id, Integer status, String countryExamineReason, Date time);

    List<ReportFailureRecordVo> queryErrorQuestion(Integer pageNo, Integer pageSize);

    QSingleDiseaseTake queryAnswerJsonByStatus(Integer id);

    List<ReportFailureRecordVo> queryErrorQuestionByName(String name, Integer pageNo, Integer pageSize);

    Integer pageDataCountByName(String name);

    Integer pageDataCount();

    Integer singleDiseaseReportCount(@Param("start")Date start, @Param("end")Date end, @Param("categoryIdList")List<String> categoryIdList);

    List<DepartmentQuantityStatisticsVo> departmentQuantityStatistics(Map<String, Object> params);

    void saveqSingleDiseaseTake(@Param("qSingleDiseaseTake")QSingleDiseaseTake qSingleDiseaseTake);

}
