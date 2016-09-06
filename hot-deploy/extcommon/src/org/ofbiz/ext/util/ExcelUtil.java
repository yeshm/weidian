package org.ofbiz.ext.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.*;
import org.ofbiz.base.util.GeneralRuntimeException;
import org.ofbiz.base.util.UtilValidate;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    /**
     * 生成excel文件
     *
     * @param fileName   文件名
     * @param sheetName  excel标签名
     * @param headerList 表格头数据集合
     * @param dataList   表格数据集合
     */
    public static OutputStream createExcelFile(String fileName, String sheetName, List<String> headerList, List<List<String>> dataList, HttpServletResponse response) throws IOException {
        return createExcelFile(fileName, sheetName, headerList, dataList, null, false, false, response);
    }

    /**
     * 生成excel文件
     *
     * @param fileName        文件名
     * @param sheetName       excel标签名
     * @param headerList      表格头数据集合
     * @param dataList        表格数据集合
     * @param isUseLowVersion 使用使用低版本
     */
    public static OutputStream createExcelFile(String fileName, String sheetName, List<String> headerList, List<List<String>> dataList, boolean isUseLowVersion, HttpServletResponse response) throws IOException {
        return createExcelFile(fileName, sheetName, headerList, dataList, null, false, isUseLowVersion, response);
    }

    public static void createExcelFileOutputResponse(String fileName, String sheetName, List<String> headerList, List<List<String>> dataList, boolean isUseLowVersion, HttpServletResponse response) throws IOException {
        byte[] buffer = new byte[1024];
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
        OutputStream fileOutputStream = createExcelFile(fileName, sheetName, headerList, dataList, null, false, isUseLowVersion, response);

        fileOutputStream.write(buffer);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    /**
     * @param fileName   文件名
     * @param sheetName  excel标签名
     * @param headerList 表格头数据集合
     * @param dataList   表格数据集合
     * @param isProtect  是否加密
     */
    public static OutputStream createExcelFile(String fileName, String sheetName, List<String> headerList, List<List<String>> dataList, boolean isProtect, boolean isUseLowVersion, HttpServletResponse response) throws IOException {
        return createExcelFile(fileName, sheetName, headerList, dataList, null, isProtect, isUseLowVersion, response);
    }

    /**
     * @param fileName   文件名
     * @param sheetName  excel标签名
     * @param headerList 表格头数据集合
     * @param dataList   表格数据集合
     * @param styleMap   样式集合
     */
    public static OutputStream createExcelFile(String fileName, String sheetName, List<String> headerList, List<List<String>> dataList, Map<String, List> styleMap, boolean isUseLowVersion, HttpServletResponse response) throws IOException {
        return createExcelFile(fileName, sheetName, headerList, dataList, styleMap, false, isUseLowVersion, response);
    }

    /**
     * 生成excel文件
     *
     * @param fileName        文件名
     * @param sheetName       excel标签名
     * @param headerList      表格头数据集合
     * @param dataList        表格数据集合
     * @param styleMap        样式集合
     * @param isProtect       是否加密
     * @param isUseLowVersion 使用使用低版本
     */
    public static OutputStream createExcelFile(String fileName, String sheetName, List<String> headerList, List<List<String>> dataList, Map<String, List> styleMap, boolean isProtect, boolean isUseLowVersion, HttpServletResponse response) throws IOException {
        if (UtilValidate.isWhitespace(fileName)) fileName = "聚微途网络科技有限公司";
        if (UtilValidate.isWhitespace(sheetName)) sheetName = "sheet1";
        if (UtilValidate.isEmpty(headerList)) throw new GeneralRuntimeException("表格标题为空");

        //这里要注意,如果直接导出该文件,需要传入response对象,response.getOutputStream()不同于ByteArrayOutputStream,如果不传入,导出的文件则会损坏
        OutputStream outputStream;
        if (UtilValidate.isEmpty(response)) {
            outputStream = new ByteArrayOutputStream(1024);
        } else {
            outputStream = new BufferedOutputStream(response.getOutputStream());
        }

        List widthLengthList = null;
        List numberFormatList = null;
        List currencyFormatList = null;
        List dateFormatList = null;
        if (UtilValidate.isNotEmpty(styleMap)) {
            widthLengthList = styleMap.get("cellWidth");
            numberFormatList = styleMap.get("numberFormat");
            currencyFormatList = styleMap.get("currencyFormat");
            dateFormatList = styleMap.get("dateFormat");
        }

        if (isUseLowVersion) {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            HSSFCellStyle style = (HSSFCellStyle) createCellStyle(wb);

            if (isProtect) sheet.protectSheet("jianlaifu");

            sheet.createFreezePane(0, 1, 0, 1);

            HSSFCell cell;
            HSSFRow row = sheet.createRow(0);
            row.setHeightInPoints(30);  // 设置行的高度

            for (int i = 0; i < headerList.size(); i++) {
                cell = row.createCell(i);
                cell.setCellValue(headerList.get(i).toString());
                cell.setCellStyle(style);
            }

            HSSFCellStyle cellStyle = (HSSFCellStyle) createCellStyle(wb);

            for (int i = 0; i < dataList.size(); i++) {
                row = sheet.createRow(i + 1);
                row.setHeightInPoints(20);      // 设置行的高度

                List data = dataList.get(i);
                for (int j = 0; j < data.size(); j++) {
                    Object obj = data.get(j);
                    cell = row.createCell(j);

                    if (UtilValidate.isNotEmpty(numberFormatList) && numberFormatList.contains(j)) {
                        cellStyle.setDataFormat((short) 3);
                    } else if (UtilValidate.isNotEmpty(currencyFormatList) && currencyFormatList.contains(j)) {
                        cellStyle.setDataFormat((short) 8);
                    } else if (UtilValidate.isNotEmpty(dateFormatList) && dateFormatList.contains(j)) {
                        cellStyle.setDataFormat(wb.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
                    }
                    cell.setCellStyle(cellStyle);

                    if (UtilValidate.isNotEmpty(obj)) {
                        if (data.get(j) instanceof BigDecimal)
                            cell.setCellValue(((BigDecimal) data.get(j)).doubleValue());
                        else if (data.get(j) instanceof Double) cell.setCellValue((Double) data.get(j));
                        else if (data.get(j) instanceof Date) cell.setCellValue((Date) data.get(j));
                        else if (data.get(j) instanceof Calendar) cell.setCellValue((Calendar) data.get(j));
                        else if (data.get(j) instanceof RichTextString) cell.setCellValue((RichTextString) data.get(j));
                        else if (data.get(j) instanceof Boolean) cell.setCellValue((Boolean) data.get(j));
                        else cell.setCellValue((String) data.get(j));
                    } else {
                        cell.setCellValue("");
                    }
                }
            }

            // 在数据填充完之后再自动计算列宽
            for (int i = 0; i < headerList.size(); i++) {
                boolean autoWidth = true;
                if (UtilValidate.isNotEmpty(widthLengthList) && (UtilValidate.isNotEmpty(widthLengthList.get(i)) && (Integer) widthLengthList.get(i) > 0))
                    autoWidth = false;
                if (autoWidth) {
                    sheet.autoSizeColumn(i, true);
                } else {
                    sheet.setColumnWidth(i, (Integer) widthLengthList.get(i) * 256);
                }
            }
            wb.write(outputStream);
        } else {
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet(sheetName);
            XSSFCellStyle style = (XSSFCellStyle) createCellStyle(wb);

            //设置文档保护
            if (isProtect) sheet.protectSheet("jianlaifu");
            //固定表头
            sheet.createFreezePane(0, 1, 0, 1);

            XSSFCell cell;
            XSSFRow row = sheet.createRow(0);
            row.setHeightInPoints(30);  // 设置行的高度

            for (int i = 0; i < headerList.size(); i++) {
                cell = row.createCell(i);
                cell.setCellValue(headerList.get(i).toString());
                cell.setCellStyle(style);
            }

            XSSFCellStyle cellStyle = (XSSFCellStyle) createCellStyle(wb);
            for (int i = 0; i < dataList.size(); i++) {
                row = sheet.createRow(i + 1);
                row.setHeightInPoints(20);      // 设置行的高度

                List data = dataList.get(i);
                for (int j = 0; j < data.size(); j++) {
                    Object obj = data.get(j);
                    cell = row.createCell(j);

                    if (UtilValidate.isNotEmpty(numberFormatList) && numberFormatList.contains(j)) {
                        cellStyle.setDataFormat((short) 3);
                    } else if (UtilValidate.isNotEmpty(currencyFormatList) && currencyFormatList.contains(j)) {
                        cellStyle.setDataFormat((short) 8);
                    } else if (UtilValidate.isNotEmpty(dateFormatList) && dateFormatList.contains(j)) {
                        cellStyle.setDataFormat(wb.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
                    }
                    cell.setCellStyle(cellStyle);

                    if (UtilValidate.isNotEmpty(obj)) {
                        if (data.get(j) instanceof BigDecimal)
                            cell.setCellValue(((BigDecimal) data.get(j)).doubleValue());
                        else if (data.get(j) instanceof Double) cell.setCellValue((Double) data.get(j));
                        else if (data.get(j) instanceof Date) cell.setCellValue((Date) data.get(j));
                        else if (data.get(j) instanceof Calendar) cell.setCellValue((Calendar) data.get(j));
                        else if (data.get(j) instanceof RichTextString) cell.setCellValue((RichTextString) data.get(j));
                        else if (data.get(j) instanceof Boolean) cell.setCellValue((Boolean) data.get(j));
                        else cell.setCellValue((String) data.get(j));
                    } else {
                        cell.setCellValue("");
                    }
                }
            }

            // 在数据填充完之后再自动计算列宽
            for (int i = 0; i < headerList.size(); i++) {
                boolean autoWidth = true;
                if (UtilValidate.isNotEmpty(widthLengthList) && (UtilValidate.isNotEmpty(widthLengthList.get(i)) && (Integer) widthLengthList.get(i) > 0))
                    autoWidth = false;
                if (autoWidth) {
                    sheet.autoSizeColumn(i, true);
                } else {
                    sheet.setColumnWidth(i, (Integer) widthLengthList.get(i) * 256);
                }
            }
            wb.write(outputStream);
        }

        return outputStream;
    }

    private static CellStyle createCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();

        style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 创建一个水平居中格式
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 创建一个垂直居中格式
        style.setWrapText(true);    // 设置单元格内容是否自动换行

        return style;
    }

    private static CellStyle createCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();

        style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 创建一个水平居中格式
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 创建一个垂直居中格式
        style.setWrapText(true);    // 设置单元格内容是否自动换行

        return style;
    }

    public static String getXlsStringValue(HSSFCell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = NumberToTextConverter.toText(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

    public static String getXlsxStringValue(XSSFCell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = NumberToTextConverter.toText(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }
}
