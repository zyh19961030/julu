package com.qu.modules.web.service.impl;

import com.qu.modules.web.entity.ZbCodeConfig;
import com.qu.modules.web.mapper.ZbCodeConfigMapper;
import com.qu.modules.web.service.IZbCodeConfigService;
import com.qu.modules.web.vo.ZbCodeConfigVo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 指标code配置表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Service
public class ZbCodeConfigServiceImpl extends ServiceImpl<ZbCodeConfigMapper, ZbCodeConfig> implements IZbCodeConfigService {

    @Override
    public List<ZbCodeConfigVo> listByName(String name) {

        return this.baseMapper.listByName(name);
    }
}
