package com.test.restaurant.admin.service;

import com.test.restaurant.admin.dao.AdminDaoImpl;
import com.test.restaurant.admin.dao.IAdminDao;
import com.test.restaurant.admin.entity.Admin;

public class AdminService {

    IAdminDao dao = new AdminDaoImpl();

    public Admin queryAdminByUsername(String name) {
        return dao.queryAdminByUsername(name);
    }

    public Integer regAdmin(Admin admin) { return dao.regAdmin(admin); }

    public Admin queryAdminByUsernameAndPwd(String username, String pwd) {
        return dao.queryAdminByUsernameAndPwd(username, pwd);
    }

    public Integer updateAdmin(Admin admin) {
        return dao.updateAdmin(admin);
    }
}
