package com.qu.modules.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.TbFollowVisitPatientRecord;
import com.qu.modules.web.param.TbFollowVisitPatientRecordAnswerAfterParam;
import com.qu.modules.web.param.TbFollowVisitPatientRecordListParam;
import com.qu.modules.web.vo.TbFollowVisitPatientRecordListVo;
import com.qu.modules.web.vo.TbFollowVisitPatientRecordVo;
import org.jeecg.common.api.vo.ResultBetter;

/**
 * @Description: 随访患者记录表
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
public interface ITbFollowVisitPatientRecordService extends IService<TbFollowVisitPatientRecord> {

    IPage<TbFollowVisitPatientRecordListVo> queryPageList(TbFollowVisitPatientRecordListParam param, Page<TbFollowVisitPatientRecord> page);

    boolean stopFollowVisit(Integer id);

    TbFollowVisitPatientRecordVo startFollowVisit(Integer id);

    ResultBetter<Boolean> answerAfter(TbFollowVisitPatientRecordAnswerAfterParam param);
}
