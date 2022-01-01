package com.zbw.crm.workbench.service.impl;

import com.zbw.crm.utils.DateTimeUtil;
import com.zbw.crm.utils.SqlSessionUtil;
import com.zbw.crm.utils.UUIDUtil;
import com.zbw.crm.vo.PaginationVO;
import com.zbw.crm.workbench.dao.*;
import com.zbw.crm.workbench.domain.*;
import com.zbw.crm.workbench.service.ClueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


    @Override
    public boolean save(Clue clue) {
        boolean flag = true;
        int count = clueDao.save(clue);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVO<Clue> pageList(Map<String, Object> map) {
        List<Clue> dataList = clueDao.pageList(map);
        int total = clueDao.getTotalByConCondition(map);
        PaginationVO<Clue> cluePaginationVO = new PaginationVO<>();
        cluePaginationVO.setDataList(dataList);
        cluePaginationVO.setTotal(total);
        return cluePaginationVO;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Override
    public List<Activity> selectActivityBycid(String id) {
        List<Activity> list = clueDao.selectActivityBycid(id);
        return list;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;
        int count = clueDao.unbund(id);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivitiesByName(String id, String name) {
        Map<String,String> map = new HashMap<>();
        map.put("clueId",id);
        map.put("aname",name);
        List<Activity> list = clueDao.getActivitiesByName(map);
        return list;
    }

    @Override
    public boolean bund(String clueId, String[] activityIds) {
        boolean flag = true;
        for(String aid : activityIds){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(clueId);
            car.setActivityId(aid);
            int count = clueDao.bund(car);
            if(count != 1){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityByName(String name) {

        List<Activity> list = clueDao.getActivityByName(name);
        return list;

    }

    @Override
    public boolean convert(String clueId, Tran tran, String createBy) {
        boolean flag = true;
        String createTime = DateTimeUtil.getSysTime();
        //通过线索id获得线索对象
        Clue clue = clueDao.getClueById(clueId);
        //1、通过线索对象提取客户信息，当客户不存在时新建一个客户(通过公司名称判断)
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);
        if (customer == null) {
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setOwner(clue.getOwner());
            customer.setPhone(clue.getPhone());
            customer.setDescription(clue.getDescription());
            customer.setName(company);
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setCreateBy(createBy);
            customer.setCreateTime(clue.getCreateTime());
            //添加客户
            int count1  = customerDao.save(customer);
            if(count1 != 1){
                flag = false;
            }
        }
        //2、添加联系人
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setDescription(clue.getDescription());
        con.setAddress(clue.getAddress());
        con.setAppellation(clue.getAppellation());
        con.setContactSummary(clue.getContactSummary());
        con.setCustomerId(customer.getId());
        con.setFullname(clue.getFullname());
        con.setOwner(clue.getOwner());
        con.setJob(clue.getJob());
        con.setSource(clue.getSource());
        con.setNextContactTime(clue.getNextContactTime());
        con.setEmail(clue.getEmail());
        con.setCreateTime(createTime);
        con.setCreateBy(createBy);
        con.setMphone(clue.getMphone());
        int count2 = contactsDao.save(con);
        if(count2 != 1){
            flag = false;
        }
        //3,线索备注转换到联系人备注和客户备注
        //查询线索备注列表
        List<ClueRemark> clueRemarkList = clueRemarkDao.getByClueId(clue.getId());
        for(ClueRemark clueRemark : clueRemarkList){
            //添加联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(clueRemark.getNoteContent());
            int count3 = contactsRemarkDao.save(contactsRemark);
            if(count3 != 1){
                flag = false;
            }
            //添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(clueRemark.getNoteContent());
            int count4 = customerRemarkDao.save(customerRemark);
            if(count4 != 1){
                flag = false;
            }
        }
        //4,<线索和市场活动关系>转换到<联系人市场活动关系>
        //查询出线索关联的市场活动列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for(ClueActivityRelation c : clueActivityRelationList){
            ContactsActivityRelation car = new ContactsActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(c.getActivityId());
            car.setContactsId(con.getId());
            int count5 = contactsActivityRelationDao.save(car);
            if(count5 != 1){
                flag = false;
            }
        }
        //5,如果交易不为空,添加交易
        if (tran != null) {
            tran.setOwner(clue.getOwner());
            tran.setSource(clue.getSource());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setContactSummary(clue.getContactSummary());
            tran.setContactsId(con.getId());
            //添加交易
            int count6 = tranDao.save(tran);
            if(count6 != 1){
                flag = false;
            }
            //添加交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setTranId(tran.getId());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setStage(tran.getStage());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            int count7 = tranHistoryDao.save(tranHistory);
            if(count7 != 1){
                flag = false;
            }
        }
        //6,删除线索备注
        int count8 = clueRemarkDao.deleteByClueId(clueId);
        if(count8 != clueRemarkList.size()){
            flag = false;
        }
        //7,删除线索和市场活动的关联关系
        int count9 = clueActivityRelationDao.deleteByClueId(clueId);
        if(count9 != clueActivityRelationList.size()){
            flag = false;
        }
        //8,删除线索
        int count10 = clueDao.deleteById(clueId);
        if(count10 != 1){
            flag = false;
        }
        return flag;
    }


}
