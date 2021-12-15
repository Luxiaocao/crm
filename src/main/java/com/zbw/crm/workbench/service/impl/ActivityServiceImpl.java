package com.zbw.crm.workbench.service.impl;

import com.zbw.crm.settings.dao.UserDao;
import com.zbw.crm.settings.domain.User;
import com.zbw.crm.utils.SqlSessionUtil;
import com.zbw.crm.vo.PaginationVO;
import com.zbw.crm.workbench.dao.ActivityDao;
import com.zbw.crm.workbench.dao.ActivityRemarkDao;
import com.zbw.crm.workbench.domain.Activity;
import com.zbw.crm.workbench.domain.ActivityRemark;
import com.zbw.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    public boolean save(Activity activity) {
        boolean flag = false;
        int count = activityDao.save(activity);
        if(count == 1){
            flag = true;
        }
        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String,Object> map) {
        int total = activityDao.getTotalByCondition(map);
        List<Activity> activityList = activityDao.getActivityListByCondition(map);

        PaginationVO<Activity> activityPaginationVO = new PaginationVO<Activity>();
        activityPaginationVO.setTotal(total);
        activityPaginationVO.setDataList(activityList);
        return activityPaginationVO;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = false;
        int count1 = activityRemarkDao.getCountByAids(ids);
        int count2 = activityRemarkDao.deleteByAids(ids);
        int count3 = activityDao.deleteByIds(ids);
        if (count1 == count2 && count3 == ids.length){
            flag = true;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getActivityAndUserList(String id) {
        List<User> userList = userDao.getUserList();
        Activity activity = activityDao.getActivityById(id);
        Map<String,Object> map = new HashMap<>();
        map.put("userList",userList);
        map.put("activity",activity);
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean flag = true;
        int count = activityDao.update(activity);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity getActivityById(String id) {
        Activity activity = activityDao.getActivityById(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> remarkList = activityRemarkDao.getRemarkListByAid(activityId);
        return remarkList;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.deleteRemarkById(id);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Boolean saveRemark(ActivityRemark remark) {
        boolean flag = true;
        int count = activityRemarkDao.saveRemark(remark);
        if(count != 1){
            flag = false;
        }
        return null;
    }

    @Override
    public ActivityRemark getRemarkById(String id) {
        ActivityRemark remark = activityRemarkDao.getRemarkById(id);
        return remark;
    }

    @Override
    public boolean updateRemark(ActivityRemark remark) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemark(remark);
        if(count != 1){
            flag = false;
        }
        return flag;
    }
}
