package com.zbw.crm.web.listener;

import com.zbw.crm.settings.service.DicService;
import com.zbw.crm.settings.service.impl.DicServiceImpl;
import com.zbw.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitLstener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("服务器开始缓存数据字典");
        ServletContext application = event.getServletContext();
        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List> map = dicService.getDicAll();
        Set<Map.Entry<String,List>> entrySet = map.entrySet();
        for(Map.Entry<String,List> entry : entrySet){
            application.setAttribute(entry.getKey(),entry.getValue());
        }
        System.out.println("服务器缓存数据字典结束");
        //---------------------各个阶段的可能性-------------------------
        ResourceBundle bundle = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = bundle.getKeys();
        Map<String, String> pMap = new HashMap<>();
        while(e.hasMoreElements()){
            String key = e.nextElement();
            String value = bundle.getString(key);
            pMap.put(key,value);
        }
        application.setAttribute("pMap",pMap);
    }


}
