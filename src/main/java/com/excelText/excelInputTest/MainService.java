package com.excelText.excelInputTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	
	@Async
	public CompletableFuture<Boolean> excelDataFile(MultipartFile excelFile) {
			
//			파일이름 & 특수문자 치환
		String fileName = excelFile.getOriginalFilename();
		fileName = fileName.replaceAll("\n", "").replaceAll("\r", "").replaceAll("&", "");		
//			폴더경로
		File fileFolder = new File("C:\\upload\\");
//			파일이 들어갈 전체경로
		File fileDir = new File("C:\\upload\\"+fileName);
		
//		받아온 엑셀 데이터를 저장할 변수
		List excelData = new ArrayList();
		
		if(excelFile != null || !excelFile.isEmpty()) {
			try {
//			업로드한 엑셀파일을 서버에 저장 > 데이터 꺼내기 > 엑셀파일삭제	
				
//			upload 폴더 없으면 생성
				if(!fileFolder.exists()) {
					fileFolder.mkdirs(); 
				}
				excelFile.transferTo(fileDir); //엑셀파일 생성
				excelData = excelUpload(fileDir); 
				fileDir.delete(); //엑셀 데이터 받아온 후 파일 삭제
			} catch (Exception e) {
//			업로드 실패 시 파일 삭제
				e.printStackTrace();
				if(fileDir.exists()) {
					fileDir.delete();
				}
			}
		}
		return CompletableFuture.completedFuture(true);
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
