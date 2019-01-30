package com.test.restaurant.common.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*下面的注解相当于web.xml中，服务器拦截所有客户端对jsp文件的请求
<filter>
    <filter-name>..
    <filter-class>..
</filter>
<filter-mapping>
    <filter-name>..
    <url-pattern>..
</filter-mapping>
*
* */
@WebFilter(filterName = "LoginFilter", urlPatterns = "*.jsp")
public class LoginFilter implements Filter {
    //当容器启动时，执行构造
    public LoginFilter() {
        System.out.println("------------------LoginFilter被构造------------------");
    }

    //当构造结束，即刻初始化
    public void init(FilterConfig config) throws ServletException {
        System.out.println("------------------LoginFilter的init方法------------------");
    }

    //当客户端发来请求时，前置于servlet执行
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("------------------LoginFilter的doFilter方法------------------");
        HttpServletRequest request = ((HttpServletRequest)req);
        HttpServletResponse response = ((HttpServletResponse)resp);
        HttpSession session = request.getSession();
        //获得请求的URL
        String url = request.getRequestURL().toString();

        //如果请求登录、注册页面，放行
        if(url.indexOf("login.jsp") >= 0 || url.indexOf("reg.jsp") >= 0){
            //过滤器中的链，一环扣一环，这里表示在此步放行，
            //如果有下一个过滤器，继续过滤，一山放出一山拦
            //如果后面没有过滤器了，那就继续执行Servlet或者返回页面
            chain.doFilter(req, resp);
            return;
        }

        //若请求其他页面，判断有对应的用户session(表示用户登录后)，放行
        if(session.getAttribute("admin") != null){
            //过滤器中的链，一环扣一环，这里表示在此步放行，
            //如果有下一个过滤器，继续过滤，一山放出一山拦
            //如果后面没有过滤器了，那就继续执行Servlet或者返回页面
            chain.doFilter(req, resp);
        }else{
            //如果没有也这个session，服务器响应一个登录页面
            response.sendRedirect("login.jsp");
        }
    }


    //当容器关闭时执行
    public void destroy() {
        System.out.println("------------------LoginFilter被销毁------------------");
    }
}
