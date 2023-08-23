package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.qu.constant.CheckDetailSetConstant;
import com.qu.modules.web.entity.CheckDetailSet;
import com.qu.modules.web.mapper.CheckDetailSetMapper;
import com.qu.modules.web.param.CheckDetailSetParam;
import com.qu.modules.web.service.ICheckDetailSetService;
import com.qu.modules.web.service.IQuestionService;
import com.qu.modules.web.vo.CheckDetailSetVo;
import com.qu.modules.web.vo.SubjectVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 检查明细列设置表
 * @Author: jeecg-boot
 * @Date:   2022-08-09
 * @Version: V1.0
 */
@Service
public class CheckDetailSetServiceImpl extends ServiceImpl<CheckDetailSetMapper, CheckDetailSet> implements ICheckDetailSetService {

    @Lazy
    @Autowired
    private IQuestionService questionService;

    @Override
    public List<CheckDetailSetVo> queryByQuestionId(Integer questionId, String userId) {
        LambdaQueryWrapper<CheckDetailSet> lambda = new QueryWrapper<CheckDetailSet>().lambda();
        lambda.eq(CheckDetailSet::getUserId,userId).eq(CheckDetailSet::getQuestionId,questionId);
        lambda.orderByAsc(CheckDetailSet::getSortNumber);
        List<CheckDetailSet> list = this.list(lambda);
        if(list.isEmpty()){
            //拿问卷数据
            List<SubjectVo> subjectVoList = questionService.queryQuestionSubjectById(questionId);
            if(subjectVoList==null || subjectVoList.isEmpty()){
                return Lists.newArrayList();
            }
            List<CheckDetailSetVo> resList = Lists.newArrayList();
            for (int i = 0; i < subjectVoList.size(); i++) {
                SubjectVo subjectVo = subjectVoList.get(i);
                CheckDetailSetVo checkDetailSetVo = new CheckDetailSetVo();
                checkDetailSetVo.setQuestionId(questionId);
                checkDetailSetVo.setSubjectId(subjectVo.getId());
                checkDetailSetVo.setSubjectName(subjectVo.getSubName());
                checkDetailSetVo.setSortNumber(subjectVo.getOrderNum());
                checkDetailSetVo.setShowType(CheckDetailSetConstant.SHOW_TYPE_YES);
                List<SubjectVo> subjectGroupVoList = subjectVo.getSubjectVoList();
                checkDetailSetVo.setQuestionParentId(CheckDetailSetConstant.QUESTIONPARENTID_DEFAULT);
                if (subjectGroupVoList == null || subjectGroupVoList.isEmpty()) {
                    checkDetailSetVo.setChildList(Lists.newArrayList());
                }else{
                    List<CheckDetailSetVo> childList = Lists.newArrayList();
                    for (SubjectVo vo : subjectGroupVoList) {
                        CheckDetailSetVo childCheckDetailSetVo = new CheckDetailSetVo();
                        childCheckDetailSetVo.setQuestionId(questionId);
                        childCheckDetailSetVo.setSubjectId(vo.getId());
                        childCheckDetailSetVo.setSubjectName(vo.getSubName());
                        childCheckDetailSetVo.setSortNumber(vo.getOrderNum());
                        childCheckDetailSetVo.setShowType(CheckDetailSetConstant.SHOW_TYPE_YES);
                        childCheckDetailSetVo.setQuestionParentId(subjectVo.getId());
                        childCheckDetailSetVo.setChildList(Lists.newArrayList());
                        childList.add(childCheckDetailSetVo);
                    }
                    checkDetailSetVo.setChildList(childList);
                }
                resList.add(checkDetailSetVo);
            }
            return resList;
        }
        List<CheckDetailSetVo> tree = getTree(list);
        return tree;
    }


    private List<CheckDetailSetVo> getTree(List<CheckDetailSet> checkDetailSetList) {
        List<CheckDetailSetVo> checkDetailSetVoList = new ArrayList<>();
        for (CheckDetailSet checkDetailSet : checkDetailSetList) {
            if (Objects.equals(0,checkDetailSet.getQuestionParentId())) {
                CheckDetailSetVo checkDetailSetVo = new CheckDetailSetVo();
                BeanUtils.copyProperties(checkDetailSet,checkDetailSetVo);
                checkDetailSetVoList.add(checkDetailSetVo);
            }
        }
        for (CheckDetailSetVo child : checkDetailSetVoList) {
            child.setChildList(getChildren(child.getSubjectId(), checkDetailSetList));
        }
        return checkDetailSetVoList;
    }


    /***
     * @Description //往下获取子集
     */
    private List<CheckDetailSetVo> getChildren(Integer id, List<CheckDetailSet> checkDetailSetList) {
        // 子菜单
        List<CheckDetailSetVo> childList = new ArrayList();
        for (CheckDetailSet checkDetailSet : checkDetailSetList) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (!org.springframework.util.StringUtils.isEmpty(checkDetailSet.getQuestionParentId())) {
                if (checkDetailSet.getQuestionParentId().equals(id)) {
                    CheckDetailSetVo checkDetailSetVo = new CheckDetailSetVo();
                    BeanUtils.copyProperties(checkDetailSet,checkDetailSetVo);
                    childList.add(checkDetailSetVo);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (CheckDetailSetVo checkDetailSetVo : childList) {
            if (!org.springframework.util.StringUtils.isEmpty(checkDetailSetVo.getQuestionParentId())) {
                // 递归
                checkDetailSetVo.setChildList(getChildren(checkDetailSetVo.getSubjectId(), checkDetailSetList));

            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return Lists.newArrayList();
        }
        return childList;
    }

    @Override
    public void addList(List<CheckDetailSetParam> paramList, String userId) {
        ArrayList<CheckDetailSet> checkDetailSetList = Lists.newArrayList();
        for (CheckDetailSetParam checkDetailSetParam : paramList) {
            CheckDetailSet checkDetailSet = new CheckDetailSet();
            BeanUtils.copyProperties(checkDetailSetParam,checkDetailSet);
            checkDetailSet.setUserId(userId);
            List<CheckDetailSetParam> childList = checkDetailSetParam.getChildList();
            for (CheckDetailSetParam detailSetParam : childList) {
                CheckDetailSet childCheckDetailSet = new CheckDetailSet();
                BeanUtils.copyProperties(detailSetParam,childCheckDetailSet);
                childCheckDetailSet.setUserId(userId);
                checkDetailSetList.add(childCheckDetailSet);
            }
            checkDetailSetList.add(checkDetailSet);
        }
        this.saveOrUpdateBatch(checkDetailSetList);
    }
}
