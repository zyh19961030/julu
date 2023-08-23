package com.qu.modules.web.service;

import com.qu.modules.web.entity.ZbCodeConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.vo.ZbCodeConfigVo;

import java.util.List;

/**
 * @Description: 指标code配置表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
public interface IZbCodeConfigService extends IService<ZbCodeConfig> {

    List<ZbCodeConfigVo> listByName(String name);

}
