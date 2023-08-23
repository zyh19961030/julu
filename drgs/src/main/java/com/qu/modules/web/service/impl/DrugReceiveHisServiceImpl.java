package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qu.modules.web.entity.DrugReceiveHis;
import com.qu.modules.web.mapper.DrugReceiveHisMapper;
import com.qu.modules.web.service.IDrugReceiveHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 药品规则接收his数据表
 * @Author: jeecg-boot
 * @Date:   2021-09-10
 * @Version: V1.0
 */
@Service
public class DrugReceiveHisServiceImpl extends ServiceImpl<DrugReceiveHisMapper, DrugReceiveHis> implements IDrugReceiveHisService {

    @Autowired
    DrugReceiveHisMapper drugReceiveHisMapper;

    @Override
    public List<DrugReceiveHis> queryByMid(Integer id) {
        LambdaQueryWrapper<DrugReceiveHis> lambda = new QueryWrapper<DrugReceiveHis>().lambda();
        lambda.eq(DrugReceiveHis::getPurposeOrActionId, id);
        lambda.eq(DrugReceiveHis::getPid, 0);
        List<DrugReceiveHis> list = this.list(lambda);
        return list;
    }

    @Override
    public List<DrugReceiveHis> queryByPid(Integer id) {
        List<DrugReceiveHis> list = drugReceiveHisMapper.queryByPid(id);
        return list;
    }

    @Override
    public List<DrugReceiveHis> queryPurposeByInput(String name) {
        List<DrugReceiveHis> list = drugReceiveHisMapper.queryPurposeByInput(name);
        return list;
    }

    @Override
    public List<DrugReceiveHis> queryActionByInput(String name) {
        List<DrugReceiveHis> list = drugReceiveHisMapper.queryActionByInput(name);
        return list;
    }

    @Override
    public Integer queryPurposeOrActionIdById(Integer id) {
        Integer purposeOrActionId = drugReceiveHisMapper.queryPurposeOrActionIdById(id);
        return purposeOrActionId;
    }
}
