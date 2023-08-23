package com.qu.modules.web.mapper;

import java.util.List;

import com.qu.modules.web.entity.DrugReceiveHis;
import org.apache.ibatis.annotations.Param;
import com.qu.modules.web.entity.DrugRulesRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 药品规则关联表
 * @Author: jeecg-boot
 * @Date:   2021-09-10
 * @Version: V1.0
 */
public interface DrugRulesRelationMapper extends BaseMapper<DrugRulesRelation> {

    //查询答案是否已存在用药目的或his物理作用
    List<DrugRulesRelation> ifExist(Integer optionId);

    //修改
    int delete(Integer optionId);

}
