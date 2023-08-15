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
import zhishusz.housepresell.database.po.Tg_LoanProjectCountM_View;

/*
 * Dao数据库操作：托管项目统计表（财务部）
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_LoanProjectCountM_ViewListDao extends BaseDao<Tg_LoanProjectCountM_View> {
	@Autowired
	private SessionFactory sessionFactory;

	public String getBasicHQL() {
		return "from Tg_LoanProjectCountM_View where 1=1"
				+ "<#if keyword ??> and (cityRegion like :keyword or  companyName like :keyword or projectName like :keyword ) </#if> "
				+ " <#if cityRegion??> and cityRegion=:cityRegion</#if>"
				+ " <#if companyName??> and companyName=:companyName</#if>"
				+ " <#if projectName??> and projectName=:projectName</#if>"
				+ " <#if ECodeOfAgreement??> and (eCodeOfAgreement like:ECodeOfAgreement)</#if>"
				+ " <#if contractSigningDate??> and contractSigningDate >= :contractSigningDate</#if>"
				+ " <#if endContractSigningDate??> and contractSigningDate <= :endContractSigningDate</#if>"
				+ " <#if preSaleCardDate??> and preSaleCardDate >= :preSaleCardDate</#if>"
				+ " <#if endPreSaleCardDate??> and preSaleCardDate <= :endPreSaleCardDate</#if>"
				+ " order by contractSigningDate desc,preSaleCardDate desc,cityRegion asc,companyName asc, projectName asc";
	}

	/**
	 * 托管项目统计表（财务部）
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
	public Map<String, Object> getLoanProjectCountM_View(String userId, String cityRegionId, String comapanyId,
			String projectId, String buildingId,String eCodeOfAgreement, String keyWord, String contractSigningDate,
			String endContractSigningDate, String preSaleCardDate, String endPreSaleCardDate, Integer pageNumber,
			Integer countPerPage) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call GET_LoanProjectCountM_View(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		
		try {
			// 设置参数
			sp.setString(1, userId);
			sp.setString(2, cityRegionId);
			sp.setString(3, comapanyId);
			sp.setString(4, projectId);
			sp.setString(5, buildingId);
			sp.setString(6, eCodeOfAgreement);
			sp.setString(7, keyWord);
			sp.setString(8, contractSigningDate);
			sp.setString(9, endContractSigningDate);
			sp.setString(10, preSaleCardDate);
			sp.setString(11, endPreSaleCardDate);
			sp.setInt(12, pageNumber);
			sp.setInt(13, countPerPage);

			// 游标类型
			sp.registerOutParameter(14, OracleTypes.CURSOR);
			sp.registerOutParameter(15, OracleTypes.INTEGER);
			sp.registerOutParameter(16, OracleTypes.INTEGER);
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(14);

			List<Tg_LoanProjectCountM_View> tg_LoanProjectCountM_ViewList = new ArrayList<Tg_LoanProjectCountM_View>();
			Tg_LoanProjectCountM_View tg_LoanProjectCountM_View = null;

			while (rs.next()) {
				tg_LoanProjectCountM_View = new Tg_LoanProjectCountM_View();

				// 主键
				tg_LoanProjectCountM_View.setTableId(Long.valueOf(ObjectUtils.toString(rs.getObject(1))));
				// 区域
				tg_LoanProjectCountM_View.setCityRegion(ObjectUtils.toString(rs.getObject(2)));
				// 开发企业
				tg_LoanProjectCountM_View.setCompanyName(ObjectUtils.toString(rs.getObject(3)));
				// 项目名称
				tg_LoanProjectCountM_View.setProjectName(ObjectUtils.toString(rs.getObject(4)));
				// 地上楼层数（总）
				tg_LoanProjectCountM_View.setUpTotalFloorNumber(Integer.valueOf(ObjectUtils.toString(rs.getObject(5))));
				// 托管合作协议签订日期
				tg_LoanProjectCountM_View.setContractSigningDate(ObjectUtils.toString(rs.getObject(6)));
				// 预售证日期
				tg_LoanProjectCountM_View.setPreSaleCardDate(ObjectUtils.toString(rs.getObject(7)));
				// 预售证编号
				tg_LoanProjectCountM_View.setPreSalePermits(ObjectUtils.toString(rs.getObject(8)));
				// 协议编号
				tg_LoanProjectCountM_View.seteCodeOfAgreement(ObjectUtils.toString(rs.getObject(9)));
				// 备注
				tg_LoanProjectCountM_View.setRemark(ObjectUtils.toString(rs.getObject(10)));

				tg_LoanProjectCountM_ViewList.add(tg_LoanProjectCountM_View);
			}

			map.put("tg_LoanProjectCountM_ViewList", tg_LoanProjectCountM_ViewList);

			int totalPage = sp.getInt(15);
			map.put("totalPage", totalPage);

			int totalCount = sp.getInt(16);

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
