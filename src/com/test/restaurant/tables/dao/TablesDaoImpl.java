package com.test.restaurant.tables.dao;

import com.test.restaurant.common.factory.ConnectionFactory;
import com.test.restaurant.tables.entity.Tables;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.List;

public class TablesDaoImpl implements ITablesDao {

    QueryRunner qr = new QueryRunner(ConnectionFactory.getDataSource());
    String sql;

    /**
     * 分页地、可能带有过滤条件地查找餐桌数据
     *
     * @param pageSize
     * @param pageNumber
     * @param no
     */
    @Override
    public List<Tables> queryTablesSeparated(Integer pageNumber, Integer pageSize, String no) {
        try {
            sql = "SELECT * FROM tables ";
            if (no != null) sql += "WHERE no = '" + no + "'";
            sql += " limit ?, ?";
            return qr.query(sql, new BeanListHandler<Tables>(Tables.class), pageNumber, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询餐桌总共记录数
     */
    @Override
    public Integer countTables(String no) {
        try {
            sql = "SELECT COUNT(*) FROM tables ";
            if (no != null) sql += "WHERE no = '" + no + "'";
            return qr.query(sql, new ScalarHandler<Long>()).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增餐桌
     *
     * @param t
     */
    @Override
    public Integer addTables(Tables t) {
        try {
            sql = "insert into tables values (null, ?, ?, 0, ?, ?)";
            return qr.update(sql, t.getNo(), t.getNum(), t.getCreatetime(), t.getUpdatetime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询餐桌号是否重名
     *
     * @param no
     */
    @Override
    public Tables queryTablesByNo(Integer no) {
        try {
            sql = "select * from tables where no = ?";
            return qr.query(sql, new BeanHandler<Tables>(Tables.class), no);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
