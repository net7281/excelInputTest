window.onload=function(){
	
	// const eventSource = new EventSource(`/uploadState`);
	
	// eventSource.onmessage = event => {
    //     console.log(event.data)
    // }

    let uploading = false;
    const area = document.querySelector("#uploading");
    const submitBt = document.querySelector("#submitBt");

    area.innerText = "업로드중";
    submitBt.setAttribute("disabled","true")

	let interval = setInterval(()=> 
		fetch("/checkUpload").then((response) => response.json())
                        .then((data) => {   console.log(data.percent + " / " + data.uploading);
                                            uploading = data.uploading;
                                            if(data.uploading===false){
                                                clearInterval(interval);
                                                area.innerText = "완료";
                                                submitBt.removeAttribute("disabled")
                                            }
                                        }
                        ) 
	,1000);


}



