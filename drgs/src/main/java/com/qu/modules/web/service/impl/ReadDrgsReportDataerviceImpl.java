package com.qu.modules.web.service.impl;

import com.google.common.collect.Lists;
import com.qu.constant.QSingleDiseaseTakeConstant;
import com.qu.modules.web.entity.DrgsReportData;
import com.qu.modules.web.entity.QSingleDiseaseTake;
import com.qu.modules.web.mapper.GenTableColumnMapper;
import com.qu.modules.web.mapper.QsubjectMapper;
import com.qu.modules.web.service.ReadDrgsReportDataervice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class ReadDrgsReportDataerviceImpl implements ReadDrgsReportDataervice {

    @Resource
    private GenTableColumnMapper genTableColumnMapper;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private QsubjectMapper qsubjectMapper;

    @Override
    public void readQSingleDiseaseTake() {
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -5);
        Date date_6 = calendar.getTime();
        String time_6 = formatter.format(date_6);
        calendar.add(Calendar.DATE, -1);
        Date date_7 = calendar.getTime();
        String time_7 = formatter.format(date_7);
        //获取视图中6天前上报的单病种数据集合
        time_6 = "2022-01-01 00:00:00";
        time_7 = "2023-06-12 00:00:00";
        //获取单病种数据
//        List<DrgsReportData> drgsReportDatas = mongoTemplate.find(new Query(new Criteria().andOperator(Criteria.where("reportTime").gte(time_6),Criteria.where("reportTime").lte(time_7))), DrgsReportData.class);
        List<DrgsReportData> drgsReportDatas = mongoTemplate.find(new Query(Criteria.where("reportTime").gte(time_6)), DrgsReportData.class);
        for (DrgsReportData drgsReportData : drgsReportDatas) {
            Map<String, Object> reportContent = drgsReportData.getReportContent();
            //子表表名
            String diseaseType = reportContent.get("diseaseType").toString();
            //子表数据
            Map<String, Object> data = (Map<String, Object>)reportContent.get("data");
            //合并三份子表数据
            Map<String, Object> rawData;
            if (reportContent.get("rawData") != null) {
                rawData = (Map<String, Object>)reportContent.get("rawData");
                Set<String> rawDataKey = rawData.keySet();
                for (String key: rawDataKey) {
                    String value = rawData.get(key) + "";
                    if (!"".equals(value) && value.length() > 0) {
                        data.put(key, value);
                    }
                }
            }
            Map<String, Object> funcResult;
            if (drgsReportData.getFuncResult() != null) {
                funcResult = (Map<String, Object>)drgsReportData.getFuncResult();
                Set<String> funcResultKey = funcResult.keySet();
                for (String key: funcResultKey) {
                    String value = funcResult.get(key) + "";
                    if (!"".equals(value) && value.length() > 0) {
                        data.put(key, value);
                    }
                }
            }
            //表字段加``
            Map<String, String> dataNew = new HashMap<>();
            if (!data.isEmpty()) {
                //患者姓名
                data.put("xm", drgsReportData.getPatientName());
                //填报时间
                data.put("answer_datetime", drgsReportData.getReportTime());
                //主表id
                String id = drgsReportData.getId();
                data.put("summary_mapping_table_id", id);

                Set<String> dataKey = data.keySet();
                //获取TQMS中单病种子表的字段
                List<String> columnNameList = qsubjectMapper.querySubColumnNameByTableName("DRGS_" + diseaseType);
                for (String key: dataKey) {
                    String value = data.get(key)+"";
                    //只解析存在于TQMS子表字段中的数据
                    if (columnNameList.contains(key) && !"".equals(value) && value.length() > 0){
                        dataNew.put("`"+key+"`", value);
                    }
                }
            }
            //插入子表数据
            if (!dataNew.isEmpty()) {
                try {
                    genTableColumnMapper.insertQSingleDiseaseTake(dataNew, "drgs_"+diseaseType);
                    log.info("添加单病种子表drgs_"+diseaseType.toLowerCase()+"数据。");
                } catch (Exception e) {
                    log.error(e.getMessage()+dataNew.get("`caseId`")+"！！！！！", e);
                }
            }
        }
    }

}
