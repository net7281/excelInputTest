package com.excelText.excelInputTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
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
		List<Map> map = mainService.excel_list();
		System.out.println(map.toString());
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
	@ResponseBody
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public SseEmitter upload(RedirectAttributes redirectAttributes, MultipartHttpServletRequest multiRequest, HttpServletRequest request, ModelMap model) {
		
		SseEmitter emitter = new SseEmitter();
		System.out.println("시작");
		uploading = true;
		
		//걸린시간시작
 		long beforeTime = System.currentTimeMillis();
 		MultipartFile excelFile = multiRequest.getFile("excelFile");
 		
 		
 		final CompletableFuture<Boolean> excelDataFile = mainService.excelDataFile(excelFile) ;
 		try {
			emitter.send(SseEmitter.event().data("시작"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 		excelDataFile.thenAccept(
 				result -> {
	 					try {
		 					long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
							long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
							System.out.println("시간차이(m) : "+secDiffTime);
	 						uploading=false;
	 						
							emitter.send(SseEmitter.event().data("되면좋겟어"));
						} catch (IOException e) {
							emitter.completeWithError(e);
						}
 						emitter.complete();
 					}
 				);
 		return emitter;
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
