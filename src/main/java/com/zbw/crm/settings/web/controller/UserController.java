package com.zbw.crm.settings.web.controller;

import com.zbw.crm.settings.domain.User;
import com.zbw.crm.settings.service.UserService;
import com.zbw.crm.settings.service.impl.UserServiceImpl;
import com.zbw.crm.utils.MD5Util;
import com.zbw.crm.utils.PrintJson;
import com.zbw.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //url-pattern
        String path = request.getServletPath();
        if("/settings/user/login.do".equals(path)){
            login(request,response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到登录验证");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        String act = request.getParameter("act");
        String pwd = MD5Util.getMD5(request.getParameter("pwd"));
        String ip = request.getRemoteAddr();
        try{
            User user = userService.login(act,pwd,ip);
            request.getSession().setAttribute("user",user);
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            e.printStackTrace();
            String message = e.getMessage();
            Map<String,Object> map = new HashMap();
            map.put("flag",false);
            map.put("msg",message);
            PrintJson.printJsonObj(response,map);
        }

    }

}
