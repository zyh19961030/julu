package com.qu.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qu.modules.web.entity.Qsubjectlib;

import java.util.List;
import java.util.Map;

/**
 * @Description: 题库题目表
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
public interface QsubjectlibMapper extends BaseMapper<Qsubjectlib> {

    int queryPageListCount(Map<String, Object> params);

    List<Qsubjectlib> queryPageList(Map<String, Object> params);

}
