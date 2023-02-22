package com.excelText.excelInputTest.sse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

@RestController
@RequestMapping("/uploadState")
public class SseController {
	
	
	@GetMapping
	public SseEmitter streamSseMvc() {
	    SseEmitter emitter = new SseEmitter();
	    ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
	    sseMvcExecutor.execute(() -> {
	        try {
	            for (int i = 0; i < 20; i++) {
	                SseEventBuilder event = SseEmitter.event()
	                        .data(System.currentTimeMillis());
	                emitter.send(event);
	                Thread.sleep(1000);
	            }
	        } catch (Exception ex) {
	            emitter.completeWithError(ex);
	        }
	    });
	    return emitter;
	}
	
	
}
