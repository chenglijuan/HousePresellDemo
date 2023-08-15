package zhishusz.housepresell.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ExtraBaseDao
{
	public Connection getConn(String className,String url,String user,String password)
	{

		try
		{
			Class.forName(className);
			
			Connection conn = DriverManager.getConnection(url, user, password); // 获取连接

			return conn;
		}
		catch (Exception e)
		{
			System.out.println("连接异常" + e.getMessage());
		}

		return null;
	}

	public void closeConn(Connection conn)
	{
		try
		{
			if (conn != null)
			{
				conn.close();
			}
		}
		catch (SQLException e)
		{
			System.out.println("关闭数据库连接异常："+e.getMessage());
		}
	}
	
	
}
