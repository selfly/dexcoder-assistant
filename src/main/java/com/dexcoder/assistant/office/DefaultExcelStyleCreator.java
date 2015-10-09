/**
 * Yolema.com Inc.
 * Copyright (c) 2011-2013 All Rights Reserved.
 */
package com.dexcoder.assistant.office;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

/**
 * 
 * @author liyd
 * @version $Id: DefaultExcelStyleCreate.java, v 0.1 2013-1-17 上午10:43:20 liyd Exp $
 */
public class DefaultExcelStyleCreator implements ExcelStyleCreator {

    /** 时间格式 */
    private static final String DATE_FORMAT  = "yyyy-MM-dd HH:mm:ss";

    /** 是否为数字正则表达式 */
    public static final String  REGEX_NUMBER = "^\\d+(\\.\\d+)?$";

    /**
     * 按指定的标题创建一个sheet
     *
     * @param workbook             工作薄对象
     * @param excelSheet the excel sheet
     * @return hSSF sheet
     */
    public HSSFSheet createSheet(HSSFWorkbook workbook, ExcelSheet excelSheet) {

        // 按指定标题创建sheet
        HSSFSheet sheet = workbook.createSheet(excelSheet.getSheetName());

        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);

        return sheet;
    }

    /**
     * 生成sheet列标题行
     *
     * @param workbook 工作薄对象
     * @param sheet sheet对象
     * @param excelSheet the excel sheet
     */
    public void createTitle(HSSFWorkbook workbook, HSSFSheet sheet, ExcelSheet excelSheet) {

        // 创建表格标题行
        HSSFRow row = sheet.createRow(0);

        List<String> rowTitles = excelSheet.getRowTitles();
        for (int i = 0; i < rowTitles.size(); i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(rowTitles.get(i));
            HSSFFont font = workbook.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            text.applyFont(font);
            cell.setCellValue(text);
        }
    }

    /**
     * 创建行
     *
     * @param workbook 工作薄
     * @param sheet sheet对象
     * @param excelRow the row
     * @param rowIndex 行索引
     * @throws Exception
     */
    public void createRow(HSSFWorkbook workbook, HSSFSheet sheet, ExcelRow excelRow, int rowIndex) {

        HSSFRow row = sheet.createRow(rowIndex);

        if (excelRow == null || excelRow.getCells() == null) {
            return;
        }

        List<ExcelCell> cells = excelRow.getCells();
        for (int i = 0; i < cells.size(); i++) {

            ExcelCell excelCell = cells.get(i);
            Object value = ((excelCell == null || excelCell.getValue() == null) ? null : excelCell
                .getValue());
            createCell(workbook, sheet, row, value, rowIndex, i);
        }
    }

    /**
     * 创建列
     *
     * @param workbook 工作薄
     * @param sheet sheet对象
     * @param row 行对象
     * @param value 列值
     * @param rowIndex 行索引
     * @param cellIndex 列索引
     */
    public void createCell(HSSFWorkbook workbook, HSSFSheet sheet, HSSFRow row, Object value,
                           int rowIndex, int cellIndex) {

        // 创建一个列
        HSSFCell cell = row.createCell(cellIndex);

        // 判断值的类型后进行强制类型转换
        if (value == null) {

            cell.setCellValue("");

        } else if (value instanceof Date) {

            // 设置日期
            createDateCellStyle(cell, value);

        } else if (value instanceof byte[]) {

            // 设置图片
            createPictureCellStyle(workbook, sheet, row, rowIndex, cellIndex, (byte[]) value);

        } else if (value instanceof String[]) {

            // 设置下拉列表
            createSelectCellStyle(sheet, cell, rowIndex, rowIndex, cellIndex, cellIndex,
                (String[]) value);

        } else {

            // 默认格式列
            createDefaultCellStyle(cell, value);
        }

    }

    /**
     * 创建日期列
     *
     * @param cell             列对象
     * @param value             列的值
     */
    public void createDateCellStyle(HSSFCell cell, Object value) {

        // 设置日期
        String dateValue = DateFormatUtils.format((Date) value, DATE_FORMAT);
        cell.setCellValue(dateValue);
    }

    /**
     * 创建图片列
     *
     * @param workbook 工作薄
     * @param sheet sheet对象
     * @param row 行对象
     * @param rowIndex 行索引
     * @param cellIndex 列索引
     * @param value 列的值
     */
    public void createPictureCellStyle(HSSFWorkbook workbook, HSSFSheet sheet, HSSFRow row,
                                       int rowIndex, int cellIndex, byte[] value) {

        // 有图片时，设置行高为60px;
        row.setHeightInPoints(60);
        // 设置图片所在列宽度为80px,注意这里单位的一个换算
        sheet.setColumnWidth(cellIndex, (int) (35.7 * 80));

        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) cellIndex,
            rowIndex, (short) cellIndex, rowIndex);
        anchor.setAnchorType(HSSFClientAnchor.MOVE_DONT_RESIZE);

        HSSFPatriarch patriarch = sheet.getDrawingPatriarch();

        patriarch.createPicture(anchor, workbook.addPicture(value, HSSFWorkbook.PICTURE_TYPE_JPEG));
    }

    /**
     * 创建下拉列表列
     *
     * @param sheet             sheet对象
     * @param cell             cell对象
     * @param firstRowIndex             开始行索引
     * @param lastRowIndex             结束行索引
     * @param firstCellIndex             开始列索引
     * @param lastCellIndex             结束列索引
     * @param cellValue             列的值
     */
    public void createSelectCellStyle(HSSFSheet sheet, HSSFCell cell, int firstRowIndex,
                                      int lastRowIndex, int firstCellIndex, int lastCellIndex,
                                      String[] cellValue) {

        CellRangeAddressList regions = new CellRangeAddressList(firstRowIndex, lastRowIndex,
            firstCellIndex, lastCellIndex);
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(cellValue);
        HSSFDataValidation dataValidate = new HSSFDataValidation(regions, constraint);
        // 加入数据有效性到当前sheet对象
        sheet.addValidationData(dataValidate);

        // 设置默认显示的值
        cell.setCellValue(cellValue[0]);

    }

    /**
     * 创建默认格式列
     *
     * @param cell             列对象
     * @param value             列的值
     */
    public void createDefaultCellStyle(HSSFCell cell, Object value) {

        String textValue = value.toString();

        boolean matcher = textValue.matches(REGEX_NUMBER);

        // 是数字并且长度小于12时，当作double处理，当长度大于等于12时数字会显示成科学计数法，所以导成字符串
        if (matcher && textValue.length() < 12) {

            cell.setCellValue(Double.parseDouble(textValue));

        } else {
            HSSFRichTextString richString = new HSSFRichTextString(textValue);
            cell.setCellValue(richString);
        }

    }

}
