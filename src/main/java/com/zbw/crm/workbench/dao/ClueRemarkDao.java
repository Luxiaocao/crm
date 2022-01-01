package com.zbw.crm.workbench.dao;

import com.zbw.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getByClueId(String id);

    int deleteByClueId(String clueId);
}
