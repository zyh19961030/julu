package com.qu.modules.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.TbUserAuxiliaryDep;

import java.util.List;

/**
 * @Description: 用户辅助科室表
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
public interface ITbUserAuxiliaryDepService extends IService<TbUserAuxiliaryDep> {

    TbUserAuxiliaryDep selectByUserIdAndDepId(String userId, String deptId);

    List<TbUserAuxiliaryDep> selectByDepId(String deptId);

    List<TbUserAuxiliaryDep> selectByPositionIdAndDepId(String positionId, String deptId);

}
