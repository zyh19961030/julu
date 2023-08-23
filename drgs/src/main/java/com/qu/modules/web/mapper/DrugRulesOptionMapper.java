package com.qu.modules.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.qu.modules.web.entity.DrugRulesOption;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 药品规则答案表
 * @Author: jeecg-boot
 * @Date:   2021-09-12
 * @Version: V1.0
 */
public interface DrugRulesOptionMapper extends BaseMapper<DrugRulesOption> {

    //根据药品规则答案表id获取药品规则id
    Integer queryOptionIdById(Integer id);

    //根据药品规则问题id删除药品规则答案
    int updateOptionBySubjectId(Integer subjectId, Date date);

    //根据药品规则答案表id获取问题id
    Integer querySubjectIdById(Integer id);

}
