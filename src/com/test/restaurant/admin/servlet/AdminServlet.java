package com.test.restaurant.admin.servlet;

import com.jspsmart.upload.File;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;
import com.test.restaurant.admin.entity.Admin;
import com.test.restaurant.admin.service.AdminService;
import net.sf.json.JSONObject;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@WebServlet(name = "AdminServlet", urlPatterns = "/admin")
public class AdminServlet extends HttpServlet {

    //通过前端获取method，根据method值通过反射调用对应方法
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=utf-8");
        String method = request.getParameter("method");
        //文件上传时，GET方式的链接传值和GET/POST通用的数据区传值都不能使用
        //只能通过multipart将文件以及其他参数以二进制方式上传至服务器
        if(method == null){
            regAdmin(request, response);
        }
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

    //登录的验证码
    protected void confirmCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //这个方法实现验证码的生成
        //创建图像缓冲区
        BufferedImage bi = new BufferedImage(68, 22, BufferedImage.TYPE_INT_RGB);
        //通过缓冲区创建一个画布
        Graphics g = bi.getGraphics();
        //创建颜色
        Color c = new Color(200, 150, 255);
        //根据背景画一个矩形框
        //为画布创建背景颜色
        g.setColor(c);
        //填充指定的矩形
        g.fillRect(0, 0, 68, 22);
        //转化为字符型的数组
        char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random r = new Random();
        int len = ch.length;
        //index用于存放随机数字
        int index;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            //产生随机数字
            index = r.nextInt(len);
            //设置颜色
            g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt(255)));
            //画数字以及数字的位置
            g.drawString(ch[index] + "", (i * 15) + 3, 18);
            sb.append(ch[index]);
        }
        //将验证码字符串保留在session中，便于后续验证使用
        request.getSession().setAttribute("picCode", sb.toString());
        ImageIO.write(bi, "JPG", response.getOutputStream());
    }

    //根据用户名查询管理员
    protected void queryAdminByUsername(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("username");
        System.out.println("un=" + name);
        AdminService as = new AdminService();
        Admin a = as.queryAdminByUsername(name);
        String text = (a == null? "success" : "exist");
        PrintWriter out = response.getWriter();
        out.print(text);
        out.close();
    }

    //验证码是否通过
    protected void validateConfirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String picCode = (String)session.getAttribute("picCode");
        System.out.println("pc=" + picCode);
        String u_picCode = (String)request.getParameter("code");
        String flag = picCode.toLowerCase().equals(u_picCode.toLowerCase())? "success":"error";
        PrintWriter out = response.getWriter();
        out.print(flag);
        out.close();
    }

    //用户注册
    protected void regAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        //使用SmartUpload的三个步骤
        //S1.构造对象
        SmartUpload su = new SmartUpload();
        //S2.初始化
        su.initialize(this, request, response);
        //S3.上传，第三方组件解析请求，获取前端发来的参数并封装到自己的对象中
        try {
            su.upload();
            Admin admin = new Admin();
            //这里的Request及其方法均为被封装的SmartUpload的普通类及方法
            //先将基本参数封装到自己的对象中
            Request req = su.getRequest();
            String username = req.getParameter("username");
            String pwd = req.getParameter("pwd");
            String phone = req.getParameter("phone");
            String email = req.getParameter("email");
            //基本参数存入实体类
            admin.setCreatetime(new Timestamp(System.currentTimeMillis()));
            admin.setEmail(email);
            admin.setUsername(username);
            admin.setPhone(phone);
            admin.setPwd(pwd);
            admin.setUpdatetime(new Timestamp(System.currentTimeMillis()));

            //获得文件参数
            Files fs = su.getFiles();
            //获得其中第一个文件
            File f = fs.getFile(0);
            //获取文件名
            String fName = f.getFileName();
            //获取文件后缀
            String fExt = f.getFileExt();
            //还可以获取到上传这个文件的控件的名字
            //如<input type="file" name="avatar">中的avatar
            String fieldName = f.getFieldName();
            //为了图片在数据库存储的独一性，图片存储到数据库的文件名需要修改，需要拼接文件后缀的名字
            String newName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fExt;

            //把文件从第三方插件中保存到指定目录
            //这里新建一个/myFiles目录
            f.saveAs("/myFiles" + newName);
            //文件在服务器中保存的路径
            admin.setSavepath("/myFiles" + newName);
            //文件原名
            admin.setShowname(fName);
            AdminService as = new AdminService();

            PrintWriter out = response.getWriter();
            if(as.regAdmin(admin) == 1){
                session.setAttribute("admin", admin);
                out.print("success");
            }else out.print("fail");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //用户注册
    protected void updateAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer id = Integer.parseInt(request.getParameter("id"));
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        Admin admin = new Admin();
        //基本参数存入实体类
        admin.setId(id);
        admin.setEmail(email);
        admin.setPhone(phone);
        admin.setUpdatetime(new Timestamp(System.currentTimeMillis()));

        AdminService as = new AdminService();
        Integer count = as.updateAdmin(admin);

        PrintWriter out = response.getWriter();
        if(count == 1){
            out.print("success");
        }else out.print("fail");
        out.close();
    }

    //验证码是否通过
    protected void queryAdminByUsernameAndPwd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String pwd = request.getParameter("pwd");
        AdminService as = new AdminService();
        Admin a = as.queryAdminByUsernameAndPwd(username, pwd);
        PrintWriter out = response.getWriter();
        Map<String, Object> map = new HashMap<>();
        if(a != null){
            map.put("username", username);
            map.put("msg", "success");
            session.setAttribute("admin", a);
        }else map.put("msg", "fail");
        out.print(JSONObject.fromObject(map));
        out.close();
    }

    //用户注销
    protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("login.jsp");
    }
}
