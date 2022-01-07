package com.zbw.crm.workbench.dao;

import com.zbw.crm.workbench.domain.Tran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TranDao {
    int save(Tran tran);

    List<Tran> getTranListByCondition(HashMap<String, Object> map);

    int getTotalByCondition(HashMap<String, Object> map);

    Tran getById(String id);

    int changeStage(Tran tran);

    int getTotal();

    List<Map<String, Object>> getCharts();
}
