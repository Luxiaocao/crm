package com.zbw.crm.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SysInitLstener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("上下文对象创建了");
    }


}
