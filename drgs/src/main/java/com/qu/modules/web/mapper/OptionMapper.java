package com.qu.modules.web.mapper;

import com.qu.modules.web.entity.Qoption;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Description: 选项表
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
public interface OptionMapper extends BaseMapper<Qoption> {

    List<Qoption> selectQoptionBySubId(Integer subId);

    int deleteOptionBySubId(Integer subId);

}
