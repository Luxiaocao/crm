package com.zbw.crm.workbench.web.controller;

import com.zbw.crm.settings.domain.User;
import com.zbw.crm.settings.service.UserService;
import com.zbw.crm.settings.service.impl.UserServiceImpl;
import com.zbw.crm.utils.DateTimeUtil;
import com.zbw.crm.utils.PrintJson;
import com.zbw.crm.utils.ServiceFactory;
import com.zbw.crm.utils.UUIDUtil;
import com.zbw.crm.vo.PaginationVO;
import com.zbw.crm.workbench.domain.Activity;
import com.zbw.crm.workbench.domain.Clue;
import com.zbw.crm.workbench.domain.Tran;
import com.zbw.crm.workbench.service.ClueService;
import com.zbw.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到创建线索控制器");
        //url-pattern
        String path = request.getServletPath();
        if("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/clue/save.do".equals(path)){
            save(request,response);
        }else if("/workbench/clue/pageList.do".equals(path)){
            pageList(request,response);
        }else if("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        }else if("/workbench/clue/showActivityList.do".equals(path)){
            showActivityList(request,response);
        }else if("/workbench/clue/unbund.do".equals(path)){
            unbund(request,response);
        }else if("/workbench/clue/getActivitiesByName.do".equals(path)){
            getActivitiesByName(request,response);
        }else if("/workbench/clue/bund.do".equals(path)){
            bund(request,response);
        }else if("/workbench/clue/getActivityByName.do".equals(path)){
            getActivityByName(request,response);
        }else if("/workbench/clue/convert.do".equals(path)){
            convert(request,response);
        }

    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入到线索转换的操作");
        String clueId = request.getParameter("clueId");
        String flag = request.getParameter("flag");
        User u = (User)request.getSession().getAttribute("user");
        String createBy = u.getName();
        Tran tran = null;
        if("transaction".equals(flag)){
            tran = new Tran();
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            tran.setId(UUIDUtil.getUUID());
            tran.setMoney(money);
            tran.setName(name);
            tran.setExpectedDate(expectedDate);
            tran.setStage(stage);
            tran.setActivityId(activityId);
            tran.setCreateBy(createBy);
            tran.setCreateTime(DateTimeUtil.getSysTime());
        }
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag1 = cs.convert(clueId,tran,createBy);
        if (flag1) {
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }
    }

    private void getActivityByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询线索可以关联的市场活动列表");
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        String name = request.getParameter("name");
        List<Activity> list = cs.getActivityByName(name);
        PrintJson.printJsonObj(response,list);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到关联市场活动的操作");
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        String clueId = request.getParameter("clueId");
        String[] activityIds = request.getParameterValues("activityId");
        boolean flag = cs.bund(clueId,activityIds);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivitiesByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询线索可以关联的市场活动列表");
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        List<Activity> list = cs.getActivitiesByName(id,name);
        PrintJson.printJsonObj(response,list);
    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到解除市场活动关联的操作");
        String id = request.getParameter("id");
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.unbund(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void showActivityList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询线索关联的市场活动操作");
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        String id = request.getParameter("id");
        List<Activity> list = cs.selectActivityBycid(id);
        PrintJson.printJsonObj(response,list);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到查询线索详情的操作");
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        String id = request.getParameter("id");
        Clue clue = cs.detail(id);
        request.setAttribute("c",clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询线索列表的操作");
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        Map<String,Object> map = new HashMap<>();
        String pageSizStr = request.getParameter("pageSize");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.parseInt(pageNoStr);
        int pageSize = Integer.parseInt(pageSizStr);
        int skipCount = (pageNo - 1) * pageSize;
        String fullname = request.getParameter("fullname");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("source",source);
        map.put("owner",owner);
        PaginationVO<Clue> paginationVo = cs.pageList(map);
        PrintJson.printJsonObj(response,paginationVo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到保存线索的操作");
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        User u = (User)request.getSession().getAttribute("user");
        String createBy = u.getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");
        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);
        boolean flag = cs.save(clue);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService us = (UserService)ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = us.getUserList();
        PrintJson.printJsonObj(response,userList);
    }
}
