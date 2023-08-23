package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.constant.QuestionCheckedDeptConstant;
import com.qu.modules.web.entity.QuestionCheckedDept;
import com.qu.modules.web.mapper.QuestionCheckedDeptMapper;
import com.qu.modules.web.service.IQuestionCheckedDeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 问卷被检查科室关联表
 * @Author: jeecg-boot
 * @Date:   2022-09-18
 * @Version: V1.0
 */
@Service
public class QuestionCheckedDeptServiceImpl extends ServiceImpl<QuestionCheckedDeptMapper, QuestionCheckedDept> implements IQuestionCheckedDeptService {

    @Override
    public List<QuestionCheckedDept> selectCheckedDeptByQuId(Integer quId, String userDeptId, Integer type) {
        LambdaQueryWrapper<QuestionCheckedDept> lambda = new QueryWrapper<QuestionCheckedDept>().lambda();
        lambda.eq(QuestionCheckedDept::getQuId, quId);
        lambda.eq(QuestionCheckedDept::getType, type);
        if(StringUtils.isNotBlank(userDeptId)){
            lambda.eq(QuestionCheckedDept::getUserDeptId, userDeptId);
        }
        return this.list(lambda);
    }

    @Override
    public List<QuestionCheckedDept> selectCheckedDeptByDeptId(String deptId, String userDeptId, Integer type) {
        LambdaQueryWrapper<QuestionCheckedDept> lambda = new QueryWrapper<QuestionCheckedDept>().lambda();
        lambda.eq(QuestionCheckedDept::getDeptId, deptId);
        lambda.eq(QuestionCheckedDept::getType, type);
        if(StringUtils.isNotBlank(userDeptId)){
            lambda.eq(QuestionCheckedDept::getUserDeptId, userDeptId);
        }
        return this.list(lambda);
    }

    @Override
    public List<QuestionCheckedDept> selectCheckedDeptByDeptIds(List<String> deptIdList, String userDeptId, Integer type) {
        LambdaQueryWrapper<QuestionCheckedDept> lambda = new QueryWrapper<QuestionCheckedDept>().lambda();
        lambda.in(QuestionCheckedDept::getDeptId, deptIdList);
        lambda.eq(QuestionCheckedDept::getType, type);
        lambda.eq(QuestionCheckedDept::getUserDeptId, userDeptId);
        return this.list(lambda);
    }

    @Override
    public List<QuestionCheckedDept> selectCheckedDeptByQuIdAndDeptId(Integer quId, String checkedDeptId) {
        LambdaQueryWrapper<QuestionCheckedDept> lambda = new QueryWrapper<QuestionCheckedDept>().lambda();
        lambda.eq(QuestionCheckedDept::getQuId, quId);
        lambda.eq(QuestionCheckedDept::getDeptId, checkedDeptId);
        lambda.eq(QuestionCheckedDept::getType, QuestionCheckedDeptConstant.TYPE_CHECKED_DEPT);
        return this.list(lambda);
    }

    @Override
    public void deleteCheckedDeptByQuId(Integer quId,Integer type, String deptId) {
        LambdaQueryWrapper<QuestionCheckedDept> lambda = new QueryWrapper<QuestionCheckedDept>().lambda();
        lambda.eq(QuestionCheckedDept::getQuId, quId);
        lambda.eq(QuestionCheckedDept::getType, type);
        if(StringUtils.isNotBlank(deptId)){
            lambda.eq(QuestionCheckedDept::getUserDeptId, deptId);
        }
        this.remove(lambda);
    }

    @Override
    public void deleteCheckedDeptByDeptIds(List<String> deptIdList, String deptId, Integer type) {
        LambdaQueryWrapper<QuestionCheckedDept> lambda = new QueryWrapper<QuestionCheckedDept>().lambda();
        lambda.in(QuestionCheckedDept::getDeptId, deptIdList);
        lambda.eq(QuestionCheckedDept::getType, type);
        lambda.eq(QuestionCheckedDept::getUserDeptId, deptId);
        this.remove(lambda);
    }
}
