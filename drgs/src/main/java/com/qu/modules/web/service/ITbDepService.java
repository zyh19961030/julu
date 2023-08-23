package com.qu.modules.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.TbDep;

import java.util.List;

/**
 * @Description: 科室表
 * @Author: jeecg-boot
 * @Date:   2022-04-20
 * @Version: V1.0
 */
public interface ITbDepService extends IService<TbDep> {

    List<TbDep> listByIdList(List<String> ids);

}
