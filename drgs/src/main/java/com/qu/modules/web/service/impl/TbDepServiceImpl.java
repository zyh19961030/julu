package com.qu.modules.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.qu.constant.Constant;
import com.qu.modules.web.entity.TbDep;
import com.qu.modules.web.mapper.TbDepMapper;
import com.qu.modules.web.service.ITbDepService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 科室表
 * @Author: jeecg-boot
 * @Date:   2022-04-20
 * @Version: V1.0
 */
@Service
public class TbDepServiceImpl extends ServiceImpl<TbDepMapper, TbDep> implements ITbDepService {

    @Override
    public List<TbDep> listByIdList(List<String> ids) {
        if(CollectionUtil.isEmpty(ids)){
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<TbDep> tbDepLambda = new QueryWrapper<TbDep>().lambda();
        tbDepLambda.eq(TbDep::getIsdelete, Constant.IS_DELETE_NO);
        tbDepLambda.in(TbDep::getId,ids);
        return this.list(tbDepLambda);
    }
}
