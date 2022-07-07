package com.qu.modules.web.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.qu.constant.Constant;
import com.qu.constant.QoptionConstant;
import com.qu.constant.QuestionConstant;
import com.qu.modules.web.entity.Qoption;
import com.qu.modules.web.entity.Qsubject;
import com.qu.modules.web.entity.Question;
import com.qu.modules.web.entity.TbDep;
import com.qu.modules.web.entity.TqmsQuotaCategory;
import com.qu.modules.web.mapper.DynamicTableMapper;
import com.qu.modules.web.mapper.OptionMapper;
import com.qu.modules.web.mapper.QsubjectMapper;
import com.qu.modules.web.mapper.QuestionMapper;
import com.qu.modules.web.mapper.TqmsQuotaCategoryMapper;
import com.qu.modules.web.param.QSingleDiseaseTakeStatisticAnalysisByDeptConditionParam;
import com.qu.modules.web.param.QuestionAgainReleaseParam;
import com.qu.modules.web.param.QuestionEditParam;
import com.qu.modules.web.param.QuestionParam;
import com.qu.modules.web.param.UpdateCategoryIdParam;
import com.qu.modules.web.param.UpdateDeptIdsParam;
import com.qu.modules.web.param.UpdateWriteFrequencyIdsParam;
import com.qu.modules.web.pojo.TbUser;
import com.qu.modules.web.service.IQuestionService;
import com.qu.modules.web.service.ITbDepService;
import com.qu.modules.web.vo.QuestionAndCategoryPageVo;
import com.qu.modules.web.vo.QuestionAndCategoryVo;
import com.qu.modules.web.vo.QuestionMonthQuarterYearCreateListVo;
import com.qu.modules.web.vo.QuestionPageVo;
import com.qu.modules.web.vo.QuestionPatientCreateListVo;
import com.qu.modules.web.vo.QuestionVo;
import com.qu.modules.web.vo.SubjectVo;
import com.qu.util.DeptUtil;
import com.qu.util.IntegerUtil;
import com.qu.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 问卷表
 * @Author: jeecg-boot
 * @Date: 2021-03-19
 * @Version: V1.0
 */
