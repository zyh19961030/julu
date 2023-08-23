package com.qu.modules.web.service;

import com.qu.modules.web.entity.DrugRulesQuestion;
import com.qu.modules.web.entity.DrugRulesSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import javax.naming.Name;
import java.util.Date;
import java.util.List;

/**
 * @Description: 药品规则问题表
 * @Author: jeecg-boot
 * @Date:   2021-09-12
 * @Version: V1.0
 */
public interface IDrugRulesSubjectService extends IService<DrugRulesSubject> {

    //点击问卷，展示答案
    List<DrugRulesSubject> querySubject(Integer id);

    //根据输入内容搜索药品规则问题
    List<DrugRulesSubject> querySubjectByInput(String name);

    //删除选中的问题
    int deleteSubject(@Param("id") Integer id, @Param("updateTime") Date updateTime);

    //根据输入内容和问卷id搜索药品规则问题
    List<DrugRulesSubject> querySubjectByInputAndId(@Param("name") String name, @Param("id") Integer id);

    //根据问题表id查询问题
    DrugRulesSubject queryById(Integer id);
}
