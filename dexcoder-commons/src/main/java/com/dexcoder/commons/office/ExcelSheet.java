package com.dexcoder.commons.office;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * excel sheet信息及数据
 *
 * Created by liyd on 7/28/14.
 */
public class ExcelSheet {

    /** sheet标题 */
    private String         sheetName;

    /** 行标题，默认为第一行 */
    private List<String>   rowTitles;

    /** sheet中的行 */
    private List<ExcelRow> rows;

    /**
     * 获取行标题
     * 
     * @return
     */
    public List<String> getRowTitles() {

        if (!CollectionUtils.isEmpty(rowTitles)) {
            return rowTitles;
        }
        if (!CollectionUtils.isEmpty(rows) && !rows.iterator().next().isEmptyRow()) {
            List<ExcelCell> cells = rows.get(0).getCells();
            rowTitles = new ArrayList<String>(cells.size());
            for (ExcelCell excelCell : cells) {
                rowTitles.add(excelCell.getStringValue());
            }
            rows.remove(0);
        }
        return rowTitles;
    }

    public void setRowTitles(List<String> rowTitles) {

        this.rowTitles = rowTitles;
    }

    public int getTotalRowsNum() {
        return rows == null ? 0 : rows.size();
    }

    public ExcelRow getRow(int i) {
        return rows == null ? null : rows.get(i);
    }

    public boolean hasRows() {
        if (rows == null) {
            return false;
        }
        boolean result = false;
        Iterator<ExcelRow> iterator = rows.iterator();
        while (iterator.hasNext()) {
            boolean emptyRow = iterator.next().isEmptyRow();
            if (!emptyRow) {
                result = true;
            }
        }
        return result;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<ExcelRow> getRows() {
        return rows;
    }

    public void setRows(List<ExcelRow> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {

        String lineSeparator = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        sb.append("sheetName:").append(sheetName);
        sb.append(lineSeparator);

        sb.append("rows:");
        if (rows == null) {
            sb.append("null");
        } else {

            sb.append("[").append(lineSeparator);
            for (ExcelRow row : rows) {
                sb.append(row == null ? "null" : row.toString());
                sb.append(lineSeparator);
            }
            sb.append("]");
        }
        return sb.toString();
    }
}
