package com.qu.modules.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.QuestionCheckedDept;

import java.util.List;

/**
 * @Description: 问卷被检查科室关联表
 * @Author: jeecg-boot
 * @Date:   2022-09-18
 * @Version: V1.0
 */
public interface IQuestionCheckedDeptService extends IService<QuestionCheckedDept> {

    List<QuestionCheckedDept> selectCheckedDeptByQuId(Integer quId,String userDeptId,Integer type);

    List<QuestionCheckedDept> selectCheckedDeptByDeptId(String deptId,String userDeptId,Integer type);

    List<QuestionCheckedDept> selectCheckedDeptByDeptIds(List<String> deptIdList, String userDeptId, Integer type);

    List<QuestionCheckedDept> selectCheckedDeptByQuIdAndDeptId(Integer quId, String checkedDeptId);

    void deleteCheckedDeptByQuId(Integer quId,Integer type, String deptId);

    void deleteCheckedDeptByDeptIds(List<String> deptIdList, String deptId,Integer type);

}
