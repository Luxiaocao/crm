package com.zbw.crm.settings.dao;

import com.zbw.crm.settings.domain.User;

import java.util.Map;

public interface UserDao {
    User login(Map<String, String> map);
}
