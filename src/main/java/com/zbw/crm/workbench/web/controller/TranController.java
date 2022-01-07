package com.zbw.crm.workbench.web.controller;

import com.zbw.crm.settings.domain.User;
import com.zbw.crm.settings.service.UserService;
import com.zbw.crm.settings.service.impl.UserServiceImpl;
import com.zbw.crm.utils.DateTimeUtil;
import com.zbw.crm.utils.PrintJson;
import com.zbw.crm.utils.ServiceFactory;
import com.zbw.crm.utils.UUIDUtil;
import com.zbw.crm.vo.PaginationVO;
import com.zbw.crm.workbench.domain.*;
import com.zbw.crm.workbench.service.ActivityService;
import com.zbw.crm.workbench.service.ContactsService;
import com.zbw.crm.workbench.service.CustomerService;
import com.zbw.crm.workbench.service.TranService;
import com.zbw.crm.workbench.service.impl.ActivityServiceImpl;
import com.zbw.crm.workbench.service.impl.ContactsServiceImpl;
import com.zbw.crm.workbench.service.impl.CustomerServiceImpl;
import com.zbw.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Service;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TranController extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到创建交易控制器");
        //url-pattern
        String path = request.getServletPath();
        if("/workbench/transaction/add.do".equals(path)){
            add(request,response);
        }else if("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request,response);
        }else if("/workbench/transaction/save.do".equals(path)){
            save(request,response);
        }else if("/workbench/transaction/pageList.do".equals(path)){
            pageList(request,response);
        }else if("/workbench/transaction/detail.do".equals(path)){
            detail(request,response);
        }else if("/workbench/transaction/showHistoryList.do".equals(path)){
            showHistoryList(request,response);
        }else if("/workbench/transaction/changeStage.do".equals(path)){
            changeStage(request,response);
        }else if("/workbench/transaction/getCharts.do".equals(path)){
            getCharts(request,response);
        }

    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询交易图标数据的操作");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> map = ts.getCharts();
        PrintJson.printJsonObj(response,map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到改变历史交易阶段的操作");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String exceptedDate = request.getParameter("exceptedDate");
        Tran tran = new Tran();
        tran.setId(id);
        tran.setStage(stage);
        tran.setMoney(money);
        tran.setExpectedDate(exceptedDate);
        User u = (User)request.getSession().getAttribute("user");
        tran.setEditBy(u.getName());
        tran.setEditTime(DateTimeUtil.getSysTime());
        Map<String,String> pMap = (Map<String,String>)this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(tran.getStage());
        tran.setPossibility(possibility);
        Map<String,Object>  map = ts.changeStage(tran);
        PrintJson.printJsonObj(response,map);
    }

    private void showHistoryList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询历史交易列表的操作");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        String tranId = request.getParameter("tranId");
        List<TranHistory> list = ts.getTranHistoryList(tranId);
        ServletContext applicatoin = this.getServletContext();
        Map<String,String> pMap = (Map<String,String>)applicatoin.getAttribute("pMap");
        for(TranHistory th : list){
            String possibility = pMap.get(th.getStage());
            th.setPossibility(possibility);
        }
        PrintJson.printJsonObj(response,list);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到查询交易详细信息的操作");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        String id = request.getParameter("id");
        Tran tran = ts.detail(id);
        String stage = tran.getStage();
        //ServletContext application = this.getServletContext();
        //ServletContext application = this.getServletConfig().getServletContext();
        ServletContext application = request.getServletContext();
        Map<String,String> map = (HashMap<String,String>)application.getAttribute("pMap");
        String possibility = map.get(stage);
        tran.setPossibility(possibility);
        request.setAttribute("tran",tran);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询交易列表的操作");
        String pageSize = request.getParameter("pageSize");
        String pageNo = request.getParameter("pageNo");
        int skipCount = (Integer.parseInt(pageNo) - 1) * Integer.parseInt(pageSize);
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String customerId = request.getParameter("customerId");
        String contactsId = request.getParameter("contactsId");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        HashMap<String, Object> map = new HashMap<>();
        map.put("skipCount",skipCount);
        map.put("pageSize",Integer.parseInt(pageSize));
        map.put("owner",owner);
        map.put("name",name);
        map.put("customerId",customerId);
        map.put("contactsId",contactsId);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        PaginationVO<Tran> paginationVO = ts.pageList(map);
        PrintJson.printJsonObj(response,paginationVO);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
            System.out.println("进入到添加交易的操作");
            String id = UUIDUtil.getUUID();
            String owner = request.getParameter("owner");
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String customerName = request.getParameter("customerName");
            String stage = request.getParameter("stage");
            String type = request.getParameter("type");
            String source = request.getParameter("source");
            String activityId = request.getParameter("activityId");
            String contactsId = request.getParameter("contactsId");
            User u = (User)request.getSession().getAttribute("user");
            String createBy = u.getName();
            String createTime = DateTimeUtil.getSysTime();
            String description = request.getParameter("description");
            String contactSummary = request.getParameter("contactSummary");
            String nextContactTime = request.getParameter("nextContactTime");
            Tran t = new Tran();
            t.setId(id);
            t.setOwner(owner);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setType(type);
            t.setSource(source);
            t.setActivityId(activityId);
            t.setContactsId(contactsId);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);
            t.setDescription(description);
            t.setContactSummary(contactSummary);
            t.setNextContactTime(nextContactTime);

            TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
            boolean flag = ts.save(t,customerName);
            if (flag) response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("模糊查询取得客户名称列表");
        String name = request.getParameter("name");
        CustomerService cs = (CustomerService)ServiceFactory.getService(new CustomerServiceImpl());
        List<Customer> list = cs.getCustomerName(name);
        PrintJson.printJsonObj(response,list);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        request.setAttribute("uList",uList);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityList();
        request.setAttribute("aList",aList);
        ContactsService cs =(ContactsService)ServiceFactory.getService(new ContactsServiceImpl());
        List<Contacts> cList = cs.getContactsList();
        request.setAttribute("cList",cList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }


}
