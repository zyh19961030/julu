package com.qu.modules.web.service;

import com.qu.modules.web.entity.TbData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 数据字典表
 * @Author: jeecg-boot
 * @Date:   2022-09-30
 * @Version: V1.0
 */
public interface ITbDataService extends IService<TbData> {

    List<TbData> selectByDataType(String dataTypeQuestionCheckCategory);


}
