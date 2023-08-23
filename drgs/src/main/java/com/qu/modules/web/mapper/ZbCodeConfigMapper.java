package com.qu.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qu.modules.web.entity.ZbCodeConfig;
import com.qu.modules.web.vo.ZbCodeConfigVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 指标code配置表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
public interface ZbCodeConfigMapper extends BaseMapper<ZbCodeConfig> {

    List<ZbCodeConfigVo> listByName(@Param("name")String name);



}
