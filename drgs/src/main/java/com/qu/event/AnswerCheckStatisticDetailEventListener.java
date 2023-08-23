package com.qu.event;


import com.google.common.collect.Lists;
import com.qu.constant.QsubjectConstant;
import com.qu.constant.QuestionConstant;
import com.qu.modules.web.dto.AnswerCheckStatisticDetailEventDto;
import com.qu.modules.web.entity.*;
import com.qu.modules.web.service.IAnswerCheckStatisticDetailService;
import com.qu.modules.web.service.ITbDepService;
import com.qu.modules.web.vo.SubjectVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AnswerCheckStatisticDetailEventListener implements ApplicationListener<AnswerCheckStatisticDetailEvent> {

    @Resource
    private IAnswerCheckStatisticDetailService answerCheckStatisticDetailService;

    @Autowired
    private ITbDepService tbDepService;

    ArrayList<String> integers = Lists.newArrayList("13","14","16","19","20","21");

    @Async
    @Override
    public void onApplicationEvent(AnswerCheckStatisticDetailEvent event) {
        AnswerCheckStatisticDetailEventDto answerCheckStatisticDetailEventDto = event.getAnswerCheckStatisticDetailEventDto();
        Question question = answerCheckStatisticDetailEventDto.getQuestion();
        List<SubjectVo> subjectList = answerCheckStatisticDetailEventDto.getSubjectList();
        Map<String, String> mapCache = answerCheckStatisticDetailEventDto.getMapCache();
        AnswerCheck answerCheck = answerCheckStatisticDetailEventDto.getAnswerCheck();
        String checkMonth = mapCache.get("check_month");
        String checkedDept  = mapCache.get("checked_dept");
        String checkedPatientName  = mapCache.get("checked_patient_name");
        String caseId  = mapCache.get("caseId");
        String checkedDoct  = mapCache.get("checked_doct");
        TbDep tbDep = tbDepService.getById(checkedDept);

        Map<SubjectVo, String> subjectGroupMap = subjectList.stream().filter(s -> {
            if (QsubjectConstant.SUB_TYPE_GROUP.equals(s.getSubType()) || QsubjectConstant.SUB_TYPE_GROUP_SCORE.equals(s.getSubType())) {
                return true;
            }
            return false;
        }).collect(Collectors.toMap(q->q, SubjectVo::getGroupIds));

        List<AnswerCheckStatisticDetail> saveList = Lists.newArrayList();
        for (int i = 0; i < subjectList.size(); i++) {
            SubjectVo qsubjectDynamicTable = subjectList.get(i);
            String subType = qsubjectDynamicTable.getSubType();
            Integer del = qsubjectDynamicTable.getDel();
            String optionColumnName = mapCache.get(qsubjectDynamicTable.getColumnName());
            if (QuestionConstant.SUB_TYPE_GROUP.equals(subType) || QuestionConstant.SUB_TYPE_TITLE.equals(subType)
                    || QuestionConstant.DEL_DELETED.equals(del) || optionColumnName == null
                    || StringUtils.isBlank(optionColumnName)) {
                continue;
            }
            if(!integers.contains(subType)){
                continue;
            }
            if(!subType.equals("21") && "y".equals(optionColumnName)){
                continue;
            }

            AnswerCheckStatisticDetail detail = new AnswerCheckStatisticDetail();
            detail.setAnswerCheckId(answerCheck.getId());
            detail.setCheckMonth(checkMonth);
            detail.setQuId(question.getId());
            detail.setAnswerUser(answerCheckStatisticDetailEventDto.getAnswerUser());
            detail.setAnswerUserName(answerCheckStatisticDetailEventDto.getAnswerUserName());
            detail.setAnswerDeptId(answerCheckStatisticDetailEventDto.getDepId());
            detail.setAnswerDeptName(answerCheckStatisticDetailEventDto.getDepName());
            detail.setCheckedDeptId(checkedDept);
            if(Objects.nonNull(tbDep)){
                detail.setCheckedDeptName(tbDep.getDepname());
            }
            detail.setPatientName(checkedPatientName);
            detail.setCaseId(caseId);
            detail.setHospitalDoctor(checkedDoct);

            out:
            for (Map.Entry<SubjectVo, String> entity : subjectGroupMap.entrySet()) {
                String value = entity.getValue();
                SubjectVo key = entity.getKey();
                String[] split = value.split(",");
                for (String s : split) {
                    if(s.equals(String.valueOf(qsubjectDynamicTable.getId()))){
                        detail.setGroupId(key.getId());
                        detail.setGroupColumnName(key.getColumnName());
                        detail.setGroupText(key.getSubName());
                        detail.setGroupScore(key.getScore().setScale(1,BigDecimal.ROUND_UP).toPlainString());
                        break out;
                    }
                }
            }

            detail.setSubjectText(qsubjectDynamicTable.getSubName());
            detail.setSubjectColumnName(qsubjectDynamicTable.getColumnName());
            detail.setSubjectId(qsubjectDynamicTable.getId());
            detail.setSubjectScoreType(qsubjectDynamicTable.getScoreType());
            detail.setAnswerValue(optionColumnName);
            List<Qoption> optionList = qsubjectDynamicTable.getOptionList();
            for (Qoption qoption : optionList) {
                if(optionColumnName.equals(qoption.getOpValue())){
                    BigDecimal optionScore = qoption.getOptionScore();
                    if(optionScore!=null){
                        detail.setAnswerScore(optionScore.setScale(1,BigDecimal.ROUND_UP).toPlainString());
                    }
                    String opName = qoption.getOpName();
                    if(StringUtils.isNotBlank(opName)){
                        detail.setAnswerText(opName);
                    }
                }
            }
            String columnNameMark = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark");
            if (StringUtils.isNotBlank(columnNameMark)) {
                detail.setMark(columnNameMark);
            }

            String columnNameMarkImg = mapCache.get(qsubjectDynamicTable.getColumnName() + "_mark_img");
            if (StringUtils.isNotBlank(columnNameMarkImg)) {
                detail.setMarkImg(columnNameMarkImg);
            }
            detail.setDel(QsubjectConstant.DEL_NORMAL);
            detail.setSource(answerCheckStatisticDetailEventDto.getSource());
            saveList.add(detail);
        }
        answerCheckStatisticDetailService.saveBatch(saveList);
    }


}
