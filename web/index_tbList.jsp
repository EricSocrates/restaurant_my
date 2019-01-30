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
        <title>餐桌列表</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
    <h2>餐桌列表</h2>
        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="headingOne">
                    <h4 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                            按桌号查询
                        </a>
                    </h4>
                </div>
                <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
                    <div class="panel-body">
                        <form class="form-inline" role="form">
                            <div class="form-group">
                                <label for="query_no">桌号：</label>
                                <input type="tel" class="form-control" id="query_no" name="query_no" placeholder="请输入桌号" autocomplete="off">
                            </div>
                            <button type="button" class="btn btn-primary" onclick="queryByCondition()">查询</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <table class="table table-bordered table-striped table-condensed table-hover">
            <!--表头-->
            <thead>
            <tr>
                <th>序号</th>
                <th>桌号</th>
                <th>人数</th>
                <th>状态</th>
            </tr>
            </thead>
            <!--jQuery动态添加数据-->
            <tbody id="data"></tbody>
        </table>
        <!--简单完成分页功能-->
        <div>
            <button id="first" type="button" class="btn btn-primary">第一页</button>
            <button id="pre" type="button" class="btn btn-success">上一页</button>
            <button id="next" type="button" class="btn btn-info">下一页</button>
            <button id="end" type="button" class="btn btn-danger">尾页</button>
        </div>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="js/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>
        <script>
            //定义当前页码
            let pageNumber = 1;
            //定义每页最多多少条数据
            let pageSize = 5;
            //定义上一页、下一页、末页的页码(pageCount)
            let pre, next, end;
            //定义查询条件
            let condition = "";

            $(function(){
                //每当页面加载完成，都要先显示数据
                queryData(pageNumber, pageSize, condition);
                //给控制翻页的按钮绑定事件，每个事件都是按参数查询动作
                $("#first").on("click", function(){
                    queryData(1, pageSize, condition);
                });
                $("#pre").on("click", function(){
                    queryData(pre, pageSize, condition);
                });
                $("#next").on("click", function(){
                    queryData(next, pageSize, condition);
                });
                $("#end").on("click", function(){
                    queryData(end, pageSize, condition);
                });
            });

            //点击查询按钮，按条件查询
            function queryByCondition(){
                condition = "";
                //获取待查询的值
                let query_no = $("#query_no").val();
                //当值不为空时才进行查询
                if(query_no != null && query_no !== ""){
                    //将要查询的桌号参数拼在链接末尾
                    condition = "&no=" + query_no;
                    //执行带过滤条件的分页查询
                    queryData(1, pageSize, condition);
                }
            }

            //分页查询函数，参数：当前页码、每页几条数据、过滤条件
            function queryData(pageNumber, pageSize, condition){
                $.ajax({
                    type: "post",
                    url: "tables",
                    //参数的意思是当前页码、每页的数据、以及可选的查询过滤条件
                    data: "method=queryTablesSeparated&pageNumber=" + pageNumber + "&pageSize=" + pageSize + condition,
                    dataType: "json",
                    success: function(data){
                        console.log(data);
                        let trs = "";
                        //对上一页、下一页、末页动态赋值
                        pre = data.pre;
                        next = data.next;
                        end = data.pageCount;
                        $.each(data.rows, function(i, tb){
                            trs += "<tr><td>" + tb.id + "</td><td>" + tb.no + "</td><td>" + tb.num + "</td><td>" + (tb.status=="1"?"正忙":"空闲") + "</td></tr>";
                        });
                        $("#data").html(trs);
                    }
                });
            }
        </script>
    </body>
</html>
