package com.zbw.crm.workbench.service.impl;

import com.zbw.crm.utils.DateTimeUtil;
import com.zbw.crm.utils.SqlSessionUtil;
import com.zbw.crm.utils.UUIDUtil;
import com.zbw.crm.vo.PaginationVO;
import com.zbw.crm.workbench.dao.ContactsDao;
import com.zbw.crm.workbench.dao.CustomerDao;
import com.zbw.crm.workbench.dao.TranDao;
import com.zbw.crm.workbench.dao.TranHistoryDao;
import com.zbw.crm.workbench.domain.Contacts;
import com.zbw.crm.workbench.domain.Customer;
import com.zbw.crm.workbench.domain.Tran;
import com.zbw.crm.workbench.domain.TranHistory;
import com.zbw.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);



    @Override
    public boolean save(Tran t, String customerName) {
        boolean flag = true;
        Customer c = customerDao.getCustomerByName(customerName);
        if (c == null) {
            c = new Customer();
            c.setId(UUIDUtil.getUUID());
            c.setName(customerName);
            c.setCreateBy(t.getCreateBy());
            c.setCreateTime(DateTimeUtil.getSysTime());
            c.setContactSummary(t.getContactSummary());
            c.setNextContactTime(t.getNextContactTime());
            c.setOwner(t.getOwner());
            int count1 = customerDao.save(c);
            if(count1 != 1){
                flag = false;
            }
        }
        //添加交易
        //不论是查询出来的客户,还是新建的客户，现在客户已经存在了
        t.setCustomerId(c.getId());
        int count2 = tranDao.save(t);
        if(count2 != 1){
            flag = false;
        }
        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setMoney(t.getMoney());
        th.setStage(t.getStage());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());
        int count3 = tranHistoryDao.save(th);
        if(count3 != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVO<Tran> pageList(HashMap<String, Object> map) {
        List<Tran> list = tranDao.getTranListByCondition(map);
        int total = tranDao.getTotalByCondition(map);
        PaginationVO<Tran> paginationVO = new PaginationVO<>();
        paginationVO.setTotal(total);
        paginationVO.setDataList(list);
        return paginationVO;
    }

    @Override
    public Tran detail(String id) {
        Tran tran = tranDao.getById(id);
        return tran;
    }

    @Override
    public List<TranHistory> getTranHistoryList(String tranId) {
        List<TranHistory> list = tranHistoryDao.getTranHistoryList(tranId);

        return list;
    }

    @Override
    public Map<String, Object> changeStage(Tran tran) {
        boolean flag = true;
        int count = tranDao.changeStage(tran);
        if(count != 1){
            flag = false;
        }
        TranHistory tranHistory = new TranHistory();
        tranHistory.setTranId(tran.getId());
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateBy(tran.getEditBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        int count2 = tranHistoryDao.save(tranHistory);
        if(count2 != 1){
            flag = false;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("tran",tran);
        return map;
    }

    @Override
    public Map<String, Object> getCharts() {
        //获取交易数量
        int total = tranDao.getTotal();
        //获取交易数据
        List<Map<String,Object>> dataList = tranDao.getCharts();
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);
        return map;
    }
}
