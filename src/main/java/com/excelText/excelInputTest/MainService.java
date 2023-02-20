package com.excelText.excelInputTest;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excelText.excelInputTest.excel.ExcelRead;
import com.excelText.excelInputTest.excel.ExcelReadOption;


@Service
public class MainService {
	
	@Autowired
	MainDao maindao;
	
//	엑셀 파일의 데이터를 List[row] = {'cell','값'} 의 형태로 반환
	public List<UserVo> excelUpload(File destFile) throws Exception{
		
//		엑셀 옵션값을 ExcelReadOption 에 저장
		ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setFilePath(destFile.getAbsolutePath()); //파일경로 추가
        excelReadOption.setOutputColumns("A", "B", "C", "D", "E"); //추출할 컬럼명 추가
        excelReadOption.setStartRow(3); //시작행 (맨 첫줄 1)
		
        List<UserVo>excelContent = ExcelRead.read(excelReadOption);
       
//        콘솔 출력 테스트
//        for(UserVo article: excelContent){
//            System.out.println(article.toString());
//        }
        
        maindao.insertCode(excelContent);
        
        return excelContent;
		
	}
	
	
//	값 하나만
//	public String excelDate(File destFile) throws Exception {
//    	ExcelReadOption excelReadOption = new ExcelReadOption();
//        excelReadOption.setFilePath(destFile.getAbsolutePath()); //파일경로 추가
//        excelReadOption.setOutputColumns("A", "B"); //추출할 컬럼명 추가
//        excelReadOption.setStartRow(1); //시작행(헤더부분 제외)
//        
//         List<Map<String, String>>excelContent  = ExcelRead.read(excelReadOption);
//         String pDate = excelContent.get(0).get("A").toString();
//         return pDate;
//    }
}
