package com.qu.modules.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.AnswerCheckUserSet;
import com.qu.modules.web.param.AnswerCheckUserSetSaveParam;
import com.qu.modules.web.param.AnswerCheckUserSetSaveServiceParam;
import com.qu.modules.web.vo.AnswerCheckSetAllDataVo;
import org.jeecg.common.api.vo.ResultBetter;

import java.util.List;

/**
 * @Description: 检查表的检查人员设置表
 * @Author: jeecg-boot
 * @Date:   2022-11-30
 * @Version: V1.0
 */
public interface IAnswerCheckUserSetService extends IService<AnswerCheckUserSet> {

    ResultBetter saveAnswerCheckUserSet(AnswerCheckUserSetSaveParam param, String deptId);

    List<AnswerCheckUserSet> selectByDeptAndType(String deptId, Integer typeColumn);

    ResultBetter<AnswerCheckSetAllDataVo> selectAnswerCheckUserSet(String deptId);

    ResultBetter saveService(List<AnswerCheckUserSetSaveServiceParam> param, String deptId);

}
