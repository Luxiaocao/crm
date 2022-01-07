package com.zbw.crm.workbench.service.impl;

import com.zbw.crm.utils.ServiceFactory;
import com.zbw.crm.utils.SqlSessionUtil;
import com.zbw.crm.workbench.dao.CustomerDao;
import com.zbw.crm.workbench.domain.Customer;
import com.zbw.crm.workbench.service.CustomerService;

import javax.xml.ws.Service;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<Customer> getCustomerName(String name) {
        List<Customer> list = customerDao.getCustomerName(name);
        return list;
    }
}
