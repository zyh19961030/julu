package com.qu.modules.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.TbDeptGroup;
import com.qu.modules.web.param.TbDeptGroupAddParam;
import com.qu.modules.web.vo.TbDeptGroupAddVo;
import com.qu.modules.web.vo.TbDeptGroupListVo;

import java.util.List;

/**
 * @Description: 分组管理表
 * @Author: jeecg-boot
 * @Date:   2022-09-19
 * @Version: V1.0
 */
public interface ITbDeptGroupService extends IService<TbDeptGroup> {

    void addOrUpdate(TbDeptGroupAddParam addParam);

    void delete(String id);

    List<TbDeptGroupListVo> queryPageList();

    List<TbDeptGroupAddVo> fastAddList();


}
