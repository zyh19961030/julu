package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.modules.web.entity.Qoptionlib;
import com.qu.modules.web.entity.Qsubject;
import com.qu.modules.web.entity.Qsubjectlib;
import com.qu.modules.web.mapper.QoptionlibMapper;
import com.qu.modules.web.mapper.QsubjectlibMapper;
import com.qu.modules.web.param.QoptionlibParam;
import com.qu.modules.web.param.QsubjectlibAddParam;
import com.qu.modules.web.param.QsubjectlibEditParam;
import com.qu.modules.web.param.QsubjectlibParam;
import com.qu.modules.web.service.IQsubjectlibService;
import com.qu.modules.web.vo.QsubjectlibPageVo;
import com.qu.modules.web.vo.QsubjectlibVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description: 题库题目表
 * @Author: jeecg-boot
 * @Date: 2021-03-22
 * @Version: V1.0
 */
@Slf4j
@Service
public class QsubjectlibServiceImpl extends ServiceImpl<QsubjectlibMapper, Qsubjectlib> implements IQsubjectlibService {

    @Autowired
    private QsubjectlibMapper qsubjectlibMapper;

    @Autowired
    private QoptionlibMapper qoptionlibMapper;

    @Override
    public QsubjectlibPageVo queryPageList(QsubjectlibParam qsubjectlibParam, Integer pageNo, Integer pageSize) {
        QsubjectlibPageVo qsubjectlibPageVo = new QsubjectlibPageVo();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("subName", qsubjectlibParam.getSubName());
            params.put("remark", qsubjectlibParam.getRemark());
            params.put("startRow", (pageNo - 1) * pageSize);
            params.put("pageSize", pageSize);
            int total = qsubjectlibMapper.queryPageListCount(params);
            List<Qsubjectlib> qsubjectlibList = qsubjectlibMapper.queryPageList(params);
            qsubjectlibPageVo.setTotal(total);
            qsubjectlibPageVo.setQsubjectlibList(qsubjectlibList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return qsubjectlibPageVo;
    }

    @Override
    public QsubjectlibVo saveQsubjectlib(QsubjectlibAddParam qsubjectlibAddParam) {
        QsubjectlibVo qsubjectlibVo = new QsubjectlibVo();
        Qsubjectlib qsubjectlib = new Qsubjectlib();

        String columnType = qsubjectlibAddParam.getColumnType();
        if (StringUtils.isNotBlank(columnType)) {
            qsubjectlib.setColumnTypeDatabase(conversionColumnType(qsubjectlibAddParam.getColumnType()));
        }
        BeanUtils.copyProperties(qsubjectlibAddParam, qsubjectlib);
        qsubjectlib.setDel(0);
        qsubjectlib.setCreater(1);
        qsubjectlib.setCreateTime(new Date());
        qsubjectlib.setUpdater(1);
        qsubjectlib.setUpdateTime(new Date());
        qsubjectlibMapper.insert(qsubjectlib);
        //拷贝到Vo对象
        BeanUtils.copyProperties(qsubjectlib, qsubjectlibVo);
        //选项
        List<Qoptionlib> qoptionlibList = new ArrayList<>();
        List<QoptionlibParam> qoptionlibParamList = qsubjectlibAddParam.getQoptionlibParamList();
        if (null != qoptionlibParamList) {
            int i = 1;
            for (QoptionlibParam qoptionlibParam : qoptionlibParamList) {
                Qoptionlib qoptionlib = new Qoptionlib();
                BeanUtils.copyProperties(qoptionlibParam, qoptionlib);
                qoptionlib.setSubId(qsubjectlib.getId());
                qoptionlib.setOpOrder(i);
                qoptionlib.setDel(0);
                qoptionlib.setCreater(1);
                qoptionlib.setCreateTime(new Date());
                qoptionlib.setUpdater(1);
                qoptionlib.setUpdateTime(new Date());
                qoptionlibMapper.insert(qoptionlib);
                i++;
                qoptionlibList.add(qoptionlib);
            }
            qsubjectlibVo.setQoptionlibList(qoptionlibList);
        }

        return qsubjectlibVo;
    }

    private String conversionColumnType(String columnType) {
        switch (columnType) {
            case "字符串":
            case "数组":
                return "varchar";
            case "数值":
                return "int";
            default:
                break;
        }
        return null;
    }

    @Override
    public QsubjectlibVo updateQsubjectlibById(QsubjectlibEditParam qsubjectlibEditParam) {
        QsubjectlibVo qsubjectlibVo = new QsubjectlibVo();
        Qsubjectlib qsubjectlib = new Qsubjectlib();

        String columnType = qsubjectlibEditParam.getColumnType();
        if (StringUtils.isNotBlank(columnType)) {
            qsubjectlib.setColumnTypeDatabase(conversionColumnType(qsubjectlibEditParam.getColumnType()));
        }
        BeanUtils.copyProperties(qsubjectlibEditParam, qsubjectlib);
        qsubjectlib.setUpdater(1);
        qsubjectlib.setUpdateTime(new Date());
        qsubjectlibMapper.updateById(qsubjectlib);
        //拷贝到Vo对象
        BeanUtils.copyProperties(qsubjectlib, qsubjectlibVo);
        //删除以前的所有选项
        int delCount = qoptionlibMapper.deleteOptionBySubId(qsubjectlib.getId());
        //选项
        List<Qoptionlib> qoptionlibList = new ArrayList<>();
        List<QoptionlibParam> qoptionlibParamList = qsubjectlibEditParam.getQoptionlibParamList();
        if (null != qoptionlibParamList) {
            int i = 1;
            for (QoptionlibParam qoptionlibParam : qoptionlibParamList) {
                Qoptionlib qoptionlib = new Qoptionlib();
                BeanUtils.copyProperties(qoptionlibParam, qoptionlib);
                qoptionlib.setSubId(qsubjectlibEditParam.getId());
                qoptionlib.setOpOrder(i);
                qoptionlib.setDel(0);
                qoptionlib.setCreater(1);
                qoptionlib.setCreateTime(new Date());
                qoptionlib.setUpdater(1);
                qoptionlib.setUpdateTime(new Date());
                qoptionlibMapper.insert(qoptionlib);
                i++;
                qoptionlibList.add(qoptionlib);
            }
            qsubjectlibVo.setQoptionlibList(qoptionlibList);
        }

        return qsubjectlibVo;
    }

    @Override
    public Boolean removeQsubjectlibById(Integer id) {
        Boolean delFlag = true;
        try {
            Qsubjectlib subjectlib = new Qsubjectlib();
            subjectlib.setId(id);
            subjectlib.setDel(1);
            subjectlib.setUpdater(1);
            subjectlib.setUpdateTime(new Date());
            qsubjectlibMapper.updateById(subjectlib);
        } catch (Exception e) {
            delFlag = false;
            log.error(e.getMessage(), e);
        }
        return delFlag;
    }

    @Override
    public QsubjectlibVo getQsubjectlibById(Integer id) {
        QsubjectlibVo qsubjectlibVo = new QsubjectlibVo();
        try {
            Qsubjectlib qsubjectlib = qsubjectlibMapper.selectById(id);
            BeanUtils.copyProperties(qsubjectlib, qsubjectlibVo);
            List<Qoptionlib> qoptionlibList = qoptionlibMapper.selectQoptionlibBySubId(qsubjectlib.getId());
            qsubjectlibVo.setQoptionlibList(qoptionlibList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return qsubjectlibVo;
    }


}
