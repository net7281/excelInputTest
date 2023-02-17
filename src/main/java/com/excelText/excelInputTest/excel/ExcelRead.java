package com.excelText.excelInputTest.excel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.excelText.excelInputTest.UserVo;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

public class ExcelRead {
	public static List<UserVo> read(ExcelReadOption excelReadOption){
		
		if(excelReadOption != null) {
//			받아온 excelReadOption안의 경로의 파일을 Workbook 객체에 저장
			Workbook wb = ExcelFileType.getWorkbook(excelReadOption.getFilePath());
			
			int sheetNum = wb.getNumberOfSheets(); // sheet의 갯수
			
			Row row = null;        // row
            Cell cell = null;      // cell
            String cellName = "";  // cell name
            int numOfCells = 0;    // cell number
            
//          각 map을 저장하는 list
//            List<Map<String, String>> result = new ArrayList<Map<String, String>>();
            List<UserVo> result = new ArrayList<UserVo>();
            
//          각 row값을 저장하는 map ("A","사과")  
//            Map<String, String> map = null;
            UserVo vo = null;
            
//          sheet의 갯수만큼 반복
            for(int k=0; k<sheetNum; k++) {
            	System.out.println("Sheet Name : " + wb.getSheetName(k));
            	Sheet sheet = wb.getSheetAt(k);
            	
//            	sheet에서 유효한 행의 개수
            	int numOfRows = sheet.getLastRowNum() + 1; 
                System.out.println("numOfRows 전체 행의 개수 : " + numOfRows);
                
//              엑셀 파일의 numOfRows가 1이 반환될 경우 예외처리
                if(numOfRows <= 1) {
//                    map = new HashMap<String, String>();
//                    map.put("error", "유효행 없음");
//                    result.add(map);
                	System.out.println("유효행 없음");
                    return null;
                }
                
//              row만큼 반복
                for(int rowIndex = excelReadOption.getStartRow() - 1; rowIndex < numOfRows; rowIndex++) {
/*						
 * 							row, cell 둘다 0부터 시작
 * 					row0  ||  cell0  |  cell1  |  cell2  |  ...
 * 					row1  ||  cell0  |  cell1  |  cell2  |  ...
 * 									   ...
 * */
//                	sheet에서 해당 row를 가져온다
                	row = sheet.getRow(rowIndex);
                	
//                	row0의 cell0이 null경우 해당 row를 입력받지 않는다 (생략가능, 수정가능)
                	if(sheet.getRow(rowIndex).getCell(0) != null && row != null) {
                		
//                		Row의 안의 Cell의 개수
                		numOfCells = row.getLastCellNum();
                		
//                		데이터를 담을 맵 객체 초기화
//                		map = new HashMap<String, String>();
                		vo = new UserVo();
                		
//                		cell의 수 만큼 반복
                		for(int cellIndex = 0; cellIndex < numOfCells; cellIndex++) {
//                			해당 cell을 가지고온다.
                			cell = row.getCell(cellIndex);
                			
//                			cell의 서식 읽기
                			if(cell == null) {
                            	continue;
                            }else {
                            	switch(cell.getCellType()) {
                            		case NUMERIC : //숫자형식일때
                            			if(DateUtil.isCellDateFormatted(cell)) { //날짜일때
                            		 		SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
                                        	cell.setCellValue(formatter.format(cell.getDateCellValue()));
                            			}else {
                            				if((cell.getNumericCellValue() + "").contains(".0")){ // 정수일때
                                            	cell.setCellValue((int)cell.getNumericCellValue() + "");
                                            }else { 
                                               	cell.setCellValue((float)cell.getNumericCellValue() + "");
                                            }
                            			}
                            			break;
                            		case STRING : //텍스트형식일때
                            			cell.setCellType(CellType.STRING);
                                        cell.setCellValue(cell.getStringCellValue().toString());
                                        break;
                            		case FORMULA : //수식일때 (계산 후 들고온다)
                                    	cell.setCellType(CellType.STRING);
                                        String temp_value = cell.getStringCellValue();
                                        if(temp_value.indexOf(".") > 0) {
                                            Double value = Double.parseDouble(String.format("%.1f", Double.parseDouble(cell.getRichStringCellValue().toString())));
                                            cell.setCellValue(value);
                                        }else {
                                            cell.setCellValue(cell.getStringCellValue());
                                        }
                                        break;
                            	}
                            }
                			
//                			현재 cell의 column명을 가지고온다
                			cellName = ExcelCellRef.getName(cell, cellIndex);
                			
//                			추출 대상의 column인지 확인
                			if( !excelReadOption.getOutputColumns().contains(cellName) ) {
                                continue;
                            }
//                			map에 {"column=값"}으로 담는다
//                			map.put(cellName, ExcelCellRef.getValue(cell, wb));
                			
//                			행별로 UserVo에 넣는다
                			if(cellName.equals("A")) vo.setIn_num(Integer.parseInt(ExcelCellRef.getValue(cell, wb)));
                			if(cellName.equals("B")) vo.setName(ExcelCellRef.getValue(cell, wb));
                			if(cellName.equals("C")) vo.setRRN(ExcelCellRef.getValue(cell, wb));
                			if(cellName.equals("D")) vo.setNumber(ExcelCellRef.getValue(cell, wb));
                			if(cellName.equals("E")) vo.setAddress(ExcelCellRef.getValue(cell, wb));
                		}
//                		map.put("successMessage", "불러오기 성공");
                		result.add(vo); // result리스트에 저장
                	}else {
                		//sheet.getRow(rowIndex).getCell(0) != null && row != null 이 아닐때
                	}
                }// row 반복
            }//sheet 반복
            return result; 
		}
		return null;
	}
}