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
import zhishusz.housepresell.database.po.Tg_BankLoanOutSituation_View;
import zhishusz.housepresell.util.MyDouble;

/*
 * Dao数据库操作：银行放款项目出账情况表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_BankLoanOutSituation_ViewListDao extends BaseDao<Tg_BankLoanOutSituation_View> {
	
	@Autowired
	private SessionFactory sessionFactory;

	public String getBasicHQL() {
		return "from Tg_BankLoanOutSituation_View where 1=1"
				+ " <#if keyword??> and (companyName like :keyword or projectName like :keyword or escrowAcount like :keyword)</#if>"
				+ " <#if companyName??> and companyName=:companyName</#if>"
				+ " <#if projectName??> and projectName=:projectName</#if>"
				+ " <#if loanOutDate??> and loanOutDate >= :loanOutDate</#if>"
				+ " <#if endLoanOutDate??> and loanOutDate <= :endLoanOutDate</#if>"
				+ " order by loanOutDate desc, companyName asc, projectName asc, escrowAcount asc";
	}

	/**
	 * 银行放款项目出账情况表
	 * 
	 * @param userid       用户主键
	 * @param companyId    企业主键
	 * @param projectId    项目主键
	 * @param keyWord      关键字
	 * @param beginTime    开始日期
	 * @param endTime      结束日期
	 * @param pageNumber   第几页
	 * @param countPerPage 每页显示条数
	 * @param escrowAcount 托管账户
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getBankLoanOutSituation_View(String userid, String companyId, String projectId,
			String keyWord, String beginTime, String endTime, Integer pageNumber, Integer countPerPage,String escrowAcount)
			throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call GET_BankLoanOutSituation_View(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		
		try {
			// 设置参数
			sp.setString(1, userid);//用戶
			sp.setString(2, companyId);//企业
			sp.setString(3, projectId); //项目
			sp.setString(4, keyWord);//关键字
			sp.setString(5, beginTime);//开始日期
			sp.setString(6, endTime);//结束日期
			sp.setInt(7, pageNumber);//第几页
			sp.setInt(8, countPerPage);//每页数量
			sp.setString(12, escrowAcount);//托管账户

			// 游标类型
			sp.registerOutParameter(9, OracleTypes.CURSOR);
			sp.registerOutParameter(10, OracleTypes.INTEGER);
			sp.registerOutParameter(11, OracleTypes.INTEGER);
			sp.registerOutParameter(13, OracleTypes.DOUBLE);
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(9);

			List<Tg_BankLoanOutSituation_View> tg_BankLoanOutSituation_ViewList = new ArrayList<Tg_BankLoanOutSituation_View>();
			Tg_BankLoanOutSituation_View tg_BankLoanOutSituation_View = null;

			while (rs.next()) {
				tg_BankLoanOutSituation_View = new Tg_BankLoanOutSituation_View();

				// 主键
				tg_BankLoanOutSituation_View.setTableId(Long.valueOf(ObjectUtils.toString(rs.getObject(1))));
				// 日期
				tg_BankLoanOutSituation_View.setLoanOutDate(ObjectUtils.toString(rs.getObject(2)));
				// 企业名称
				tg_BankLoanOutSituation_View.setCompanyName(ObjectUtils.toString(rs.getObject(3)));
				// 项目名称
				tg_BankLoanOutSituation_View.setProjectName(ObjectUtils.toString(rs.getObject(4)));
				// 托管账户
				tg_BankLoanOutSituation_View.setEscrowAcount(ObjectUtils.toString(rs.getObject(5)));
				// 托管账户简称
				tg_BankLoanOutSituation_View.setEscrowAcountShortName(ObjectUtils.toString(rs.getObject(6)));
				// 出账金额
				tg_BankLoanOutSituation_View.setLoanAmountOut(Double.valueOf(ObjectUtils.toString(rs.getObject(7))));

				tg_BankLoanOutSituation_ViewList.add(tg_BankLoanOutSituation_View);
			}

			map.put("tg_BankLoanOutSituation_ViewList", tg_BankLoanOutSituation_ViewList);

			int totalPage = sp.getInt(10);
			map.put("totalPage", totalPage);

			int totalCount = sp.getInt(11);

			map.put("totalCount", totalCount);
			
			Double totalAmount = sp.getDouble(13);
			
			map.put("totalAmount", MyDouble.getInstance().getShort(totalAmount/10000, 2));
			
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
