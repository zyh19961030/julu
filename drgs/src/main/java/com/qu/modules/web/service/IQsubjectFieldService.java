package com.qu.modules.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.QsubjectField;

import java.util.List;

/**
 * @Description: 题目字段表
 * @Author: jeecg-boot
 * @Date:   2021-05-10
 * @Version: V1.0
 */
public interface IQsubjectFieldService extends IService<QsubjectField> {

    List<QsubjectField> getBySubjectName(String subjectName);

}
