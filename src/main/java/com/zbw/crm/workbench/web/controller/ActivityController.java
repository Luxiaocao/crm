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
import com.zbw.crm.workbench.domain.ActivityRemark;
import com.zbw.crm.workbench.service.ActivityService;
import com.zbw.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器");
        //url-pattern
        String path = request.getServletPath();
        if("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/activity/save.do".equals(path)){
            save(request,response);
        }else if("/workbench/activity/pageList.do".equals(path)){
            pageList(request,response);
        }else if("/workbench/activity/delete.do".equals(path)){
            delete(request,response);
        }else if("/workbench/activity/getActivityAndUserList.do".equals(path)){
            getActivityAndUserList(request,response);
        }else if("/workbench/activity/update.do".equals(path)){
            update(request,response);
        }else if("/workbench/activity/detail.do".equals(path)){
            detail(request,response);
        }else if("/workbench/activity/getRemarkListByAid.do".equals(path)){
            getRemarkListByAid(request,response);
        }else if("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(request,response);
        }else if("/workbench/activity/getRemarkById.do".equals(path)){
            getRemarkById(request,response);
        }else if("/workbench/activity/updateRemark.do".equals(path)){
            updateRemark(request,response);
        }
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        ActivityRemark remark = new ActivityRemark();
        User u = (User)request.getSession(false).getAttribute("user");
        remark.setNoteContent(noteContent);
        remark.setId(id);
        remark.setEditTime(DateTimeUtil.getSysTime());
        remark.setEditBy(u.getName());
        remark.setEditFlag("1");
        boolean flag = as.updateRemark(remark);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("a",remark);
        PrintJson.printJsonObj(response,map);
    }

    private void getRemarkById(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        ActivityRemark remark = as.getRemarkById(id);
        PrintJson.printJsonObj(response,remark);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到添加市场活动的操作");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");
        ActivityRemark remark = new ActivityRemark();
        remark.setId(UUIDUtil.getUUID());
        remark.setNoteContent(noteContent);
        remark.setCreateTime(DateTimeUtil.getSysTime());
        User u = (User)request.getSession(false).getAttribute("user");
        remark.setCreateBy(u.getName());
        remark.setActivityId(activityId);
        remark.setEditFlag("0");
        Boolean flag = as.saveRemark(remark);
        PrintJson.printJsonObj(response,remark);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到删除备注的操作");
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        String activityId = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> remarkList = as.getRemarkListByAid(activityId);
        PrintJson.printJsonObj(response,remarkList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转详情页的操作");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id = request.getParameter("id");
        String ownerName = request.getParameter("ownerName");
        Activity activity = as.getActivityById(id);
        request.setAttribute("a",activity);
        request.setAttribute("ownerName",ownerName);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到修改市场活动的操作");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String editTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession(false).getAttribute("user");
        String createBy = user.getName();
        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(user.getName());
        boolean flag = as.update(activity);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityAndUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询要修改的市场活动信息");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id = request.getParameter("id");
        Map<String,Object> map = as.getActivityAndUserList(id);
        PrintJson.printJsonObj(response,map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到删除市场活动信息操作");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String[] ids = request.getParameterValues("id");
        boolean flag = as.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询市场活动信息列表");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        int pageNo = Integer.parseInt(pageNoStr);
        int pageSize = Integer.parseInt(pageSizeStr);
        int skipCount = (pageNo - 1) * pageSize;
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        Map<String,Object> map = new HashMap<>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        PaginationVO<Activity> activityPaginationVO = as.pageList(map);
        PrintJson.printJsonObj(response,activityPaginationVO);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("添加市场活动");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession(false).getAttribute("user");
        String createBy = user.getName();
        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        boolean flag = as.save(activity);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户列表");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        PrintJson.printJsonObj(response,uList);
    }
}
