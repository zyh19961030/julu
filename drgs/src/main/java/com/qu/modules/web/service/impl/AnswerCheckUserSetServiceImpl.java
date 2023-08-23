package com.qu.modules.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jeecg.common.api.vo.ResultBetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qu.constant.AnswerCheckUserSetConstant;
import com.qu.constant.QuestionCheckedDeptConstant;
import com.qu.modules.web.entity.AnswerCheckUserSet;
import com.qu.modules.web.entity.Question;
import com.qu.modules.web.entity.QuestionCheckedDept;
import com.qu.modules.web.entity.TbUser;
import com.qu.modules.web.mapper.AnswerCheckUserSetMapper;
import com.qu.modules.web.param.AnswerCheckUserSetSaveParam;
import com.qu.modules.web.param.AnswerCheckUserSetSaveServiceParam;
import com.qu.modules.web.service.IAnswerCheckUserSetService;
import com.qu.modules.web.service.IQuestionCheckedDeptService;
import com.qu.modules.web.service.IQuestionService;
import com.qu.modules.web.service.ITbUserService;
import com.qu.modules.web.vo.AnswerCheckSetAllDataVo;

import cn.hutool.core.collection.CollectionUtil;

/**
 * @Description: 检查表的检查人员设置表
 * @Author: jeecg-boot
 * @Date: 2022-11-30
 * @Version: V1.0
 */
@Service
public class AnswerCheckUserSetServiceImpl extends ServiceImpl<AnswerCheckUserSetMapper, AnswerCheckUserSet> implements IAnswerCheckUserSetService {

    @Lazy
    @Autowired
    private IQuestionService questionService;

    @Lazy
    @Autowired
    private ITbUserService tbUserService;

    @Autowired
    private IQuestionCheckedDeptService questionCheckedDeptService;

    @Override
    public ResultBetter saveAnswerCheckUserSet(AnswerCheckUserSetSaveParam param, String deptId) {
        //类型
        Integer type = param.getType();
        LambdaUpdateWrapper<AnswerCheckUserSet> lambda = new UpdateWrapper<AnswerCheckUserSet>().lambda();

        //先删除旧的 再插入新的
        Date date = new Date();
        AnswerCheckUserSet emptyEntity = new AnswerCheckUserSet();
        if (AnswerCheckUserSetConstant.TYPE_LINE.equals(type)) {
            lambda.eq(AnswerCheckUserSet::getType, AnswerCheckUserSetConstant.TYPE_LINE)
                    .eq(AnswerCheckUserSet::getDeptId, deptId)
                    .set(AnswerCheckUserSet::getDel, AnswerCheckUserSetConstant.DEL_DELETED);
            this.update(emptyEntity, lambda);

            List<String> userIdList = param.getUserId();
            if (CollectionUtil.isNotEmpty(userIdList)) {
                List<AnswerCheckUserSet> saveList = Lists.newArrayList();
                for (int i = 0; i < userIdList.size(); i++) {
                    AnswerCheckUserSet answerCheckUserSet = new AnswerCheckUserSet();
                    answerCheckUserSet.setUserId(userIdList.get(i));
                    answerCheckUserSet.setDeptId(deptId);
                    answerCheckUserSet.setUpdateTime(date);
                    answerCheckUserSet.setCreateTime(date);
                    answerCheckUserSet.setDel(AnswerCheckUserSetConstant.DEL_NORMAL);
                    answerCheckUserSet.setType(AnswerCheckUserSetConstant.TYPE_LINE);
                    answerCheckUserSet.setSortNumber(i+1);
                    saveList.add(answerCheckUserSet);
                }
                this.saveBatch(saveList);
            }
            return ResultBetter.ok();
        } else if (AnswerCheckUserSetConstant.TYPE_COLUMN.equals(type)) {
            lambda.eq(AnswerCheckUserSet::getType, AnswerCheckUserSetConstant.TYPE_COLUMN).eq(AnswerCheckUserSet::getDeptId, deptId).set(AnswerCheckUserSet::getDel, AnswerCheckUserSetConstant.DEL_DELETED);

            this.update(emptyEntity, lambda);

            List<Integer> quIdList = param.getQuId();
            if (CollectionUtil.isNotEmpty(quIdList)) {
                List<AnswerCheckUserSet> saveList = Lists.newArrayList();
                for (int i = 0; i < quIdList.size(); i++) {
                    AnswerCheckUserSet answerCheckUserSet = new AnswerCheckUserSet();
                    answerCheckUserSet.setQuId(quIdList.get(i));
                    answerCheckUserSet.setDeptId(deptId);
                    answerCheckUserSet.setUpdateTime(date);
                    answerCheckUserSet.setCreateTime(date);
                    answerCheckUserSet.setDel(AnswerCheckUserSetConstant.DEL_NORMAL);
                    answerCheckUserSet.setType(AnswerCheckUserSetConstant.TYPE_COLUMN);
                    answerCheckUserSet.setSortNumber(i+1);
                    saveList.add(answerCheckUserSet);
                }
                this.saveBatch(saveList);
            }
            return ResultBetter.ok();
        }
        return ResultBetter.error("保存失败");
    }

    @Override
    public List<AnswerCheckUserSet> selectByDeptAndType(String deptId, Integer typeColumn) {
        LambdaUpdateWrapper<AnswerCheckUserSet> lambda = new UpdateWrapper<AnswerCheckUserSet>().lambda();
        lambda.eq(AnswerCheckUserSet::getDeptId,deptId);
        lambda.eq(AnswerCheckUserSet::getType,typeColumn);
        lambda.eq(AnswerCheckUserSet::getDel, AnswerCheckUserSetConstant.DEL_NORMAL);
        lambda.orderByAsc(AnswerCheckUserSet::getSortNumber);
        return this.list(lambda);
    }

