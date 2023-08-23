package com.qu.modules.web.service;

import com.qu.modules.web.entity.TbInspectStatsTemplateDep;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 科室检查统计模板
 * @Author: jeecg-boot
 * @Date:   2022-09-18
 * @Version: V1.0
 */
public interface ITbInspectStatsTemplateDepService extends IService<TbInspectStatsTemplateDep> {

    /**
     * 查询问卷和科室的模板类型
     * @param deptId
     * @param questionIds
     * @return
     */
    List<TbInspectStatsTemplateDep> selectByQuestionIds(String deptId, List<String> questionIds);

    /**
     * 查询科室统计类型的模板根据问卷和科室
     * @param deptId
     * @param questionIds
     * @return
     */
    List<TbInspectStatsTemplateDep> selectDeptStatisticsByQuestionIds(String deptId, List<String> questionIds);
}
