package com.test.restaurant.tables.service;

import com.test.restaurant.tables.dao.ITablesDao;
import com.test.restaurant.tables.dao.TablesDaoImpl;
import com.test.restaurant.tables.entity.Tables;

import java.util.List;

public class TablesService {

    ITablesDao dao = new TablesDaoImpl();

    public List<Tables> queryTablesSeparated(Integer pageNumber, Integer pageSize, String no) {
        return dao.queryTablesSeparated(pageNumber, pageSize, no);
    }

    public int countTables(String no) {
        return dao.countTables(no);
    }

    public Integer addTables(Tables t) {
        return dao.addTables(t);
    }

    public Tables queryTablesByNo(Integer no) {
        return dao.queryTablesByNo(no);
    }
}
