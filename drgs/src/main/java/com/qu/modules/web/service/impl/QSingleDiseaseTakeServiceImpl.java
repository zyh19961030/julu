package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.modules.web.entity.Getdzbxx;
import com.qu.modules.web.entity.QSingleDiseaseTake;
import com.qu.modules.web.mapper.GenTableColumnMapper;
import com.qu.modules.web.mapper.QSingleDiseaseTakeMapper;
import com.qu.modules.web.mapper.QsubjectMapper;
import com.qu.modules.web.service.GetdzbxxService;
import com.qu.modules.web.service.IQSingleDiseaseTakeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class QSingleDiseaseTakeServiceImpl extends ServiceImpl<QSingleDiseaseTakeMapper, QSingleDiseaseTake> implements IQSingleDiseaseTakeService {

    @Autowired
    private GenTableColumnMapper genTableColumnMapper;

    @Autowired
    private QsubjectMapper qsubjectMapper;

    @Autowired
    private GetdzbxxService getdzbxxService;


    @Override
    public void getGetdzbxxInsertDrgs() {
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -5);
        Date date_6 = calendar.getTime();
        //六天前
        String time_6 = formatter.format(date_6);
        calendar.add(Calendar.DATE, -1);
        Date date_7 = calendar.getTime();
        //七天前
        String time_7 = formatter.format(date_7);
        //获取视图中6天前上报的单病种数据集合
//        time_6 = "20230613";
//        time_7 = "20220101";
        List<HashMap<Object, Object>> list = getdzbxxService.queryDRGIDList(time_6, time_7);
        if (list != null && list.size() > 0) {
            for (HashMap<Object, Object> map_all : list) {
                if (!map_all.isEmpty()) {
                    String DRGID = map_all.get("DRGID")+"";
                    List<Getdzbxx> getdzbxxList = (List<Getdzbxx>)map_all.get("getdzbxxList");
                    //获取TQMS中单病种子表的字段
                    List<String> columnNameList = qsubjectMapper.querySubColumnNameByTableName("DRGS_"+DRGID);
                    HashMap map = new HashMap<>();
                    for (Getdzbxx getdzbxx : getdzbxxList) {
                        String colid = getdzbxx.getCOLID();
                        String colcode = getdzbxx.getCOLCODE();
                        //只解析存在于TQMS子表字段中的数据
                        if (columnNameList.contains(colid) && colcode != null && colcode.length() > 0){
                            map.put("`"+colid+"`", colcode);
                        }
                    }
                    if (!map.isEmpty()) {
                        try {
                            genTableColumnMapper.insertData(map, "drgs_"+DRGID.toLowerCase());
                            log.info("添加单病种子表drgs_"+DRGID.toLowerCase()+"数据。");
                        } catch (Exception e) {
                            log.error(e.getMessage()+map.get("`caseId`")+"！！！！！", e);
                        }
                    }
                }
            }
        }
    }

}
