package com.qu.modules.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.qu.modules.web.entity.DrugRulesQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 药品规则问卷表
 * @Author: jeecg-boot
 * @Date:   2021-09-12
 * @Version: V1.0
 */
public interface DrugRulesQuestionMapper extends BaseMapper<DrugRulesQuestion> {

    //修改问卷删除状态
    int updateQuestion(@Param("questionId") Integer questionId, @Param("del") int del, @Param("updateTime") Date updateTime);

    //查询问题所属的问卷
    DrugRulesQuestion queryQuestionById(Integer id);

    //通过id查询问卷是否存在
    DrugRulesQuestion queryQuestionIfExistById(Integer id);

    //根据输入内容搜索药品规则问卷
    List<DrugRulesQuestion> queryQuestionByInput(String name);

}
