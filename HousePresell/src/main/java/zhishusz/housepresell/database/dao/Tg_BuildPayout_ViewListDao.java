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
import zhishusz.housepresell.database.po.Tg_BuildPayout_View;

/*
 * Dao数据库操作：托管楼幢拨付明细统计表报表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_BuildPayout_ViewListDao extends BaseDao<Tg_BuildPayout_View> {
	@Autowired
	private SessionFactory sessionFactory;

	public String getBasicHQL() {
		return "from Tg_BuildPayout_View where 1=1"
				+ " <#if keyword??> and (companyName like :keyword or projectName like :keyword  or eCodeFromPayoutBill like :keyword )</#if>"
				+ " <#if companyName??> and companyName=:companyName</#if>"
				+ " <#if projectName??> and projectName=:projectName</#if>"
				+ " <#if ECodeFroMconstruction??> and eCodeFroMconStruction=:ECodeFroMconstruction</#if>"
				+ " <#if payoutDate??> and payoutDate >=:payoutDate</#if>"
				+ " <#if endPayoutDate??> and payoutDate <=:endPayoutDate</#if>"
				+ " order by payoutDate desc, companyName asc, projectName asc, eCodeFroMconstruction asc, eCodeFromPayoutBill asc";
	}

	/**
	 * 托管楼幢拨付明细统计表
	 * 
	 * @param comapanyId   开发企业Id
	 * @param projectId    项目Id
	 * @param keyWord      关键字
	 * @param beginTime    开始日期
	 * @param endTime      结束日期
	 * @param pageNumber   第几页
	 * @param countPerPage 每页显示条数
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getBuildPayout_View(String userId, String comapanyId, String projectId,
			String buildingId, String keyWord, String beginTime, String endTime, Integer pageNumber,
			Integer countPerPage) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call GET_BuildPayout_View(?,?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		
		try {
			// 设置参数
			sp.setString(1, userId);
			sp.setString(2, comapanyId);
			sp.setString(3, projectId);
			sp.setString(4, buildingId);
			sp.setString(5, keyWord);
			sp.setString(6, beginTime);
			sp.setString(7, endTime);
			sp.setInt(8, pageNumber);
			sp.setInt(9, countPerPage);

			// 游标类型
			sp.registerOutParameter(10, OracleTypes.CURSOR);
			sp.registerOutParameter(11, OracleTypes.INTEGER);
			sp.registerOutParameter(12, OracleTypes.INTEGER);
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			
			rs = (ResultSet) sp.getObject(10);

			List<Tg_BuildPayout_View> tg_BuildPayout_ViewList = new ArrayList<Tg_BuildPayout_View>();
			Tg_BuildPayout_View tg_BuildPayout_View = null;

			while (rs.next()) {
				tg_BuildPayout_View = new Tg_BuildPayout_View();

				// 主键
				tg_BuildPayout_View.setTableId(Long.valueOf(ObjectUtils.toString(rs.getObject(1))));				
				// 开发企业
				tg_BuildPayout_View.setCompanyName(ObjectUtils.toString(rs.getObject(2)));
				// 项目名称
				tg_BuildPayout_View.setProjectName(ObjectUtils.toString(rs.getObject(3)));
				// 楼幢
				tg_BuildPayout_View.seteCodeFroMconstruction(ObjectUtils.toString(rs.getObject(4)));				
				// 资金拨付单号
				tg_BuildPayout_View.seteCodeFromPayoutBill(ObjectUtils.toString(rs.getObject(5)));			
				// 本次申请支付金额
				tg_BuildPayout_View.setCurrentApplyPayoutAmount(Double.valueOf(ObjectUtils.toString(rs.getObject(6))));
				// 本次实际支付金额
				tg_BuildPayout_View.setCurrentPayoutAmount(Double.valueOf(ObjectUtils.toString(rs.getObject(7))));
				// 记账日期
				tg_BuildPayout_View.setPayoutDate(ObjectUtils.toString(rs.getObject(8)));
				// 银行名称
				tg_BuildPayout_View.setPayoutBank(ObjectUtils.toString(rs.getObject(9)));
				// 拨付账号
				tg_BuildPayout_View.setPayoutBankAccount(ObjectUtils.toString(rs.getObject(10)));
				// 当前形象进度
				tg_BuildPayout_View.setCurrentFigureProgress(ObjectUtils.toString(rs.getObject(11)));

				tg_BuildPayout_ViewList.add(tg_BuildPayout_View);
			}

			map.put("tg_BuildPayout_ViewList", tg_BuildPayout_ViewList);

			int totalPage = sp.getInt(11);
			map.put("totalPage", totalPage);

			int totalCount = sp.getInt(12);

			map.put("totalCount", totalCount);

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
