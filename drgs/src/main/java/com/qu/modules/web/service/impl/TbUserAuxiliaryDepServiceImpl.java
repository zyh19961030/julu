package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.constant.Constant;
import com.qu.modules.web.entity.TbUserAuxiliaryDep;
import com.qu.modules.web.mapper.TbUserAuxiliaryDepMapper;
import com.qu.modules.web.service.ITbUserAuxiliaryDepService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 用户辅助科室表
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
@Service
public class TbUserAuxiliaryDepServiceImpl extends ServiceImpl<TbUserAuxiliaryDepMapper, TbUserAuxiliaryDep> implements ITbUserAuxiliaryDepService {

    @Override
    public TbUserAuxiliaryDep selectByUserIdAndDepId(String userId, String deptId) {
        LambdaQueryWrapper<TbUserAuxiliaryDep> lambda = new QueryWrapper<TbUserAuxiliaryDep>().lambda();
        lambda.eq(TbUserAuxiliaryDep::getUserId, userId)
                .eq(TbUserAuxiliaryDep::getDepId, deptId)
                .eq(TbUserAuxiliaryDep::getIsDelete, Constant.IS_DELETE_NO);
        return this.getOne(lambda);
    }


    @Override
    public List<TbUserAuxiliaryDep> selectByDepId(String deptId) {
        LambdaQueryWrapper<TbUserAuxiliaryDep> lambda = new QueryWrapper<TbUserAuxiliaryDep>().lambda();
        lambda.eq(TbUserAuxiliaryDep::getDepId, deptId)
                .eq(TbUserAuxiliaryDep::getIsDelete, Constant.IS_DELETE_NO);
        return this.list(lambda);
    }

    @Override
    public List<TbUserAuxiliaryDep> selectByPositionIdAndDepId(String positionId, String deptId) {
        LambdaQueryWrapper<TbUserAuxiliaryDep> lambda = new QueryWrapper<TbUserAuxiliaryDep>().lambda();
        lambda.eq(TbUserAuxiliaryDep::getPositionId, positionId)
                .eq(TbUserAuxiliaryDep::getDepId, deptId)
                .eq(TbUserAuxiliaryDep::getIsDelete, Constant.IS_DELETE_NO);
        return this.list(lambda);
    }
}
