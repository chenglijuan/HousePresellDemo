package zhishusz.housepresell.database.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import lombok.Getter;
import lombok.Setter;
import oracle.jdbc.OracleTypes;
import zhishusz.housepresell.database.po.Tg_LoanAmountStatement_View;
import zhishusz.housepresell.util.IFieldAnnotation;

/*
 * Dao数据库操作：托管现金流量表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_LoanAmountStatement_ViewListDao extends BaseDao<Tg_LoanAmountStatement_View>
{
	@Autowired
	private SessionFactory sessionFactory;

	public String getBasicHQL()
    {
    	return "from Tg_LoanAmountStatement_View where 1=1"
    			+ " <#if billTimeStamp??> and billTimeStamp >=:billTimeStamp</#if>"
    	    	+ " <#if endBillTimeStamp??> and billTimeStamp <=:endBillTimeStamp</#if>"
    			+ "  order by billTimeStamp desc";
    }
	

	/**
	 * 托管现金流量表
	 * @param keyWord      关键字
	 * @param beginTime    开始日期
	 * @param endTime      结束日期
	 * @param pageNumber   第几页
	 * @param countPerPage 每页显示条数
	 * @param querykind 查询类别  1 按日查询  2 按月查询  3 按年查询
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getLoanAmountStatement_View(String keyWord,
			String beginTime, String endTime, Integer pageNumber, Integer countPerPage, String querykind) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();

		String sql = "{call GET_LoanAmountStatement_View(?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		
		try {
			// 设置参数
			sp.setString(1, querykind);
			sp.setString(2, keyWord);
			sp.setString(3, beginTime);
			sp.setString(4, endTime);
			sp.setInt(5, pageNumber);
			sp.setInt(6, countPerPage);

			// 游标类型
			sp.registerOutParameter(7, OracleTypes.CURSOR);
			sp.registerOutParameter(8, OracleTypes.INTEGER);
			sp.registerOutParameter(9, OracleTypes.INTEGER);
			sp.registerOutParameter(10, OracleTypes.NUMBER);
			sp.registerOutParameter(11, OracleTypes.NUMBER);
			
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(7);

			List<Tg_LoanAmountStatement_View> tg_LoanAmountStatement_Viewlist = new ArrayList<Tg_LoanAmountStatement_View>();
			Tg_LoanAmountStatement_View tg_LoanAmountStatement_View = null;

			while (rs.next()) {
				tg_LoanAmountStatement_View=new Tg_LoanAmountStatement_View();				
				
				//主键
				tg_LoanAmountStatement_View.setTableId(Long.valueOf(ObjectUtils.toString(rs.getObject(1))));
				//日期 
				tg_LoanAmountStatement_View.setBillTimeStamp(ObjectUtils.toString(rs.getObject(2)));
				//上期结余
				tg_LoanAmountStatement_View.setLastAmount(Double.valueOf(ObjectUtils.toString(rs.getObject(3))));
				//托管资金入账金额
				tg_LoanAmountStatement_View.setLoanAmountIn(Double.valueOf(ObjectUtils.toString(rs.getObject(4))));
				//存单存入
				tg_LoanAmountStatement_View.setDepositReceipt(Double.valueOf(ObjectUtils.toString(rs.getObject(5))));
				//资金拨付金额
				tg_LoanAmountStatement_View.setPayoutAmount(Double.valueOf(ObjectUtils.toString(rs.getObject(6))));
				//存单到期
				tg_LoanAmountStatement_View.setDepositExpire(Double.valueOf(ObjectUtils.toString(rs.getObject(7))));
				//活期余额
				tg_LoanAmountStatement_View.setCurrentBalance(Double.valueOf(ObjectUtils.toString(rs.getObject(8))));
							
				tg_LoanAmountStatement_Viewlist.add(tg_LoanAmountStatement_View);
			}
		
			map.put("qs_ProjectEscrowAmount_ViewLists", tg_LoanAmountStatement_Viewlist);
			

			int totalPage = sp.getInt(8);
			map.put("totalPage", totalPage);

			int totalCount = sp.getInt(9);

			map.put("totalCount", totalCount);
			
			double amountIn = sp.getDouble(10);
			map.put("amountIn", amountIn);
			double amountOut = sp.getDouble(11);
			map.put("amountOut", amountOut);

			if (null != rs) {
				rs.close();
			}
			sp.close();

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
			/*if(sessionFactory!=null) {
				sessionFactory.getCurrentSession().flush();
			}*/
		}
		return map;
	}
}
