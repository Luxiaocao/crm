package com.zbw.crm.settings.service.impl;

import com.zbw.crm.exception.LoginException;
import com.zbw.crm.settings.dao.UserDao;
import com.zbw.crm.settings.domain.User;
import com.zbw.crm.settings.service.UserService;
import com.zbw.crm.utils.DateTimeUtil;
import com.zbw.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String act, String pwd,String ip) throws LoginException{
        Map<String,String> map = new HashMap();
        map.put("act",act);
        map.put("pwd",pwd);
        User user = userDao.login(map);
        String currentTime = DateTimeUtil.getSysTime();
        if (user == null) {
            throw new LoginException("账号或密码错误");
        }
        if(user.getAllowIps() != null && user.getAllowIps() !=""){
            if(!user.getAllowIps().contains(ip)){
                throw new LoginException("无效的ip地址");
            }
        }
        if(user.getExpireTime().compareTo(currentTime) < 0){
            throw new LoginException("账号已失效");
        }
        if("0".equals(user.getLockState())){
            throw new LoginException("账号已锁定");
        }
        return user;
    }

    public List<User> getUserList() {
        List<User> list = userDao.getUserList();
        return list;
    }
}
