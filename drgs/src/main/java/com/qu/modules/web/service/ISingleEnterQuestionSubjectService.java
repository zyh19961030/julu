package com.qu.modules.web.service;

import com.qu.modules.web.entity.SingleEnterQuestionSubject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 录入表单填报题目表
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface ISingleEnterQuestionSubjectService extends IService<SingleEnterQuestionSubject> {

    List<SingleEnterQuestionSubject> selectBySingleEnterQuestionIdList(List<Integer> singleEnterQuestionIdList);

    List<SingleEnterQuestionSubject> selectBySingleEnterQuestionId(Integer singleEnterQuestionId);
}
