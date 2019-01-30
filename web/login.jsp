<%--
  Created by IntelliJ IDEA.
  User: Yy
  Date: 2019/1/24
  Time: 1:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>登录</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="modal fade" id="login_modal" data-backdrop="static">
            <div class="modal-dialog">
                <div class="modal-content">
                    <%--enctype默认值为application/x-www-form-urlencoded，意思是将表单中的参数以name=value&xxx=xxx的格式输出--%>
                    <form class="form-horizontal" role="form" id="loginForm" enctype="application/x-www-form-urlencoded">
                        <div class="modal-header text-center">
                            <h4 class="modal-title">管理员登录</h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="username" class="col-sm-2 control-label">用户名</label>
                                <div class="col-sm-7">
                                    <input type="text" class="form-control" id="username" name="username" placeholder="请输入用户名" autocomplete="off" onblur="checkUsername(this.value)" required>
                                </div>
                                <div class="col-sm-3" style="padding:10px;">
                                    <span id="validate_username" style="font-size: 12px;"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="pwd" class="col-sm-2 control-label">密码</label>
                                <div class="col-sm-7">
                                    <input type="password" class="form-control" id="pwd" name="pwd" placeholder="请输入密码" onblur="checkPwd(this.value)" required>
                                </div>
                                <div class="col-sm-3" style="padding:10px;">
                                    <span id="validate_pwd" style="font-size: 12px;"></span>
                                </div>
                            </div>
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
                            <a href="reg.jsp" class="btn btn-success">前往注册</a>
                            <button type="button" class="btn btn-primary" id="goLogin" onclick="login()">登录</button>
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
            let username = false;
            let pwd = false;
            let confirmCode = false;
            $(function(){
                //模态框页面加载时弹出
                $("#login_modal").modal("show");
            });

            function checkUsername(value){
                if(value.length == 0){
                    username = false;
                    $("#validate_username").text("用户名不能为空").css("color", "red");
                    activeButton();
                }
                else {
                    username = true;
                    $("#validate_username").text("");
                    activeButton();
                }
            }

            function checkPwd(value){
                if(value.length == 0){
                    pwd = false;
                    $("#validate_pwd").text("密码不能为空").css("color", "red");
                    activeButton();
                }
                else {
                    pwd = true;
                    $("#validate_pwd").text("");
                    activeButton();
                }
            }

            function activeButton(){
                if(username && pwd && confirmCode) {
                    $("#goLogin").removeAttr("disabled");
                }
                else{
                    $("#goLogin").attr("disabled", "disabled");
                }
            }

            function login(){
                $.post("admin?method=queryAdminByUsernameAndPwd", $("#loginForm").serialize(), function(data){
                    console.log(data);
                    if(data.msg == "success"){
                        alert("尊敬的" + data.username + "，欢迎回来！");
                        location.href = "index.jsp";
                    }else alert("用户名或密码错误，请重试！");
                },"json");
            }
        </script>
    </body>
</html>
