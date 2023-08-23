package com.qu.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ZbdjMapper extends BaseMapper {

    List<Map<String,String>> selecthospitalMedicalRecord(@Param("sql") String sql);

    int insertZbdj(@Param("sql") String sql);
}
