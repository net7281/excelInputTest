package com.excelText.excelInputTest.excel;

import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

//
public class ExcelCellRef {

//	Column Name 출력
	public static String getName(Cell cell, int cellIndex) {
		int cellNum = 0;
        if(cell != null) {
            cellNum = cell.getColumnIndex();
        }
        else {
            cellNum = cellIndex;
        }
        return CellReference.convertNumToColString(cellNum);
	}
	
//	Cell 값을 가져옴
	public static String getValue(Cell cell, Workbook wb) {
        String value = "";
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        
        if(cell == null) {
            value = "";
        }
        if(cell != null) {
        	switch (cell.getCellType()) {
			case FORMULA:
				if(evaluator.evaluateFormulaCell(cell) ==CellType.NUMERIC) {
					value = (float)cell.getNumericCellValue() + ""; // 계산된 수식의 값을 가져옴
				}else if(evaluator.evaluateFormulaCell(cell)==CellType.STRING) {
                    value = cell.getStringCellValue(); // 계산된 수식의 문자값을 가져옴
                }
				break;
				
			case NUMERIC :
				if(DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
                    value = formatter.format(cell.getDateCellValue());
                }else {
                    if((cell.getNumericCellValue()+"").contains(".0")) {
                    	value = (int)cell.getNumericCellValue() + "";
                    }else {
                    	value = (float)cell.getNumericCellValue() + "";
                    }
                }
                break;
                
			case STRING :
				value = cell.getStringCellValue();
                break;
			
			case BOOLEAN : 
            	value = String.valueOf(cell.getBooleanCellValue());
                break;
                
            case BLANK : 
            	value = "";
                break;
                
            case ERROR :
            	value = cell.getErrorCellValue() + "";
                break;

                
			default :
				value = cell.getStringCellValue();
			}
        }
        return value;
    }
}
