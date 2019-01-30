package com.test.restaurant.common.factory;
import org.apache.commons.dbcp.BasicDataSource;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class ConnectionFactory {
    //具体实现基础的数据源
    private static BasicDataSource bds = new BasicDataSource();
    private ConnectionFactory(){}
    //在静态代码块中加载db.properties的配置资源文件
    static{
        try {
            Properties properties = new Properties();
            //获取输入流，将配置资源文件以流的形式加载进来

            //第一种找资源文件的方式
            //以ConnectionFactory.class（类文件与.java文件目录结构相同）所在目录（包的内部）为起点，"../"为转到上一级目录，
            //要找到db.properties文件，需要向上转五次
            //InputStream is = ConnectionFactory.class.getResourceAsStream("../../../../../db.properties");

            //第二种找资源文件的方式
            //通过获取classpath的路径（类加载器的路径）直接从包外目录获取文件
            InputStream is = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties");

            //加载文件内容
            properties.load(is);
            is.close();

            //设置连接池属性
            //properties对象中根据key获取加载进来的value值
            //（用法同map的get()通过key取value，但是map返回Object，properties返回String）
            bds.setDriverClassName(properties.getProperty("m.driver"));
            bds.setUrl(properties.getProperty("m.url"));
            bds.setUsername(properties.getProperty("m.user"));
            bds.setPassword(properties.getProperty("m.pwd"));

            bds.setMaxWait(Long.parseLong(properties.getProperty("m.maxWait")));
            bds.setMaxActive(Integer.parseInt(properties.getProperty("m.maxActive")));
            bds.setMaxIdle(Integer.parseInt(properties.getProperty("m.maxIdle")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //对外提供连接，仅此一条连接
    public static Connection getConnection(){
        try {
            return bds.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //对外提供数据源，从连接池中取连接，多个连接
    public static DataSource getDataSource(){
        return bds;
    }
}