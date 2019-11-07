package com.mockst.cracker.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @Auther: zhiwei
 * @Date: 2019/11/6 21:43
 * @Description:
 */
public class ExcelUtil {

    /**
     * * 判断指定的单元格是否是合并单元格
     * *
     * * @param sheet
     * * @param row  行下标
     * * @param column 列下标
     * * @return
     */
    public static Result isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    Row frow = sheet.getRow(firstRow);
                    Cell fcell = frow.getCell(firstColumn);
                    return new Result(true, firstRow + 1, lastRow + 1, firstColumn + 1, lastColumn + 1,getCellValue(fcell));
                }
            }
        }
        Cell cell = sheet.getRow(row)!=null?sheet.getRow(row).getCell(column):null;
        return new Result(false, row, row, column, column,getCellValue(cell));
    }


    /**
     * 获取单元格的值
     */
    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        }
        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        }
        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return cell.getCellFormula();
        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return "";
    }

    public static class Result {
        public boolean merged;
        public int startRow;
        public int endRow;
        public int startCol;
        public int endCol;
        public String cellValue;

        public Result(boolean merged, int startRow, int endRow
                , int startCol, int endCol,String cellValue) {
            this.merged = merged;
            this.startRow = startRow;
            this.endRow = endRow;
            this.startCol = startCol;
            this.endCol = endCol;
            this.cellValue = cellValue;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "merged=" + merged +
                    ", startRow=" + startRow +
                    ", endRow=" + endRow +
                    ", startCol=" + startCol +
                    ", endCol=" + endCol +
                    ", cellValue='" + cellValue + '\'' +
                    '}';
        }
    }

}
