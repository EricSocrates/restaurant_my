package com.test.restaurant.test.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BeanListHandler<T> implements ResultSetHandler<List<T>> {

    private Class<T> cls;

    public BeanListHandler(Class<T> cls){
        this.cls = cls;
    }
    //把任意的ResultSet转换为任意类型
    @Override
    public List<T> handle(ResultSet rs) {

        try {
            List<T> data = new ArrayList();
            while(rs.next()){
                //通过反射的方式构造对象
                //newInstance()构造对象会new这个类的空参构造方法
                //很多框架底层也使用了反射
                //所以实体类的空参构造方法务必要保证可用
                T obj = cls.newInstance();
                //数据库中字段->属性
                //获取这个类中所有属性对象
                Field[] fs = cls.getDeclaredFields();
                //f为实体类中的属性对象
                for(Field f : fs) {
                    //属性名字
                    String attrName = f.getName();
                    //获得属性所属类的名字
                    Class filedType = cls.getDeclaredField(attrName).getType();
                    //拼出这个属性的setter方法名
                    String setter0 = "set" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1);
                    //反射获取setter方法，setter代表setter方法的名字
                    //最后一个参数指的是setter方法所属类的类型，因为每个setter方法的返回值类型就是其类
                    Method setter = cls.getDeclaredMethod(setter0, filedType);
                    //给属性赋值
                    //obj的setter这个方法被执行
                    setter.invoke(obj, rs.getObject(attrName));
                }
                data.add(obj);
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
