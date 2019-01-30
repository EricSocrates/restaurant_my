package com.test.restaurant.tables.servlet;

import com.test.restaurant.common.page.Page;
import com.test.restaurant.tables.entity.Tables;
import com.test.restaurant.tables.service.TablesService;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*注解相当于写在web.xml的这段代码，有了注解，web.xml中注册的这段内容可以不写
 * <servlet>
 *       <servlet-name>TablesServlet</servlet-name>
 *       <servlet-class>com.test.restaurant.tables.servlet.TablesServlet</servlet-class>
 * </servlet>
 * <servlet-mapping>
 *     <servlet-name>TablesServlet</servlet-name>
 *     <url-pattern>/tables</url-pattern>
 * </servlet-mapping>
 * */
@WebServlet(name = "TablesServlet", urlPatterns = "/tables")
public class TablesServlet extends HttpServlet {

    //通过前端获取method，根据method值通过反射调用对应方法
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        String method = request.getParameter("method");
        //文件上传时，GET方式的链接传值和GET/POST通用的数据区传值都不能使用
        //只能通过multipart将文件以及其他参数以二进制方式上传至服务器
        try {
            Class c = this.getClass();
            Method m = c.getDeclaredMethod(method, HttpServletRequest.class, HttpServletResponse.class);
            //如果之前调用方法是主动的，那么这个方法相当于被动调用执行了
            //反射相当于将陈述句变为被动句
            m.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void queryTablesSeparated(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        String no = request.getParameter("no");

        TablesService ts = new TablesService();
        //填充Page的对象，分页的计算只需要我们传入参数
        Page<Tables> tPage = new Page<>();
        tPage.setPageNumber(pageNumber);
        tPage.setPageSize(pageSize);
        tPage.setTotal(ts.countTables(no));
        //获取行的参数，直接由Page算好，传入即可
        tPage.setRows(ts.queryTablesSeparated(tPage.getStart(), tPage.getPageSize(), no));

        PrintWriter out = response.getWriter();
        //将组装好的page转为JSON字符串放入响应中发回客户端
        out.print(JSONObject.fromObject(tPage));
        out.close();
    }

    protected void addTables(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Tables t = new Tables();
        t.setNo(Integer.parseInt(request.getParameter("no")));
        t.setNum(Integer.parseInt(request.getParameter("num")));
        t.setCreatetime(new Timestamp(System.currentTimeMillis()));
        t.setUpdatetime(new Timestamp(System.currentTimeMillis()));
        TablesService ts = new TablesService();
        PrintWriter out = response.getWriter();
        out.print(ts.addTables(t) == 1? "success" : "fail");
        out.close();
    }

    protected void queryTablesByNo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer no = Integer.parseInt(request.getParameter("no"));
        TablesService ts = new TablesService();
        PrintWriter out = response.getWriter();
        out.print(ts.queryTablesByNo(no) != null? "exist" : "success");
        out.close();
    }
}
