package com.test.restaurant.admin.dao;

import com.test.restaurant.admin.entity.Admin;
import com.test.restaurant.common.factory.ConnectionFactory;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.sql.DataSource;

public class AdminDaoImpl implements IAdminDao {

    QueryRunner qr = new QueryRunner(ConnectionFactory.getDataSource());
    String sql;
    /**
     * 根据用户名和密码查询管理员
     *
     * @param username
     * @param pwd
     */
    @Override
    public Admin queryAdminByUsernameAndPwd(String username, String pwd) {
        try {
            sql = "SELECT * FROM admin WHERE username = ? AND pwd = MD5(?)";
            return qr.query(sql, new BeanHandler<Admin>(Admin.class), username, pwd);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 注册管理员
     *
     * @param admin
     */
    @Override
    public Integer regAdmin(Admin admin) {
        try {
            sql = "INSERT INTO admin VALUES(null, ?, MD5(?), ?, ?, ?, ?, ?, ?)";
            return qr.update(sql, admin.getUsername(), admin.getPwd(), admin.getEmail(), admin.getPhone(), admin.getSavepath(), admin.getShowname(), admin.getCreatetime(), admin.getUpdatetime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据名字查询管理员
     *
     * @param username
     */
    @Override
    public Admin queryAdminByUsername(String username) {
        try {
            sql = "SELECT * FROM admin WHERE username = ?";
            return qr.query(sql, new BeanHandler<Admin>(Admin.class), username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新管理员信息
     *
     * @param admin
     */
    @Override
    public Integer updateAdmin(Admin admin) {
        try {
            sql = "UPDATE admin SET email = ?, phone = ?, updatetime = ? WHERE id = ?";
            return qr.update(sql, admin.getEmail(), admin.getPhone(), admin.getUpdatetime(), admin.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

