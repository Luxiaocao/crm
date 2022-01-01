package com.zbw.crm.workbench.dao;


import com.zbw.crm.vo.PaginationVO;
import com.zbw.crm.workbench.domain.Activity;
import com.zbw.crm.workbench.domain.Clue;
import com.zbw.crm.workbench.domain.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue clue);

    List<Clue> pageList(Map<String, Object> map);

    int getTotalByConCondition(Map<String, Object> map);

    Clue detail(String id);

    List<Activity> selectActivityBycid(String id);

    int unbund(String id);

    List<Activity> getActivitiesByName(Map<String, String> map);

    int bund(ClueActivityRelation car);

    List<Activity> getActivityByName(String name);

    Clue getClueById(String clueId);

    int deleteById(String clueId);
}
