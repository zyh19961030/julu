package com.qu.modules.web.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.QSingleDiseaseStatisticDept;
import com.qu.modules.web.param.QSingleDiseaseTakeNumberListParam;
import com.qu.modules.web.param.QSingleDiseaseTakeReportStatisticByDeptParam;
import com.qu.modules.web.param.QSingleDiseaseTakeStatisticAnalysisByDeptParam;
import com.qu.modules.web.param.QSingleDiseaseTakeStatisticAnalysisParam;
import com.qu.modules.web.param.QSingleDiseaseTakeStatisticDepartmentComparisonChartParam;
import com.qu.modules.web.vo.QSingleDiseaseTakeNumberVo;
import com.qu.modules.web.vo.QSingleDiseaseTakeReportStatisticPageVo;
import com.qu.modules.web.vo.QSingleDiseaseTakeStatisticAnalysisVo;
import com.qu.modules.web.vo.QSingleDiseaseTakeStatisticDepartmentComparisonChartVo;
import com.qu.modules.web.vo.QSingleDiseaseTakeStatisticDepartmentComparisonVo;

/**
 * @Description: 单病种统计表
 * @Author: jeecg-boot
 * @Date:   2022-04-12
 * @Version: V1.0
 */
public interface IQSingleDiseaseStatisticDeptService extends IService<QSingleDiseaseStatisticDept> {

    void processData(String startDateString,String endDateString);

    void statisticLastMonth();

    List<QSingleDiseaseTakeStatisticDepartmentComparisonVo> singleDiseaseStatisticDepartmentComparison(QSingleDiseaseTakeStatisticAnalysisParam qSingleDiseaseTakeStatisticAnalysisParam);

    List<QSingleDiseaseTakeStatisticDepartmentComparisonChartVo> singleDiseaseStatisticDepartmentComparisonChart(QSingleDiseaseTakeStatisticDepartmentComparisonChartParam qSingleDiseaseTakeStatisticDepartmentComparisonChartParam);

    List<QSingleDiseaseTakeNumberVo> deptSingleDiseaseNumberList3(QSingleDiseaseTakeNumberListParam qSingleDiseaseTakeNumberListParam);

    QSingleDiseaseTakeReportStatisticPageVo allSingleDiseaseReportStatisticByDept(QSingleDiseaseTakeReportStatisticByDeptParam qSingleDiseaseTakeReportStatisticByDeptParam, Integer pageNo, Integer pageSize, List<String> deptIdList);

    List<QSingleDiseaseTakeStatisticAnalysisVo> singleDiseaseStatisticAnalysisByDept(QSingleDiseaseTakeStatisticAnalysisByDeptParam qSingleDiseaseTakeStatisticAnalysisByDeptParam);


}
