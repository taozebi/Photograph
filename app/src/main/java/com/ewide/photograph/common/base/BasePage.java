package com.ewide.photograph.common.base;

import java.io.Serializable;

/**
 * 分页基类
 * Created by Taoze on 2018/6/20.
 */

public class BasePage<T> implements Serializable{

    private int pageSize = 10;
    private int pageIndex = 1;
    private T condition;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public T getCondition() {
        return condition;
    }

    public void setCondition(T condition) {
        this.condition = condition;
    }
}
