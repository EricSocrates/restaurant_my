function reloadConfirmCode(){
    let time=new Date().getTime();
    //点击重新设置img的src属性
    //加时间是为了防止浏览器缓存之前的GET从而使每次请求不发生变化
    $("#imageCode").attr("src", "admin?method=confirmCode&d="+time);
}

function checkConfirmCode(value){
    $.ajax({
        url: "admin",
        data: "method=validateConfirm&code=" + value,
        type: "post",
        dataType: "text",
        success: function(msg){
            if(msg == "success"){
                confirmCode = true;
                $("#validate_code").text("验证码正确").css("color", "green");
                activeButton();
                return;
            }
            confirmCode = false;
            $("#validate_code").text("验证码错误").css("color", "red");
            activeButton();
        },
        error: function(msg){
            console.log("验证码的错误消息：" + msg);
        }
    });
}