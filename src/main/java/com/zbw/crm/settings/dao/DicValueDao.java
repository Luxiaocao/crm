package com.zbw.crm.settings.dao;

import com.zbw.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getDicListByCode(String code);
}
