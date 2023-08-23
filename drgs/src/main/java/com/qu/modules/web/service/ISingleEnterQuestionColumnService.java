package com.qu.modules.web.service;

import com.qu.modules.web.entity.SingleEnterQuestionColumn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 录入表单展示列表
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface ISingleEnterQuestionColumnService extends IService<SingleEnterQuestionColumn> {

    List<SingleEnterQuestionColumn> selectBySingleEnterQuestionId(Integer singleEnterQuestionId);
}
