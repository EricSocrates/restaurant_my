package com.test.restaurant.test.utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtils {

    public DBUtils(){}

    //不想每次都在使用连接时向方法中传Connection的对象，就先在构造方法中定义它

    private DataSource ds;

    public DBUtils(DataSource ds){
        this.ds = ds;
    }

    /*
    * 可变参数：
    * 1.放在参数列表最后
    * 2.用法同数组
    * */

    //发现方法中代码复用频繁，提取出用于填充参数的方法
    private void fillStatement(PreparedStatement pst, Object...params) throws Exception{
        //给参数赋值
        if(params != null){
            for(int i = 0; i < params.length; i++){
                //数据库对应的列参数索引从1开始，数组下标从0开始
                pst.setObject(i + 1, params[i]);
            }
        }
    }

    //模拟DBUtils-->QueryRunner中的update方法
    public Integer update(Connection conn, String sql, Object...params) throws Exception{
       //被提取到另一个方法中
        /* //S1.创建statement对象
        PreparedStatement pst = conn.prepareStatement(sql);
        //S2.给参数赋值
        if(params != null){
            for(int i = 0; i < params.length; i++){
                //数据库对应的列参数索引从1开始，数组下标从0开始
                pst.setObject(i + 1, params[i]);
            }
        }*/
        //S1.创建Statement对象
        PreparedStatement pst = conn.prepareStatement(sql);
        //S2.给参数赋值
        fillStatement(pst);
        //S3.执行操作
        //insert/delete/update均可以使用这个方法处理
        return pst.executeUpdate();
    }

    //模拟DBUtils-->QueryRunner中的query方法
    public List<Map<String, Object>> query(Connection conn, String sql, Object...params) throws Exception{
        //S1.创建Statement对象
        PreparedStatement pst = conn.prepareStatement(sql);
        //S2.给参数赋值
        fillStatement(pst);
        //S3.接收查询到的结果
        //创建对象接收Map,有几条记录就有几个Map
        List<Map<String, Object>> resultList = new ArrayList<>();
        //通过ResultSet获得行数（查到几条记录）
        ResultSet rs = pst.executeQuery();
        //获取结果集的元数据（字段名/字段类型/字段长度）
        ResultSetMetaData rsmd = rs.getMetaData();
        //获取列数
        int count = rsmd.getColumnCount();
        while(rs.next()){
            Map<String, Object> map = new HashMap<>();
            //遍历每一列(列的索引值从1开始)
            for(int i = 1; i <= count; i++){
                //获取列名
                String cName = rsmd.getColumnName(i);
                //获取列内容
                Object obj = rs.getObject(cName);
                map.put(cName, obj);
            }
            resultList.add(map);
        }
        return resultList;
    }

    //不需要传连接的update()
    //模拟DBUtils-->QueryRunner中的update方法
    public Integer update(String sql, Object...params) throws Exception{
        //被提取到另一个方法中
        /* //S1.创建statement对象
        PreparedStatement pst = conn.prepareStatement(sql);
        //S2.给参数赋值
        if(params != null){
            for(int i = 0; i < params.length; i++){
                //数据库对应的列参数索引从1开始，数组下标从0开始
                pst.setObject(i + 1, params[i]);
            }
        }*/
        //S1.创建Statement对象
        PreparedStatement pst = this.ds.getConnection().prepareStatement(sql);
        //S2.给参数赋值
        fillStatement(pst);
        //S3.执行操作
        //insert/delete/update均可以使用这个方法处理
        return pst.executeUpdate();
    }

    public <T> T query(String sql, ResultSetHandler<T> rsh, Object...params) throws Exception{
        //S1.创建Statement对象
        PreparedStatement pst = this.ds.getConnection().prepareStatement(sql);
        //S2.给参数赋值
        fillStatement(pst, params);
        //S3.接收查询到的结果
        //通过ResultSet获得行数（查到几条记录）
        ResultSet rs = pst.executeQuery();
        //直接使用ResultSetHandler的实现类处理结果集
        //比如 传入BeanListHandler，这里就返回BeanListHandler实现的handle方法
        return rsh.handle(rs);
    }
}
