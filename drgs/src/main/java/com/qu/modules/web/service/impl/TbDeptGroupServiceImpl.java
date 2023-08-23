package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.qu.modules.web.entity.TbDep;
import com.qu.modules.web.entity.TbDeptGroup;
import com.qu.modules.web.entity.TbDeptGroupRelation;
import com.qu.modules.web.mapper.TbDeptGroupMapper;
import com.qu.modules.web.param.TbDeptGroupAddParam;
import com.qu.modules.web.service.ITbDepService;
import com.qu.modules.web.service.ITbDeptGroupRelationService;
import com.qu.modules.web.service.ITbDeptGroupService;
import com.qu.modules.web.vo.TbDeptGroupAddVo;
import com.qu.modules.web.vo.TbDeptGroupDepVo;
import com.qu.modules.web.vo.TbDeptGroupListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 分组管理表
 * @Author: jeecg-boot
 * @Date: 2022-09-19
 * @Version: V1.0
 */
@Service
public class TbDeptGroupServiceImpl extends ServiceImpl<TbDeptGroupMapper, TbDeptGroup> implements ITbDeptGroupService {


    @Autowired
    private ITbDeptGroupRelationService tbDeptGroupRelationService;
    @Autowired
    private ITbDepService tbDepService;

    @Override
    public void addOrUpdate(TbDeptGroupAddParam addParam) {
        String id = addParam.getId();
        String groupName = addParam.getGroupName();
        TbDeptGroup byId = this.getById(id);
        Date date = new Date();
        if (byId != null) {
            byId.setGroupName(groupName);
            this.updateById(byId);
        } else {
            TbDeptGroup tbDeptGroup = new TbDeptGroup();
            tbDeptGroup.setGroupName(groupName);
            tbDeptGroup.setCreateTime(date);
            this.save(tbDeptGroup);
            id = tbDeptGroup.getId();
        }

        List<String> deptIdList = addParam.getDeptIdList();
        ArrayList<TbDeptGroupRelation> relationArrayList = Lists.newArrayList();
        for (String deptId : deptIdList) {
            TbDeptGroupRelation relation = new TbDeptGroupRelation();
            relation.setDeptId(deptId);
            relation.setGroupId(id);
            relation.setCreateTime(date);
            relationArrayList.add(relation);
        }
        tbDeptGroupRelationService.deleteByGroupId(id);
        tbDeptGroupRelationService.saveBatch(relationArrayList);
    }

    @Override
    public void delete(String id) {
        TbDeptGroup byId = this.getById(id);
        if (byId == null) {
            return;
        }
        this.removeById(id);

        tbDeptGroupRelationService.deleteByGroupId(id);
    }

    @Override
    public List<TbDeptGroupListVo> queryPageList() {
        List<TbDeptGroup> list = this.list();
        if (list.isEmpty()) {
            return Lists.newArrayList();
        }
        //查询关联表
        List<TbDeptGroupRelation> relationList = tbDeptGroupRelationService.list();
        Map<String, List<String>> relationMap = relationList.stream().collect(Collectors.toMap(TbDeptGroupRelation::getGroupId, r->{
                    ArrayList<String> strings = Lists.newArrayList(r.getDeptId());
                    strings.add(r.getDeptId());
                    return strings;
                },
                (List<String> n1, List<String> n2) -> {
                    n1.addAll(n2);
                    return n1;
                }));
        ArrayList<TbDeptGroupListVo> resList = Lists.newArrayList();
        for (TbDeptGroup tbDeptGroup : list) {
            TbDeptGroupListVo vo = new TbDeptGroupListVo();
            List<String> deptList = relationMap.get(tbDeptGroup.getId());
            List<TbDep> tbDepList = tbDepService.listByIdList(deptList);
            vo.setId(tbDeptGroup.getId());
            vo.setGroupName(tbDeptGroup.getGroupName());
            vo.setDeptIdList(tbDepList.stream().map(TbDep::getDepname).distinct().collect(Collectors.toList()));
            resList.add(vo);
        }
        return resList;
    }

    @Override
    public List<TbDeptGroupAddVo> fastAddList() {
        List<TbDeptGroup> list = this.list();
        if (list.isEmpty()) {
            return Lists.newArrayList();
        }
        //查询关联表
        List<TbDeptGroupRelation> relationList = tbDeptGroupRelationService.list();
        Map<String, List<String>> relationMap = relationList.stream().collect(Collectors.toMap(TbDeptGroupRelation::getGroupId, r->{
                    ArrayList<String> strings = Lists.newArrayList(r.getDeptId());
                    strings.add(r.getDeptId());
                    return strings;
                },
                (List<String> n1, List<String> n2) -> {
                    n1.addAll(n2);
                    return n1;
                }));
        ArrayList<TbDeptGroupAddVo> resList = Lists.newArrayList();
        for (TbDeptGroup tbDeptGroup : list) {
            TbDeptGroupAddVo vo = new TbDeptGroupAddVo();
            List<String> deptList = relationMap.get(tbDeptGroup.getId());
            List<TbDep> tbDepList = tbDepService.listByIdList(deptList);
            vo.setId(tbDeptGroup.getId());
            vo.setGroupName(tbDeptGroup.getGroupName());
            vo.setDeptIdList(tbDepList.stream().map(dep->{
                TbDeptGroupDepVo depVo = new TbDeptGroupDepVo();
                depVo.setDepartmentId(dep.getId());
                depVo.setDepartmentName(dep.getDepname());
                return depVo;
            }).distinct().collect(Collectors.toList()));
            resList.add(vo);
        }
        return resList;
    }
}
