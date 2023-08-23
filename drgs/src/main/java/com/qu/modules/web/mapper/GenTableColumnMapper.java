package com.qu.modules.web.mapper;


import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface GenTableColumnMapper {


    /**
     * 获取所有表
     * @return
     */
    public List<String> getSQLQueryTables();

    public String getSQLQueryComment(String tableName, String columnName);

    List<LinkedHashMap<String, Object>> superSelect(String sql);

    int superCount(String sql);

    int selectCount(@Param(value = "paramMap") Map<String, Object> map, @Param(value = "table_name") String tableName);

    void insertData(@Param(value = "map") Map<String, String> map, @Param(value = "table_name") String tableName);

    void updateData(@Param(value = "map") Map<String, Object> map, @Param(value = "paramMap") Map<String, Object> paramMap, @Param(value = "table_name") String tableName);

    String selectId(@Param(value = "paramMap") Map<String, Object> map, @Param(value = "table_name") String tableName);

    void createTable(String sql);

    void insertQSingleDiseaseTake(@Param(value = "map") Map<String, String> map, @Param(value = "table_name") String tableName);

}
