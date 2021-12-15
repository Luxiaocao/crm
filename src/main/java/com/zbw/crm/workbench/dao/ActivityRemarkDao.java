package com.zbw.crm.workbench.dao;

import com.zbw.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    int deleteRemarkById(String id);

    int saveRemark(ActivityRemark remark);

    ActivityRemark getRemarkById(String id);

    int updateRemark(ActivityRemark remark);
}
