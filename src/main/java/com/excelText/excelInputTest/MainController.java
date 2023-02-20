package com.excelText.excelInputTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MainController {
	
	@Inject
	MainService mainService;
	
	//업로드중인지 체크
	boolean uploading = false;
	
	//첫페이지
	@GetMapping(value="/")
	public String home( HttpServletRequest request, ModelMap model) {
		model.addAttribute("uploading", uploading);
		return "index";
	}
	//이동
	@GetMapping(value="/rehome")
	public String rehome( String url) {
//		model.addAttribute("uploading", uploading);
		System.out.println();
		return "redirect:/";
	}
	
//	엑셀파일 업로드
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public Callable<String> upload(RedirectAttributes redirectAttributes, MultipartHttpServletRequest multiRequest, HttpServletRequest request, ModelMap model) {
		
		System.out.println("시작");
		uploading = true;
		
		//걸린시간시작
 		long beforeTime = System.currentTimeMillis();
 		
 		return()->{
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
				excelData = mainService.excelUpload(fileDir); 
				fileDir.delete(); //엑셀 데이터 받아온 후 파일 삭제
			} catch (Exception e) {
	//			업로드 실패 시 파일 삭제
				e.printStackTrace();
				if(fileDir.exists()) {
					fileDir.delete();
				}	
			}
		}
		long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
		System.out.println("시간차이(m) : "+secDiffTime);
		
		model.addAttribute("excelDate", excelData);
		uploading=false;
		return "result";};
	}
	
	
	//비동기 테스트
	@RequestMapping(value="/asyncTest1")
	public String asyncTest1(ModelMap model) {
		System.out.println(uploading);
		model.addAttribute("uploading", uploading);
		return "asyncTest1";
	}
	
	@RequestMapping(value="/download")
	public String download(@RequestParam("name") String name, HttpServletResponse response) throws Exception {
		String fileName = new String();
		if(name.equals("엑셀업로드테스트_양식.xlsx")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar c1 = Calendar.getInstance();
			String strToday = sdf.format(c1.getTime()) ;
			fileName = new String((strToday+"_"+name).getBytes("UTF-8"),"ISO-8859-1");
		}else {
			fileName = new String(name.getBytes("UTF-8"),"ISO-8859-1");
		}
		try {
			File file = ResourceUtils.getFile("classpath:static/file/"+name);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename="+fileName);
			response.setHeader("Content-Transfer-Encoding", "binary");
			
			byte[] fileByte = FileUtils.readFileToByteArray(file);
			
			response.getOutputStream().write(fileByte);
			response.getOutputStream().flush();
			response.getOutputStream().close();
			
			System.out.println(file.exists());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
