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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
	
	//?????????????????? ??????
	boolean uploading = false;
	
	//????????????
	@GetMapping(value="/")
	public String home( HttpServletRequest request, ModelMap model) {
		model.addAttribute("uploading", uploading);
		return "index";
	}
	
//	???????????? ?????????
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public String upload(RedirectAttributes redirectAttributes, MultipartHttpServletRequest multiRequest, HttpServletRequest request, ModelMap model) {
		
		System.out.println("??????");
		uploading = true;
		
		//??????????????????
 		long beforeTime = System.currentTimeMillis();
 		MultipartFile excelFile = multiRequest.getFile("excelFile");
 		
 		
 		final CompletableFuture<Boolean> excelDataFile = mainService.excelDataFile(excelFile) ;
 		excelDataFile.thenAccept(
 				result -> {
	 					long afterTime = System.currentTimeMillis(); // ?????? ?????? ?????? ?????? ????????????
						long secDiffTime = (afterTime - beforeTime)/1000; //??? ????????? ??? ??????
						System.out.println("????????????(m) : "+secDiffTime);
 						uploading=false;
 					}
 				);
 		return "redirect:/";
	}
	
	
	//????????? ?????????
	@RequestMapping(value="/asyncTest1")
	public String asyncTest1(ModelMap model) {
		System.out.println(uploading);
		model.addAttribute("uploading", uploading);
		return "asyncTest1";
	}
	
	@RequestMapping(value="/download")
	public String download(@RequestParam("name") String name, HttpServletResponse response) throws Exception {
		String fileName = new String();
		if(name.equals("????????????????????????_??????.xlsx")) {
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
	
	@ResponseBody
	@RequestMapping(value="/checkUpload")
	public void checkUpload(HttpServletResponse response)throws Exception{
		
	}
	
	@RequestMapping(value="/ExcelDownload")
	public void ExcelDownload(HttpServletResponse response)throws Exception{
		
		//??????????????????
 		long beforeTime = System.currentTimeMillis();
 		
//		????????????
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timesdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		Calendar c1 = Calendar.getInstance();
		String strToday = sdf.format(c1.getTime()) ;
		String strTodayTime = timesdf.format(c1.getTime()) ;
		
		// ?????? ?????????
		String filename = strToday+"_excel.xlsx";
		
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		
//      ???????????????
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font Bold = workbook.createFont();
		Bold.setBold(true);
		style.setFont(Bold);
		style.setAlignment(HorizontalAlignment.CENTER);
		
//		?????? ?????? ??????
		Sheet sheet = workbook.createSheet(strToday+"excelData");
		Row row;
		Cell cell;
		int rowNo = 0;
		int count=1;
		
		// ?????? ?????? ??????
		List<Map> columnDate = mainService.getColumName();
		//??????
		row = sheet.createRow(rowNo++);
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,columnDate.size()-1));
		cell = row.createCell(0);
		cell.setCellValue("????????? : "+strTodayTime);
		cell.setCellStyle(style);
		
		//?????????
		row = sheet.createRow(rowNo++);
		for(int i = 0; i<columnDate.size() ;i++) {
			cell = row.createCell(i);
			cell.setCellValue((String)columnDate.get(i).get("column_name"));
			cell.setCellStyle(style);
		}
		
		//??? ABC
		List<String> column_place = new ArrayList<>();
		char aString = 65;
		for(int i=0; i<columnDate.size();i++) {
			column_place.add(aString+"");
			aString++;
		}
		System.out.println(column_place.toString());
		
		int rowCount = mainService.getRowCount();
		
		
		System.out.println("i = " + Math.ceil(rowCount/10000));
		
		for(int i=0; i<= Math.ceil(rowCount/10000) ; i++) {
			List<Map> dataList= mainService.getDataforExcel(column_place,i);
			System.out.println("a = " + dataList.size() + "- i = "+i);
			for(int a=0;a<dataList.size();a++) {
				row = sheet.createRow(rowNo++);
				for(int j=0; j<columnDate.size(); j++) {
					cell = row.createCell(j);
					String place = (String) columnDate.get(j).get("column_place");
					cell.setCellValue(String.valueOf(dataList.get(a).get(place)));
				}
				// ???????????? flush ??????
                if (rowNo % 100 == 0) {
                  ((SXSSFSheet) sheet).flushRows(100); 
                }
			}
			
		}
		
		
		
		// ????????? ????????? ????????? ??????
		response.setContentType("ms-vnd/excel");
		response.setHeader("Content-Disposition", "attachment;filename="+filename);

		// ?????? ??????
		workbook.write(response.getOutputStream());
		workbook.close();
		
		long afterTime = System.currentTimeMillis(); // ?????? ?????? ?????? ?????? ????????????
		long secDiffTime = (afterTime - beforeTime)/1000; //??? ????????? ??? ??????
		System.out.println("????????????(m) : "+secDiffTime);
		
	}
	
}
