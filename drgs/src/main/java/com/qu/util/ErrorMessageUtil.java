package com.qu.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qu.modules.web.entity.QSingleDiseaseTake;
import com.qu.modules.web.entity.Qsubject;
import com.qu.modules.web.mapper.QSingleDiseaseTakeMapper;
import com.qu.modules.web.mapper.QsubjectMapper;
import com.qu.modules.web.param.SingleDiseaseAnswer;
import com.qu.modules.web.vo.CountryExamineReasonVo;
import com.qu.modules.web.vo.ReportFailureRecordParameterVo;
import com.qu.modules.web.vo.ReportFailureRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class ErrorMessageUtil {

    @Autowired
    QSingleDiseaseTakeMapper qSingleDiseaseTakeMapper;

    @Autowired
    QsubjectMapper qsubjectMapper;

    private static ErrorMessageUtil errorMessageUtil;

    @PostConstruct
    public void inti(){
        errorMessageUtil = this;
        errorMessageUtil.qsubjectMapper = this.qsubjectMapper;
        errorMessageUtil.qSingleDiseaseTakeMapper = this.qSingleDiseaseTakeMapper;
    }


    /**
     * 获取上报失败问卷对应问题答案
     * @param reportFailureRecordVoList
     */
    public static List<ReportFailureRecordParameterVo> getErrorSubjectAnswer(List<ReportFailureRecordVo> reportFailureRecordVoList) {
        List<ReportFailureRecordParameterVo> ReportFailureRecordParameterVoList = new ArrayList<>();
        reportFailureRecordVoList.forEach(reportFailureRecordVo -> {
            List<CountryExamineReasonVo> countryExamineReasons7 = new ArrayList<>();
            List<CountryExamineReasonVo> countryExamineReasons9 = new ArrayList<>();
            ReportFailureRecordParameterVo reportFailureRecordParameterVo = new ReportFailureRecordParameterVo();
            CountryExamineReasonVo countryExamineReasonVo = new CountryExamineReasonVo();
            Integer status = reportFailureRecordVo.getStatus();
            Integer id = reportFailureRecordVo.getId();
            String countryExamineReason = reportFailureRecordVo.getCountryExamineReason();
            if (countryExamineReason != null && !countryExamineReason.isEmpty()) {
                if (status == 7) {
                    JSONObject jsonObject = JSON.parseObject(countryExamineReason);
                    String errorMessage = jsonObject.getString("errorMessage");
                    String[] split = errorMessage.split(",");
                    for (int i = 0; i < split.length; i++) {
                        String answer = null;
                        String subjectName;
                        String examineReason;
                        countryExamineReasonVo = new CountryExamineReasonVo();
                        String[] split1 = split[i].split(":");
                        String columnName;
                        if (i == 0) {
                            columnName = split1[0].substring(2, split1[0].length() - 1);
                        } else {
                            columnName = split1[0].substring(1, split1[0].length() - 1);
                        }
                        examineReason = split1[1].substring(1,split1[1].length() - 1);
                        Integer questionId = reportFailureRecordVo.getQuestionId();
                        subjectName = errorMessageUtil.qsubjectMapper.querySubNameByQuidAndColumnName(questionId, columnName);
                        if (examineReason.equals(("该项为必填项，请进行填写"))) {
                        } else {
                            QSingleDiseaseTake qSingleDiseaseTake = errorMessageUtil.qSingleDiseaseTakeMapper.queryAnswerJsonByStatus(id);
                            String answerJson = (String)qSingleDiseaseTake.getAnswerJson();
                            List<SingleDiseaseAnswer> singleDiseaseAnswerList = JSON.parseArray(answerJson, SingleDiseaseAnswer.class);
                            if (singleDiseaseAnswerList != null && !singleDiseaseAnswerList.isEmpty()) {
                                for (SingleDiseaseAnswer a : singleDiseaseAnswerList) {
                                    String subColumnName = a.getSubColumnName();
                                    if (columnName.equals(subColumnName)) {
                                        answer = a.getSubValue();
                                    }
                                }
                            }
                        }
                        countryExamineReasonVo.setSubjectName(subjectName);
                        countryExamineReasonVo.setAnswer(answer);
                        countryExamineReasonVo.setExamineReason(examineReason);
                        countryExamineReasons7.add(countryExamineReasonVo);
                    }
                    reportFailureRecordParameterVo.setCountryExamineReason(countryExamineReasons7);
                } else if (status == 9) {
                    countryExamineReasonVo.setExamineReason("上报出错!");
                    countryExamineReasons9.add(countryExamineReasonVo);
                    reportFailureRecordParameterVo.setCountryExamineReason(countryExamineReasons9);
                }
                reportFailureRecordParameterVo.setId(reportFailureRecordVo.getId());
                reportFailureRecordParameterVo.setHospitalInNo(reportFailureRecordVo.getHospitalInNo());
                reportFailureRecordParameterVo.setPatientName(reportFailureRecordVo.getPatientName());
                reportFailureRecordParameterVo.setQuestionName(reportFailureRecordVo.getQuestionName());
                reportFailureRecordParameterVo.setDepartmentName(reportFailureRecordVo.getDepartmentName());
                reportFailureRecordParameterVo.setCountryReportTime(reportFailureRecordVo.getCountryReportTime());
                reportFailureRecordParameterVo.setWriteTime(reportFailureRecordVo.getWriteTime());
                ReportFailureRecordParameterVoList.add(reportFailureRecordParameterVo);
            }
        });
        return ReportFailureRecordParameterVoList;
    }

}
