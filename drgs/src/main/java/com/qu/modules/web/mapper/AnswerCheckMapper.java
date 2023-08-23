package com.qu.modules.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qu.modules.web.entity.AnswerCheck;
import com.qu.modules.web.param.CheckQuestionCountStatisticParam;
import com.qu.modules.web.vo.CheckQuestionCountStatisticVo;

/**
 * @Description: 检查表问卷总表
 * @Author: jeecg-boot
 * @Date:   2022-07-30
 * @Version: V1.0
 */
public interface AnswerCheckMapper extends BaseMapper<AnswerCheck> {

    List<CheckQuestionCountStatisticVo> checkQuestionCountStatistic(@Param("param") CheckQuestionCountStatisticParam statisticParam);

}
