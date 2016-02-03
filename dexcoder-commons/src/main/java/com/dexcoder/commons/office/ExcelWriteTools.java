package com.dexcoder.commons.office;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.dexcoder.commons.exceptions.CommonsAssistantException;

/**
 * excel写工具类
 * 
 * Created by liyd on 7/28/14.
 */
public class ExcelWriteTools {

    /**
     * 添加一个sheet
     *
     * @param excelSheet the excel sheet
     * @param file the file
     */
    public static void addSheet(ExcelSheet excelSheet, File file) {

        ExcelStyleCreator excelStyleCreator = getExcelStyleCreator();
        addSheet(excelSheet, file, excelStyleCreator);
    }

    /**
     * 添加一个sheet
     *
     * @param excelSheet the excel sheet
     * @param file the file
     * @param excelStyleCreator the excel style creator
     */
    public static void addSheet(ExcelSheet excelSheet, File file, ExcelStyleCreator excelStyleCreator) {

        if (!excelSheet.hasRows()) {
            return;
        }
        OutputStream out = null;

        try {

            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));

            // 如果有多个sheet，在标题后面加上序列
            HSSFSheet sheet = excelStyleCreator.createSheet(workbook, excelSheet);

            // 产生表格标题行
            excelStyleCreator.createTitle(workbook, sheet, excelSheet);

            // 生成文件体
            createBody(workbook, sheet, excelSheet, excelStyleCreator);

            out = new FileOutputStream(file);

            // 导出
            workbook.write(out);
        } catch (Exception e) {
            throw new CommonsAssistantException("添加一个Sheet失败", e);
        } finally {
            IOUtils.closeQuietly(out);
        }

    }

    /**
     * Excel导出方法，除了第一行列标题加粗外无任何样式
     *
     * @param excelSheetList the excel sheet list
     * @param file the file
     */
    public static void write(List<ExcelSheet> excelSheetList, File file) {

        ExcelStyleCreator excelStyleCreate = getExcelStyleCreator();
        write(excelSheetList, file, excelStyleCreate);
    }

    /**
     * Excel导出方法，使用自定义的样式实现
     *
     * @param excelSheetList the excel sheet list
     * @param file the file
     * @param excelStyleCreator the excel style creator
     */
    public static void write(List<ExcelSheet> excelSheetList, File file, ExcelStyleCreator excelStyleCreator) {

        OutputStream os = null;
        try {

            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();

            for (ExcelSheet excelSheet : excelSheetList) {
                if (!excelSheet.hasRows()) {
                    continue;
                }
                // 创建 sheet
                HSSFSheet sheet = excelStyleCreator.createSheet(workbook, excelSheet);

                // 产生表格标题行
                excelStyleCreator.createTitle(workbook, sheet, excelSheet);

                // 生成文件体
                createBody(workbook, sheet, excelSheet, excelStyleCreator);
            }
            // 导出
            os = new FileOutputStream(file);
            workbook.write(os);
        } catch (Exception e) {
            throw new CommonsAssistantException("Excel导出失败", e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    /**
     * 生成Excel数据文件体
     *
     * @param workbook 工作薄
     * @param sheet sheet对象
     * @param excelSheet the excel sheet
     * @param excelStyleCreator the excel style creator
     */
    private static void createBody(HSSFWorkbook workbook, HSSFSheet sheet, ExcelSheet excelSheet,
                                   ExcelStyleCreator excelStyleCreator) {
        List<ExcelRow> rows = excelSheet.getRows();

        if (CollectionUtils.isEmpty(rows)) {
            return;
        }

        for (int i = 0; i < rows.size(); i++) {

            ExcelRow excelRow = rows.get(i);

            // 创建一行 第一行是标题，所以i+1
            excelStyleCreator.createRow(workbook, sheet, excelRow, i + 1);
        }
    }

    /**
     * 获取生成Excel元素样式的处理类
     *
     * @return
     */
    private static ExcelStyleCreator getExcelStyleCreator() {
        return new DefaultExcelStyleCreator();
    }

}
