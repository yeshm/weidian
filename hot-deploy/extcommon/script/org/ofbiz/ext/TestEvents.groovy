package org.ofbiz.ext

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.ofbiz.entity.Delegator
import org.ofbiz.ext.util.ExcelUtil
import org.ofbiz.ext.util.ExtFileUtil
import org.ofbiz.ext.util.ExtUtilDateTime
import org.ofbiz.ext.util.RenderUtil

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

public String test() {
    HttpServletRequest request = request
    HttpServletResponse response = response
    Delegator delegator = delegator

    def fileUrl = "完整格式2.xls"
    def inputStream = new FileInputStream(fileUrl)

    def fileUrl2 = "待发货订单20160905104044.xls"
    def inputStream2 = new FileInputStream(fileUrl)

    def rowCount = 0

    Workbook wb = new HSSFWorkbook(inputStream)
    Sheet sheet1 = wb.getSheetAt(0)
    wb.getSheet()

    Workbook wb2 = new HSSFWorkbook(inputStream2)
    Sheet sheet2 = wb2.getSheetAt(0)

    def rowNum =  sheet1.getLastRowNum()
    for (int i =0;i<rowNum;i++) {

        Row row  = sheet1.getRow(i)
        Row row2  = sheet2.getRow(i)


        rowCount++
        if (rowCount == 1) continue
        Cell applyIdCell = row.getCell(0)
        Cell merchantNameCell = row.getCell(1)
        Cell merchantTransCodeNoCell = row.getCell(2)
        Cell applyAmountCell = row.getCell(3)
        Cell withdrawWayCell = row.getCell(4)
        Cell bankNameCell = row.getCell(5)
        Cell bankCardNoCell = row.getCell(6)
        Cell bankCardNameCell = row.getCell(7)
        Cell bankCardMobileCell = row.getCell(8)
        Cell bankBranchInfoCell = row.getCell(9)
        Cell isHaveMoneyCell = row.getCell(10)
        Cell auditCommentsCell = row.getCell(11)

        def applyId = ExcelUtil.getXlsStringValue(applyIdCell)
        def merchantName = ExcelUtil.getXlsStringValue(merchantNameCell)
        def merchantTransCodeNo = ExcelUtil.getXlsStringValue(merchantTransCodeNoCell).trim()
        def applyAmount = ExcelUtil.getXlsStringValue(applyAmountCell)
        def withdrawWay = ExcelUtil.getXlsStringValue(withdrawWayCell)
        def bankName = ExcelUtil.getXlsStringValue(bankNameCell)
        def bankCardNo = ExcelUtil.getXlsStringValue(bankCardNoCell)
        def bankCardName = ExcelUtil.getXlsStringValue(bankCardNameCell)
        def bankCardMobile = ExcelUtil.getXlsStringValue(bankCardMobileCell)
        def bankBranchInfo = ExcelUtil.getXlsStringValue(bankBranchInfoCell)
        def isHaveMoney = ExcelUtil.getXlsStringValue(isHaveMoneyCell)
        def auditComments = ExcelUtil.getXlsStringValue(auditCommentsCell)

    }

    RenderUtil.renderText(response, "hello world!")
    RenderUtil.renderText(response, "\n")

    return "success"
}

public String ajax() {
    HttpServletRequest request = request
    HttpServletResponse response = response
    Delegator delegator = delegator

    request.setAttribute("party", [name: "xxxx"])

    return "success"
}