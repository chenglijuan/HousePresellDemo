package zhishusz.housepresell.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 第三方数据源
 * @ClassName:  JdbcConnectionUtil   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月25日 上午9:17:59   
 * @version V1.0 
 *
 */
public class JDBCConnectionUtil
{
	public Connection getConn()
	{

		try
		{
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String localPath = directoryUtil.getProjectRoot()+"WEB-INF/classes/ysjdbc.properties";// 项目路径
			// D:/Workspaces/MyEclipse%202017%20CI/.metadata/.me_tcat85/webapps/HousePresell/
			if (localPath.contains("%20"))
			{
				localPath = localPath.replace("%20", " "); 
			}
			
//			String workspace = this.getClass().getResource("/").getFile();
//			//拼接文件路径
//			String filepath= workspace + "ysjdbc.properties";
			//创建对象
			Properties pro = new Properties();
//			/housepresell/src/main/resources/ysjdbc.properties
			
			InputStream in = new BufferedInputStream(new FileInputStream(localPath));
			pro.load(in);
			
			String driver = pro.getProperty("driver");
			String url = pro.getProperty("url");
			String username = pro.getProperty("username");
			String password = pro.getProperty("password");
			
			//加载驱动
			Class.forName(driver);
			
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//			String url = "jdbc:oracle:thin:@192.168.1.8:1521:orcl"; 
//			String user = "PSMS";
//			String password = "PSMS";
			Connection conn = DriverManager.getConnection(url, username, password);

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
