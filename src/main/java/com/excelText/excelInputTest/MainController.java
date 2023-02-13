package com.excelText.excelInputTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	
	//첫페이지
	@GetMapping(value="/")
	public String home( HttpServletRequest request, ModelMap model) {
		return "index";
	}
	
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public String upload(RedirectAttributes redirectAttributes, MultipartHttpServletRequest multiRequest, ModelMap model) {
	
		MultipartFile excelFile = multiRequest.getFile("excelFile");

		List<Map> excelData = new ArrayList<Map>();
		
		try {
			String fileName = excelFile.getOriginalFilename();
			fileName = fileName.replaceAll("\n", "").replaceAll("\r", "").replaceAll("&", "");
			File fileDir = new File("C:\\upload\\"+fileName);
			
			if(!fileDir.exists()) {
				try{
					fileDir.mkdir(); //폴더 생성합니다. ("새폴더"만 생성)
				    System.out.println("폴더가 생성완료.");
			        } 
			        catch(Exception e){
				    e.getStackTrace();
				}     
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return "result";
	}
}
