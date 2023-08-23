package com.qu.modules.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.TbFollowVisitPatientTemplate;
import com.qu.modules.web.param.TbFollowVisitPatientTemplateAllPatientListParam;
import com.qu.modules.web.param.TbFollowVisitPatientTemplateGenerateParam;
import com.qu.modules.web.param.TbFollowVisitPatientTemplateListParam;
import com.qu.modules.web.vo.TbFollowVisitPatientTemplateAllPatientListVo;
import com.qu.modules.web.vo.TbFollowVisitPatientTemplateInfoVo;
import com.qu.modules.web.vo.TbFollowVisitPatientTemplateListVo;

/**
 * @Description: 随访患者模板总记录表
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
public interface ITbFollowVisitPatientTemplateService extends IService<TbFollowVisitPatientTemplate> {

    IPage<TbFollowVisitPatientTemplateListVo> queryPageList(Page<TbFollowVisitPatientTemplate> page, TbFollowVisitPatientTemplateListParam param);

    boolean stopFollowVisit(Integer id);

    TbFollowVisitPatientTemplateInfoVo info(Integer id);

    IPage<TbFollowVisitPatientTemplateAllPatientListVo> allPatientList(Page<TbFollowVisitPatientTemplate> page, TbFollowVisitPatientTemplateAllPatientListParam param);

    void generatePatientTemplate(TbFollowVisitPatientTemplateGenerateParam param);

}
