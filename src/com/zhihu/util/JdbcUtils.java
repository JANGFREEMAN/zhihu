package com.zhihu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zhihu.model.User;

/**
 * JDBC工具类
 * @author zhangyx
 *
 */
public class JdbcUtils {

    // 表示定义数据库的用户名sd
    private static final String USERNAME = "root";
    // 定义数据库的密码
    private static final String PASSWORD = "";
    // 定义数据库的驱动信息
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    // 定义访问数据库的地址
    private  static final String URL = "jdbc:mysql://localhost:3306/zhihu?useUnicode=true&characterEncoding=utf8";
   //保存用户SQL
    private static String SQL = "insert into `user` (username,signature,location,industry,sex,company,job,university,major,PersionProfile,followees,followers) values(?,?,?,?,?,?,?,?,?,?,?,?)";
    // 定义数据库的链接
    private static Connection connection;
    // 定义sql语句的执行对象
    private static PreparedStatement pstmt;
	
    static{
	  try
      {
          Class.forName(DRIVER);
          connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
      }
      catch (Exception e)
      {
    	  e.printStackTrace();
      }
    }
    
	public static void SaveUser(User user) {
		 try {
			pstmt = connection.prepareStatement(SQL);
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getSignature());
			pstmt.setString(3, user.getLocation());
			pstmt.setString(4, user.getIndustry());
			pstmt.setString(5, user.getSex());
			pstmt.setString(6, user.getCompany());
			pstmt.setString(7, user.getJob());
			pstmt.setString(8, user.getUniversity());
			pstmt.setString(9, user.getMajor());
			pstmt.setString(10, user.getPersionProfile());
			pstmt.setString(11, user.getFollow() == null ? "" : user.getFollow().toString());
			pstmt.setString(12, user.getFollower() == null ? "":user.getFollower().toString());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			
		}
	}

}
