package com.excelText.excelInputTest;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.excelText.excelInputTest.excel.ExcelRead;
import com.excelText.excelInputTest.excel.ExcelReadOption;


@Service
public class MainService {
	
//	엑셀 파일의 데이터를 List[row] = {'cell','값'} 의 형태로 반환
	public List<Map<String, String>> excelUpload(File destFile) throws Exception{
		
//		엑셀 옵션값을 ExcelReadOption 에 저장
		ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setFilePath(destFile.getAbsolutePath()); //파일경로 추가
        excelReadOption.setOutputColumns("A", "B"); //추출할 컬럼명 추가
        excelReadOption.setStartRow(1); //시작행  1 부터 시작
		
        List<Map<String, String>>excelContent  = ExcelRead.read(excelReadOption);
		
        for(Map<String, String> article: excelContent){
            for(String column : article.keySet()) {
            	System.out.print(column + " : " + article.get(column) +" / ");
            }
            System.out.println();
        }
        
        return excelContent;
		
	}
	
	
//	값 하나만
	public String excelDate(File destFile) throws Exception {
    	ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setFilePath(destFile.getAbsolutePath()); //파일경로 추가
        excelReadOption.setOutputColumns("A", "B"); //추출할 컬럼명 추가
        excelReadOption.setStartRow(1); //시작행(헤더부분 제외)
        
         List<Map<String, String>>excelContent  = ExcelRead.read(excelReadOption);
         String pDate = excelContent.get(0).get("A").toString();
         return pDate;
    }
}
