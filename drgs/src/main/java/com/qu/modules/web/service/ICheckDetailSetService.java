package com.qu.modules.web.service;

import com.qu.modules.web.entity.CheckDetailSet;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.param.CheckDetailSetParam;
import com.qu.modules.web.vo.CheckDetailSetVo;

import java.util.List;

/**
 * @Description: 检查明细列设置表
 * @Author: jeecg-boot
 * @Date:   2022-08-09
 * @Version: V1.0
 */
public interface ICheckDetailSetService extends IService<CheckDetailSet> {

    List<CheckDetailSetVo> queryByQuestionId(Integer questionId, String userId);

    void addList(List<CheckDetailSetParam> paramList, String userId);

}
