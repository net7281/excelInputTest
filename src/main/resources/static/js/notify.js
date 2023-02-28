window.onload=function(){
	
	const eventSource = new EventSource(`/upload`);
	
	eventSource.onmessage = event => {
        console.log(event.data)
    }
	
}