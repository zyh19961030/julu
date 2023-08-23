package com.qu.modules.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.Question;
import com.qu.modules.web.entity.TbDep;
import com.qu.modules.web.param.*;
import com.qu.modules.web.pojo.Data;
import com.qu.modules.web.pojo.TbUser;
import com.qu.modules.web.vo.*;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.api.vo.ResultBetter;

import java.util.List;

/**
 * @Description: 问卷表
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
public interface IQuestionService extends IService<Question> {

    Question saveQuestion(QuestionParam questionParam, TbUser tbUser);

    QuestionVo queryById(Integer id);

    /**
     * 新的查询题目接口，增加数据id入参
     * @param param
     * @return
     */
    QuestionVo queryByIdNew(QuestionQueryByIdParam param,Integer type);

    /**
     * 后续可能应该删除,使用queryByIdNew
     * @param param
     * @return
     */
    QuestionVo answerCheckQueryById(QuestionQueryByIdParam param);

    /**
     * 后续可能应该删除,使用queryByIdNew
     * @param param
     * @return
     */
    QuestionVo singleDiseaseQueryById(QuestionQueryByIdParam param);

    List<ViewNameVo> queryByViewName(QuestionCheckedDepParam viewName);

    Question updateQuestionById(QuestionEditParam questionEditParam, TbUser tbUser);

    ResultBetter<Boolean> release(IdParam idParam, TbUser tbUser);

    boolean removeQuestionById(Integer id, TbUser tbUser);

    QuestionAndCategoryPageVo queryPageList(QuestionListParam questionListParam, Integer pageNo, Integer pageSize);

    QuestionPageVo questionFillInList(QuestionParam questionParam, Integer pageNo, Integer pageSize);

    IPage<QuestionCheckVo> checkQuestionList(QuestionCheckParam questionCheckParam, Integer pageNo, Integer pageSize,Data data);

    List<QuestionCheckClassificationVo> checkQuestionClassificationList(QuestionCheckParam questionCheckParam, Data data);

    List<QuestionCheckVo> checkQuestionClassificationSubsetList(QuestionCheckClassificationParam param, Data data);

    List<QuestionNameVo> queryCheckQuestionClassification(String name, Data data);

    List<QuestionNameVo> queryCheckQuestionClassificationByUpdate(QueryCheckQuestionClassificationByUpdateParam param, Data data);

    List<QuestionStatisticsCheckVo> statisticsCheckList(QuestionCheckParam questionParam);

    List<CheckQuestionHistoryStatisticVo> checkQuestionHistoryStatisticList(QuestionCheckParam questionCheckParam,Data data);

    List<CheckQuestionHistoryStatisticClassificationVo> checkQuestionHistoryStatisticClassificationList(QuestionCheckParam questionCheckParam,Data data);

    List<CheckQuestionHistoryStatisticVo> checkQuestionHistoryStatisticClassificationSubsetList(QuestionCheckClassificationParam questionCheckClassificationParam,Data data);

    /**
     * 检查管理_参数设置列表
     *
     * @param questionCheckParam
     * @param data
     * @return
     */
    List<CheckQuestionParameterSetListVo> checkQuestionParameterSetList(QuestionCheckParam questionCheckParam,Data data);

    /**
     * 检查管理_历史统计列表_上级督查_填报记录和检查明细_被检查科室筛选条件
     * @param deptListParam
     * @param data
     * @return
     */
    List<CheckQuestionHistoryStatisticDeptListDeptVo> checkQuestionHistoryStatisticInspectedDeptList(CheckQuestionHistoryStatisticDeptListParam deptListParam, Data data);

    /**
     * 检查管理_历史统计列表_上级督查_填报记录和检查明细_检查科室筛选条件
     * @param deptListParam
     * @param data
     * @return
     */
    List<CheckQuestionHistoryStatisticDeptListDeptVo> checkQuestionHistoryStatisticDeptList(CheckQuestionHistoryStatisticDeptListParam deptListParam, Data data);

    /**
     * 检查管理_历史统计列表_科室自查_填报记录和检查明细_自查科室筛选条件
     * @param deptListParam
     * @param data
     * @return
     */
    List<CheckQuestionHistoryStatisticDeptListDeptVo> checkQuestionHistoryStatisticSelfDeptList(CheckQuestionHistoryStatisticDeptListParam deptListParam, Data data);

    void updateDeptIdsParam(UpdateDeptIdsParam updateDeptIdsParam);

    void updateSeeDeptIdsParam(UpdateDeptIdsParam updateDeptIdsParam);

    void updateCheckedDeptIdsParam(UpdateCheckedDeptIdsParam updateCheckedDeptIdsParam);

    List<String> selectCheckedDeptIdsParam(SelectCheckedDeptIdsParam selectCheckedDeptIdsParam);

    void updateResponsibilityUserIdsParam(String deptId,UpdateResponsibilityUserIdsParam param);

    List<String> selectResponsibilityUserIdsParam(String deptId,SelectResponsibilityUserIdsParam param);

    QuestionVo queryPersonById(Integer id);

    void updateCategoryIdParam(UpdateCategoryIdParam updateCategoryIdParam);

    void updateTemplateIdIdParam(UpdateTemplateIdParam param);

    void updateCheckProjectIdParam(UpdateCheckProjectIdParam param);

    Boolean againRelease(QuestionAgainReleaseParam questionAgainreleaseParam);

    Result<?> generateTraceability(String id);

    List<Question> queryQuestionByInput(String name);

    void updateWriteFrequencyIdsParam(UpdateWriteFrequencyIdsParam updateWriteFrequencyIdsParam);

    void updateQuestionIcon(UpdateQuestionIconParam updateQuestionIconParam);

    List<QuestionPatientCreateListVo> patientCreateList(String name, String deptId);

    List<QuestionMonthQuarterYearCreateListVo> monthQuarterYearCreateList(String type, String deptId);

    List<QuestionMonthQuarterYearCreateListVo> registerCreateList(String deptId);

    List<TbDep> singleDiseaseStatisticAnalysisByDeptCondition(QSingleDiseaseTakeStatisticAnalysisByDeptConditionParam qSingleDiseaseTakeStatisticAnalysisByDeptConditionParam, String deptId, String type);

    List<String> selectSingleDiseaseDeptIdList(String categoryId);


    IPage<QuestionMiniAppPageVo> queryPageListByMiniApp(String deptId,String userId, Integer pageNo, Integer pageSize);

    IPage<QuestionMiniAppPageVo> answerQueryPageListByMiniApp(String deptId, String userId, Integer pageNo, Integer pageSize);

    /**
     * CheckDetailSet检查表设置列时使用
     * @param id
     * @return
     */
    List<SubjectVo> queryQuestionSubjectById(Integer id);

    ResultBetter<QuestionSetColumnVo> setColumn(Data data);

    List<Question> getCheckByIds(List<Integer> quIdList);

    Result<?> copyQuestion(CopyQuestionParam copyQuestionParam, Data data);

    List<QuestionNameVo> queryByQuestionName(String name);

    List<QuestionNameVo> queryCheckQuestion(String name, String deptId);

    List<QuestionNameVo> enterQuestionSelectRegister(String name);



}
