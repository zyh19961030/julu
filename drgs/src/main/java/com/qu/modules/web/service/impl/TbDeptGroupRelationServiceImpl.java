package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.modules.web.entity.TbDeptGroupRelation;
import com.qu.modules.web.mapper.TbDeptGroupRelationMapper;
import com.qu.modules.web.service.ITbDeptGroupRelationService;
import org.springframework.stereotype.Service;

/**
 * @Description: 分组管理与科室关联表
 * @Author: jeecg-boot
 * @Date: 2022-09-19
 * @Version: V1.0
 */
@Service
public class TbDeptGroupRelationServiceImpl extends ServiceImpl<TbDeptGroupRelationMapper, TbDeptGroupRelation> implements ITbDeptGroupRelationService {

    @Override
    public void deleteByGroupId(String id) {
        LambdaQueryWrapper<TbDeptGroupRelation> lambda = new QueryWrapper<TbDeptGroupRelation>().lambda();
        lambda.eq(TbDeptGroupRelation::getGroupId, id);
        this.remove(lambda);
    }
}
