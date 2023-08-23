package com.qu.modules.web.service;

import com.qu.modules.web.entity.TbUserPosition;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 用户职位中间表
 * @Author: jeecg-boot
 * @Date:   2022-12-01
 * @Version: V1.0
 */
public interface ITbUserPositionService extends IService<TbUserPosition> {

    List<TbUserPosition> getByPositionId(String positionId);

}
