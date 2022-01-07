package com.zbw.crm.workbench.dao;

import com.zbw.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer);

    List<Customer> getCustomerName(String name);


}
