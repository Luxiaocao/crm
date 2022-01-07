package com.zbw.crm.workbench.service;

import com.zbw.crm.vo.PaginationVO;
import com.zbw.crm.workbench.domain.Tran;
import com.zbw.crm.workbench.domain.TranHistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TranService {

    boolean save(Tran t, String customerName);

    PaginationVO<Tran> pageList(HashMap<String, Object> map);

    Tran detail(String id);

    List<TranHistory> getTranHistoryList(String tranId);

    Map<String, Object> changeStage(Tran tran);

    Map<String, Object> getCharts();
}
