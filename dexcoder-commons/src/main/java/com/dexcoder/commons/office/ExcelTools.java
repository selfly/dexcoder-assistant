/**
 *
 */
package com.dexcoder.commons.office;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;

/**
 * excel解析工具，兼容03,07以上格式
 *
 * @author liyd
 */
public class ExcelTools {

    /**
     * 读取excel文件
     *
     * @param file
     * @return
     */
    public static List<ExcelSheet> read(File file) {

        byte[] bytes = readFileToByte(file);
        return read(bytes);
    }

    /**
     * 读取excel文件
     *
     * @param bytes
     * @return
     */
    public static List<ExcelSheet> read(byte[] bytes) {

        Workbook workbook = readWorkbook(bytes);

        int numberOfSheets = workbook.getNumberOfSheets();
        List<ExcelSheet> sheetList = new ArrayList<ExcelSheet>(numberOfSheets);
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            ExcelSheet excelSheet = readSheet(sheet);
            sheetList.add(excelSheet);
        }
        return sheetList;
    }

    /**
     * 读取excel的第一个sheet
     *
     * @param file
     * @return
     */
    public static ExcelSheet readFirstSheet(File file) {

        byte[] bytes = readFileToByte(file);
        return readFirstSheet(bytes);
    }

    /**
     * 读取excel的第一个sheet
     *
     * @param bytes
     * @return
     */
    public static ExcelSheet readFirstSheet(byte[] bytes) {

        Workbook workbook = readWorkbook(bytes);
        Sheet sheet = workbook.getSheetAt(0);
        return readSheet(sheet);
    }

    /**
     * 读取一个sheet
     *
     * @param sheet
     * @return
     */
    private static ExcelSheet readSheet(Sheet sheet) {

        if (sheet == null) {
            return null;
        }

        ExcelSheet excelSheet = new ExcelSheet(sheet.getSheetName());

        int rowsCount = sheet.getLastRowNum();
        List<ExcelRow> rows = new ArrayList<ExcelRow>(rowsCount);
        for (int i = 0; i <= rowsCount; i++) {

            Row row = sheet.getRow(i);

            ExcelRow excelRow = readRow(row);

            rows.add(excelRow);
        }
        excelSheet.setRows(rows);
        return excelSheet;
    }

    /**
     * 解析row
     *
     * @param row
     */
    private static ExcelRow readRow(Row row) {

        ExcelRow excelRow = new ExcelRow();
        if (row == null) {
            return excelRow;
        }
        int cellCount = row.getLastCellNum();
        List<ExcelCell> excelCells = new ArrayList<ExcelCell>(cellCount);
        for (int i = 0; i < cellCount; i++) {

            Cell cell = row.getCell(i);

            ExcelCell excelCell = readCell(cell);

            excelCells.add(excelCell);
        }

        excelRow.setCells(excelCells);
        return excelRow;
    }

    /**
     * 解析cell
     *
     * @param cell
     * @return
     */
    private static ExcelCell readCell(Cell cell) {

        ExcelCell excelCell = new ExcelCell();
        if (cell == null) {
            return excelCell;
        }
        int type = cell.getCellType();
        Object value;
        switch (type) {

            case Cell.CELL_TYPE_FORMULA:
                value = cell.getCellFormula();
                break;

            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;

            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    value = StringUtils.replace(dateFormat.format(date), " 00:00:00", "");
                } else {
                    value = cell.getNumericCellValue();
                    DecimalFormat df = new DecimalFormat("0");
                    value = df.format(value);
                }
                break;

            default:
                value = cell.getStringCellValue();
                break;
        }
        excelCell.setValue(value);
        return excelCell;
    }

    /**
     * 读取workbook
     *
     * @param bytes the bytes
     * @return workbook
     */
    private static Workbook readWorkbook(byte[] bytes) {

        Workbook workbook;

        InputStream is = null;

        try {
            is = new ByteArrayInputStream(bytes);
            workbook = WorkbookFactory.create(is);
        } catch (Exception e) {
            throw new RuntimeException("初始化Workbook失败", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return workbook;
    }

    /**
     * 将文件转换成byte数组
     *
     * @param file
     * @return
     */
    private static byte[] readFileToByte(File file) {
        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException("将文件转换成byte数组失败", e);
        }
    }

    /**
     * 写入数据到excel文件
     *
     * @param file        写入的excel文件
     * @param excelSheets 一个sheet的数据
     */
    public static void write(File file, ExcelSheet... excelSheets) {

        ExcelStyleCreator excelStyleCreator = getExcelStyleCreator();
        write(file, excelStyleCreator, excelSheets);
    }

    /**
     * 写入数据到excel的byte数组
     * 如果传入的bytes数组不是一个空的excel文件,则追加sheet,否则生成一个新的excel文件
     *
     * @param bytes       写入的excel文件byte数组
     * @param excelSheets 一个或多个sheet的数据
     */
    public static byte[] write(byte[] bytes, ExcelSheet... excelSheets) {

        ExcelStyleCreator excelStyleCreator = getExcelStyleCreator();
        return write(bytes, excelStyleCreator, excelSheets);
    }

    /**
     * 写入数据到excel的byte数组
     *
     * @param excelSheets 一个或多个sheet的数据
     */
    public static byte[] write(ExcelSheet... excelSheets) {
        return write(new byte[] {}, excelSheets);
    }

    /**
     * 指定样式生成器,写入数据到excel的byte数组
     *
     * @param excelSheets the excel sheet
     */
    public static byte[] write(ExcelStyleCreator excelStyleCreator, ExcelSheet... excelSheets) {
        return write(new byte[] {}, excelStyleCreator, excelSheets);
    }

    /**
     * 指定样式生成器,写入数据到excel的byte数组
     *
     * @param file              the file
     * @param excelStyleCreator the excel style creator
     * @param excelSheets       the excel sheet
     */
    public static void write(File file, ExcelStyleCreator excelStyleCreator, ExcelSheet... excelSheets) {

        try {
            byte[] bytes = null;
            if (file.exists()) {
                bytes = FileUtils.readFileToByteArray(file);
            }
            byte[] result = write(bytes, excelStyleCreator, excelSheets);
            FileUtils.writeByteArrayToFile(file, result);
        } catch (IOException e) {
            throw new RuntimeException("读写文件失败:" + file.getAbsolutePath(), e);
        }
    }

    /**
     * 指定样式生成器,写入数据到excel的byte数组
     *
     * @param bytes             the file
     * @param excelStyleCreator the excel style creator
     * @param excelSheets       the excel sheet
     */
    public static byte[] write(byte[] bytes, ExcelStyleCreator excelStyleCreator, ExcelSheet... excelSheets) {

        ByteArrayOutputStream bos = null;
        try {
            // 声明一个工作薄
            HSSFWorkbook workbook = (ArrayUtils.isEmpty(bytes) ? new HSSFWorkbook() : new HSSFWorkbook(
                new ByteArrayInputStream(bytes)));

            for (ExcelSheet excelSheet : excelSheets) {

                // 如果有多个sheet，在标题后面加上序列
                HSSFSheet sheet = excelStyleCreator.createSheet(workbook, excelSheet);

                // 产生表格标题行
                excelStyleCreator.createTitle(workbook, sheet, excelSheet);

                // 生成文件体
                createBody(workbook, sheet, excelSheet, excelStyleCreator);
            }

            bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("添加一个Sheet失败", e);
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(bos);
        }

    }

    /**
     * 生成Excel数据文件体
     *
     * @param workbook          工作薄
     * @param sheet             sheet对象
     * @param excelSheet        the excel sheet
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
