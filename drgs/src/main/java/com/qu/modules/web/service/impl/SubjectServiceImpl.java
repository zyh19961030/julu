package com.qu.modules.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.qu.constant.QoptionConstant;
import com.qu.constant.QsubjectConstant;
import com.qu.modules.web.entity.Qoption;
import com.qu.modules.web.entity.Qsubject;
import com.qu.modules.web.mapper.OptionMapper;
import com.qu.modules.web.mapper.QsubjectMapper;
import com.qu.modules.web.param.*;
import com.qu.modules.web.pojo.TbUser;
import com.qu.modules.web.service.IQuestionService;
import com.qu.modules.web.service.ISubjectService;
import com.qu.modules.web.vo.QsubjectIdAndNameVo;
import com.qu.modules.web.vo.StatisticsCheckTableSubjectVo;
import com.qu.modules.web.vo.SubjectNameVo;
import com.qu.modules.web.vo.SubjectVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 题目表
 * @Author: jeecg-boot
 * @Date: 2021-03-19
 * @Version: V1.0
 */
@Slf4j
@Service
public class SubjectServiceImpl extends ServiceImpl<QsubjectMapper, Qsubject> implements ISubjectService {

    @Resource
    private QsubjectMapper qsubjectMapper;

    @Resource
    private OptionMapper optionMapper;

    @Lazy
    @Autowired
    private IQuestionService questionService;

    @Override
    public SubjectVo saveSubject(SubjectParam subjectParam, TbUser tbUser) {
        SubjectVo subjectVo = new SubjectVo();
        Qsubject subject = new Qsubject();

        String columnType = subjectParam.getColumnType();
        if (StringUtils.isNotBlank(columnType)) {
            subject.setColumnTypeDatabase(conversionColumnType(subjectParam.getColumnType()));
        }else{
            subject.setColumnType(QsubjectConstant.COLUMN_TYPE_DEFAULT);
            subject.setColumnTypeDatabase(QsubjectConstant.COLUMN_TYPE_DATABASE_DEFAULT);
        }

        BeanUtils.copyProperties(subjectParam, subject);
        subject.setMarkHistory(subject.getMark());
        String columnName = subjectParam.getColumnName();
        if(StringUtils.isNotBlank(columnName)){
            LambdaQueryWrapper<Qsubject> lambda = new QueryWrapper<Qsubject>().lambda();
            lambda.eq(Qsubject::getQuId,subjectParam.getQuId())
                    .eq(Qsubject::getColumnName,columnName);
//                    .eq(Qsubject::getDel,QsubjectConstant.DEL_NORMAL);
            Integer integer = qsubjectMapper.selectCount(lambda);
            if (integer > 0) {//字段重复
                return null;
            }
        }

        //计算题号
        Integer subSumCount = qsubjectMapper.selectSumCount(subjectParam.getQuId());
        if(subSumCount==null){
            subject.setOrderNum(1);
        }else{
            subject.setOrderNum(subSumCount + 1);
        }

        //如果是分组题，计算分组题号字段
        if (subjectParam.getSubType().equals(QsubjectConstant.SUB_TYPE_GROUP) || subjectParam.getSubType().equals(QsubjectConstant.SUB_TYPE_GROUP_SCORE)) {
            String[] gids = subjectParam.getGroupIds();
            StringBuffer groupIds = new StringBuffer();
            if (null != gids) {
                for (String gid : gids) {
                    groupIds.append(gid);
                    groupIds.append(",");
                }
            }
            subject.setGroupIds(groupIds.toString());
        }

        Date date = new Date();
        String userId = tbUser.getId();

        subject.setDel(QsubjectConstant.DEL_NORMAL);
        subject.setCreater(userId);
        subject.setCreateTime(date);
        subject.setUpdater(userId);
        subject.setUpdateTime(date);
        qsubjectMapper.insert(subject);
        //拷贝到Vo对象
        BeanUtils.copyProperties(subject, subjectVo);
        //选项
        List<Qoption> optionList = new ArrayList<>();
        List<QoptionParam> optionParamList = subjectParam.getOptionParamList();
        if (null != optionParamList) {
            int i = 1;
            for (QoptionParam optionParam : optionParamList) {
                Qoption option = new Qoption();
                BeanUtils.copyProperties(optionParam, option);
                option.setSubId(subject.getId());
                option.setOpOrder(i);
                option.setDel(QoptionConstant.DEL_NORMAL);
                option.setCreater(userId);
                option.setCreateTime(date);
                option.setUpdater(userId);
                option.setUpdateTime(date);
                optionMapper.insert(option);
                i++;
                optionList.add(option);
            }
            subjectVo.setOptionList(optionList);
        }

        return subjectVo;
    }

