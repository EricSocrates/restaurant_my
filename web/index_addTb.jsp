<%--
  Created by IntelliJ IDEA.
  User: Yy
  Date: 2019/1/23
  Time: 10:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>添加餐桌</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <form class="form-horizontal" role="form" id="addForm">
            <%--写在隐藏表单域中可以不在提交时写在method中--%>
            <input type="hidden" name="method" value="addTables"/>
            <div class="form-group">
                <label for="no" class="col-sm-2 control-label">桌号</label>
                <div class="col-sm-8">
                    <input type="tel" class="form-control" id="no"
                           name="no" placeholder="请输入桌号" autocomplete="off" onblur="checkNo(this.value)" required>
                </div>
                <div class="col-sm-2" style="padding:10px;">
                    <span id="validate_no" style="font-size: 12px;"></span>
                </div>
            </div>
            <div class="form-group">
                <label for="num" class="col-sm-2 control-label">人数</label>
                <div class="col-sm-8">
                    <input type="tel" class="form-control" id="num"
                           name="num" placeholder="请输入可容纳人数" autocomplete="off" onblur="checkNum(this.value)" required>
                </div>
                <div class="col-sm-2" style="padding:10px;">
                    <span id="validate_num" style="font-size: 12px;"></span>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="button" id="addBtn" onclick="add()" class="btn btn-primary" disabled>添加</button>
                </div>
            </div>
        </form>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="js/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>
        <script>
            let no = false;
            let num = false;
            function checkNo(value){
                if(value > 0){
                    $.ajax({
                       url: "tables",
                       data: "method=queryTablesByNo&no=" + value,
                       method: "post",
                       dataType: "text",
                       success: function(msg){
                           if(msg == "exist"){
                               no = false;
                               $("#validate_no").text("餐桌号重复").css("color", "red");
                               activeButton();
                           }else{
                               no = true;
                               $("#validate_no").text("验证成功").css("color", "green");
                               activeButton();
                           }
                       },
                       error: function (msg) {
                           console.log("新增餐桌错误" + msg);
                       }
                    });
                }else if(value <= 0){
                    no = false;
                    $("#validate_no").text("餐桌号填写有误").css("color", "red");
                    activeButton();
                }
                else{
                    no = false;
                    $("#validate_no").text("餐桌号不能为空").css("color", "red");
                    activeButton();
                }
            }
            function checkNum(value){
                if(value > 0){
                    num = true;
                    $("#validate_num").text("验证成功").css("color", "green");
                    activeButton();
                }else if(value <= 0){
                    num = false;
                    $("#validate_num").text("人数填写有误").css("color", "red");
                    activeButton();
                }
                else{
                    num = false;
                    $("#validate_num").text("人数不能为空").css("color", "red");
                    activeButton();
                }
            }
            function activeButton(){
                if(no && num) $("#addBtn").removeAttr("disabled");
                else $("#addBtn").attr("disabled", "disabled");
            }
            function add(){
                $.post("tables", $("#addForm").serialize(), function(msg){
                    if(msg == "success"){
                        alert("添加成功！");
                        location.href = "index_tbList.jsp";
                    }else{
                        alert("添加失败，请重试！");
                    }
                }, "text");
            }
        </script>
    </body>
</html>
