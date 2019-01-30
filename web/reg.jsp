<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>注册</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <!--BootStrap模态框-->
        <div class="modal fade" id="reg_modal" data-backdrop="static">
            <div class="modal-dialog">
                <div class="modal-content">
                    <%--enctype值为multipart/form-data时，意思是底层浏览器把参数以分隔符分隔开，把文件以二进制的形式发送到服务器--%>
                    <form class="form-horizontal" role="form" id="regForm" enctype="multipart/form-data" method="post">
                        <div class="modal-header text-center">
                            <h4 class="modal-title">管理员注册</h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="avatar" class="col-sm-2 control-label">头像</label>
                                <div class="col-sm-8">
                                    <input type="file" class="form-control" id="avatar" name="avatar" onchange="checkAvatar(this.value)" required>
                                </div>
                                <div class="col-sm-2" style="padding:10px;">
                                    <span id="validate_avatar" style="font-size: 12px;"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2" style="font-size: 12px;color: red;">
                                    请选择.jpg/.png/.gif格式的图片上传
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="username" class="col-sm-2 control-label">用户名</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" id="username" name="username" placeholder="请输入4位字符以上的用户名" autocomplete="off" onblur="checkUsername(this.value)" required>
                                </div>
                                <div class="col-sm-2" style="padding:10px;">
                                    <span id="validate_username" style="font-size: 12px;"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="pwd" class="col-sm-2 control-label">密码</label>
                                <div class="col-sm-8">
                                    <input type="password" class="form-control" id="pwd" name="pwd" placeholder="请输入4位以上英文字母开头的密码（可输入数字）" onblur="checkPwd(this.value)" required>
                                </div>
                                <div class="col-sm-2" style="padding:10px;">
                                    <span id="validate_pwd" style="font-size: 12px;"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="email" class="col-sm-2 control-label">邮箱</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" id="email" name="email" placeholder="请输入邮箱" autocomplete="off" onblur="checkEmail(this.value)" required>
                                </div>
                                <div class="col-sm-2" style="padding:10px;">
                                    <span id="validate_email" style="font-size: 12px;"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="phone" class="col-sm-2 control-label">手机号</label>
                                <div class="col-sm-8">
                                    <%--手机号不需要输入框右端这个上下箭头，遂切换输入框为tel类型--%>
                                    <input type="tel" class="form-control" id="phone" name="phone" placeholder="请输入手机号" autocomplete="off" onblur="checkPhone(this.value)" required>
                                </div>
                                <div class="col-sm-2" style="padding:10px;">
                                    <span id="validate_phone" style="font-size: 12px;"></span>
                                </div>
                            </div>
                            <!--验证码功能-->
                            <div class="form-group">
                                <label for="confirmCode" class="col-sm-2 control-label">验证码</label>
                                <div class="col-sm-3">
                                    <input type="text" class="form-control" id="confirmCode" name="confirmCode" autocomplete="off" onblur="checkConfirmCode(this.value)" required>
                                </div>
                                <div class="col-sm-4">
                                    <!--第一次加载验证码的servlet类源地址src-->
                                    <img alt="验证码" id="imageCode" onclick="reloadConfirmCode()" src="admin?method=confirmCode"/>
                                    <%--href这样写，为了防止空跳转 --%>
                                    <a href="javascript:void(0);" onclick="reloadConfirmCode()">看不清楚</a><br>
                                </div>
                                <div class="col-sm-2" style="padding:10px;">
                                    <span id="validate_code" style="font-size: 12px;"></span>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <a href="login.jsp" class="btn btn-success">前往登录</a>
                            <button type="button" class="btn btn-primary" onclick="reg()" id="goReg" disabled>提交注册</button>
                        </div>
                    </form>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="js/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>
        <%--导入验证码工具--%>
        <script src="js/util.js"></script>
        <script>
            /*
            * 这里的傻瓜表单验证是最基本的的验证方式，每次失去焦点时验证并检测是否激活按钮，
            * 将onblur的方法放在ready函数中也可以。
            * 检测用户名是否重名、验证码检测是否正确，都使用的ajax的同步，因为若为异步，
            * 将与主线程不再是同线程，变量值不能动态修改，
            * 当变量都为true时，激活注册按钮，有一个无法通过验证，按钮都不能被激活。
            * */

            /*
            * 当然还可以直接在form表单onsubmit时直接“return 方法名()”，
            * 在提交时进行整个表单的验证，但是这样的用户体验并不好，
            * 遂使用这个笨重但是体验较好的方法，后期会有更好的表单验证办法
            * */
            let username = false;
            let pwd = false;
            let email = false;
            let phone = false;
            let avatar = false;
            let confirmCode = false;
            $(function(){
                //模态框页面加载时弹出
                $("#reg_modal").modal("show");
            });

            function checkAvatar(value){
                console.log(value);
                if(value != null){
                    if(value.endsWith(".jpg") || value.endsWith(".gif") || value.endsWith(".png")){
                        avatar = true;
                        $("#validate_avatar").text("验证成功").css("color", "green");
                        activeButton();
                        return;
                    }
                    avatar = false;
                    $("#validate_avatar").text("头像格式错误").css("color", "red");
                    activeButton();
                    return;
                }
                avatar = false;
                $("#validate_avatar").text("头像不能为空").css("color", "red");
                activeButton();
            }

            function checkUsername(value){
                if(value.length > 4){
                    $.ajax({
                       url: "admin",
                       data: "method=queryAdminByUsername&username=" + value,
                       type: "post",
                       dataType: "text",
                       success: function(msg){
                           if(msg == "exist"){
                               username = false;
                               $("#validate_username").text("用户名重复").css("color", "red");
                               activeButton();
                               return;
                           }
                           username = true;
                           $("#validate_username").text("用户名可使用").css("color", "green");
                           activeButton();
                       },
                       error: function(msg){
                           console.log("username错误消息：" + msg);
                       }
                    });
                }
                else {
                    username = false;
                    $("#validate_username").text("用户名不合法").css("color", "red");
                    activeButton();
                }
            }

            function checkPwd(value){
                if(/^[a-zA-Z][a-zA-Z0-9]{4,}$/.test(value)){
                    pwd = true;
                    $("#validate_pwd").text("验证成功").css("color", "green");
                    activeButton();
                    return;
                }
                pwd = false;
                $("#validate_pwd").text("密码不合法").css("color", "red");
                activeButton();
            }

            function checkEmail(value){
                if(/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/.test(value)){
                    email = true;
                    $("#validate_email").text("验证成功").css("color", "green");
                    activeButton();
                    return;
                }
                email = false;
                $("#validate_email").text("邮箱不合法").css("color", "red");
                activeButton();
            }

            function checkPhone(value){
                if(/^1[34578]\d{9}$/.test(value)){
                    phone = true;
                    $("#validate_phone").text("验证成功").css("color", "green");
                    activeButton();
                    return;
                }
                phone = false;
                $("#validate_phone").text("手机号不合法").css("color", "red");
                activeButton();
            }

            function activeButton(){
                if(avatar && username && pwd && email && phone && confirmCode) {
                    $("#goReg").removeAttr("disabled");
                }
                else{
                    $("#goReg").attr("disabled", "disabled");
                }
            }

            // ajax方式将注册表单信息、文件提交到服务器
            function reg(){
                $.ajax({
                    url: "admin",
                    type: "POST",
                    cache: false,
                    data: new FormData($('#regForm')[0]),
                    dataType: "text",
                    processData: false,
                    contentType: false,
                    success: function(msg){
                        if(msg == "success"){
                            alert("注册成功！");
                            location.href="index.jsp";
                        }else{
                            alert("注册失败，请重试！");
                            location.href="reg.jsp";
                        }
                    },
                    error: function(msg){
                        console.log("注册错误信息：" + msg);
                    }
                });
            }
        </script>
    </body>
</html>
