package com.qu.modules.web.service;

import com.qu.modules.web.entity.DrugRulesQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description: 药品规则问卷表
 * @Author: jeecg-boot
 * @Date:   2021-09-12
 * @Version: V1.0
 */
public interface IDrugRulesQuestionService extends IService<DrugRulesQuestion> {

    //展示药品规则问卷
    List<DrugRulesQuestion> queryQuestion(String name);

    //通过id查询问卷是否删除
    List<DrugRulesQuestion> queryQuestionIfDelById(Integer id);

    //通过id查询问卷是否存在
    List<DrugRulesQuestion> queryQuestionIfExistById(Integer id);

    //修改问卷删除状态
    int updateQuestion(@Param("questionId") Integer questionId, @Param("del") int del, @Param("updateTime") Date updateTime);

    //查询问题所属的问卷
    DrugRulesQuestion queryQuestionsById(Integer id);

    //通过id查询问卷
    List<DrugRulesQuestion> queryById(Integer id);
}