    private String conversionColumnType(String columnType) {
        switch (columnType) {
            case "字符串":
            case "数组":
                return "varchar";
            case "数值":
                return "int";
            default:
                break;
        }
        return null;
    }

    @Override
    public SubjectVo insertSubject(InsertSubjectParam insertSubjectParam, TbUser tbUser) {
        SubjectVo subjectVo = new SubjectVo();
        Qsubject subject = new Qsubject();

        String columnType = insertSubjectParam.getColumnType();
        if (StringUtils.isNotBlank(columnType)) {
            subject.setColumnTypeDatabase(conversionColumnType(insertSubjectParam.getColumnType()));
        }else{
            subject.setColumnType(QsubjectConstant.COLUMN_TYPE_DEFAULT);
            subject.setColumnTypeDatabase(QsubjectConstant.COLUMN_TYPE_DATABASE_DEFAULT);
        }
        BeanUtils.copyProperties(insertSubjectParam, subject);
        subject.setMarkHistory(subject.getMark());
        String columnName = insertSubjectParam.getColumnName();
        if(StringUtils.isNotBlank(columnName)){
            LambdaQueryWrapper<Qsubject> lambda = new QueryWrapper<Qsubject>().lambda();
            lambda.eq(Qsubject::getQuId,insertSubjectParam.getQuId())
                    .eq(Qsubject::getColumnName,columnName);
//                    .eq(Qsubject::getDel,QsubjectConstant.DEL_NORMAL);
            Integer integer = qsubjectMapper.selectCount(lambda);
            if (integer > 0) {//字段重复
                return null;
            }
        }

        //计算题号
        Integer nextOrderNum = qsubjectMapper.selectNextOrderNum(insertSubjectParam.getUpSubId());
        subject.setOrderNum(nextOrderNum + 1);

        //如果是分组题，计算分组题号字段
        if (insertSubjectParam.getSubType().equals(QsubjectConstant.SUB_TYPE_GROUP) || insertSubjectParam.getSubType().equals(QsubjectConstant.SUB_TYPE_GROUP_SCORE)) {
            String[] gids = insertSubjectParam.getGroupIds();
            StringBuffer groupIds = new StringBuffer();
            if (null != gids) {
                for (String gid : gids) {
                    groupIds.append(gid);
                    groupIds.append(",");
                }
            }
            subject.setGroupIds(groupIds.toString());
        }
        Date date = new Date();
        String userId = tbUser.getId();

        subject.setDel(QsubjectConstant.DEL_NORMAL);
        subject.setCreater(userId);
        subject.setCreateTime(date);
        subject.setUpdater(userId);
        subject.setUpdateTime(date);
        qsubjectMapper.insert(subject);

        //把这道题以后的所有题的序号都加1
        Map<String, Object> insParam = new HashMap<>();
        insParam.put("quId", insertSubjectParam.getQuId());
        insParam.put("nextOrderNum", nextOrderNum + 1);
        insParam.put("subId", subject.getId());
        qsubjectMapper.updateNextOrderNum(insParam);

        //如果upSubId属于分组，把此题也插入分组中
        Map<String, Object> selectParam = new HashMap<>();
        selectParam.put("quId", insertSubjectParam.getQuId());
        selectParam.put("subId", insertSubjectParam.getUpSubId());
        Qsubject groupQsubject = qsubjectMapper.selectGroupQsubject(selectParam);
        if (groupQsubject != null) {
            String groupIds = groupQsubject.getGroupIds();
            String[] gids = groupIds.split(",");
            StringBuffer groupIdsNew = new StringBuffer();
            for (String gid : gids) {
                groupIdsNew.append(gid);
                groupIdsNew.append(",");
                if (Integer.parseInt(gid) == insertSubjectParam.getUpSubId()) {
                    groupIdsNew.append(subject.getId());
                    groupIdsNew.append(",");
                }
            }
            //更新数据库
            Qsubject updateQsubject = new Qsubject();
            updateQsubject.setId(groupQsubject.getId());
            updateQsubject.setGroupIds(groupIdsNew.toString());
            qsubjectMapper.updateById(updateQsubject);
        }

        //拷贝到Vo对象
        BeanUtils.copyProperties(subject, subjectVo);
        //选项
        List<Qoption> optionList = new ArrayList<>();
        List<QoptionParam> optionParamList = insertSubjectParam.getOptionParamList();
        if (null != optionParamList) {
            int i = 1;
            for (QoptionParam optionParam : optionParamList) {
                Qoption option = new Qoption();
                BeanUtils.copyProperties(optionParam, option);
                option.setSubId(subject.getId());
                option.setOpOrder(i);
                option.setDel(QoptionConstant.DEL_NORMAL);
                option.setCreater(userId);
                option.setCreateTime(date);
                option.setUpdater(userId);
                option.setUpdateTime(date);
                optionMapper.insert(option);
                i++;
                optionList.add(option);
            }
            subjectVo.setOptionList(optionList);
        }

        return subjectVo;
    }

