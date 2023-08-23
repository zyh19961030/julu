package com.qu.modules.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qu.modules.web.entity.Question;
import com.qu.modules.web.mapper.QuestionMapper;
import com.qu.modules.web.mapper.ZbdjMapper;
import com.qu.modules.web.service.IZbdjService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ZbdjServiceImpl extends ServiceImpl<QuestionMapper,Question> implements IZbdjService {

    @Autowired
    private ZbdjMapper zbdjMapper;

    @Override
    public Result<Boolean> batchGeneerate(String tableName) {
        Result<Boolean> result = new Result<Boolean>();
        try {
            //查询原始数据
            String selectsql = "select BAH,CASE CYKB WHEN '05.01' THEN '妇科' WHEN '05.02' THEN '产科' WHEN '04.01' THEN '普外科' WHEN '10' THEN '眼科' WHEN '04.02' THEN '神经外科' WHEN '03.04' THEN '心内科' WHEN '07' THEN '儿科' WHEN '03.02' THEN '消化内科' WHEN '03.03' THEN '神经内科' WHEN '19' THEN '肿瘤科' WHEN '03.06' THEN '肾病科' WHEN '03.05' THEN '血液内科' WHEN '04.04' THEN '泌尿外科' WHEN '04.03' THEN '骨科' WHEN '03.07' THEN '内分泌科' WHEN '12' THEN '口腔科' WHEN '17' THEN '结核病科' WHEN '11' THEN '耳鼻喉科' WHEN '07.01' THEN '新生儿科' WHEN '04.05' THEN '胸外科' WHEN '03.10' THEN '老年病科' WHEN '03.01' THEN '呼吸内科' WHEN '28' THEN '重症医学科' WHEN '20' THEN '急诊医学科' END as CYKB," +
                    "CYBF,XM,SFZH,CASE XB WHEN 1 THEN '男' WHEN 2 THEN '女' END as XB ,DATE_FORMAT(CSRQ,'%Y-%m-%d %H:%i:%s') as CSRQ,NL,DATE_FORMAT(RYSJ,'%Y-%m-%d %H:%i:%s') as RYSJ,CASE WHEN BLZDF>0 THEN 'y' ELSE 'n' END as BLZDF,CASE WHEN SSJCZRQ1 is null THEN 'n' ELSE 'y' END as SSJCZRQ1 " +
                    "from hospital_medical_record_202101 ";
            List<Map<String, String>> hospitalMedicalRecordList = zbdjMapper.selecthospitalMedicalRecord(selectsql);
            //循环赋值
            //插入对应的表
            Map<String,Integer> CYKBMap = new HashMap<>();
            for(int i=0;i<hospitalMedicalRecordList.size();i++){
                Map<String,String> hospitalMedicalRecord = hospitalMedicalRecordList.get(i);
                if("zbdj_bingankebinganguanli".equals(tableName)){
                    insertbingankebinganguanliData(tableName,hospitalMedicalRecord,i);
                }else if ("zbdj_linchaungbinganguanli".equals(tableName)){
                    insertlinchaungbinganguanliData(tableName,hospitalMedicalRecord,i,CYKBMap);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setResult(false);
            result.setMessage(e.getMessage());
            return result;
        }
        result.setResult(true);
        result.setMessage("数据创建成功!");
        return result;
    }

    public void insertbingankebinganguanliData(String tableName,Map<String,String> hospitalMedicalRecord,int i){
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ");
        sql.append(tableName);
        sql.append("( caseId,KSMC,BFN,XM,SFZ,SEX,CSRQ,AGE,INTIME,a,b,c,d,e,f,g,h,i,j ) values (");
        sql.append("'" + hospitalMedicalRecord.get("BAH") + "',");
        sql.append("'" + hospitalMedicalRecord.get("CYKB") + "',");
        sql.append("'" + hospitalMedicalRecord.get("CYBF") + "',");
        sql.append("'" + hospitalMedicalRecord.get("XM") + "',");
        sql.append("'" + hospitalMedicalRecord.get("SFZH") + "',");
        sql.append("'" + hospitalMedicalRecord.get("XB") + "',");
        sql.append("'" + hospitalMedicalRecord.get("CSRQ") + "',");
        sql.append("'" + hospitalMedicalRecord.get("NL") + "',");
        sql.append("'" + hospitalMedicalRecord.get("RYSJ") + "', ");
        sql.append("'" + ((i+1)%5==0?"n":"y") + "', ");
        sql.append("'" + ((i+1)%50==0?"n":"y") + "', ");
        sql.append("'" + ((i+1)%100>=93?"n":"y") + "', ");
        sql.append("'" + ((i+1)%100>=99?"n":"y") + "', ");
        sql.append("'" + ((i+1)%100>=93?"n":"y") + "', ");
        sql.append("'" + ((i+1)%100>=99?"n":"y") + "', ");
        sql.append("'" + ((i+1)%10000>=9999?"n":"y") + "', ");
        sql.append("'" + "y" + "', ");
        sql.append("'" + "y" + "', ");
        sql.append("'" + ((i+1)%100>=91?"n":"y") + "' ");
        sql.append(")");
        log.info("-----insert sql:{}", sql.toString());
        zbdjMapper.insertZbdj(sql.toString());
    }

    public void insertlinchaungbinganguanliData(String tableName,Map<String,String> hospitalMedicalRecord,int i,Map<String,Integer> CYKBMap){
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ");
        sql.append(tableName);
        sql.append("( caseId,KSMC,BFN,XM,SFZ,SEX,CSRQ,AGE,INTIME,a,b,d,e,f,h,j,l,o,p,r,s,u,w,x ) values (");
        sql.append("'" + hospitalMedicalRecord.get("BAH") + "',");
        sql.append("'" + hospitalMedicalRecord.get("CYKB") + "',");
        sql.append("'" + hospitalMedicalRecord.get("CYBF") + "',");
        sql.append("'" + hospitalMedicalRecord.get("XM") + "',");
        sql.append("'" + hospitalMedicalRecord.get("SFZH") + "',");
        sql.append("'" + hospitalMedicalRecord.get("XB") + "',");
        sql.append("'" + hospitalMedicalRecord.get("CSRQ") + "',");
        sql.append("'" + hospitalMedicalRecord.get("NL") + "',");
        sql.append("'" + hospitalMedicalRecord.get("RYSJ") + "', ");
        //a
        sql.append("'" + ((i+1)%100>=99?"n":"y") + "', ");
        //手术患者 SSJCZRQ1
        sql.append("'" + hospitalMedicalRecord.get("SSJCZRQ1") + "', ");
        //d
        sql.append("'" + ((i+1)%100>=99?"n":"y") + "', ");
        //e
        sql.append("'" + ((i+1)%100>=99?"n":"y") + "', ");
        //患者接受CT/MRI检查，根据科室判断
        String flag = "";
        if(hospitalMedicalRecord.get("CYKB").equals("神经外科")){
            flag = "y";
        }else if(hospitalMedicalRecord.get("CYKB").equals("骨科")){
            Integer x = CYKBMap.get("骨科");
            if(x==null){
                CYKBMap.put("骨科",1);
                x=0;
            }else{
                CYKBMap.put("骨科",x+1);
            }
            flag = x%50>=49?"n":"y";
        }else if(hospitalMedicalRecord.get("CYKB").equals("普外科")){
            flag = "y";
        }else if(hospitalMedicalRecord.get("CYKB").equals("呼吸内科")){
            flag = "y";
        }else if(hospitalMedicalRecord.get("CYKB").equals("儿科")){
            Integer x = CYKBMap.get("儿科");
            if(x==null){
                CYKBMap.put("儿科",1);
                x=0;
            }else{
                CYKBMap.put("儿科",x+1);
            }
            flag = x%10>=3?"n":"y";
        }else if(hospitalMedicalRecord.get("CYKB").equals("心内科")){
            Integer x = CYKBMap.get("心内科");
            if(x==null){
                CYKBMap.put("心内科",1);
                x=0;
            }else{
                CYKBMap.put("心内科",x+1);
            }
            flag = x%5>=4?"n":"y";
        }else if(hospitalMedicalRecord.get("CYKB").equals("耳鼻喉科")){
            Integer x = CYKBMap.get("耳鼻喉科");
            if(x==null){
                CYKBMap.put("耳鼻喉科",1);
                x=0;
            }else{
                CYKBMap.put("耳鼻喉科",x+1);
            }
            flag = x%25>=24?"n":"y";
        }else if(hospitalMedicalRecord.get("CYKB").equals("妇科")){
            Integer x = CYKBMap.get("妇科");
            if(x==null){
                CYKBMap.put("妇科",1);
                x=0;
            }else{
                CYKBMap.put("妇科",x+1);
            }
            flag = x%4>=3?"n":"y";
        }else if(hospitalMedicalRecord.get("CYKB").equals("产科")){
            Integer x = CYKBMap.get("产科");
            if(x==null){
                CYKBMap.put("产科",1);
                x=0;
            }else{
                CYKBMap.put("产科",x+1);
            }
            flag = x%10>=1?"n":"y";
        }else{
            Integer x = CYKBMap.get(hospitalMedicalRecord.get("CYKB"));
            if(x==null){
                CYKBMap.put(hospitalMedicalRecord.get("CYKB"),1);
                x=0;
            }else{
                CYKBMap.put(hospitalMedicalRecord.get("CYKB"),x+1);
            }
            flag = x%20>=11?"n":"y";
        }
        //f
        sql.append("'" + flag + "', ");
        //h
        sql.append("'" + hospitalMedicalRecord.get("BLZDF") + "', ");
        //i
        sql.append("'" + "y" + "', ");
        //j
        sql.append("'" + "y" + "', ");
        //
        sql.append("'" + "y" + "', ");
        sql.append("'" + "y" + "', ");
        sql.append("'" + "y" + "', ");
        sql.append("'" + "y" + "', ");
        sql.append("'" + "y" + "', ");
        sql.append("'" + "y" + "', ");
        sql.append("'" + "y" + "' ");
        sql.append(")");
        log.info("-----insert sql:{}", sql.toString());
        zbdjMapper.insertZbdj(sql.toString());
    }
}
