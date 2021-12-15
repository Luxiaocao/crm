package com.zbw.crm.vo;

import java.util.List;

public class PaginationVO<E> {
    private int total;
    private List<E> dataList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<E> getDataList() {
        return dataList;
    }

    public void setDataList(List<E> dataList) {
        this.dataList = dataList;
    }
}