    @Override
    public SubjectVo updateQsubjectById(SubjectEditParam subjectEditParam, TbUser tbUser) {
        SubjectVo subjectVo = new SubjectVo();
        Qsubject subject = new Qsubject();

        String columnType = subjectEditParam.getColumnType();
        if (StringUtils.isNotBlank(columnType)) {
            subject.setColumnTypeDatabase(conversionColumnType(subjectEditParam.getColumnType()));
        }else{
            subject.setColumnType(QsubjectConstant.COLUMN_TYPE_DEFAULT);
            subject.setColumnTypeDatabase(QsubjectConstant.COLUMN_TYPE_DATABASE_DEFAULT);
        }
        BeanUtils.copyProperties(subjectEditParam, subject);
        if(!QsubjectConstant.MARK_OPEN.equals(subject.getMarkHistory())){
            subject.setMarkHistory(subject.getMark());
        }
        String columnName = subjectEditParam.getColumnName();
        if(StringUtils.isNotBlank(columnName)){
            LambdaQueryWrapper<Qsubject> lambda = new QueryWrapper<Qsubject>().lambda();
            lambda.eq(Qsubject::getQuId,subjectEditParam.getQuId())
                    .eq(Qsubject::getColumnName,columnName)
                    .ne(Qsubject::getId,subjectEditParam.getId());
//                    .eq(Qsubject::getDel,QsubjectConstant.DEL_NORMAL);
            Integer integer = qsubjectMapper.selectCount(lambda);
            if (integer > 0) {//字段重复
                return null;
            }
        }
        //如果是分组题，计算分组题号字段
        if (subjectEditParam.getSubType() != null) {
            Map<String, String> existCache = new HashMap<>();
            if (subjectEditParam.getSubType().equals(QsubjectConstant.SUB_TYPE_GROUP) || subjectEditParam.getSubType().equals(QsubjectConstant.SUB_TYPE_GROUP_SCORE)) {
                String[] gids = subjectEditParam.getGroupIds();
                StringBuffer groupIds = new StringBuffer();
                if (null != gids) {
                    for (String gid : gids) {
                        groupIds.append(gid);
                        groupIds.append(",");
                        existCache.put(gid, "1");
                    }
                }
                subject.setGroupIds(groupIds.toString());
            }
            //如果其他分组题中groupIds字段包含此题中的groupIds,删除
            //查询此问卷下所有的分组题
            Map<String, Object> groupParam = new HashMap();
            groupParam.put("quId", subjectEditParam.getQuId());
            groupParam.put("subId", subjectEditParam.getId());
            List<Qsubject> qsubjectList = qsubjectMapper.selectGroupQsubjectByQuId(groupParam);
            for (Qsubject qsubject : qsubjectList) {
                StringBuffer groupIdsUpdate = new StringBuffer();
                String groupIdsDB = qsubject.getGroupIds();
                if (groupIdsDB != null && groupIdsDB.length() > 0) {
                    String[] gidsdb = groupIdsDB.split(",");
                    for (String giddb : gidsdb) {
                        if (existCache.get(giddb) == null) {
                            groupIdsUpdate.append(giddb);
                            groupIdsUpdate.append(",");
                        }
                    }
                    Qsubject qsubjectUpdate = new Qsubject();
                    qsubjectUpdate.setId(qsubject.getId());
                    qsubjectUpdate.setGroupIds(groupIdsUpdate.toString());
                    qsubjectMapper.updateById(qsubjectUpdate);
                }
            }
        }
        String userId = tbUser.getId();
        Date date = new Date();
        subject.setUpdater(userId);
        subject.setUpdateTime(date);
        qsubjectMapper.updateById(subject);
        BigDecimal valueMin = subject.getValueMin();
        if(valueMin==null){
            LambdaUpdateWrapper<Qsubject> lambda = new UpdateWrapper<Qsubject>().lambda();
            lambda.eq(Qsubject::getId,subject.getId()).set(Qsubject::getValueMin,null);
            Qsubject a = new Qsubject();
            this.baseMapper.update(a,lambda);
        }
        BigDecimal valueMax = subject.getValueMax();
        if(valueMax==null){
            LambdaUpdateWrapper<Qsubject> lambda = new UpdateWrapper<Qsubject>().lambda();
            lambda.eq(Qsubject::getId,subject.getId()).set(Qsubject::getValueMax,null);
            Qsubject a = new Qsubject();
            this.baseMapper.update(a,lambda);
        }

        //拷贝到Vo对象
        BeanUtils.copyProperties(subject, subjectVo);
        //删除以前的所有选项
        //int delCount = optionMapper.deleteOptionBySubId(subject.getId());
        //选项
        List<Qoption> optionList = new ArrayList<>();
        List<QoptionParam> optionParamList = subjectEditParam.getOptionParamList();
        if (null != optionParamList) {
            int i = 1;
            for (QoptionParam optionParam : optionParamList) {
                Qoption option = new Qoption();
                BeanUtils.copyProperties(optionParam, option);
                option.setSubId(subject.getId());
                option.setOpOrder(i);
                option.setDel(QoptionConstant.DEL_NORMAL);
                option.setCreater(userId);
                option.setCreateTime(date);
                option.setUpdater(userId);
                option.setUpdateTime(date);
                if (option.getId() != null && option.getId() != 0) {
                    optionMapper.updateById(option);
                } else {
                    optionMapper.insert(option);
                }
                i++;
                optionList.add(option);
            }
            //删除应该删除的选项
            List<Integer> optionDeleteList = subjectEditParam.getOptionDeleteList();
            if(optionDeleteList!=null && !optionDeleteList.isEmpty()){
                optionMapper.deleteBatchIds(optionDeleteList);
            }
            subjectVo.setOptionList(optionList);
        }

        return subjectVo;
    }

