package com.qu.exporter;

import org.apache.poi.hssf.usermodel.HSSFSheet;

public interface ExcelDataBuilder {

    void build(HSSFSheet sheet, Object data);
}
