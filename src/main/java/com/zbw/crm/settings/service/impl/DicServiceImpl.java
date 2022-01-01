package com.zbw.crm.settings.service.impl;

import com.zbw.crm.settings.dao.DicTypeDao;
import com.zbw.crm.settings.dao.DicValueDao;
import com.zbw.crm.settings.domain.DicType;
import com.zbw.crm.settings.domain.DicValue;
import com.zbw.crm.settings.service.DicService;
import com.zbw.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List> getDicAll() {
        List<DicType> dtList = dicTypeDao.getDicAll();
        Map<String,List> map = new HashMap<>();
        for(DicType dicType : dtList){
            String code = dicType.getCode();
            List<DicValue> list = dicValueDao.getDicListByCode(code);
            map.put(code + "List",list);
        }
        return map;
    }
}
