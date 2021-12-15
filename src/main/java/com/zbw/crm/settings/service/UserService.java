package com.zbw.crm.settings.service;

import com.zbw.crm.exception.LoginException;
import com.zbw.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String act, String pwd ,String ip) throws LoginException;

    List<User> getUserList();
}