    @Override
    public ResultBetter<AnswerCheckSetAllDataVo> selectAnswerCheckUserSet(String deptId) {
        //查询列
        List<AnswerCheckUserSet> columnList = this.selectByDeptAndType(deptId,AnswerCheckUserSetConstant.TYPE_COLUMN);
        AnswerCheckSetAllDataVo vo = new AnswerCheckSetAllDataVo();
        //表头
        List<LinkedHashMap<String, String>> fieldItems = Lists.newArrayList();
        LinkedHashMap<String, String> fieldItemUser = Maps.newLinkedHashMap();
        fieldItems.add(fieldItemUser);
        fieldItemUser.put("fieldTxt", "人员");
        fieldItemUser.put("fieldId", "tb_user");
        List<Integer> quIdList = columnList.stream().map(AnswerCheckUserSet::getQuId).distinct().collect(Collectors.toList());
        List<Question> questionList = questionService.getCheckByIds(quIdList);
        Map<Integer, Question> questionMap = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));
        for (Integer quId : quIdList) {
            Question question = questionMap.get(quId);
            if(question!=null){
                LinkedHashMap<String, String> fieldItem = Maps.newLinkedHashMap();
                fieldItems.add(fieldItem);
                fieldItem.put("fieldTxt", question.getQuName());
//                fieldItem.put("fieldId", String.format("column_%s",question.getId()));
                fieldItem.put("fieldId", String.valueOf(question.getId()));
            }
        }
        vo.setFieldItems(fieldItems);

        //查询行
        List<AnswerCheckUserSet> lineList = this.selectByDeptAndType(deptId,AnswerCheckUserSetConstant.TYPE_LINE);
        //数据
        List<LinkedHashMap<String, String>> detailDataList = Lists.newArrayList();
        vo.setDetailDataList(detailDataList);

        List<String> userIdList = lineList.stream().map(AnswerCheckUserSet::getUserId).distinct().collect(Collectors.toList());
        List<TbUser> userList = tbUserService.getByIds(userIdList);
        Map<String, TbUser> userMap = userList.stream().collect(Collectors.toMap(TbUser::getId, Function.identity()));
        for (String userId : userIdList) {
            TbUser tbUser = userMap.get(userId);
            if(tbUser!=null){
                LinkedHashMap<String, String> valueItem = Maps.newLinkedHashMap();
                detailDataList.add(valueItem);
                valueItem.put("tb_user", tbUser.getUsername());
                valueItem.put("dataKey", tbUser.getId());
                //循环一遍问卷
                for (Integer quId : quIdList) {
                    valueItem.put(String.valueOf(quId), "0");
                }
            }
        }

        //处理数据 查询问卷的责任人
        List<QuestionCheckedDept> questionCheckedDeptList = questionCheckedDeptService.selectCheckedDeptByDeptIds(userIdList,deptId, QuestionCheckedDeptConstant.TYPE_RESPONSIBILITY_USER);
        if(CollectionUtil.isNotEmpty(questionCheckedDeptList)){
            Map<String, List<QuestionCheckedDept>> questionCheckedDeptMap = questionCheckedDeptList.stream().collect(Collectors.toMap(QuestionCheckedDept::getDeptId, Lists::newArrayList,
                    (List<QuestionCheckedDept> n1, List<QuestionCheckedDept> n2) -> {
                        n1.addAll(n2);
                        return n1;
                    }));
            for (LinkedHashMap<String, String> stringObjectLinkedHashMap : detailDataList) {
                String dataKey = stringObjectLinkedHashMap.get("dataKey");
                List<QuestionCheckedDept> checkedDeptList = questionCheckedDeptMap.get(dataKey);
                if(CollectionUtil.isNotEmpty(checkedDeptList)){
                    for (QuestionCheckedDept questionCheckedDept : checkedDeptList) {
                        stringObjectLinkedHashMap.put(String.valueOf(questionCheckedDept.getQuId()),"1");
                    }
                }

            }
        }

        return ResultBetter.ok(vo);
    }

    @Override
    public ResultBetter saveService(List<AnswerCheckUserSetSaveServiceParam> param, String deptId) {
        ArrayList<QuestionCheckedDept> addList = Lists.newArrayList();
        Date date = new Date();
        for (AnswerCheckUserSetSaveServiceParam answerCheckUserSetSaveServiceParam : param) {
            List<Integer> quIdList = answerCheckUserSetSaveServiceParam.getQuId();
            if(CollectionUtil.isNotEmpty(quIdList)){
                for (Integer quId : quIdList) {
                    QuestionCheckedDept questionCheckedDept = new QuestionCheckedDept();
                    questionCheckedDept.setQuId(quId);
                    questionCheckedDept.setDeptId(answerCheckUserSetSaveServiceParam.getUserId());
                    questionCheckedDept.setCreateTime(date);
                    questionCheckedDept.setUpdateTime(date);
                    questionCheckedDept.setType(QuestionCheckedDeptConstant.TYPE_RESPONSIBILITY_USER);
                    questionCheckedDept.setUserDeptId(deptId);
                    addList.add(questionCheckedDept);
                }
            }
        }
        //先删除
        List<String> userIdList = param.stream().map(AnswerCheckUserSetSaveServiceParam::getUserId).distinct().collect(Collectors.toList());
        questionCheckedDeptService.deleteCheckedDeptByDeptIds(userIdList,deptId,QuestionCheckedDeptConstant.TYPE_RESPONSIBILITY_USER);
        //保存
        questionCheckedDeptService.saveBatch(addList);
        return ResultBetter.ok();
    }
}
