package com.zbw.crm.workbench.dao;

import com.zbw.crm.workbench.domain.ClueActivityRelation;
import com.zbw.crm.workbench.domain.ContactsActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    List<ClueActivityRelation> getListByClueId(String clueId);


    int deleteByClueId(String clueId);
}
