package com.qu.modules.web.service;

import com.qu.modules.web.entity.Qoption;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 选项表
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
public interface IOptionService extends IService<Qoption> {

    //根据问题id查询答案
    List<Qoption> queryOptionBySubId(Integer subId);

    /**
     * 根据题目idList查询数据
     * @param subjectIdList
     * @return
     */
    List<Qoption> selectBySubjectList(List<Integer> subjectIdList);

}
