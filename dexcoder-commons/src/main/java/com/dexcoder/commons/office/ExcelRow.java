package com.dexcoder.commons.office;

import java.util.List;

/**
 * sheet中的一行数据
 *
 * Created by liyd on 7/28/14.
 */
public class ExcelRow {

    /** 行中的列 */
    private List<ExcelCell> cells;

    public List<ExcelCell> getCells() {
        return cells;
    }

    public void setCells(List<ExcelCell> cells) {
        this.cells = cells;
    }

    public int getTotalCellsNum() {
        return cells == null ? 0 : cells.size();
    }

    public boolean hasCells() {
        return (cells != null && !cells.isEmpty());
    }

    public ExcelCell getCell(int i) {
        return cells == null ? null : cells.get(i);
    }

    public Object getCellValue(int i) {
        return cells == null ? null : cells.get(i).getValue();
    }

    public String getCellStringValue(int i) {
        return cells == null ? null : cells.get(i).getStringValue();
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
