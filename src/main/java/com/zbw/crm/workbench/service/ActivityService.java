package com.zbw.crm.workbench.service;

import com.zbw.crm.vo.PaginationVO;
import com.zbw.crm.workbench.domain.Activity;
import com.zbw.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity activity);

    PaginationVO<Activity> pageList(Map<String,Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getActivityAndUserList(String id);

    boolean update(Activity activity);

    Activity getActivityById(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    Boolean saveRemark(ActivityRemark remark);

    ActivityRemark  getRemarkById(String id);

    boolean updateRemark(ActivityRemark remark);
}
