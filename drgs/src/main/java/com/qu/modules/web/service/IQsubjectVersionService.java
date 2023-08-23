package com.qu.modules.web.service;

import com.qu.modules.web.entity.QsubjectVersion;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 题目版本表
 * @Author: jeecg-boot
 * @Date:   2022-09-25
 * @Version: V1.0
 */
public interface IQsubjectVersionService extends IService<QsubjectVersion> {

    /**
     * 根据问卷id查询题目
     * @param quId
     * @param questionVersionId 问卷版本表id
     * @return
     */
    List<QsubjectVersion> selectSubjectVersionByQuIdAndVersion(Integer quId, String questionVersionId);

}
