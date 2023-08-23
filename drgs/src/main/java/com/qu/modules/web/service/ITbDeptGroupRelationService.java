package com.qu.modules.web.service;

import com.qu.modules.web.entity.TbDeptGroupRelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 分组管理与科室关联表
 * @Author: jeecg-boot
 * @Date:   2022-09-19
 * @Version: V1.0
 */
public interface ITbDeptGroupRelationService extends IService<TbDeptGroupRelation> {

    void deleteByGroupId(String id);

}