    @Override
    public Boolean removeSubjectById(Integer id, TbUser tbUser) {
        Boolean delFlag = true;
        try {
            Qsubject subject = this.getById(id);
            if(subject==null){
                return false;
            }
            String userId = tbUser.getId();
            subject.setDel(QsubjectConstant.DEL_DELETED);
            subject.setUpdater(userId);
            subject.setUpdateTime(new Date());
            qsubjectMapper.updateById(subject);
            //如果此题在分组中，删除分组题中的groupIds
            Qsubject groupQsubject = qsubjectMapper.selectIdByGroupIdsLike(id,subject.getQuId());
            if (groupQsubject != null) {
                Integer groupSubId = groupQsubject.getId();
                String groupIds = groupQsubject.getGroupIds();
                String[] gids = groupIds.split(",");
                StringBuffer groupIdsNew = new StringBuffer();
                for (String gid : gids) {
                    if (id != Integer.parseInt(gid)) {
                        groupIdsNew.append(gid);
                        groupIdsNew.append(",");
                    }
                }
                Qsubject qsubjectUpdate = new Qsubject();
                qsubjectUpdate.setId(groupSubId);
                qsubjectUpdate.setGroupIds(groupIdsNew.toString());
                qsubjectMapper.updateById(qsubjectUpdate);
            }
        } catch (Exception e) {
            delFlag = false;
            log.error(e.getMessage(), e);
        }
        return delFlag;
    }

