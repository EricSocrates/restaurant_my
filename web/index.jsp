<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
  <head>
    <title>首页</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <style>
      /*防止页面上方与浏览器之间有空隙*/
      .page-header{
        margin: 2px 0px;
      }
    </style>
  </head>
  <body>
    <%--BootStrap模态框--%>
    <!-- Modal -->
    <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            <h4 class="modal-title" id="myModalLabel">管理员信息</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal" role="form" id="updateForm">
              <div class="modal-body">
                <input type="hidden" name="method" value="updateAdmin">
                <input type="hidden" name="id" value="${admin.id}">
                <div class="form-group">
                  <label class="col-sm-2 control-label">头像</label>
                  <div class="col-sm-10">
                    <image width="200px" height="100px" id="avatar" name="avatar" src="<%=request.getContextPath()%>${admin.savepath}"></image>
                  </div>
                </div>
                <div class="form-group">
                  <label for="username" class="col-sm-2 control-label">用户名</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" id="username" value="${admin.username}" name="username" disabled>
                  </div>
                  <div class="col-sm-2" style="padding:10px;">
                    <span id="validate_username" style="font-size: 12px;"></span>
                  </div>
                </div>
                <div class="form-group">
                  <label for="email" class="col-sm-2 control-label">邮箱</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" id="email" name="email" value="${admin.email}" placeholder="请输入邮箱" autocomplete="off" oninput="checkEmail(this.value)" required>
                  </div>
                  <div class="col-sm-2" style="padding:10px;">
                    <span id="validate_email" style="font-size: 12px;"></span>
                  </div>
                </div>
                <div class="form-group">
                  <label for="phone" class="col-sm-2 control-label">手机号</label>
                  <div class="col-sm-8">
                    <%--手机号不需要输入框右端这个上下箭头，遂切换输入框为tel类型--%>
                    <input type="tel" class="form-control" id="phone" name="phone" value="${admin.phone}" placeholder="请输入手机号" autocomplete="off" oninput="checkPhone(this.value)" required>
                  </div>
                  <div class="col-sm-2" style="padding:10px;">
                    <span id="validate_phone" style="font-size: 12px;"></span>
                  </div>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" id="goUpdate" onclick="update()" disabled>更新</button>
          </div>
        </div>
      </div>
    </div>
    <%--页头--%>
    <%--<div class="page-header">
      <img src="xxx" alt="" width="100%" height="130px">
      &lt;%&ndash;这里可以加页头图片&ndash;%&gt;
    </div>--%>
    <%--导航条--%>
    <nav class="navbar navbar-default" role="navigation">
      <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">餐厅管理</a>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          <ul class="nav navbar-nav">
            <li class="active"><a href="javascript:void(0)">首页</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown"><img class="img-circle" style="width: 30px;height: 30px;margin-right: 10px" src="<%=request.getContextPath()%>${admin.savepath}" alt="头像"/>${admin.username}<span class="caret"></span></a>
              <ul class="dropdown-menu" role="menu">
                <li><a href="javascript:void(0)" data-toggle="modal" data-target="#updateModal">个人信息</a></li>
                <li class="divider"></li>
                <li><a href="javascript:void(0);" onclick="logout()">注销</a></li>
              </ul>
            </li>
          </ul>
        </div><!-- /.navbar-collapse -->
      </div><!-- /.container-fluid -->
    </nav>
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-3">
          <div class="list-group">
            <a href="index_welcome.jsp" class="list-group-item active" target="test">
              首页
            </a>
            <%--使用iFrame将其他页面引入index--%>
            <a href="index_addTb.jsp" class="list-group-item" target="test">添加餐桌</a>
            <a href="index_tbList.jsp" class="list-group-item" target="test">餐桌列表</a>
          </div>
        </div>
        <div class="col-md-9">
          <iframe src="index_welcome.jsp" frameborder="0" width="100%" height="1000px" name="test">

          </iframe>
        </div>
      </div>
    </div>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="js/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
    <script>
      //_pre的意思是信息与原来相同（true）
      let pwd = true;
      let pwd_pre = true;
      let email = true;
      let email_pre = true;
      let phone = true;
      let phone_pre = true;


      $(function() {
        $(".list-group a").click(function() {
          //给被点击的添加class，同胞元素（siblings）删除class
          $(this).addClass("active").siblings().removeClass("active");
        });
      });

      function checkEmail(value){
        if(value == "${admin.email}"){
          email = true;
          email_pre = true;
          $("#validate_email").text("");
          activeButton();
          return;
        } else if(/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/.test(value)){
          email = true;
          email_pre = false;
          $("#validate_email").text("验证成功").css("color", "green");
          activeButton();
          return;
        }
        email_pre = false;
        email = false;
        $("#validate_email").text("邮箱不合法").css("color", "red");
        activeButton();
      }

      function checkPhone(value){
        if(value == "${admin.phone}"){
          phone = true;
          phone_pre = true;
          $("#validate_phone").text("");
          activeButton();
          return;
        }else if(/^1[34578]\d{9}$/.test(value)){
          phone = true;
          phone_pre = false;
          $("#validate_phone").text("验证成功").css("color", "green");
          activeButton();
          return;
        }
        phone_pre = false;
        phone = false;
        $("#validate_phone").text("手机号不合法").css("color", "red");
        activeButton();
      }

      function activeButton(){
        if(pwd_pre && email_pre && phone_pre) $("#goUpdate").attr("disabled", "disabled");
        else if(pwd && email && phone) $("#goUpdate").removeAttr("disabled");
        else $("#goUpdate").attr("disabled", "disabled");
      }

      // ajax方式将注册表单信息、文件提交到服务器
      function update(){
        $.post("admin", $('#updateForm').serialize(), function(msg){
          console.log(msg);
            if(msg == "success"){
              alert("修改成功！");
              location.href="index.jsp";
            }else alert("修改失败，请重试！");
        }, "text");
      }

      function logout(){
        alert("注销成功！");
        location.href = "admin?method=logout";
      }
    </script>
  </body>
</html>