package com.zbw.crm.workbench.service;

import com.zbw.crm.vo.PaginationVO;
import com.zbw.crm.workbench.domain.Activity;
import com.zbw.crm.workbench.domain.Clue;
import com.zbw.crm.workbench.domain.Tran;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    PaginationVO<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);

    List<Activity> selectActivityBycid(String id);

    boolean unbund(String id);

    List<Activity> getActivitiesByName(String id, String name);

    boolean bund(String clueId, String[] activityIds);

    List<Activity> getActivityByName(String name);


    boolean convert(String clueId, Tran tran, String createBy);
}
