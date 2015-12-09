/**
 * Yolema.com Inc.
 * Copyright (c) 2011-2013 All Rights Reserved.
 */
package com.dexcoder.commons.office;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Excel样式创建方法接口
 * 
 * @author liyd
 * @version $Id: ExcelStyleCreate.java, v 0.1 2013-1-17 上午10:43:55 liyd Exp $
 */
public interface ExcelStyleCreator {

    /**
     * 按指定的标题创建一个sheet
     *
     * @param workbook 工作薄对象
     * @param excelSheet the excel sheet
     * @return hSSF sheet
     */
    public HSSFSheet createSheet(HSSFWorkbook workbook, ExcelSheet excelSheet);

    /**
     * 生成sheet列标题行
     *
     * @param workbook 工作薄对象
     * @param sheet sheet对象
     * @param excelSheet the excel sheet
     */
    public void createTitle(HSSFWorkbook workbook, HSSFSheet sheet, ExcelSheet excelSheet);

    /**
     * 创建行
     *
     * @param workbook 工作薄
     * @param sheet sheet对象
     * @param excelRow the sheet row
     * @param rowIndex 行索引
     * @throws Exception
     */
    public void createRow(HSSFWorkbook workbook, HSSFSheet sheet, ExcelRow excelRow, int rowIndex);

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
                           int rowIndex, int cellIndex);

    /**
     * 创建日期列
     *
     * @param cell 列对象
     * @param value 列的值
     */
    public void createDateCellStyle(HSSFCell cell, Object value);

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
                                       int rowIndex, int cellIndex, byte[] value);

    /**
     * 创建下拉列表列
     *
     * @param sheet sheet对象
     * @param cell cell对象
     * @param firstRowIndex 开始行索引
     * @param lastRowIndex 结束行索引
     * @param firstCellIndex 开始列索引
     * @param lastCellIndex 结束列索引
     * @param cellValue 列的值
     */
    public void createSelectCellStyle(HSSFSheet sheet, HSSFCell cell, int firstRowIndex,
                                      int lastRowIndex, int firstCellIndex, int lastCellIndex,
                                      String[] cellValue);

    /**
     * 创建默认格式列
     *
     * @param cell 列对象
     * @param value 列的值
     */
    public void createDefaultCellStyle(HSSFCell cell, Object value);

}
