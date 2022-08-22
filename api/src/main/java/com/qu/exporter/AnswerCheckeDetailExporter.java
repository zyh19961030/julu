package com.qu.exporter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.google.common.collect.Lists;
import com.qu.modules.web.vo.AnswerCheckDetailListVo;
import com.qu.util.ExcelExportUtil;


public class AnswerCheckeDetailExporter implements ExcelDataBuilder {
    @Override
    public void build(HSSFSheet sheet, HSSFRow row, Object data) {
        if (data == null) {
            return;
        }
        AnswerCheckDetailListVo tmp = (AnswerCheckDetailListVo) data;
        HSSFCell cell = null;
        List<String> keyList = createTitle(sheet, row, cell, tmp.getFieldItems());
        List<LinkedHashMap<String, Object>> detailDataList = tmp.getDetailDataList();
        for (int i = 0; i < detailDataList.size(); i++) {
            row = sheet.createRow(i+2);

            Map<String, Object> project = detailDataList.get(i);
//            Map<String, Object> project = Maps.newConcurrentMap();

            for (int j = 0; j < keyList.size(); j++) {
                String s = keyList.get(j);
                cell = row.createCell(j);
                Object value = project.get(s);
                if (value == null) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue(value.toString());
                }
            }

        }



    }

    private List<String> createTitle(HSSFSheet sheet, HSSFRow row, HSSFCell cell, List<LinkedHashMap<String, Object>> fieldItems) {
        row = sheet.createRow(0);
        List<String> keyList = Lists.newArrayList();
        for (int i = 0; i < fieldItems.size(); i++) {
            Object fieldChildListObj = fieldItems.get(i).get("fieldChildList");
            if(fieldChildListObj==null){
                ExcelExportUtil.fillInCell(row, cell, i, fieldItems.get(i).getOrDefault("fieldTxt","").toString());

                CellRangeAddress region = new CellRangeAddress(0, 1, i, i);
                sheet.addMergedRegion(region);
                keyList.add(fieldItems.get(i).getOrDefault("fieldId","").toString());
            }else{
                List<LinkedHashMap<String,Object>> fieldChildList = (List<LinkedHashMap<String,Object>>)fieldChildListObj;
                if(fieldChildList.isEmpty()){
                    ExcelExportUtil.fillInCell(row, cell, i, fieldItems.get(i).getOrDefault("fieldTxt","").toString());

                    CellRangeAddress region = new CellRangeAddress(0, 1, i, i);
                    sheet.addMergedRegion(region);
                    keyList.add(fieldItems.get(i).getOrDefault("fieldId","").toString());
                }else{
                    cell = row.createCell(i);
                    cell.setCellValue(fieldItems.get(i).getOrDefault("fieldTxt","").toString());

                    //合并单元格
                    int size = fieldChildList.size();
                    CellRangeAddress region = new CellRangeAddress(0, 0, i, i+size-1);
                    sheet.addMergedRegion(region);

                    row = sheet.createRow(1);
                    for (int j = 0; j < fieldChildList.size(); j++) {
                        LinkedHashMap<String, Object> stringObjectLinkedHashMap = fieldChildList.get(j);
                        cell = row.createCell(i+j);
                        cell.setCellValue(stringObjectLinkedHashMap.getOrDefault("fieldTxt","").toString());
                        keyList.add(fieldItems.get(i).getOrDefault("fieldId","").toString());
                    }

                }
            }
        }

        return keyList;
    }
}