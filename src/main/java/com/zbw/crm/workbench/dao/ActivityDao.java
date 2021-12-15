package com.zbw.crm.workbench.dao;

import com.zbw.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    int save(Activity activity);

    int getTotal(Map<String, Object> map);

    int getTotalByCondition(Map<String, Object> map);

    List<Activity> getActivityListByCondition(Map<String, Object> map);

    int getContByIds(String[] ids);

    int deleteByIds(String[] ids);

    Activity getActivityById(String id);

    int update(Activity activity);
}
