package com.qu.modules.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.SingleEnterQuestion;
import com.qu.modules.web.param.*;
import com.qu.modules.web.pojo.Data;
import com.qu.modules.web.vo.SingleEnterQuestionEnterQuestionHeadListVo;
import com.qu.modules.web.vo.SingleEnterQuestionInfoVo;
import com.qu.modules.web.vo.SingleEnterQuestionListVo;
import com.qu.modules.web.vo.SingleEnterQuestionQuestionCheckVo;
import org.jeecg.common.api.vo.ResultBetter;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Description: 录入表单表
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface ISingleEnterQuestionService extends IService<SingleEnterQuestion> {

    ResultBetter add(SingleEnterQuestionAddParam param);

    IPage<SingleEnterQuestionListVo> queryPageList(Page<SingleEnterQuestion> page, SingleEnterQuestionListParam param);

    ResultBetter delete(IdIntegerParam param);

    SingleEnterQuestionInfoVo info(String id);

    ResultBetter edit(SingleEnterQuestionUpdateParam param);

    List<SingleEnterQuestion> selectAllByQuestionIdList(List<Integer> questionIdList);

    List<SingleEnterQuestionQuestionCheckVo> startCheckList(QuestionCheckParam questionCheckParam, Data data);

    ResultBetter<SingleEnterQuestionEnterQuestionHeadListVo> enterQuestionHeadList(SingleEnterQuestionEnterQuestionHeadListParam param);

    IPage<LinkedHashMap<String, Object>> enterQuestionDataList(SingleEnterQuestionEnterQuestionListParam param, Integer pageNo, Integer pageSize);

    ResultBetter saveData(String cookie, SingleEnterQuestionSaveDataParam saveParam);

    ResultBetter amendmentSaveData(String cookie, SingleEnterQuestionAmendmentSaveDataParam amendmentSaveDataParam);


}
