package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.modules.web.entity.TbUserPosition;
import com.qu.modules.web.mapper.TbUserPositionMapper;
import com.qu.modules.web.service.ITbUserPositionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 用户职位中间表
 * @Author: jeecg-boot
 * @Date:   2022-12-01
 * @Version: V1.0
 */
@Service
public class TbUserPositionServiceImpl extends ServiceImpl<TbUserPositionMapper, TbUserPosition> implements ITbUserPositionService {

    @Override
    public List<TbUserPosition> getByPositionId(String positionId) {
        LambdaQueryWrapper<TbUserPosition> lambda = new QueryWrapper<TbUserPosition>().lambda();
        lambda.eq(TbUserPosition::getPositionid,positionId);
        return this.list(lambda);
    }
}
