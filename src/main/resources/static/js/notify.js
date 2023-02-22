window.onload=function(){
	
	const eventSource = new EventSource(`/uploadState`);
	
	eventSource.onmessage = event => {
        console.log(event.data)
    }
	
}