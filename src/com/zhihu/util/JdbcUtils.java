package com.zhihu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zhihu.model.User;

/**
 * JDBC������
 * @author zhangyx
 *
 */
public class JdbcUtils {

    // ��ʾ�������ݿ���û���sd
    private static final String USERNAME = "root";
    // �������ݿ������
    private static final String PASSWORD = "";
    // �������ݿ��������Ϣ
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    // ����������ݿ�ĵ�ַ
    private  static final String URL = "jdbc:mysql://localhost:3306/zhihu?useUnicode=true&characterEncoding=utf8";
   //�����û�SQL
    private static String SQL = "insert into `user` (username,signature,location,industry,sex,company,job,university,major,PersionProfile,followees,followers) values(?,?,?,?,?,?,?,?,?,?,?,?)";
    // �������ݿ������
    private static Connection connection;
    // ����sql����ִ�ж���
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
