package com.zbw.crm.workbench.dao;

import com.zbw.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer);
}
