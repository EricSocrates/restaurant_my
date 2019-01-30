package com.test.restaurant.tables.dao;

import com.test.restaurant.tables.entity.Tables;

import java.util.List;

public interface ITablesDao {

    /**
     * 分页地、可能带有过滤条件地查找餐桌数据
     * */
    public List<Tables> queryTablesSeparated(Integer pageSize, Integer pageNumber, String no);

    /**
     * 查询餐桌总共记录数
     * */
    public Integer countTables(String no);

    /**
     * 新增餐桌
     * */
    public Integer addTables(Tables t);

    /**
     * 查询餐桌号是否重名
     * */
    public Tables queryTablesByNo(Integer no);
}
