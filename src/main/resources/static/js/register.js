// 注册按钮点击事件
function cliRegister(){
    let username = document.querySelector("#username");
    let password1 = document.querySelector("#password1");
    let password2 = document.querySelector("#password2");


    let name = username.value;
    let pass1 = password1.value;
    let pass2 = password2.value;

    if(name.trim() === ""){
        alert("请输入用户名");
        username.focus();
        return;
    }

    if(pass1.trim() === "" || pass2.trim() === "" ){
        alert("请输入密码");
        password1.focus();
        return;
    }

    if(pass1.trim().length <= 6 ){
        alert("密码长度需要大于6位");
        password1.focus();
        return;
    }


    if(pass1 != pass2){
        alert("两次密码不一致!");
        password1.value = "";
        password2.value = "";
        password1.focus();
        return;
    }

    jQuery.ajax({
        url :"user/register",
        type:"post",
        data:{
            "username":name,
            "password1":pass1,
            "password2":pass2
        },
        success:function(obj){
            if(obj.status === 1){
                //重定向到登录页面
                location.href = "login.html";
            }else{
                alert("注册失败："+obj.msg);
                username.value = "";
                password1.value = "";
                password2.value = "";
                username.focus();
            }
        },
        error:function(obj){
            alert("请求失败!");
        }
    });
}