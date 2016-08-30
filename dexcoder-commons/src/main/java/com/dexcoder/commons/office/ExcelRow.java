package com.dexcoder.commons.office;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * sheet中的一行数据
 * <p>
 * Created by liyd on 7/28/14.
 */
public class ExcelRow {

    /**
     * 行中的列
     */
    private List<ExcelCell> cells;

    public ExcelRow() {
        cells = new ArrayList<ExcelCell>();
    }

    public List<ExcelCell> getCells() {
        return cells;
    }

    public void setCells(Object... values) {
        for (Object value : values) {
            this.addCell(value);
        }
    }

    public void setCells(List<ExcelCell> cells) {
        this.cells = cells;
    }

    public int getTotalCellsNum() {
        return cells.size();
    }

    public boolean hasCells() {
        return !cells.isEmpty();
    }

    public boolean isEmptyRow() {
        if (cells.isEmpty()) {
            return true;
        }
        for (ExcelCell excelCell : cells) {
            if (excelCell != null && excelCell.getValue() != null && StringUtils.isNotBlank(excelCell.getStringValue())) {
                return false;
            }
        }
        return true;
    }

    public void addCell(Object value) {
        ExcelCell excelCell = new ExcelCell(value);
        this.cells.add(excelCell);
    }

    public ExcelCell getFirstCell() {
        return cells.isEmpty() ? null : cells.iterator().next();
    }

    public ExcelCell getCell(int i) {
        return cells.isEmpty() ? null : cells.get(i);
    }

    public Object getCellValue(int i) {
        return cells.isEmpty() ? null : cells.get(i).getValue();
    }

    public String getCellStringValue(int i) {
        return cells.isEmpty() ? null : cells.get(i).getStringValue();
    }

    @Override
    public String toString() {

        if (cells == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (ExcelCell excelCell : cells) {
            sb.append(excelCell == null ? "null" : excelCell.toString()).append(" ");
        }
        return sb.toString();
    }
}
