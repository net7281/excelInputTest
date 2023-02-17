package com.excelText.excelInputTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainController {
	
	@Inject
	MainService mainService;
	
	//첫페이지
	@GetMapping(value="/")
	public String home( HttpServletRequest request, ModelMap model) {
		return "index";
	}
	
//	엑셀파일 업로드
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public String upload(RedirectAttributes redirectAttributes, MultipartHttpServletRequest multiRequest, ModelMap model) {
		
		//걸린시간용
 		long beforeTime = System.currentTimeMillis();
	
		MultipartFile excelFile = multiRequest.getFile("excelFile");
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
				mainService.excelUpload(fileDir); 
				fileDir.delete(); //엑셀 데이터 받아온 후 파일 삭제
			} catch (Exception e) {
	//			업로드 실패 시 파일 삭제
				if(fileDir.exists()) {
					fileDir.delete();
				}	
			}
		}
		long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
		System.out.println("시간차이(m) : "+secDiffTime);
		
		model.addAttribute("excelDate", excelData);
		return "result";
	}
	
	@RequestMapping(value="/download")
	public String download() {
		String fileName = "엑셀업로드테스트_양식.xlsx";
		String saveFileName = "c:/tmp/엑셀업로드테스트_양식.xlsx";
		String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8";
		return contentType;
		
	}
}
