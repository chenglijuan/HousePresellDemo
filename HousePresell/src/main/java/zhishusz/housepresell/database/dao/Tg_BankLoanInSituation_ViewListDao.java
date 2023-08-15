package zhishusz.housepresell.database.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import oracle.jdbc.OracleTypes;
import zhishusz.housepresell.database.po.Tg_BankLoanInSituation_View;
import zhishusz.housepresell.util.MyDouble;

/*
 * Dao数据库操作：银行放款项目入账情况表
 * Company：ZhiShuSZ
 */
@Repository
public class Tg_BankLoanInSituation_ViewListDao extends BaseDao<Tg_BankLoanInSituation_View>
{
	@Autowired
	private SessionFactory sessionFactory;

	public String getBasicHQL()
	{
		return "from Tg_BankLoanInSituation_View where 1=1"
				+ " <#if keyword??> and (companyName like :keyword or projectName like :keyword or escrowAcount like :keyword)</#if>"
				+ " <#if companyName??> and companyName=:companyName</#if>"
				+ " <#if projectName??> and projectName=:projectName</#if>"
				+ " <#if billTimeStamp??> and billTimeStamp >= :billTimeStamp</#if>"
				+ " <#if endBillTimeStamp??> and billTimeStamp <= :endBillTimeStamp</#if>"

				+ " order by billTimeStamp desc, companyName asc, projectName asc, escrowAcount asc";
	}

