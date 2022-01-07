package com.zbw.crm.workbench.service.impl;

import com.zbw.crm.utils.SqlSessionUtil;
import com.zbw.crm.workbench.dao.ContactsDao;
import com.zbw.crm.workbench.domain.Contacts;
import com.zbw.crm.workbench.service.ContactsService;

import java.util.List;

public class ContactsServiceImpl implements ContactsService {
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    public List<Contacts> getContactsList() {
        List<Contacts> list = contactsDao.getContactsList();
        return list;
    }
}
