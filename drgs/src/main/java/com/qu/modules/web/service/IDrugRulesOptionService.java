package com.qu.modules.web.service;

import com.qu.modules.web.entity.DrugRulesOption;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @Description: 药品规则答案表
 * @Author: jeecg-boot
 * @Date:   2021-09-12
 * @Version: V1.0
 */
public interface IDrugRulesOptionService extends IService<DrugRulesOption> {

    //根据选择问题的id获取答案
    List<DrugRulesOption> queryOption(Integer subjectId);

    //根据药品规则答案表id获取答案id
    Integer queryOptionIdById(Integer id);

    //根据药品规则问题id删除药品规则答案
    int updateOptionBySubjectId(Integer subjectId, Date date);

    //根据药品规则答案表id获取问题id
    Integer querySubjectIdById(Integer id);
}