	/**
	 * 银行放款项目入账情况表
	 * 
	 * @param userid
	 *            用户主键
	 * @param companyId
	 *            企业主键
	 * @param projectId
	 *            项目主键
	 * @param keyWord
	 *            关键字
	 * @param beginTime
	 *            开始日期
	 * @param endTime
	 *            结束日期
	 * @param pageNumber
	 *            第几页
	 * @param countPerPage
	 *            每页显示条数
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getBankLoanInSituation_View(String userid, String companyId, String projectId,
			String keyWord, String beginTime, String endTime, Integer pageNumber, Integer countPerPage)
			throws SQLException
	{

		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();

		String sql = "{call GET_BankLoanInSituation_View(?,?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;

		try
		{
			// 设置参数
			sp.setString(1, userid);
			sp.setString(2, companyId);
			sp.setString(3, projectId);
			sp.setString(4, keyWord);
			sp.setString(5, beginTime);
			sp.setString(6, endTime);
			sp.setInt(7, pageNumber);
			sp.setInt(8, countPerPage);

			// 游标类型
			sp.registerOutParameter(9, OracleTypes.CURSOR);
			sp.registerOutParameter(10, OracleTypes.INTEGER);
			sp.registerOutParameter(11, OracleTypes.INTEGER);
			sp.registerOutParameter(12, OracleTypes.DOUBLE);
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(9);

			List<Tg_BankLoanInSituation_View> tg_BankLoanInSituation_ViewList = new ArrayList<Tg_BankLoanInSituation_View>();
			Tg_BankLoanInSituation_View tg_BankLoanInSituation_View = null;

			while (rs.next())
			{
				tg_BankLoanInSituation_View = new Tg_BankLoanInSituation_View();

				// 主键
				tg_BankLoanInSituation_View.setTableId(Long.valueOf(ObjectUtils.toString(rs.getObject(1))));
				// 日期
				tg_BankLoanInSituation_View.setBillTimeStamp(ObjectUtils.toString(rs.getObject(2)));
				// 企业名称
				tg_BankLoanInSituation_View.setCompanyName(ObjectUtils.toString(rs.getObject(3)));
				// 项目名称
				tg_BankLoanInSituation_View.setProjectName(ObjectUtils.toString(rs.getObject(4)));
				// 托管账户
				tg_BankLoanInSituation_View.setEscrowAcount(ObjectUtils.toString(rs.getObject(5)));
				// 托管账户简称
				tg_BankLoanInSituation_View.setEscrowAcountShortName(ObjectUtils.toString(rs.getObject(6)));
				// 存单存入
				tg_BankLoanInSituation_View.setLoanAmountIn(Double.valueOf(ObjectUtils.toString(rs.getObject(7))));

				tg_BankLoanInSituation_ViewList.add(tg_BankLoanInSituation_View);
			}

			map.put("tg_BankLoanInSituation_ViewList", tg_BankLoanInSituation_ViewList);

			int totalPage = sp.getInt(10);
			map.put("totalPage", totalPage);

			int totalCount = sp.getInt(11);

			map.put("totalCount", totalCount);

			Double totalAmount = sp.getDouble(12);
			
			map.put("totalAmount", MyDouble.getInstance().getShort(totalAmount/10000, 2));

		}
		catch (SQLException e)
		{
			System.out.println("查询异常");
			e.printStackTrace();
		}
		finally
		{
			if (rs != null)
			{
				rs.close();
			}
			if (sp != null)
			{
				sp.close();
			}
			if (connection != null)
			{
				connection.close();
			}
			/*
			 * if(sessionFactory!=null) {
			 * sessionFactory.getCurrentSession().flush();
			 * }
			 */
		}
		return map;
	}
	
	
	/**
	 * 银行放款项目入账情况表
	 * 
	 * @param userid
	 *            用户主键
	 * @param companyId
	 *            企业主键
	 * @param projectId
	 *            项目主键
	 * @param keyWord
	 *            关键字
	 * @param beginTime
	 *            开始日期
	 * @param endTime
	 *            结束日期
	 * @param pageNumber
	 *            第几页
	 * @param countPerPage
	 *            每页显示条数
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getBankLoanInSituation_View(String userid, String companyId, String projectId,
			String keyWord, String beginTime, String endTime, Integer pageNumber, Integer countPerPage ,String escrowAcount)
			throws SQLException
	{

		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();

		String sql = "{call GET_BankLoanInSituation_View(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;

		try
		{
			// 设置参数
			sp.setString(1, userid);
			sp.setString(2, companyId);
			sp.setString(3, projectId);
			sp.setString(4, keyWord);
			sp.setString(5, beginTime);
			sp.setString(6, endTime);
			sp.setString(7, escrowAcount);
			sp.setInt(8, pageNumber);
			sp.setInt(9, countPerPage);

			// 游标类型
			sp.registerOutParameter(10, OracleTypes.CURSOR);
			sp.registerOutParameter(11, OracleTypes.INTEGER);
			sp.registerOutParameter(12, OracleTypes.INTEGER);
			sp.registerOutParameter(13, OracleTypes.DOUBLE);
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(10);

			List<Tg_BankLoanInSituation_View> tg_BankLoanInSituation_ViewList = new ArrayList<Tg_BankLoanInSituation_View>();
			Tg_BankLoanInSituation_View tg_BankLoanInSituation_View = null;

			while (rs.next())
			{
				tg_BankLoanInSituation_View = new Tg_BankLoanInSituation_View();

				// 主键
				tg_BankLoanInSituation_View.setTableId(Long.valueOf(ObjectUtils.toString(rs.getObject(1))));
				// 日期
				tg_BankLoanInSituation_View.setBillTimeStamp(ObjectUtils.toString(rs.getObject(2)));
				// 企业名称
				tg_BankLoanInSituation_View.setCompanyName(ObjectUtils.toString(rs.getObject(3)));
				// 项目名称
				tg_BankLoanInSituation_View.setProjectName(ObjectUtils.toString(rs.getObject(4)));
				// 托管账户
				tg_BankLoanInSituation_View.setEscrowAcount(ObjectUtils.toString(rs.getObject(5)));
				// 托管账户简称
				tg_BankLoanInSituation_View.setEscrowAcountShortName(ObjectUtils.toString(rs.getObject(6)));
				// 存单存入
				tg_BankLoanInSituation_View.setLoanAmountIn(Double.valueOf(ObjectUtils.toString(rs.getObject(7))));

				tg_BankLoanInSituation_ViewList.add(tg_BankLoanInSituation_View);
			}

			map.put("tg_BankLoanInSituation_ViewList", tg_BankLoanInSituation_ViewList);

			int totalPage = sp.getInt(11);
			map.put("totalPage", totalPage);

			int totalCount = sp.getInt(12);

			map.put("totalCount", totalCount);

			Double totalAmount = sp.getDouble(13);
			
			map.put("totalAmount", MyDouble.getInstance().getShort(totalAmount/10000, 2));

		}
		catch (SQLException e)
		{
			System.out.println("查询异常");
			e.printStackTrace();
		}
		finally
		{
			if (rs != null)
			{
				rs.close();
			}
			if (sp != null)
			{
				sp.close();
			}
			if (connection != null)
			{
				connection.close();
			}
			/*
			 * if(sessionFactory!=null) {
			 * sessionFactory.getCurrentSession().flush();
			 * }
			 */
		}
		return map;
	}
}
