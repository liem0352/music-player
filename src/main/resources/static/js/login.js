
// 登录按钮点击事件
function cliLogin(){
    let username = document.querySelector("#username");
    let password = document.querySelector("#password");

    let name = username.value;
    let pass = password.value;

    if(name.trim() === ""){
        alert("请输入用户名");
        username.focus();
        return;
    }

    if(pass.trim() === ""){
        alert("请输入密码");
        password.focus();
        return;
    }

    jQuery.ajax({
        type : "post",
        url : "user/login",
        data:{
            "username":name.trim(),
            "password": pass.trim()
        },
        success:function(obj){
            if(obj.status === 1){
                // 跳转到音乐列表页面
                location.href = "list.html";
            }else{
                alert("登录失败:"+obj.msg);
                username.value = "";
                password.value = "";
                username.focus();
            }
        },
        error:function(obj){
            console.log("请求失败:"+obj);
        }
    });
}
