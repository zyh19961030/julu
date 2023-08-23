package com.qu.modules.web.service;

import org.jeecg.common.api.vo.ResultBetter;

import com.qu.modules.web.entity.TbUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.pojo.Data;
import com.qu.modules.web.vo.QuestionSetLineVo;

import java.util.List;

/**
 * @Description: 用户表
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
public interface ITbUserService extends IService<TbUser> {

    ResultBetter<QuestionSetLineVo> setLine(Data data);

    List<TbUser> getByIds(List<String> userIdList);

}
