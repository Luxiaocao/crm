package com.zbw.crm.web.listener;

import com.zbw.crm.settings.service.DicService;
import com.zbw.crm.settings.service.impl.DicServiceImpl;
import com.zbw.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitLstener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("服务器开始缓存数据字典");
        ServletContext servletContext = event.getServletContext();
        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List> map = dicService.getDicAll();
        Set<Map.Entry<String,List>> entrySet = map.entrySet();
        for(Map.Entry<String,List> entry : entrySet){
            servletContext.setAttribute(entry.getKey(),entry.getValue());
        }
        System.out.println("服务器缓存数据字典结束");
    }


}
