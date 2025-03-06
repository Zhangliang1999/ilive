package com.bvRadio.iLive.iLive.action.util;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelUtil {
	// excel导出
	public static HSSFWorkbook excelExport(String[] title, List<String[]> body) {
		// 1、创建HSSFWorkbook文件对应一个Excel文件
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 2、创建一个sheet
		HSSFSheet sheet = workbook.createSheet();
		// 3、在sheet中添加表头第0行
		HSSFRow rowHead = sheet.createRow(0);
		// 4、创建单元格 并设置表头格式
		HSSFCellStyle style = workbook.createCellStyle();
		// style.setAlignment(HSSFCellStyle.ALIGN_CENTER); //创建一个居中格式
		// 声明对象
		HSSFCell cell = null;
		// 创建标题
		for (int i = 0; i < title.length; i++) {
			cell = rowHead.createCell(i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}

		// 添加内容
		if (body.size() > 0) {
			for (int i = 0; i < body.size(); i++) {
				String[] s = body.get(i);
				HSSFRow rowBody = sheet.createRow(i + 1);
				for (int j = 0; j < s.length; j++) {
					HSSFCell cell2 = rowBody.createCell(j);
					cell2.setCellValue(s[j]);
				}
			}
		}
		return workbook;
	}
}
