package zhishusz.housepresell.database.dao.extra;

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

import cn.hutool.core.util.StrUtil;
import oracle.jdbc.OracleTypes;
import zhishusz.housepresell.database.dao.BaseDao;
import zhishusz.housepresell.database.po.extra.Qs_ProjectEscrowAmount_View;

/*
 * Dao数据库操作：项目托管资金收支情况表
 * Company：ZhiShuSZ
 * 
 * keyword：关键字
 * theNameOfCompany：开发企业
 * theNameOfProject：项目
 * 
 * recordDateStart：入账日期起始时间
 * recordDateEnd：入账日期结束时间
 */
@Repository
public class Qs_ProjectEscrowAmount_ViewDao extends BaseDao<Qs_ProjectEscrowAmount_View> {
	@Autowired
	private SessionFactory sessionFactory;

	public String getBasicHQL() {
		return "from Qs_ProjectEscrowAmount_View where 1=1"
				+ " <#if theNameOfCompany??> and theNameOfCompany=:theNameOfCompany</#if>"
				+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
				+ " <#if recordDateStart??> and recordDate >= :recordDateStart</#if>"
				+ " <#if recordDateEnd??> and recordDate < :recordDateEnd</#if>"
				+ " <#if keyword??> and ( eCodeFromConstruction like :keyword)</#if>"
				+ " order by theNameOfCompany,theNameOfProject";
	}

	/**
	 * 托管资金收支情况表
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
	public Map<String, Object> getProjectEscrowAmount_View(String userId, String comapanyId, String projectId,
			String keyWord, String beginTime, String endTime, Integer pageNumber, Integer countPerPage,String cityRegionId)
			throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call GET_PROJECTESCROWAMOUNT(?,?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		try {
			// 设置参数
			sp.setString(1, userId);
			sp.setString(2, comapanyId);
			sp.setString(3, projectId);
			sp.setString(4, keyWord);
			sp.setString(5, beginTime);
			sp.setString(6, endTime);
			sp.setString(7, cityRegionId);
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

			List<Qs_ProjectEscrowAmount_View> qs_ProjectEscrowAmount_ViewList = new ArrayList<Qs_ProjectEscrowAmount_View>();
			Qs_ProjectEscrowAmount_View qs_ProjectEscrowAmount_View = null;

			while (rs.next()) {
				qs_ProjectEscrowAmount_View = new Qs_ProjectEscrowAmount_View();

				qs_ProjectEscrowAmount_View.setTableId(Long.valueOf(ObjectUtils.toString(rs.getObject(1))));
				// 流水号
				qs_ProjectEscrowAmount_View.setSerialNumber(ObjectUtils.toString(rs.getObject(2)));
				// 入账日期
				qs_ProjectEscrowAmount_View.setRecordDate(ObjectUtils.toString(rs.getObject(3)));
				// 开发企业
				qs_ProjectEscrowAmount_View.setTheNameOfCompany(ObjectUtils.toString(rs.getObject(4)));
				// 项目名称
				qs_ProjectEscrowAmount_View.setTheNameOfProject(ObjectUtils.toString(rs.getObject(5)));
				// 楼幢（施工楼幢）
				qs_ProjectEscrowAmount_View.seteCodeFromConstruction(ObjectUtils.toString(rs.getObject(6)));
				// 放款户数
				qs_ProjectEscrowAmount_View.setLoansCountHouse(Integer.valueOf(StrUtil.isBlank(ObjectUtils.toString(rs.getObject(7))) ? "0" : ObjectUtils.toString(rs.getObject(7))));
				// 托管收入
				qs_ProjectEscrowAmount_View.setIncome(Double.valueOf( StrUtil.isBlank(ObjectUtils.toString(rs.getObject(8))) ? "0.00" : ObjectUtils.toString(rs.getObject(8))));
				// 托管支出
				qs_ProjectEscrowAmount_View.setPayout(Double.valueOf(StrUtil.isBlank(ObjectUtils.toString(rs.getObject(9))) ? "0.00" : ObjectUtils.toString(rs.getObject(9))));
				// 余额
				qs_ProjectEscrowAmount_View.setCurrentFund(Double.valueOf(StrUtil.isBlank(ObjectUtils.toString(rs.getObject(10))) ? "0.00" : ObjectUtils.toString(rs.getObject(10))));
				// 溢出金额
				qs_ProjectEscrowAmount_View.setSpilloverAmount(Double.valueOf(StrUtil.isBlank(ObjectUtils.toString(rs.getObject(11))) ? "0.00" : ObjectUtils.toString(rs.getObject(11))));
				// 备注
				qs_ProjectEscrowAmount_View.setRemarkNote(ObjectUtils.toString(rs.getObject(12)));
				//所属区域
				qs_ProjectEscrowAmount_View.setCityRegionname(ObjectUtils.toString(rs.getObject(13)));

				qs_ProjectEscrowAmount_ViewList.add(qs_ProjectEscrowAmount_View);
			}

			map.put("qs_ProjectEscrowAmount_ViewList", qs_ProjectEscrowAmount_ViewList);

			int totalPage = sp.getInt(11);
			map.put("totalPage", totalPage);

			int totalCount = sp.getInt(12);

			map.put("totalCount", totalCount);

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
