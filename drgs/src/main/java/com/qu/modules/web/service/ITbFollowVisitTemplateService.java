package com.qu.modules.web.service;

import org.jeecg.common.api.vo.ResultBetter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.TbFollowVisitTemplate;
import com.qu.modules.web.param.TbFollowVisitTemplateAddOrUpdateParam;
import com.qu.modules.web.param.TbFollowVisitTemplateListParam;
import com.qu.modules.web.pojo.Data;
import com.qu.modules.web.vo.TbFollowVisitTemplateInfoVo;
import com.qu.modules.web.vo.TbFollowVisitTemplateListVo;

/**
 * @Description: 随访模板表
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
public interface ITbFollowVisitTemplateService extends IService<TbFollowVisitTemplate> {

    IPage<TbFollowVisitTemplateListVo> queryPageList(Page<TbFollowVisitTemplate> page, TbFollowVisitTemplateListParam listParam);

    ResultBetter<Boolean> addOrUpdate(TbFollowVisitTemplateAddOrUpdateParam param, Data data);

    TbFollowVisitTemplateInfoVo info(String id);

    boolean deactivate(Integer id);

}
