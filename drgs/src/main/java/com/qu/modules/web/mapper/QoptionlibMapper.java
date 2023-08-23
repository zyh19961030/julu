package com.qu.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qu.modules.web.entity.Qoptionlib;

import java.util.List;

/**
 * @Description: 题库选项表
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
public interface QoptionlibMapper extends BaseMapper<Qoptionlib> {

    int deleteOptionBySubId(Integer subId);

    List<Qoptionlib> selectQoptionlibBySubId(Integer id);

}