@Slf4j
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QsubjectMapper subjectMapper;

    @Autowired
    private OptionMapper optionMapper;

    @Autowired
    private DynamicTableMapper dynamicTableMapper;

    @Autowired
    private TqmsQuotaCategoryMapper tqmsQuotaCategoryMapper;

    @Autowired
    private ITbDepService tbDepService;

    @Override
    public Question saveQuestion(QuestionParam questionParam, TbUser tbUser) {
        Question question = new Question();
        try {
            BeanUtils.copyProperties(questionParam, question);
            question.setQuStatus(QuestionConstant.QU_STATUS_DRAFT);
            question.setQuStop(QuestionConstant.QU_STOP_NORMAL);
            question.setDel(QuestionConstant.DEL_NORMAL);
            Date date = new Date();
            String userId = tbUser.getId();
            question.setCreater(userId);
            question.setCreateTime(date);
            question.setUpdater(userId);
            question.setUpdateTime(date);
            questionMapper.insert(question);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return question;
    }

    @Override
    public QuestionVo queryById(Integer id) {
        QuestionVo questionVo = new QuestionVo();

        Question question = questionMapper.selectById(id);
        BeanUtils.copyProperties(question, questionVo);
        List<Qsubject> subjectList = subjectMapper.selectSubjectByQuId(id);
        if(subjectList.isEmpty()){
            return questionVo;
        }

        List<Integer> collect = subjectList.stream().map(Qsubject::getId).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<Qoption> lambda = new QueryWrapper<Qoption>().lambda();
        lambda.in(Qoption::getSubId,collect);
        lambda.in(Qoption::getDel, QoptionConstant.DEL_NORMAL);
        lambda.orderByAsc(Qoption::getOpOrder);
        List<Qoption> qoptions = optionMapper.selectList(lambda);

        Map<Integer, ArrayList<Qoption>> optionMap = qoptions.stream().collect(Collectors.toMap(Qoption::getSubId, Lists::newArrayList, (ArrayList<Qoption> k1, ArrayList<Qoption> k2) -> {
            k1.addAll(k2);
            return k1;
        }));

        List<SubjectVo> subjectVoList = new ArrayList<>();
        ArrayList<Qoption> optionEmptyList = Lists.newArrayList();
        for (Qsubject subject : subjectList) {
            SubjectVo subjectVo = new SubjectVo();
            BeanUtils.copyProperties(subject, subjectVo);
//                List<Qoption> qoptionList = optionMapper.selectQoptionBySubId(subject.getId());
            ArrayList<Qoption> qoptionsList = optionMap.get(subject.getId());
            subjectVo.setOptionList(qoptionsList==null?optionEmptyList:qoptionsList);
            subjectVoList.add(subjectVo);
        }
        //开始组装分组题逻辑
        //先缓存
        Map<Integer, SubjectVo> mapCache = new HashMap<>();
        for (SubjectVo subjectVo : subjectVoList) {
            mapCache.put(subjectVo.getId(), subjectVo);
        }
        //开始算
        StringBuffer groupIdsAll = new StringBuffer();
        for (SubjectVo subjectVo : subjectVoList) {
            //如果是分组题
            if (subjectVo.getSubType().equals("8")) {
                String groupIds = subjectVo.getGroupIds();//包含题号
                if (null != groupIds) {
                    String[] gids = groupIds.split(",");
                    List<SubjectVo> subjectVoGroupList = new ArrayList<>();
                    for (String subId : gids) {
                        groupIdsAll.append(subId);
                        groupIdsAll.append(",");
                        if (!StringUtil.isEmpty(subId)) {
                            SubjectVo svo = mapCache.get(Integer.parseInt(subId));
                            subjectVoGroupList.add(svo);
                        }
                    }
                    //设置到分组题对象列表
                    subjectVo.setSubjectVoList(subjectVoGroupList);
                }
            }
        }
        //删除在subjectVoList集合中删除groupIdsAll包含的题
        if (groupIdsAll.length() != 0) {
            String removeIds = groupIdsAll.toString();
            String[] remIds = removeIds.split(",");
            if (null != remIds && remIds.length > 0) {
                for (int i = 0; i < subjectVoList.size(); i++) {
                    //for (SubjectVo subjectVo : subjectVoList) {
                    SubjectVo subjectVo = subjectVoList.get(i);
                    Integer nowId = subjectVo.getId();
                    for (String remId : remIds) {
                        if (!IntegerUtil.isNull(nowId) && !StringUtil.isEmpty(remId)) {
                            if (nowId == Integer.parseInt(remId)) {
                                subjectVoList.remove(i);//移除
                                i--;
                            }
                        }
                    }

                }
            }
        }
        questionVo.setSubjectVoList(subjectVoList);

        return questionVo;
    }

    @Override
    public Question updateQuestionById(QuestionEditParam questionEditParam, TbUser tbUser) {
        Question question = new Question();
        try {
            BeanUtils.copyProperties(questionEditParam, question);
            String userId = tbUser.getId();
            question.setUpdater(userId);
            question.setUpdateTime(new Date());
            questionMapper.updateById(question);
            question = questionMapper.selectById(questionEditParam.getId());
            //如果是已发布，建表
            if (question.getQuStatus() == 1) {
                StringBuffer sql = new StringBuffer();
                sql.append("CREATE TABLE `" + question.getTableName() + "` (");
                sql.append("`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',");
                List<Qsubject> subjectList = subjectMapper.selectSubjectByQuId(questionEditParam.getId());
                for (Qsubject qsubject : subjectList) {
                    Integer limitWords = qsubject.getLimitWords();
                    if(limitWords==null || limitWords==0){
                        limitWords=50;
                    }
                    String subType = qsubject.getSubType();
                    Integer del = qsubject.getDel();
                    if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType) || QuestionConstant.DEL_DELETED.equals(del)) {
                        continue;
                    }
                    sql.append("`")
                            .append(qsubject.getColumnName())
                            .append("` ")
                            .append(qsubject.getColumnTypeDatabase()==null?"varchar":qsubject.getColumnTypeDatabase())
                            .append("(")
                            .append(limitWords)
                            .append(") COMMENT '")
                            .append(qsubject.getSubName())
                            .append("',");
                }
                sql.append(" `answer_datetime` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '填报时间',");
                sql.append(" `tbksmc` varchar(255) NULL COMMENT '填报科室名称',");
                sql.append(" `tbksdm` varchar(255) NULL COMMENT '填报科室代码',");
                sql.append(" `summary_mapping_table_id` varchar(255) NULL COMMENT '对应总表的id，可以当主键',");
                sql.append(" PRIMARY KEY (`id`)");
                if(subjectList.size()>=50){
                    sql.append(") ENGINE=MyISAM DEFAULT CHARSET=utf8;");
                }else{
                    sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
                }
                dynamicTableMapper.createDynamicTable(sql.toString());
            }
        } catch (Exception e) {
            question = null;
            log.error(e.getMessage(), e);
        }
        return question;
    }

    @Override
    public boolean removeQuestionById(Integer id, TbUser tbUser) {
        boolean delFlag = true;
        try {
            Question question = new Question();
            question.setId(id);
            String userId = tbUser.getId();
            question.setDel(QuestionConstant.DEL_DELETED);
            question.setUpdater(userId);
            question.setUpdateTime(new Date());
            questionMapper.updateById(question);
        } catch (Exception e) {
            delFlag = false;
            log.error(e.getMessage(), e);
        }
        return delFlag;
    }

    @Override
    public QuestionAndCategoryPageVo queryPageList(QuestionParam questionParam, Integer pageNo, Integer pageSize) {
        QuestionAndCategoryPageVo questionAndCategoryPageVo = new QuestionAndCategoryPageVo();
        QueryWrapper<TqmsQuotaCategory> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("is_single_disease", TqmsQuotaCategoryConstant.IS_SINGLE_DISEASE);
        List<TqmsQuotaCategory> quotaCategoryList = tqmsQuotaCategoryMapper.selectList(queryWrapper);
        Map<Long, String> quotaCategoryMap = quotaCategoryList.stream().collect(Collectors.toMap(TqmsQuotaCategory::getCategoryId, TqmsQuotaCategory::getCategoryName, (k1, k2) -> k1));

//        Integer i = 9;
//        i.longValue()

        Map<String, Object> params = new HashMap<>();
        params.put("quName", questionParam.getQuName());
        params.put("quDesc", questionParam.getQuDesc());
        params.put("startRow", (pageNo - 1) * pageSize);
        params.put("pageSize", pageSize);
        int total = questionMapper.queryPageListCount(params);
        List<QuestionAndCategoryVo> questionList = questionMapper.queryPageList(params);
        for (QuestionAndCategoryVo questionAndCategoryVo : questionList) {
            String categoryId = questionAndCategoryVo.getCategoryId();
            if (StringUtils.isNotBlank(categoryId)) {
                questionAndCategoryVo.setCategoryName(quotaCategoryMap.get(Long.parseLong(categoryId)));
            }
        }
        questionAndCategoryPageVo.setTotal(total);
        questionAndCategoryPageVo.setQuestionList(questionList);
        return questionAndCategoryPageVo;
    }

    @Override
    public QuestionPageVo questionFillInList(QuestionParam questionParam, Integer pageNo, Integer pageSize) {
        QuestionPageVo questionPageVo = new QuestionPageVo();
        Map<String, Object> params = new HashMap<>();
        params.put("quName", questionParam.getQuName());
        params.put("quDesc", questionParam.getQuDesc());
        params.put("startRow", (pageNo - 1) * pageSize);
        params.put("pageSize", pageSize);
        int total = questionMapper.questionFillInListCount(params);
        List<Question> questionList = questionMapper.questionFillInList(params);
        questionPageVo.setTotal(total);
        questionPageVo.setQuestionList(questionList);
        return questionPageVo;
    }

    @Override
    public void updateDeptIdsParam(UpdateDeptIdsParam updateDeptIdsParam) {
        String[] quIds = updateDeptIdsParam.getQuIds();
        String[] deptIds = updateDeptIdsParam.getDeptIds();
        if (quIds != null && deptIds != null) {
            StringBuffer deptid = new StringBuffer();
            for (String did : deptIds) {
                deptid.append(did);
                deptid.append(",");
            }
            ///更新
            for (String qid : quIds) {
                Question question = new Question();
                question.setId(Integer.parseInt(qid));
                question.setDeptIds(deptid.toString());
                question.setUpdateTime(new Date());
                questionMapper.updateById(question);
            }
        }
    }

    @Override
    public void updateSeeDeptIdsParam(UpdateDeptIdsParam updateDeptIdsParam) {
        String[] quIds = updateDeptIdsParam.getQuIds();
        String[] deptIds = updateDeptIdsParam.getDeptIds();
        if (quIds != null && deptIds != null) {
            StringBuffer deptid = new StringBuffer();
            for (String did : deptIds) {
                deptid.append(did);
                deptid.append(",");
            }
            ///更新
            for (String qid : quIds) {
                Question question = new Question();
                question.setId(Integer.parseInt(qid));
                question.setSeeDeptIds(deptid.toString());
                question.setUpdateTime(new Date());
                questionMapper.updateById(question);
            }
        }
    }

    @Override
    public QuestionVo queryPersonById(Integer id) {
        QuestionVo questionVo = new QuestionVo();
        try {
            Question question = questionMapper.selectById(id);
            BeanUtils.copyProperties(question, questionVo);
            List<Qsubject> subjectList = subjectMapper.selectPersonSubjectByQuId(id);
            List<SubjectVo> subjectVoList = new ArrayList<>();
            for (Qsubject subject : subjectList) {
                SubjectVo subjectVo = new SubjectVo();
                BeanUtils.copyProperties(subject, subjectVo);
                List<Qoption> qoptionList = optionMapper.selectQoptionBySubId(subject.getId());
                subjectVo.setOptionList(qoptionList);
                subjectVoList.add(subjectVo);
            }
            //开始组装分组题逻辑
            //先缓存
            Map<Integer, SubjectVo> mapCache = new HashMap<>();
            for (SubjectVo subjectVo : subjectVoList) {
                mapCache.put(subjectVo.getId(), subjectVo);
            }
            //开始算
            StringBuffer groupIdsAll = new StringBuffer();
            for (SubjectVo subjectVo : subjectVoList) {
                //如果是分组题
                if (subjectVo.getSubType().equals("8")) {
                    String groupIds = subjectVo.getGroupIds();//包含题号
                    if (null != groupIds) {
                        String[] gids = groupIds.split(",");
                        List<SubjectVo> subjectVoGroupList = new ArrayList<>();
                        for (String subId : gids) {
                            groupIdsAll.append(subId);
                            groupIdsAll.append(",");
                            SubjectVo svo = mapCache.get(Integer.parseInt(subId));
                            subjectVoGroupList.add(svo);
                        }
                        //设置到分组题对象列表
                        subjectVo.setSubjectVoList(subjectVoGroupList);
                    }
                }
            }
            //删除在subjectVoList集合中删除groupIdsAll包含的题
            if (groupIdsAll.length() != 0) {
                String removeIds = groupIdsAll.toString();
                String[] remIds = removeIds.split(",");
                if (null != remIds && remIds.length > 0) {
                    for (int i = 0; i < subjectVoList.size(); i++) {
                        //for (SubjectVo subjectVo : subjectVoList) {
                        SubjectVo subjectVo = subjectVoList.get(i);
                        Integer nowId = subjectVo.getId();
                        for (String remId : remIds) {
                            if (nowId == Integer.parseInt(remId)) {
                                subjectVoList.remove(i);//移除
                                i--;
                            }
                        }

                    }
                }
            }
            questionVo.setSubjectVoList(subjectVoList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return questionVo;
    }

    @Override
    public void updateCategoryIdParam(UpdateCategoryIdParam updateCategoryIdParam) {
        Integer[] quIds = updateCategoryIdParam.getQuId();
        Integer[] categoryIds = updateCategoryIdParam.getCategoryId();
        Integer categoryType = updateCategoryIdParam.getCategoryType();
        if (quIds != null && categoryIds != null) {
            StringBuffer categoryId = new StringBuffer();
            if (categoryIds.length == 1) {
                categoryId.append(categoryIds[0]==null?"":categoryIds[0]);
            } else {
                for (Integer cid : categoryIds) {
                    categoryId.append(cid);
                    categoryId.append(",");
                }
            }
            //更新
            for (Integer qid : quIds) {
                Question question = new Question();
                question.setId(qid);
                question.setCategoryId(categoryId.toString());
                question.setCategoryType(categoryType);
                question.setUpdateTime(new Date());
                questionMapper.updateById(question);
            }
        }
    }

    @Override
    public Boolean againRelease(QuestionAgainReleaseParam questionAgainreleaseParam) {
        try {
            Question question = questionMapper.selectById(questionAgainreleaseParam.getId());
            StringBuffer sql = new StringBuffer();
            sql.append("ALTER TABLE `" + question.getTableName() + "` ");
            List<Qsubject> subjectList = subjectMapper.selectBatchIds(questionAgainreleaseParam.getSubjectIds());
             for (Qsubject qsubject : subjectList) {
                sql.append(" ADD COLUMN ");

                Integer limitWords = qsubject.getLimitWords();
                if (limitWords == null || limitWords == 0) {
                    limitWords = 50;
                }
                String subType = qsubject.getSubType();
                Integer del = qsubject.getDel();
                if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType) || QuestionConstant.DEL_DELETED.equals(del)) {
                    continue;
                }
                sql.append("`")
                        .append(qsubject.getColumnName())
                        .append("` ")
                        .append(qsubject.getColumnTypeDatabase())
                        .append("(")
                        .append(limitWords)
                        .append(") NULL COMMENT '")
                        .append(qsubject.getSubName())
                        .append("',");
            }
            sql.delete(sql.length()-1,sql.length());
            sql.append(" ; ");
            dynamicTableMapper.createDynamicTable(sql.toString());
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<Question> queryQuestionByInput(String name) {
        List<Question> questions = questionMapper.queryQuestionByInput(name);
        return questions;
    }

    @Override
    public void updateWriteFrequencyIdsParam(UpdateWriteFrequencyIdsParam updateWriteFrequencyIdsParam) {
        Integer[] quIds = updateWriteFrequencyIdsParam.getQuId();
        Integer writeFrequency = updateWriteFrequencyIdsParam.getWriteFrequency();
        if (quIds != null && writeFrequency != null) {
            //更新
            for (Integer qid : quIds) {
                Question question = new Question();
                question.setId(qid);
                question.setWriteFrequency(writeFrequency);
                question.setUpdateTime(new Date());
                questionMapper.updateById(question);
            }
        }
    }

    @Override
    public List<QuestionPatientCreateListVo> patientCreateList(String name, String deptId) {
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        if(StringUtils.isNotBlank(name)){
            lambda.like(Question::getQuName,name);
        }
        lambda.eq(Question::getWriteFrequency,QuestionConstant.WRITE_FREQUENCY_PATIENT_WRITE);
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        lambda.and(wrapper->wrapper.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_NORMAL).or().isNull(Question::getCategoryType));

        //科室匹配 问卷设置科室权限---
        if(StringUtils.isNotBlank(deptId)){
            lambda.like(Question::getDeptIds,deptId);
        }
        List<Question> questions = questionMapper.selectList(lambda);
        List<QuestionPatientCreateListVo> patientCreateListVos = questions.stream().map(q -> {
            QuestionPatientCreateListVo patientCreateListVo = new QuestionPatientCreateListVo();
            patientCreateListVo.setIcon(q.getIcon());
            patientCreateListVo.setQuName(q.getQuName());
            patientCreateListVo.setIcon(q.getIcon());
            patientCreateListVo.setId(q.getId());
            return patientCreateListVo;
        }).collect(Collectors.toList());
        return patientCreateListVos;
    }

    @Override
    public List<QuestionMonthQuarterYearCreateListVo> monthQuarterYearCreateList(String type, String deptId) {
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        if("0".equals(type)){
            lambda.eq(Question::getWriteFrequency,QuestionConstant.WRITE_FREQUENCY_MONTH);
        }else if("1".equals(type)){
            lambda.eq(Question::getWriteFrequency,QuestionConstant.WRITE_FREQUENCY_QUARTER);
        }else if("2".equals(type)){
            lambda.eq(Question::getWriteFrequency,QuestionConstant.WRITE_FREQUENCY_YEAR);
        }else{
            lambda.in(Question::getWriteFrequency,QuestionConstant.WRITE_FREQUENCY_MONTH_QUARTER_YEAR);
        }
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        lambda.and(wrapper->wrapper.eq(Question::getCategoryType, QuestionConstant.CATEGORY_TYPE_NORMAL).or().isNull(Question::getCategoryType));

        //科室匹配 问卷设置科室权限---
        if(StringUtils.isNotBlank(deptId)){
            lambda.like(Question::getDeptIds,deptId);
        }
        List<Question> questions = questionMapper.selectList(lambda);
        List<QuestionMonthQuarterYearCreateListVo> patientCreateListVos = questions.stream().map(q -> {
            QuestionMonthQuarterYearCreateListVo patientCreateListVo = new QuestionMonthQuarterYearCreateListVo();
            patientCreateListVo.setIcon(q.getIcon());
            patientCreateListVo.setQuName(q.getQuName());
            patientCreateListVo.setIcon(q.getIcon());
            patientCreateListVo.setId(q.getId());
            return patientCreateListVo;
        }).collect(Collectors.toList());
        return patientCreateListVos;
    }

    @Override
    public List<TbDep> singleDiseaseStatisticAnalysisByDeptCondition(QSingleDiseaseTakeStatisticAnalysisByDeptConditionParam qSingleDiseaseTakeStatisticAnalysisByDeptConditionParam, String deptId, String type) {
        String categoryId = qSingleDiseaseTakeStatisticAnalysisByDeptConditionParam.getCategoryId();
        List<String>  deptIdList = selectSingleDiseaseDeptIdList(categoryId);
        LambdaQueryWrapper<TbDep> tbDepLambda = new QueryWrapper<TbDep>().lambda();
        tbDepLambda.eq(TbDep::getIsdelete, Constant.IS_DELETE_NO);
        if(DeptUtil.isClinical(type)){
            tbDepLambda.eq(TbDep::getId,deptId);
        }
        tbDepLambda.in(TbDep::getId,deptIdList);
        return tbDepService.list(tbDepLambda);
    }

    @Override
    public List<String> selectSingleDiseaseDeptIdList(String categoryId) {
        LambdaQueryWrapper<Question> lambda = new QueryWrapper<Question>().lambda();
        lambda.eq(Question::getQuStatus,QuestionConstant.QU_STATUS_RELEASE);
        lambda.eq(Question::getCategoryType,QuestionConstant.CATEGORY_TYPE_SINGLE_DISEASE);
        lambda.eq(Question::getDel,QuestionConstant.DEL_NORMAL);
        if(StringUtils.isNotBlank(categoryId)){
            lambda.eq(Question::getCategoryId, categoryId);
        }
        List<Question> questionList = questionMapper.selectList(lambda);
        if(questionList.isEmpty()){
            return Lists.newArrayList();
        }
        List<String> deptIdList = Lists.newArrayList();
        for (Question question : questionList) {
            String deptIds = question.getDeptIds();
            if(StringUtils.isNotBlank(deptIds)){
                deptIdList.addAll(Arrays.asList(deptIds.split(",")));
            }
        }

        return deptIdList.stream().distinct().collect(Collectors.toList());
    }
}
