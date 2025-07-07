import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

//jdbc工具类
public class JDBCUtil {
    /**
     * 驱动名
     */
    public static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    /**
     * 连接数据库地址
     */
    public static final String URL = "jdbc:mysql://localhost:3306/electronic_mall?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=UTC";
    /**
     * 数据库用户名
     */
    public static final String USERNAME = "root";
    /**
     * 数据库密码
     */
    public static final String PASSWORD = "root";

    /**
     * 构造方法私有化
     */
    private JDBCUtil() {
    }

    /**
     * 获取数据连接
     *
     * @return
     */
    public static Connection getConnection() {
        try {
            // 加载驱动
            Class.forName(DRIVER_CLASS_NAME);
            // 2.获取连接
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 关闭相关资源
     *
     * @param con
     *            数据库链接
     * @param st
     *            查询接口
     * @param rs
     *            结果集
     */
    public static void close(Connection con, Statement st, ResultSet rs) {
        try {
            try {
                if (rs != null) {
                    rs.close();

                    rs = null;
                }
            } finally {
                try {
                    if (st != null) {
                        st.close();

                        st = null;
                    }
                } finally {
                    if (con != null) {
                        con.close();

                        con = null;
                    }
                }
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 查询指定sql的结果集记录数，返回值为int类型
     *
     * @param sql  查询sql(用计数聚合函数查询)
     *
     * @param args 可变参数
     *
     * @return 返回条数
     */
    public static int queryForCount(String sql, Object... args) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            for (int i = 0; args != null && i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                // 返回第一行第一列
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(con, ps, rs);
        }
        return 0;
    }

    /**
     * 判断实体类中的属性是否在结果集中存在(列名跟属性名是否一样)
     *
     * @param rsmd      结果集的元信息
     *
     * @param fieldName 实体类的属性
     *
     * @return
     */
    private static boolean isExists(ResultSetMetaData rsmd, String fieldName) {

        try {
            int count = rsmd.getColumnCount();
            for (int i = 0; i < count; i++) {
                // 获取每列的字段名字,字段的下标从1开始
                String columnName = rsmd.getColumnLabel(i + 1);
                if (columnName.equals(fieldName)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 查询多条数据，将查询结果集的每条记录封装成指定类型对象，返回类型为List
     * <br/>
     * 注意:表的字段数据类型必须和类的属性数据类型一致 表的列名必须和类的属性名一样
     *
     * @param sql  查询sql
     *
     * @param c    类类型
     *
     * @param args 可变参数(sql中带条件字段值即sql中的?)
     *
     * @return 返回查询结果记录集合List
     */
    public static <T> List<T> queryAll(String sql, Class<T> c, Object... args) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            for (int i = 0; args != null && i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            // 通过反射获取类的所有属性
            Field fields[] = c.getDeclaredFields();
            list = new ArrayList<T>();
            // 获取结果集rs的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // 操作结果集
            while (rs.next()) {
                // 根据类类型实例化对象
                T o = c.newInstance();
                /**
                 * 对象的属性赋值，需要调用set方法，需要获取set方法对象，需要知道set方法名字
                 */
                for (int i = 0; i < fields.length; i++) {
                    // 获取每一个属性
                    Field field = fields[i];
                    // 获取字段名字
                    String fieldName = field.getName();// id--setI d
                    // 获取字段类型
                    Class<?> fieldType = field.getType();
                    // 获取set方法名
                    String methodName = "set" + (fieldName.charAt(0) + "").toUpperCase() + fieldName.substring(1);
                    // 获取set方法对象
                    Method method = c.getMethod(methodName, fieldType);

                    /**
                     * 注意点: 1.如果查询的结果的字段名和属性名不一样会报错User(id,name,password) select id,name from t_user
                     * 2.数据库表列名跟java实体类属性名一样
                     */
                    // 判断字段是否在结果集中存在(列名跟属性名是否一样)
                    if (isExists(rsmd, fieldName)) {// fieldName=category
                        // 根据类的属性名字获取字段的值
                        Object value = rs.getObject(fieldName);
                        if (value != null) {
                            // 调用set方法，对象中的属性赋值
                            method.invoke(o, value);
                        }

                    }

                }
                // 需要把对象放到List中，然后返回list

                list.add(o);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(con, ps, rs);
        }

        return list;
    }

    /**
     * 结果最多1条记录，返回指定对象类型数据
     *
     * @param sql  指定sql
     * @param c    对象的类类型
     *
     * @param args 动态参数/条件
     * @return 指定对象类型
     */
    public static <T> T queryForObject(String sql, Class<T> c, Object... args) {
        List<T> list = queryAll(sql, c, args);// 调用了查询全部的方法
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;

    }

    /**
     * 对表增删改
     * 
     * @param sql  需要执行的sql语句
     * @param args 条件 sql语句有几个?对应的几个变量
     * @return >=1表示操作成功 <1操作失败
     */
    public static int update(String sql, Object... args) {
        Connection con = null;

        PreparedStatement ps = null;
        try {
            // 获取连接
            con = getConnection();
            // 创建statement对象
            ps = con.prepareStatement(sql);

            // 设置参数
            for (int i = 0; args != null && i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            // 执行SQL语句
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            close(con, ps, null);
        }
        return 0;

    }

}
