package com.test.restaurant.admin.dao;


import com.test.restaurant.admin.entity.Admin;

public interface IAdminDao {
    /**
     * 根据用户名和密码查询管理员
     * */
    public Admin queryAdminByUsernameAndPwd(String name, String pwd);

    /**
     * 根据名字查询管理员
     * */
    public Admin queryAdminByUsername(String name);

    /**
     * 注册管理员
     * */
    public Integer regAdmin(Admin admin);

    /**
     * 更新管理员信息
     * */
    public Integer updateAdmin(Admin admin);
}
