package com.qu.modules.web.service;

import com.qu.modules.web.entity.QoptionVersion;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 选项版本表
 * @Author: jeecg-boot
 * @Date:   2022-09-25
 * @Version: V1.0
 */
public interface IQoptionVersionService extends IService<QoptionVersion> {

    /**
     * 根据问卷版本id 和题目id集合查询选项
     * @param questionVersionId
     * @param subjectVersionIdList
     * @return
     */
    List<QoptionVersion> selectOptionVersionByQuIdAndVersion(String questionVersionId, List<Integer> subjectVersionIdList);
}
