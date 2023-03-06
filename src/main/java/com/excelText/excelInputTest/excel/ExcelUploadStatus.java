package com.excelText.excelInputTest.excel;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ExcelUploadStatus {
	
	private int percent = 0;
	private int totalCount = 0;
	private boolean uploading =false;
	
}
