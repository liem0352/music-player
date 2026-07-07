function uploadMusic(){

    let file = document.querySelector(".login-form #file");
    let singer = document.querySelector(".login-form #singer");
    var formData = new FormData($('#uploadForm')[0]);

    if(file.value.trim() === ""){
        alert("请选择文件");
        return;
    }

    if(singer.value.trim() === ""){
        alert("请输入歌手");
        singer.focus();
        return;
    }

    jQuery.ajax({
        url : "music/upload",
        type : "post",
        data : formData,
        cache: false,
        processData: false,
        contentType: false,
        success:function(obj){
            if(obj.data.result === "success"){
                //从定向的音乐列表页
                location.href = obj.data.redirect;
            }else{
                alert("上传失败!"+obj.msg);
                file.value = "";
                singer.value = "";
            }
        },
        error:function(obj){
            console.log("请求失败!"+obj);
        }
    });
}