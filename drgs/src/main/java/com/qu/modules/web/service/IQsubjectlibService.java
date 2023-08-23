package com.qu.modules.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.Qsubject;
import com.qu.modules.web.entity.Qsubjectlib;
import com.qu.modules.web.param.QsubjectlibAddParam;
import com.qu.modules.web.param.QsubjectlibEditParam;
import com.qu.modules.web.param.QsubjectlibParam;
import com.qu.modules.web.vo.QsubjectlibPageVo;
import com.qu.modules.web.vo.QsubjectlibVo;

import java.util.List;

/**
 * @Description: 题库题目表
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
public interface IQsubjectlibService extends IService<Qsubjectlib> {

    QsubjectlibPageVo queryPageList(QsubjectlibParam qsubjectlibParam, Integer pageNo, Integer pageSize);

    QsubjectlibVo saveQsubjectlib(QsubjectlibAddParam qsubjectlibAddParam);

    QsubjectlibVo updateQsubjectlibById(QsubjectlibEditParam qsubjectlibEditParam);

    Boolean removeQsubjectlibById(Integer id);

    QsubjectlibVo getQsubjectlibById(Integer id);



}
