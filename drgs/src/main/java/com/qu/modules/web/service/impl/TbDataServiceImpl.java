package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.constant.TbDataConstant;
import com.qu.modules.web.entity.TbData;
import com.qu.modules.web.mapper.TbDataMapper;
import com.qu.modules.web.service.ITbDataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 数据字典表
 * @Author: jeecg-boot
 * @Date:   2022-09-30
 * @Version: V1.0
 */
@Service
public class TbDataServiceImpl extends ServiceImpl<TbDataMapper, TbData> implements ITbDataService {

    @Override
    public List<TbData> selectByDataType(String dataTypeQuestionCheckCategory) {
        LambdaQueryWrapper<TbData> lambda = new QueryWrapper<TbData>().lambda();
        lambda.eq(TbData::getDatatype, dataTypeQuestionCheckCategory);
        lambda.eq(TbData::getStatus,TbDataConstant.STATUS_OPEN);
        return this.list(lambda);
    }
}
