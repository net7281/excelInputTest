package com.excelText.excelInputTest.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//파일 확장자를 체크 후 Workbook 객체에 리턴하는 클래스
public class ExcelFileType {
	
	public static Workbook getWorkbook(String filePath) {
		
//		FileInputStream은 파일의 경로에 있는 파일을 읽어서 Byte로 가져온다. (파일 없으면 RuntimeException 발생)
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(filePath);
		}catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		Workbook wb = null;
		
//		입력받은 파일이 .XLS 라면 HSSFWorkbook에, .XLSX라면 XSSFWorkbook에 초기화
		if(filePath.toUpperCase().endsWith(".XLS")) {
			try {
				wb = new HSSFWorkbook(fis);
			} catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
		}else if(filePath.toUpperCase().endsWith(".XLSX")) {
            try {
                wb = new XSSFWorkbook(fis);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
		
		if(fis != null) {
            try {
                 fis.close();
            } catch (IOException e) {
                 System.out.println("ExcelFileType : 예외상황발생");
            }
        }
		
		return wb;
	}
}
