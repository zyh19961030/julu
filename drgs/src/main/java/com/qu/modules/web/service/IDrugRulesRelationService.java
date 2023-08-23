package com.qu.modules.web.service;

import com.qu.modules.web.entity.DrugReceiveHis;
import com.qu.modules.web.entity.DrugRulesRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 药品规则关联表
 * @Author: jeecg-boot
 * @Date:   2021-09-10
 * @Version: V1.0
 */
public interface IDrugRulesRelationService extends IService<DrugRulesRelation> {

    //查询答案是否已存在用药目的或his物理作用
    List<DrugRulesRelation> ifExist(Integer optionId);

    //修改
    int delete(Integer optionId);

    //根据答案id获取关联关系
    List<DrugRulesRelation> queryByOptionId(Integer optionId, Integer subject_type);

}
