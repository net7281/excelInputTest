window.onload=function(){
	
	const eventSource = new EventSource(`/uploadState`);
	
	eventSource.onmessage = function (event) {
    	alert("완료")
	};
	
}