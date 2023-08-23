package com.qu.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qu.modules.web.dto.QSingleDiseaseTakeReportStatisticDto;
import com.qu.modules.web.entity.QSingleDiseaseStatisticHospital;
import com.qu.modules.web.param.QSingleDiseaseTakeReportQuantityRankingParam;
import com.qu.modules.web.vo.QSingleDiseaseTakeReportQuantityRankingVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: 单病种统计院级表
 * @Author: jeecg-boot
 * @Date:   2022-04-21
 * @Version: V1.0
 */
public interface QSingleDiseaseStatisticHospitalMapper extends BaseMapper<QSingleDiseaseStatisticHospital> {


    Integer allSingleDiseaseReportStatisticCount(Map<String, Object> params);

    List<QSingleDiseaseTakeReportStatisticDto> allSingleDiseaseReportStatistic(Map<String, Object> params);

    List<QSingleDiseaseTakeReportQuantityRankingVo> singleDiseaseReportQuantityRanking(@Param("qSingleDiseaseTakeReportQuantityRankingParam") QSingleDiseaseTakeReportQuantityRankingParam qSingleDiseaseTakeReportQuantityRankingParam);

    List<QSingleDiseaseTakeReportQuantityRankingVo> singleDiseaseReportWriteRanking(@Param("qSingleDiseaseTakeReportQuantityRankingParam") QSingleDiseaseTakeReportQuantityRankingParam qSingleDiseaseTakeReportQuantityRankingParam);



}
