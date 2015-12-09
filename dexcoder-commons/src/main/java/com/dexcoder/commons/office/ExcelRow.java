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
