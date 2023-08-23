package com.qu.util;

import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import com.qu.exporter.ExcelDataBuilder;

/**
 * Excel导出工具
 */
public class ExcelExportUtil {

    public static void export(HttpServletResponse response, ExcelDataBuilder builder, Object data) {
        OutputStream outputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            String fileName = System.currentTimeMillis() + ".xls";
//            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            outputStream = response.getOutputStream();

            // 创建新的Excel 工作簿
            Workbook workbook = buildWorkbook(builder, data);

//            String root = System.getProperties().getProperty("user.home");
//            String separator = System.getProperties().getProperty("file.separator");
//            String path = root + separator + fileName;
            //写本地磁盘
//            fileOutputStream = new FileOutputStream(new File(path));
//            workbook.write(fileOutputStream);

            //下载
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Workbook buildWorkbook(ExcelDataBuilder builder, Object data) {
        //创建Excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();

        //创建工作表
        HSSFSheet sheet = workbook.createSheet();

        //格式化单元格数据用
        HSSFDataFormat format = workbook.createDataFormat();

        //创建行
//        HSSFRow row = sheet.createRow(1);

        //设置字体
        HSSFFont font = workbook.createFont();

        font.setFontHeightInPoints((short) 20); //字体高度
        font.setColor(HSSFFont.COLOR_RED); //字体颜色
        font.setFontName("黑体"); //字体
        font.setBoldweight((short) 5);
        font.setItalic(true); //是否使用斜体

        //设置单元格类型
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setWrapText(true);

        // 创建每行单元格
//        HSSFCell cell = null;

        //构造数据
        builder.build(sheet, data);
//
//        for (int i = 0; i < 10; i++) {
//            row = sheet.createRow(i);
//
//            cell = row.createCell(0);
//            cell.setCellValue("下载表格");
//
//            cell = row.createCell(1);
//            cell.setCellValue("这是表格");
//        }

        return workbook;
    }

    /**
     * 填充表格内容
     *
     * @param row
     * @param cell
     * @param index
     * @param val
     */
    public static void fillInCell(HSSFRow row, HSSFCell cell, int index, String val) {
        cell = row.createCell(index);
        cell.setCellValue(val);
    }
}
