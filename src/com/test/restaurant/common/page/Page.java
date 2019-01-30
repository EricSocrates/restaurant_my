package com.test.restaurant.common.page;

import java.util.List;

//该类负责封装与分页有关的操作
public class Page<T> {
    //当前页码
    private int pageNumber;
    //每页显示几条记录
    private int pageSize;
    //总页数
    private int pageCount;
    //总记录数
    private int total;
    //上一页
    private int pre;
    //下一页
    private int next;
    //当前页面从总的第几条数据开始显示
    private int start;
    //当前页的数据
    private List<T> rows;

    //总页数只能通过其他参数来计算
    //不可设置，算法推导见下面的分页介绍
    //（https://blog.csdn.net/qq_30257081/article/details/86100708#3.2）
    public int getPageCount() {
        return (total + pageSize - 1) / pageSize;
    }

    //只有当当前页码大于1时，才会返回公式计算的当前页数-1
    //上一页不可以设置，只可以通过传进来的其他参数获取
    public int getPre() {
        if(pageNumber > 1) return pageNumber - 1;
        return 1;
    }

    //只有当当前页码小于总页数（end）时，才会返回公式计算的当前页数+1
    //下一页不可以设置，只可以通过传进来的其他参数获取
    public int getNext() {
        if(pageNumber < getPageCount()) return pageNumber + 1;
        return getPageCount();
    }

    //计算出当前页面从总的第几条数据开始显示
    //不可设置，只可以通过算法得出
    public int getStart() {
        return (pageNumber - 1) * pageSize;
    }



    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
