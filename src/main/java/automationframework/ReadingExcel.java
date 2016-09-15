package automationframework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import automationframework.ExcelValueVO;

public class ReadingExcel {

	public ArrayList readExcel() throws IOException{
		ArrayList arraydata = new ArrayList();

		try{
			
			String filePath = "./src/main/resources/data/";
			//String fileName = "PDH-7-Item_Pages_Old.xlsx";
			String fileName = "New.xlsx";
			String sheetName = "A-Attribute Group Association";
			//objExcelFile.readExcel(filePath,"PDH-7-Item_Pages_Old.xlsx","A-Attribute Group Association");
			System.out.println(filePath+fileName);
			File file = new File(filePath+fileName);
			FileInputStream inputStream = new FileInputStream(file);
			//Workbook workbook = null;
			XSSFWorkbook  workbook = null;
			String fileExtensionName = fileName.substring(fileName.indexOf("."));
			System.out.println(fileExtensionName);
			if(fileExtensionName.equals(".xlsx")){
				workbook = new XSSFWorkbook(inputStream);
				System.out.println("XLXS");

			} else if(fileExtensionName.equals(".xls")){

				//  workbook = new HSSFWorkbook(inputStream);
				System.out.println("XLX");
			}
			XSSFSheet sheet = workbook.getSheet(sheetName);
			int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum()+1;
			System.out.println("first row"+sheet.getLastRowNum());
			System.out.println("last row"+sheet.getFirstRowNum());
			System.out.println("Row Count is "+rowCount);
			Cell cell;
			XSSFRow  row;
			
			
			for (int i = 6; i < rowCount+1; i++) {
				ExcelValueVO excelValue = new ExcelValueVO();
				if(sheet.getRow(i) == null) {
					row = sheet.createRow(i);
				}else{
					row = sheet.getRow(i);

					if(row.getCell(4) == null ){

						cell = row.createCell(i);
					}else{
						cell=row.getCell(4);
						String icc=cell.getStringCellValue();
						excelValue.setIcc(icc);
					}if(row.getCell(5) == null){
						cell = row.createCell(i);
					}else{
						cell=row.getCell(5);
						String viewattrgrp=cell.getStringCellValue();
						excelValue.setViewAttrGroup(viewattrgrp);
					}if(row.getCell(6) == null){
						cell = row.createCell(i);
					}else{
						cell=row.getCell(6);
						String displayname=cell.getStringCellValue();
						excelValue.setDisplayName(displayname);
					}
					if(row.getCell(7) == null){
						cell = row.createCell(i);
					}else{
						cell=row.getCell(7);
						String internalname=cell.getStringCellValue();
						excelValue.setInternalName(internalname);
					}
						
				}
				arraydata.add(excelValue);
			}


		}catch (Exception e){

			e.printStackTrace();
		}
		return arraydata;

	}
	
	

}