    @Override
    public Boolean updateOrderNum(UpdateOrderNumParam updateOrderNumParam) {
        Boolean flag = true;
        try {
            Qsubject qsubjecta = qsubjectMapper.selectById(updateOrderNumParam.getIda());
            Qsubject qsubjectb = qsubjectMapper.selectById(updateOrderNumParam.getIdb());
            int orderNuma = qsubjecta.getOrderNum();
            int orderNumb = qsubjectb.getOrderNum();
            qsubjecta.setOrderNum(orderNumb);
            qsubjectb.setOrderNum(orderNuma);
            qsubjectMapper.updateById(qsubjecta);
            qsubjectMapper.updateById(qsubjectb);
            //把分组的groupIds字段调换顺序
            int ida = qsubjecta.getId();
            int idb = qsubjectb.getId();
            Qsubject groupQsubject = qsubjectMapper.selectIdByGroupIdsLike(ida, qsubjecta.getQuId());
            if (groupQsubject != null) {
                Integer groupSubId = groupQsubject.getId();
                String groupIds = groupQsubject.getGroupIds();
                String[] gids = groupIds.split(",");
                int ida_index = 0;
                int idb_index = 0;
                for (int i = 0; i < gids.length; i++) {
                    String val = gids[i];
                    if (ida == Integer.parseInt(val)) {
                        ida_index = i;
                    }
                    if (idb == Integer.parseInt(val)) {
                        idb_index = i;
                    }
                }
                gids[ida_index] = String.valueOf(idb);
                gids[idb_index] = String.valueOf(ida);
                StringBuffer groupIdsNew = new StringBuffer();
                for (String id : gids) {
                    groupIdsNew.append(id);
                    groupIdsNew.append(",");
                }
                Qsubject qsubjectUpdate = new Qsubject();
                qsubjectUpdate.setId(groupSubId);
                qsubjectUpdate.setGroupIds(groupIdsNew.toString());
                qsubjectMapper.updateById(qsubjectUpdate);
            }
        } catch (Exception e) {
            flag = false;
            log.error(e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public void editLogic(SubjectLogicParam subjectLogicParam) {
        List<LogicParam> subjectLogicList = subjectLogicParam.getSubjectLogicList();
        subjectLogicList.forEach(s->{
            Integer id = s.getId();
            String jumpLogic = s.getJumpLogic();
            Qsubject byId = this.getById(id);
            if(byId!=null){
                byId.setJumpLogic(jumpLogic);
                this.updateById(byId);
            }
        });

        List<LogicParam> optionLogicList = subjectLogicParam.getOptionLogicList();
        optionLogicList.forEach(o->{
            Integer id = o.getId();
            String jumpLogic = o.getJumpLogic();
            Qoption qoption = optionMapper.selectById(id);
            if(qoption!=null){
                qoption.setJumpLogic(jumpLogic);
                optionMapper.updateById(qoption);
            }
        });
    }

    @Override
    public void editSpecialLogic(SubjectSpecialLogicParam subjectSpecialLogicParam){
        List<SpecialLogicParam> subjectSpecialLogicList = subjectSpecialLogicParam.getSubjectSpecialLogicList();
        subjectSpecialLogicList.forEach(s->{
            Integer id = s.getId();
            String specialJumpLogic = s.getSpecialJumpLogic();
            Qsubject byId = this.getById(id);
            if(byId!=null){
                byId.setSpecialJumpLogic(specialJumpLogic);
                this.updateById(byId);
            }
        });

        List<SpecialLogicParam> optionSpecialLogicList = subjectSpecialLogicParam.getOptionSpecialLogicList();
        optionSpecialLogicList.forEach(o->{
            Integer id = o.getId();
            String specialJumpLogic = o.getSpecialJumpLogic();
            Qoption qoption = optionMapper.selectById(id);
            if(qoption!=null){
                qoption.setSpecialJumpLogic(specialJumpLogic);
                optionMapper.updateById(qoption);
            }
        });
    }

    @Override
    public String querySubjectNmae(Integer subjectId) {
        String name = qsubjectMapper.querySubjectNmae(subjectId);
        return name;
    }

    @Override
    public List<Qsubject> querySubjectByInput(String name) {
        List<Qsubject> qsubjects = qsubjectMapper.querySubjectByInput(name);
        return qsubjects;
    }

    @Override
    public List<Qsubject> querySubjectByQuantityStatistics(SubjectQuantityStatisticsParam subjectQuantityStatisticsParam) {
        LambdaQueryWrapper<Qsubject> lambda = new QueryWrapper<Qsubject>().lambda();
        lambda.eq(Qsubject::getQuId,subjectQuantityStatisticsParam.getId())
                .eq(Qsubject::getDel,QsubjectConstant.DEL_NORMAL)
                .in(Qsubject::getSubType, QsubjectConstant.QUANTITY_STATISTICS_LIST);
        return qsubjectMapper.selectList(lambda);
    }

    @Override
    public List<Qsubject> querySubjectByScoreCount(SubjectQuantityStatisticsParam subjectQuantityStatisticsParam) {
        LambdaQueryWrapper<Qsubject> lambda = new QueryWrapper<Qsubject>().lambda();
        lambda.eq(Qsubject::getQuId,subjectQuantityStatisticsParam.getId())
                .eq(Qsubject::getDel,QsubjectConstant.DEL_NORMAL)
                .in(Qsubject::getSubType, QsubjectConstant.SCORE_COUNT_LIST);
        return qsubjectMapper.selectList(lambda);
    }

    @Override
    public List<Qsubject> querySubjectByResultEvaluate(SubjectQuantityStatisticsParam subjectQuantityStatisticsParam) {
        LambdaQueryWrapper<Qsubject> lambda = new QueryWrapper<Qsubject>().lambda();
        lambda.eq(Qsubject::getQuId,subjectQuantityStatisticsParam.getId())
                .eq(Qsubject::getDel,QsubjectConstant.DEL_NORMAL)
                .in(Qsubject::getSubType, QsubjectConstant.RESULT_EVALUATE_LIST);
        return qsubjectMapper.selectList(lambda);
    }

    @Override
    public List<QsubjectIdAndNameVo> querySubjectByDataSource(SubjectQuantityStatisticsParam subjectQuantityStatisticsParam) {
        LambdaQueryWrapper<Qsubject> lambda = new QueryWrapper<Qsubject>().lambda();
        lambda.eq(Qsubject::getQuId,subjectQuantityStatisticsParam.getId())
                .eq(Qsubject::getDel,QsubjectConstant.DEL_NORMAL)
                .eq(Qsubject::getSubType, QsubjectConstant.SUB_TYPE_HOSPITAL_USER);
        List<Qsubject> qsubjects = qsubjectMapper.selectList(lambda);
        if(qsubjects.isEmpty()){
            return Lists.newArrayList();
        }
        List<QsubjectIdAndNameVo> resList = qsubjects.stream().map(q -> {
            QsubjectIdAndNameVo vo = new QsubjectIdAndNameVo();
            BeanUtils.copyProperties(q, vo);
            return vo;
        }).collect(Collectors.toList());
        return resList;
    }

    @Override
    public List<Qsubject> querySubjectByQuId(Integer id) {
        List<Qsubject> list = qsubjectMapper.querySubjectByQuestionId(id);
        return list;
    }

    @Override
    public Qsubject querySubjectById(Integer id) {
        Qsubject qsubject = qsubjectMapper.querySubjectById(id);
        return qsubject;
    }

    @Override
    public List<StatisticsCheckTableSubjectVo> statisticsCheckTable(StatisticsCheckTableParam statisticsCheckTableParam) {
//        LambdaQueryWrapper<Qsubject> lambda = new QueryWrapper<Qsubject>().lambda();
//        lambda.eq(Qsubject::getQuId,statisticsCheckTableParam.getId())
//                .eq(Qsubject::getDel,QsubjectConstant.DEL_NORMAL);
//        List<Qsubject> subjectList = qsubjectMapper.selectList(lambda);
        List<SubjectVo> subjectVoList = questionService.queryQuestionSubjectById(statisticsCheckTableParam.getId());
        if(subjectVoList==null || subjectVoList.isEmpty()){
            return Lists.newArrayList();
        }
        List<StatisticsCheckTableSubjectVo> statisticsCheckTableList = setResList(subjectVoList);


//        List<StatisticsCheckTableSubjectVo> statisticsCheckTableList = subjectList.stream().map(q -> {
//            StatisticsCheckTableSubjectVo vo = new StatisticsCheckTableSubjectVo();
//            BeanUtils.copyProperties(q,vo);
//            return vo;
//        }).collect(Collectors.toList());
        return statisticsCheckTableList;
    }

    private List<StatisticsCheckTableSubjectVo> setResList(List<SubjectVo> subjectVoList) {
        List<StatisticsCheckTableSubjectVo> res = Lists.newArrayList();
        for (SubjectVo subjectVo : subjectVoList) {
            StatisticsCheckTableSubjectVo vo = new StatisticsCheckTableSubjectVo();
            BeanUtils.copyProperties(subjectVo,vo);
            res.add(vo);

            if(subjectVo.getSubjectVoList()!=null && !subjectVo.getSubjectVoList().isEmpty()){
                List<StatisticsCheckTableSubjectVo> statisticsCheckTableSubjectVos = setResList(subjectVo.getSubjectVoList());
                vo.setChildList(statisticsCheckTableSubjectVos);
            }else{
                vo.setChildList(Lists.newArrayList());
            }
        }
        return res;
    }

    @Override
    public List<SubjectVo> selectSubjectAndOptionByQuId(Integer questionId) {
        List<Qsubject> subjectList = this.selectSubjectByQuId(questionId);
        if(subjectList.isEmpty()){
            return Lists.newArrayList();
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
            ArrayList<Qoption> qoptionsList = optionMap.get(subject.getId());
            subjectVo.setOptionList(qoptionsList==null?optionEmptyList:qoptionsList);
            subjectVoList.add(subjectVo);
        }
        return subjectVoList;
    }

    @Override
    public List<Qsubject> selectSubjectByQuId(Integer quId) {
        return qsubjectMapper.selectSubjectByQuId(quId);
    }

    @Override
    public List<Qsubject> selectPersonSubjectByQuId(Integer quId) {
        return qsubjectMapper.selectPersonSubjectByQuId(quId);
    }

    @Override
    public List<SubjectNameVo> enterQuestionSelectSubject(EnterQuestionSelectSubjectParam param) {
        LambdaQueryWrapper<Qsubject> lambda = new QueryWrapper<Qsubject>().lambda();
        if(StringUtils.isNotBlank(param.getName())){
            lambda.in(Qsubject::getSubName, param.getName());
        }
        lambda.eq(Qsubject::getQuId,param.getQuId());
        lambda.eq(Qsubject::getDel,QsubjectConstant.DEL_NORMAL);
        List<Qsubject> list = this.list(lambda);
        List<SubjectNameVo> resList = list.stream().map(q -> {
            SubjectNameVo vo = new SubjectNameVo();
            BeanUtils.copyProperties(q, vo);
            return vo;
        }).collect(Collectors.toList());
        return resList;
    }


    @Override
    public List<Qsubject> selectByIds(List<Integer> subjectIdList) {
        if(CollectionUtil.isEmpty(subjectIdList)){
            return Lists.newArrayList();
        }
        return this.baseMapper.selectBatchIds(subjectIdList);
    }
}
